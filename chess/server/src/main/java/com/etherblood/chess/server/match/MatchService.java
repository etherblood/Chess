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

	public List<MatchRequest> matchRequests() {
		return matchRepo.getMatchRequests(userContextService.currentUserId());
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
    	validateNextMove(moves, stateMove, match.getStartFen());
    	
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
    
    private void validateNextMove(List<MatchMove> moves, Move nextMove, String startFen) {
    	//TODO
    	
    	ChessState state = stateFromMoves(moves, startFen);
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
    
    private ChessState stateFromMoves(List<MatchMove> moves, String startFen) {
    	ChessState state = new ChessState();
        new ChessSetup().fromFen(state, startFen);
        MoveExecutor executor = new MoveExecutor();
        boolean whiteToMove = true;
        for (MatchMove move : moves) {
            executor.makeMove(state, ChessModelConverter.convertMove(move, whiteToMove));
        }
        return state;
    }

}
