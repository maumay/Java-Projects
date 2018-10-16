/**
 *
 */
package jenjinn.engine.bitboards;

import static java.lang.Long.bitCount;
import static jenjinn.engine.bitboards.BitboardUtils.bitboardsIntersect;

import java.util.NoSuchElementException;

import jenjinn.engine.base.Square;
import jflow.iterators.AbstractFlow;
import jflow.iterators.Flow;
import jflow.utilities.Optionals;

/**
 * @author ThomasB
 *
 */
public final class BitboardIterator extends AbstractFlow<Square>
{
	private final long source;
	private int cached = -1, elementsReturned = 0;

	public BitboardIterator(long source) 
	{
		super(Optionals.ofInt(bitCount(source)));
		this.source = source;
	}

	@Override
	public boolean hasNext()
	{
		return elementsReturned < size.getAsInt();
	}

	@Override
	public Square next()
	{
		if (hasNext()) {
			int loopStart = cached < 0 ? 0 : cached + 1;
			for (int i = loopStart; i < 64; i++) {
				if (bitboardsIntersect(1L << i, source)) {
					cached = i;
					elementsReturned++;
					return Square.of(cached);
				}
			}
			throw new AssertionError();
		}
		else {
			throw new NoSuchElementException();
		}
	}

	@Override
	public void skip()
	{
		next();
	}

	public static Flow<Square> from(long bitboard)
	{
		return new BitboardIterator(bitboard);
	}
}
