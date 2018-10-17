/**
 *
 */
package jenjinn.engine.eval;

import static jenjinn.engine.bitboards.Bitboards.emptyBoardAttackset;

import jenjinn.engine.base.Square;
import jenjinn.engine.bitboards.BitboardIterator;
import jenjinn.engine.pieces.ChessPiece;
import jflow.seq.Seq;

/**
 * @author ThomasB
 */
public final class KingSafetyArea
{
	private final long outer, inner, all;

	private KingSafetyArea(Square src)
	{
		final ChessPiece king = ChessPiece.WHITE_KING;
		this.inner = emptyBoardAttackset(king, src);
		this.outer = BitboardIterator.from(inner)
				.mapToLong(sq -> emptyBoardAttackset(king, sq))
				.fold(0L, (a, b) -> a | b) & ~(src.bitboard | inner);
		this.all = outer | inner;
	}

	public long getOuterArea()
	{
		return outer;
	}

	public long getInnerArea()
	{
		return inner;
	}

	public long getTotalArea()
	{
		return all;
	}

	private static final Seq<KingSafetyArea> CACHE = Square.ALL.map(KingSafetyArea::new);

	public static KingSafetyArea get(Square src)
	{
		return CACHE.get(src.ordinal());
	}
}
