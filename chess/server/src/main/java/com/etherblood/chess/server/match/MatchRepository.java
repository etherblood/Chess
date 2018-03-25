package com.etherblood.chess.server.match;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.etherblood.chess.server.match.model.ChessMatch;
import com.etherblood.chess.server.match.model.MatchMove;
import com.etherblood.chess.server.match.model.MatchRequest;
import com.etherblood.chess.server.match.model.QChessMatch;
import com.etherblood.chess.server.match.model.QMatchMove;
import com.etherblood.chess.server.match.model.QMatchRequest;
import com.etherblood.chess.server.persistence.AbstractRepository;

/**
 *
 * @author Philipp
 */
@Repository
public class MatchRepository extends AbstractRepository {

    private static final QChessMatch qMatch = QChessMatch.chessMatch;
    private static final QMatchMove qMove = QMatchMove.matchMove;
    private static final QMatchRequest qRequest = QMatchRequest.matchRequest;

    public ChessMatch getMatchById(UUID id) {
        return from(qMatch)
        		.where(qMatch.id.eq(id))
        		.uniqueResult(qMatch);
    }

    public MatchRequest getMatchRequestByMatchAndPlayerId(UUID matchId, UUID playerId) {
        return from(qRequest)
        		.where(qRequest.match.id.eq(matchId))
        		.where(qRequest.player.id.eq(playerId))
        		.join(qRequest.match).fetch()
        		.uniqueResult(qRequest);
    }
    
    public List<MatchMove> getMatchMoves(UUID matchId) {
    	return from(qMove)
    			.where(qMove.match.id.eq(matchId))
    			.orderBy(qMove.index.asc())
    			.list(qMove);
    }

	public List<UUID> getActiveMatchIds() {
		return from(qMatch)
				.where(qMatch.started.isNotNull())
				.where(qMatch.ended.isNull())
				.list(qMatch.id);
	}

	public List<UUID> getRequestedMatchIds(UUID playerId) {
		return from(qRequest)
				.where(qRequest.player.id.eq(playerId))
				.list(qRequest.match.id);
	}
}
