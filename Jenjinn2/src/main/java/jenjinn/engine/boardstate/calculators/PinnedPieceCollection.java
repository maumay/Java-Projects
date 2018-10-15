/**
 *
 */
package jenjinn.engine.boardstate.calculators;

import static java.util.Collections.unmodifiableMap;
import static java.util.function.Function.identity;

import java.util.Map;
import java.util.Set;

import jenjinn.engine.base.BoardSquare;
import jflow.iterators.Flow;
import jflow.iterators.factories.Iterate;
import jflow.iterators.iterables.FlowIterable;

/**
 * @author ThomasB
 *
 */
public final class PinnedPieceCollection implements FlowIterable<PinnedPiece>
{
	private final Map<BoardSquare, PinnedPiece> cache;

	public PinnedPieceCollection(Flow<PinnedPiece> pinnedPieces)
	{
		cache = unmodifiableMap(pinnedPieces.toMap(PinnedPiece::getLocation, identity()));
	}

	@Override
	public Flow<PinnedPiece> iterator()
	{
		return Iterate.over(cache.values());
	}

	public Set<BoardSquare> getLocations()
	{
		return cache.keySet();
	}

	public boolean containsLocation(BoardSquare location)
	{
		return cache.containsKey(location);
	}

	public long getConstraintAreaOfPieceAt(BoardSquare location)
	{
		return cache.get(location).getConstrainedArea();
	}
}
