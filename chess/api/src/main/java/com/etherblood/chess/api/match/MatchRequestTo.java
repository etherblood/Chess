package com.etherblood.chess.api.match;

import java.util.UUID;

public class MatchRequestTo {

	public UUID matchId, requesterId, receiverId;

	@Override
	public String toString() {
		return "MatchRequestTo [matchId=" + matchId + ", requesterId=" + requesterId + ", receiverId=" + receiverId
				+ "]";
	}
}
