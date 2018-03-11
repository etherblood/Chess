package com.etherblood.chess.server.match;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etherblood.chess.engine.ChessSetup;
import com.etherblood.chess.engine.ChessState;
import com.etherblood.chess.engine.moves.Move;
import com.etherblood.chess.engine.moves.generators.MoveGenerator;
import com.etherblood.chess.engine.moves.handlers.MoveExecutor;
import com.etherblood.chess.engine.util.Mask;
import com.etherblood.chess.server.match.events.MatchMoveEvent;
import com.etherblood.chess.server.match.model.MatchConfig;
import com.etherblood.chess.server.match.model.MatchData;
import com.etherblood.chess.server.match.model.MatchMove;
import com.etherblood.chess.server.poll.PollService;
import com.etherblood.chess.server.user.account.AccountService;

/**
 *
 * @author Philipp
 */
@Service
public class MatchService {

    private final Map<UUID, MatchData> matches = new ConcurrentHashMap<>();
    private final AccountService userService;
    private final PollService pollService;

    @Autowired
    public MatchService(AccountService userService, PollService pollService) {
        this.userService = userService;
        this.pollService = pollService;
    }

    public MatchData getMatch(UUID matchId) {
        return matches.get(matchId);
    }

    public MatchData createMatch(MatchConfig config) {
        validateConfig(config);
        MatchData match = new MatchData(UUID.randomUUID(), config, new Date());
        matches.put(match.getId(), match);
        return match;
    }

    private void validateConfig(MatchConfig config) {
        Objects.requireNonNull(userService.getAccountById(config.getWhitePlayer()));
        Objects.requireNonNull(userService.getAccountById(config.getBlackPlayer()));
    }

    public Map<Integer, Integer> matchPieces(UUID matchId) {
        ChessState state = getMatchState(matchId);
        Map<Integer, Integer> result = new HashMap<>();
        long pieces = state.allPieces();
        while (pieces != 0) {
            int square = Long.numberOfTrailingZeros(pieces);
            int piece = state.pieces[square];

            result.put(square, piece);
//            result.add(new SquarePiecePair(convert(piece), ChessSquare.fromInt(square)));
            pieces ^= Mask.toFlag(square);
        }
        return result;
    }

    private ChessState getMatchState(UUID matchId) {
        ChessState state = new ChessState();
        new ChessSetup().reset(state);
        MatchData match = matches.get(matchId);
        MoveExecutor executor = new MoveExecutor();
        for (MatchMove move : match.getMoves()) {
            executor.makeMove(state, move.getMove());
        }
        return state;
    }

    public void move(UUID matchId, Move selected) {
        Objects.requireNonNull(selected);
        MatchData match = getMatch(matchId);
        match.getMoves().add(new MatchMove(selected, new Date()));
        pollService.sendEvent(new MatchMoveEvent(matchId, selected), match.getSpectators());
    }

    private List<Move> generateMoves(ChessState state) {
        MoveExecutor exec = new MoveExecutor();
        MoveGenerator generator = new MoveGenerator();
        Move[] moves = new Move[256];
        for (int i = 0; i < moves.length; i++) {
            moves[i] = new Move();
        }
        int numMoves = generator.generateMoves(state, moves, 0);
        List<Move> result = new ArrayList<>();
        for (int i = 0; i < numMoves; i++) {
            Move move = moves[i];
            exec.makeMove(state, move);
            if (!generator.isThreateningKing(state)) {
                result.add(move);
            }
            exec.unmakeMove(state, move);
        }
        return result;
    }

    public List<Move> moves(UUID matchId) {
        return generateMoves(getMatchState(matchId));
    }

    public Collection<MatchData> getAllMatches() {
        return matches.values();
    }

}
