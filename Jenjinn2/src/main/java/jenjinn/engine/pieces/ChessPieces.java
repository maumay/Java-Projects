/**
 *
 */
package jenjinn.engine.pieces;

import static jflow.utilities.CollectionUtil.head;
import static jflow.utilities.CollectionUtil.last;

import jenjinn.engine.base.Side;
import jflow.collections.FList;
import jflow.collections.Lists;
import jflow.iterators.Flow;
import jflow.iterators.factories.Iter;

/**
 * @author ThomasB
 *
 */
public final class ChessPieces
{
	private ChessPieces() {}

	private static final FList<ChessPiece> ALL_PIECES = Lists.build(ChessPiece.values());
	private static final FList<ChessPiece> WHITE_PIECES = Iter.over(ALL_PIECES).take(6).toList();
	private static final FList<ChessPiece> BLACK_PIECES = Iter.over(ALL_PIECES).drop(6).toList();
	private static final FList<ChessPiece> WHITE_PINNING_PIECES = Lists.build(ChessPiece.WHITE_QUEEN, ChessPiece.WHITE_ROOK, ChessPiece.WHITE_BISHOP);
	private static final FList<ChessPiece> BLACK_PINNING_PIECES = Lists.build(ChessPiece.BLACK_QUEEN, ChessPiece.BLACK_ROOK, ChessPiece.BLACK_BISHOP);

	public static FList<ChessPiece> all()
	{
		return ALL_PIECES;
	}

	public static FList<ChessPiece> white()
	{
		return WHITE_PIECES;
	}

	public static FList<ChessPiece> black()
	{
		return BLACK_PIECES;
	}

	public static FList<ChessPiece> whitePinningPieces()
	{
		return WHITE_PINNING_PIECES;
	}

	public static FList<ChessPiece> blackPinningPieces()
	{
		return BLACK_PINNING_PIECES;
	}

	public static FList<ChessPiece> pinnersOn(Side side)
	{
		return side.isWhite()? WHITE_PINNING_PIECES : BLACK_PINNING_PIECES;
	}

	public static FList<ChessPiece> ofSide(Side side)
	{
		return side.isWhite()? white() : black();
	}

	public static Flow<ChessPiece> iterate()
	{
		return Iter.over(all());
	}

	public static ChessPiece fromIndex(int index)
	{
		return ChessPiece.values()[index];
	}

	public static ChessPiece king(Side side)
	{
		return side.isWhite()? last(white()) : last(black());
	}

	public static ChessPiece pawn(Side side)
	{
		return side.isWhite()? head(white()) : head(black());
	}
}
