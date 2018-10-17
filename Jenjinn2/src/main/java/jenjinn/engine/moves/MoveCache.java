/**
 *
 */
package jenjinn.engine.moves;

import static java.util.Arrays.asList;

import jenjinn.engine.base.CastleZone;
import jenjinn.engine.base.Square;
import jenjinn.engine.bitboards.BitboardIterator;
import jenjinn.engine.pieces.Piece;
import jflow.seq.Seq;

/**
 * @author ThomasB
 */
public final class MoveCache {

	private MoveCache() {}

	private static final Seq<CastleMove> CASTLE_MOVE_CACHE = CastleZone.ALL.map(CastleMove::new);
	private static final Seq<StandardMove[]> STANDARD_MOVE_CACHE = createStandardMoveCache();

	static Seq<StandardMove[]> createStandardMoveCache()
	{
		Seq<StandardMove[]> moveCache = Square.ALL.map(i -> new StandardMove[64]);

		for (Piece piece : asList(Piece.WHITE_KNIGHT, Piece.WHITE_QUEEN)) {
			Square.ALL.forEach(square ->
			{
				BitboardIterator.from(piece.getSquaresOfControl(square, 0L, 0L))
				.forEach(loc -> moveCache.get(square.ordinal())[loc.ordinal()] = new StandardMove(square, loc));
			});
		}
		return moveCache;
	}

	public static StandardMove getMove(Square source, Square target)
	{
		assert STANDARD_MOVE_CACHE.get(source.ordinal())[target.ordinal()] != null : "Requested impossible move or my logic is wrong.";
		return STANDARD_MOVE_CACHE.get(source.ordinal())[target.ordinal()];
	}

	public static CastleMove getMove(CastleZone zone)
	{
		return CASTLE_MOVE_CACHE.get(zone.ordinal());
	}
}
