package com.etherblood.chess.api.match.events.data;

import java.util.Date;
import java.util.UUID;

import com.etherblood.chess.api.match.ChessResult;

public class MatchEndedTo {
	public UUID matchId;
	public Date ended;
	public ChessResult result;
}
