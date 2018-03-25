package com.etherblood.chess.api.match;

import java.util.List;
import java.util.UUID;

import com.etherblood.chess.api.match.moves.ChessMove;

public class ChessMatchTo {

	public UUID id;
	public String startFen;
    public UUID whiteId, blackId;
	public List<ChessMove> moves;
}
