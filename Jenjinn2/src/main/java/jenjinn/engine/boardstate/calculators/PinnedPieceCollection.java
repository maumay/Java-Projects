/**
 *
 */
package jenjinn.engine.boardstate.calculators;

import static java.util.Collections.unmodifiableMap;
import static java.util.function.Function.identity;

import java.util.Map;
import java.util.Set;

import jenjinn.engine.base.Square;
import jflow.iterators.Flow;
import jflow.iterators.factories.Iter;
import jflow.iterators.iterables.FlowIterable;

/**
 * @author ThomasB
 *
 */
public final class PinnedPieceCollection implements FlowIterable<PinnedPiece>
{
	private final Map<Square, PinnedPiece> cache;

	public PinnedPieceCollection(Flow<PinnedPiece> pinnedPieces)
	{
		cache = unmodifiableMap(pinnedPieces.toMap(PinnedPiece::getLocation, identity()));
	}

	@Override
	public Flow<PinnedPiece> iterator()
	{
		return Iter.over(cache.values());
	}

	public Set<Square> getLocations()
	{
		return cache.keySet();
	}

	public boolean containsLocation(Square location)
	{
		return cache.containsKey(location);
	}

	public long getConstraintAreaOfPieceAt(Square location)
	{
		return cache.get(location).getConstrainedArea();
	}
}
