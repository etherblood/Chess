package com.etherblood.chess.server.match;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etherblood.chess.api.match.ChessMatchTo;
import com.etherblood.chess.api.match.moves.ChessMove;
import com.etherblood.chess.engine.ChessSetup;
import com.etherblood.chess.engine.ChessState;
import com.etherblood.chess.engine.moves.Move;
import com.etherblood.chess.engine.moves.generators.MoveGenerator;
import com.etherblood.chess.engine.moves.handlers.MoveExecutor;
import com.etherblood.chess.server.match.model.ChessMatch;
import com.etherblood.chess.server.match.model.MatchMove;
import com.etherblood.chess.server.match.model.MatchRequest;
import com.etherblood.chess.server.user.account.model.Account;
import com.etherblood.chess.server.user.authentication.UserContextService;

/**
 *
 * @author Philipp
 */
@Service
public class MatchService {

    private final static Logger LOG = LoggerFactory.getLogger(MatchService.class);

	private final MatchRepository matchRepo;
    private final UserContextService userContextService;

    @Autowired
    public MatchService(UserContextService userContextService, MatchRepository matchRepo) {
        this.userContextService = userContextService;
        this.matchRepo = matchRepo;
    }
    
    public List<UUID> activeMatchIds() {
    	return matchRepo.getActiveMatchIds();
    }

	public List<UUID> requestedMatchIds() {
		return matchRepo.getRequestedMatchIds(userContextService.currentUserId());
	}
    
    public List<MatchMove> getMatchMoves(UUID matchId) {
    	return matchRepo.getMatchMoves(matchId);
    }

    public ChessMatch getMatch(UUID matchId) {
        return matchRepo.getMatchById(matchId);
    }

    @Transactional
    public MatchRequest createMatch(ChessMatchTo config) {
    	UUID currentUserId = userContextService.currentUserId();
    	if(!currentUserId.equals(config.whiteId) && !currentUserId.equals(config.blackId)) {
    		throw new IllegalStateException("cant create matches for other users");
    	}
    	//TODO: validate startFen
    	Account whiteProxy = matchRepo.proxyById(Account.class, config.whiteId);
    	Account blackProxy = matchRepo.proxyById(Account.class, config.blackId);
    	ChessMatch match = new ChessMatch();
    	match.setId(UUID.randomUUID());
    	match.setWhite(whiteProxy);
		match.setBlack(blackProxy);
		match.setStartFen(config.startFen != null? config.startFen: ChessSetup.DEFAULT_STARTPOSITION);
    	matchRepo.persist(match);
    	
    	MatchRequest request = new MatchRequest();
    	request.setId(UUID.randomUUID());
    	request.setMatch(match);
    	request.setPlayer(currentUserId.equals(config.whiteId)? blackProxy: whiteProxy);
    	matchRepo.persist(request);
    	LOG.info("created {} and {}", match, request);
        return request;
    }
    
    @Transactional
    public ChessMatch acceptMatchChallenge(UUID matchId) {
    	UUID currentUserId = userContextService.currentUserId();
		MatchRequest request = matchRepo.getMatchRequestByMatchAndPlayerId(matchId, currentUserId);
    	ChessMatch match = request.getMatch();
    	match.setStarted(new Date());
    	matchRepo.remove(request);
    	LOG.info("accepted {}", request);
    	return match;
    }
    
    @Transactional
    public void makeMove(UUID matchId, ChessMove move) {
    	ChessMatch match = matchRepo.getMatchById(matchId);
    	List<MatchMove> moves = matchRepo.getMatchMoves(matchId);
    	boolean whiteToMove = (moves.size() & 1) == 0;
    	if(whiteToMove) {
    		if(!match.getWhite().getId().equals(userContextService.currentUserId())) {
    			throw new IllegalStateException("current player is not white player of this match");
    		}
    	} else {
    		if(!match.getBlack().getId().equals(userContextService.currentUserId())) {
    			throw new IllegalStateException("current player is not black player of this match");
    		}
    	}
    	Move stateMove = ChessModelConverter.convertMove(move, whiteToMove);
    	validateNextMove(moves, stateMove);
    	
    	MatchMove matchMove = new MatchMove();
    	matchMove.setId(UUID.randomUUID());
    	matchMove.setFrom(move.from);
    	matchMove.setTo(move.to);
    	matchMove.setType(move.type);
    	matchMove.setIndex(moves.size());
    	matchMove.setMatch(match);
    	matchRepo.persist(matchMove);
    	
    	//TODO: end match if move game-ending
    	LOG.info("applied {}", matchMove);
    }
    
    private void validateNextMove(List<MatchMove> moves, Move nextMove) {
    	//TODO
    	
    	ChessState state = stateFromMoves(moves);
//    	MoveGenerator moveGen = new MoveGenerator();
//    	Move[] moveBuffer = MoveGenerator.createBuffer(1000);
//    	for (int i = 0; i < moveBuffer.length; i++) {
//			moveBuffer[i] = new Move();
//		}
//    	int moveCount = moveGen.generateMoves(state, moveBuffer, 0);
//    	for (int i = 0; i < moveCount; i++) {
//			Move move = moveBuffer[i];
//			if(move.from == nextMove.from && move.to == nextMove.to && move.info == nextMove.info) {
//				return;
//			}
//		}
//    	throw new IllegalStateException("invalid move " + nextMove);
    }
    
    private ChessState stateFromMoves(List<MatchMove> moves) {
    	ChessState state = new ChessState();
        new ChessSetup().reset(state);
        MoveExecutor executor = new MoveExecutor();
        boolean whiteToMove = true;
        for (MatchMove move : moves) {
            executor.makeMove(state, ChessModelConverter.convertMove(move, whiteToMove));
        }
        return state;
    }
//
//    public Map<Integer, Integer> matchPieces(UUID matchId) {
//        ChessState state = getMatchState(matchId);
//        Map<Integer, Integer> result = new HashMap<>();
//        long pieces = state.allPieces();
//        while (pieces != 0) {
//            int square = Long.numberOfTrailingZeros(pieces);
//            int piece = state.pieces[square];
//
//            result.put(square, piece);
////            result.add(new SquarePiecePair(convert(piece), ChessSquare.fromInt(square)));
//            pieces ^= Mask.toFlag(square);
//        }
//        return result;
//    }
//
//    private ChessState getMatchState(UUID matchId) {
//        ChessState state = new ChessState();
//        new ChessSetup().reset(state);
//        MatchData match = matches.get(matchId);
//        MoveExecutor executor = new MoveExecutor();
//        for (MatchMove move : match.getMoves()) {
//            executor.makeMove(state, move.getMove());
//        }
//        return state;
//    }
//
//    public void move(UUID matchId, Move selected) {
//        Objects.requireNonNull(selected);
//        MatchData match = getMatch(matchId);
//        match.getMoves().add(new MatchMove(selected, new Date()));
//        pollService.sendEvent(new MatchMoveEvent(matchId, selected), match.getSpectators());
//    }
//
//    private List<Move> generateMoves(ChessState state) {
//        MoveExecutor exec = new MoveExecutor();
//        MoveGenerator generator = new MoveGenerator();
//        Move[] moves = new Move[256];
//        for (int i = 0; i < moves.length; i++) {
//            moves[i] = new Move();
//        }
//        int numMoves = generator.generateMoves(state, moves, 0);
//        List<Move> result = new ArrayList<>();
//        for (int i = 0; i < numMoves; i++) {
//            Move move = moves[i];
//            exec.makeMove(state, move);
//            if (!generator.isThreateningKing(state)) {
//                result.add(move);
//            }
//            exec.unmakeMove(state, move);
//        }
//        return result;
//    }
//
//    public List<Move> moves(UUID matchId) {
//        return generateMoves(getMatchState(matchId));
//    }
//
//    public Collection<MatchData> getAllMatches() {
//        return matches.values();
//    }

}
