package com.etherblood.chess.server.match;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etherblood.chess.api.match.ChessMatchTo;
import com.etherblood.chess.api.match.ChessResult;
import com.etherblood.chess.api.match.moves.ChessMove;
import com.etherblood.chess.engine.ChessSetup;
import com.etherblood.chess.engine.ChessState;
import com.etherblood.chess.engine.util.ChessWrapper;
import com.etherblood.chess.engine.util.Player;
import com.etherblood.chess.server.match.model.ChessMatch;
import com.etherblood.chess.server.match.model.MatchMove;
import com.etherblood.chess.server.match.model.MatchRequest;
import com.etherblood.chess.server.time.TimeService;
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
	private final TimeService timeService;

	@Autowired
	public MatchService(UserContextService userContextService, MatchRepository matchRepo, TimeService timeService) {
		this.userContextService = userContextService;
		this.matchRepo = matchRepo;
		this.timeService = timeService;
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
		if (!currentUserId.equals(config.whiteId) && !currentUserId.equals(config.blackId)) {
			throw new IllegalStateException("cant create matches for other users");
		}
		Account whiteProxy = matchRepo.proxyById(Account.class, config.whiteId);
		Account blackProxy = matchRepo.proxyById(Account.class, config.blackId);
		ChessMatch match = new ChessMatch();
		match.setId(UUID.randomUUID());
		match.setWhite(whiteProxy);
		match.setBlack(blackProxy);
		match.setStartFen(config.startFen != null ? config.startFen : ChessSetup.DEFAULT_STARTPOSITION);
		match.setResult(ChessResult.UNDECIDED);
		updateMatchResult(match, Collections.emptyList());
		if (match.getResult() != ChessResult.UNDECIDED) {
			throw new IllegalStateException("may not create already ended match");
		}
		matchRepo.persist(match);

		MatchRequest request = new MatchRequest();
		request.setId(UUID.randomUUID());
		request.setMatch(match);
		request.setPlayer(currentUserId.equals(config.whiteId) ? blackProxy : whiteProxy);
		matchRepo.persist(request);
		LOG.info("created {} and {}", match, request);
		return request;
	}

	@Transactional
	public ChessMatch acceptMatchChallenge(UUID matchId) {
		UUID currentUserId = userContextService.currentUserId();
		MatchRequest request = matchRepo.getMatchRequestByMatchAndPlayerId(matchId, currentUserId);
		ChessMatch match = request.getMatch();
		match.setStarted(timeService.now().toDate());
		matchRepo.remove(request);
		LOG.info("accepted {}", request);
		return match;
	}

	@Transactional
	public ChessMatch makeMove(UUID matchId, ChessMove move) {
		ChessMatch match = matchRepo.getMatchById(matchId);
		if (match.getEnded() != null || match.getResult() != ChessResult.UNDECIDED) {
			throw new IllegalStateException("cant make move for ended match");
		}
		List<MatchMove> moves = matchRepo.getMatchMoves(matchId);
		boolean whiteToMove = (moves.size() & 1) == 0;
		if (whiteToMove) {
			if (!match.getWhite().getId().equals(userContextService.currentUserId())) {
				throw new IllegalStateException("current player is not white player of this match");
			}
		} else {
			if (!match.getBlack().getId().equals(userContextService.currentUserId())) {
				throw new IllegalStateException("current player is not black player of this match");
			}
		}

		MatchMove matchMove = new MatchMove();
		matchMove.setId(UUID.randomUUID());
		matchMove.setFrom(move.from);
		matchMove.setTo(move.to);
		matchMove.setType(move.type);
		matchMove.setIndex(moves.size());
		matchMove.setMatch(match);
		matchRepo.persist(matchMove);
		LOG.info("applied {}", matchMove);

		List<MatchMove> allMoves = new ArrayList<>();
		allMoves.addAll(moves);
		allMoves.add(matchMove);

		updateMatchResult(match, allMoves);
		return match;
	}

	private void updateMatchResult(ChessMatch match, List<MatchMove> allMoves) {
		ChessResult matchResult = getMatchResult(allMoves, match.getStartFen());
		if (matchResult != ChessResult.UNDECIDED) {
			match.setResult(matchResult);
			match.setEnded(timeService.now().toDate());
			LOG.info("{} ended", match);
		}
	}

	private ChessResult getMatchResult(List<MatchMove> moves, String startFen) {
		ChessWrapper chessWrapper = new ChessWrapper();
		ChessState state = new ChessState();
		ChessSetup setup = new ChessSetup();
		setup.fromFen(state, startFen);
		for (MatchMove move : moves) {
			int moveInfo = ChessModelConverter.convertMoveType(move.getType(), state.currentPlayer() == Player.WHITE);
			int from = ChessModelConverter.convertSquare(move.getFrom());
			int to = ChessModelConverter.convertSquare(move.getTo());
			chessWrapper.makeMove(state, chessWrapper.find(state, from, to, moveInfo));
		}

		if (chessWrapper.legalMoves(state).isEmpty()) {
			if (chessWrapper.isKingChecked(state)) {
				return state.currentPlayer() == Player.WHITE ? ChessResult.BLACK_VICTORY : ChessResult.WHITE_VICTORY;
			} else {
				return ChessResult.DRAW;
			}
		}
		if (chessWrapper.isFiftyRule(state) || chessWrapper.is3FoldRepetition(state)
				|| chessWrapper.insufficientMatingMaterial(state)) {
			return ChessResult.DRAW;
		}
		return ChessResult.UNDECIDED;
	}

	public List<UUID> historyMatchIds(int page, int pageSize) {
		return matchRepo.historyMatchIds(page, pageSize);
	}

}
