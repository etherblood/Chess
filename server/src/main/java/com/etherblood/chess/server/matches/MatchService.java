package com.etherblood.chess.server.matches;

import com.etherblood.chess.engine.ChessSetup;
import com.etherblood.chess.engine.ChessState;
import com.etherblood.chess.engine.moves.Move;
import com.etherblood.chess.engine.moves.generators.MoveGenerator;
import com.etherblood.chess.engine.moves.handlers.MoveExecutor;
import com.etherblood.chess.engine.util.Mask;
import com.etherblood.chess.engine.util.Piece;
import com.etherblood.chess.server.matches.events.MatchMoveEvent;
import com.etherblood.chess.server.matches.model.CandidateMove;
import com.etherblood.chess.server.matches.model.ChessPiece;
import com.etherblood.chess.server.matches.model.ChessPieceColor;
import com.etherblood.chess.server.matches.model.ChessPieceType;
import com.etherblood.chess.server.matches.model.MatchConfig;
import com.etherblood.chess.server.matches.model.MatchData;
import com.etherblood.chess.server.matches.model.MatchMove;
import com.etherblood.chess.server.polling.PollService;
import com.etherblood.chess.server.users.UserService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Philipp
 */
@Service
public class MatchService {

    private final Map<UUID, MatchData> matches = new ConcurrentHashMap<>();
    private final UserService userService;
    private final PollService pollService;

    @Autowired
    public MatchService(UserService userService, PollService pollService) {
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
        Objects.requireNonNull(userService.getUser(config.getWhitePlayer()));
        Objects.requireNonNull(userService.getUser(config.getBlackPlayer()));
    }

    public List<ChessPiece> matchPieces(UUID matchId) {
        ChessState state = getMatchState(matchId);
        List<ChessPiece> result = new ArrayList<>();
        long pieces = state.allPieces();
        while (pieces != 0) {
            int square = Long.numberOfTrailingZeros(pieces);
            int piece = state.pieces[square];

            result.add(new ChessPiece(convert(piece), ChessPieceColor.values()[Piece.owner(piece)], square));
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

    private ChessPieceType convert(int chessPiece) {
        int whitePiece = Piece.asWhitePiece(chessPiece);
        for (ChessPieceType value : ChessPieceType.values()) {
            if (value.getPieceCode() == whitePiece) {
                return value;
            }
        }
        throw new IllegalArgumentException(String.valueOf(chessPiece));
    }

    public void move(UUID matchId, CandidateMove candidate) {
        int prom = candidate.getPromotion() != null ? candidate.getPromotion().getPieceCode() : -1;
        ChessState state = getMatchState(matchId);
        Move selected = null;
        for (Move move : generateMoves(state)) {
            if (move.from == candidate.getFrom() && move.to == candidate.getTo()) {
                if (selected == null || move.info == prom) {
                    selected = move;
                }
            }
        }
        Objects.requireNonNull(selected);
        MatchData match = getMatch(matchId);
        match.getMoves().add(new MatchMove(selected, new Date()));
        pollService.sendEvent(new MatchMoveEvent(matchId, candidate), match.getSpectators());
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
            if(!generator.isThreateningKing(state)) {
                result.add(move);
            }
            exec.unmakeMove(state, move);
        }
        return result;
    }

    public List<CandidateMove> moves(UUID matchId) {
        List<CandidateMove> moves = new ArrayList<>();
        for (Move move : generateMoves(getMatchState(matchId))) {
            ChessPieceType promotion = null;
            if(move.info > Piece.RESERVED_3) {
                promotion = convert(move.info);
            }
            moves.add(new CandidateMove(move.from, move.to, promotion));
        }
        return moves;
    }

    public Collection<MatchData> getAllMatches() {
        return matches.values();
    }

}
