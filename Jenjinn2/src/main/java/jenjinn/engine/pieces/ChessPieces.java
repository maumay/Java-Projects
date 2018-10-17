/**
 *
 */
package jenjinn.engine.pieces;

import jenjinn.engine.base.Side;
import jflow.seq.Seq;

/**
 * @author ThomasB
 *
 */
public final class ChessPieces
{
	private ChessPieces() {}

	private static final Seq<ChessPiece> ALL_PIECES = Seq.of(ChessPiece.values());
	private static final Seq<ChessPiece> WHITE_PIECES = ALL_PIECES.take(6);
	private static final Seq<ChessPiece> BLACK_PIECES = ALL_PIECES.drop(6);
	private static final Seq<ChessPiece> WHITE_PINNING_PIECES = Seq.of(ChessPiece.WHITE_QUEEN, ChessPiece.WHITE_ROOK, ChessPiece.WHITE_BISHOP);
	private static final Seq<ChessPiece> BLACK_PINNING_PIECES = Seq.of(ChessPiece.BLACK_QUEEN, ChessPiece.BLACK_ROOK, ChessPiece.BLACK_BISHOP);

	public static Seq<ChessPiece> all()
	{
		return ALL_PIECES;
	}

	public static Seq<ChessPiece> white()
	{
		return WHITE_PIECES;
	}

	public static Seq<ChessPiece> black()
	{
		return BLACK_PIECES;
	}

	public static Seq<ChessPiece> whitePinningPieces()
	{
		return WHITE_PINNING_PIECES;
	}

	public static Seq<ChessPiece> blackPinningPieces()
	{
		return BLACK_PINNING_PIECES;
	}

	public static Seq<ChessPiece> pinnersOn(Side side)
	{
		return side.isWhite()? WHITE_PINNING_PIECES : BLACK_PINNING_PIECES;
	}

	public static Seq<ChessPiece> ofSide(Side side)
	{
		return side.isWhite()? white() : black();
	}

//	public static Flow<ChessPiece> iterate()
//	{
//		return Iter.over(all());
//	}

	public static ChessPiece fromIndex(int index)
	{
		return ChessPiece.values()[index];
	}

//	public static ChessPiece king(Side side)
//	{
//		return side.isWhite()? last(white()) : last(black());
//	}
//
//	public static ChessPiece pawn(Side side)
//	{
//		return side.isWhite()? head(white()) : head(black());
//	}
}
