package com.etherblood.chess.server.match;

import com.etherblood.chess.api.match.ChessSquare;
import com.etherblood.chess.api.match.moves.ChessMove;
import com.etherblood.chess.api.match.moves.MoveType;
import com.etherblood.chess.engine.moves.Move;
import com.etherblood.chess.engine.util.Piece;
import com.etherblood.chess.server.match.model.MatchMove;

/**
 *
 * @author Philipp
 */
public class ChessModelConverter {

	public static MoveType convertMoveType(int moveType) {
		MoveType type;
		switch (moveType) {
		case Piece.EMPTY:
			type = MoveType.SIMPLE;
			break;
		case Piece.RESERVED_1:
			type = MoveType.PAWN_JUMP;
			break;
		case Piece.RESERVED_2:
			type = MoveType.EN_PASSANT;
			break;
		case Piece.RESERVED_3:
			type = MoveType.CASTLING;
			break;
		case Piece.W_KNIGHT:
		case Piece.B_KNIGHT:
			type = MoveType.PROMOTION_KNIGHT;
			break;
		case Piece.W_BISHOP:
		case Piece.B_BISHOP:
			type = MoveType.PROMOTION_BISHOP;
			break;
		case Piece.W_ROOK:
		case Piece.B_ROOK:
			type = MoveType.PROMOTION_ROOK;
			break;
		case Piece.W_QUEEN:
		case Piece.B_QUEEN:
			type = MoveType.PROMOTION_QUEEN;
			break;
		default:
			throw new AssertionError();
		}
		return type;
	}

	public static int convertMoveType(MoveType moveType, boolean whiteToMove) {
		int type;
		switch (moveType) {
		case SIMPLE:
			type = Piece.EMPTY;
			break;
		case PAWN_JUMP:
			type = Piece.RESERVED_1;
			break;
		case EN_PASSANT:
			type = Piece.RESERVED_2;
			break;
		case CASTLING:
			type = Piece.RESERVED_3;
			break;
		case PROMOTION_KNIGHT:
			type = whiteToMove ? Piece.W_KNIGHT : Piece.B_KNIGHT;
			break;
		case PROMOTION_BISHOP:
			type = whiteToMove ? Piece.W_BISHOP : Piece.B_BISHOP;
			break;
		case PROMOTION_ROOK:
			type = whiteToMove ? Piece.W_ROOK : Piece.B_ROOK;
			break;
		case PROMOTION_QUEEN:
			type = whiteToMove ? Piece.W_QUEEN : Piece.B_QUEEN;
			break;
		default:
			throw new AssertionError();
		}
		return type;
	}

	public static Move convertMove(ChessMove move, boolean whiteToMove) {
		int type;
		switch (move.type) {
		case SIMPLE:
			type = Piece.EMPTY;
			break;
		case PAWN_JUMP:
			type = Piece.RESERVED_1;
			break;
		case EN_PASSANT:
			type = Piece.RESERVED_2;
			break;
		case CASTLING:
			type = Piece.RESERVED_3;
			break;
		case PROMOTION_KNIGHT:
			type = whiteToMove ? Piece.W_KNIGHT : Piece.B_KNIGHT;
			break;
		case PROMOTION_BISHOP:
			type = whiteToMove ? Piece.W_BISHOP : Piece.B_BISHOP;
			break;
		case PROMOTION_ROOK:
			type = whiteToMove ? Piece.W_ROOK : Piece.B_ROOK;
			break;
		case PROMOTION_QUEEN:
			type = whiteToMove ? Piece.W_QUEEN : Piece.B_QUEEN;
			break;
		default:
			throw new AssertionError();
		}
		return new Move(ChessSquare.toInt(move.from), ChessSquare.toInt(move.to), 0, type);
	}

	public static ChessMove convertMove(MatchMove move) {
		return new ChessMove(move.getType(), move.getFrom(), move.getTo());
	}
	
	public static int convertSquare(ChessSquare square) {
		return ChessSquare.toInt(square);
	}
}
