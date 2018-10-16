/**
 *
 */
package jflow.iterators.impl.zip;

import static java.util.Arrays.asList;

import org.junit.jupiter.api.Test;

import jflow.iterators.AbstractFlow;
import jflow.iterators.misc.LongPair;
import jflow.iterators.misc.LongWith;
import jflow.testutilities.AbstractFlowIterable;
import jflow.testutilities.AbstractIterableLongs;
import jflow.testutilities.IteratorExampleProvider;
import jflow.testutilities.IteratorTest;

/**
 * @author ThomasB
 *
 */
class AbstractLongFlowZipTest extends IteratorExampleProvider implements IteratorTest
{
	@Test
	void testZipWithLong()
	{
		final AbstractIterableLongs small = getSmallLongTestIteratorProvider();
		final AbstractIterableLongs mid = getLongTestIteratorProvider();
		final AbstractIterableLongs large = getLargeLongTestIteratorProvider();
		final AbstractIterableLongs empty = getEmptyLongTestIteratorProvider();

		assertObjectIteratorAsExpected(
				asList(LongPair.of(0, 0), LongPair.of(1, 1), LongPair.of(2, 2), LongPair.of(3, 3), LongPair.of(4, 4)),
				createZipIteratorProviderFrom(mid, mid));

		assertObjectIteratorAsExpected(
				asList(LongPair.of(0, 10), LongPair.of(1, 11), LongPair.of(2, 12), LongPair.of(3, 13), LongPair.of(4, 14)),
				createZipIteratorProviderFrom(mid, large));

		assertObjectIteratorAsExpected(
				asList(LongPair.of(0, 10), LongPair.of(1, 11)),
				createZipIteratorProviderFrom(mid, small));

		assertObjectIteratorAsExpected(asList(), createZipIteratorProviderFrom(mid, empty));
		assertObjectIteratorAsExpected(asList(), createZipIteratorProviderFrom(empty, empty));
		assertObjectIteratorAsExpected(asList(), createZipIteratorProviderFrom(empty, mid));
	}

	private AbstractFlowIterable<LongPair> createZipIteratorProviderFrom(final AbstractIterableLongs first, final AbstractIterableLongs second)
	{
		return new AbstractFlowIterable<LongPair>() {
			@Override
			public AbstractFlow<LongPair> iterator() {
				return first.iterator().zipWith(second.iterator());
			}
		};
	}

	@Test
	void testZipWithObject()
	{
		final AbstractIterableLongs populatedLongs = getLongTestIteratorProvider();
		final AbstractIterableLongs emptyLongs = getEmptyLongTestIteratorProvider();

		final AbstractFlowIterable<String> smallObjects = getSmallObjectTestIteratorProvider();
		final AbstractFlowIterable<String> midObjects = getObjectTestIteratorProvider();
		final AbstractFlowIterable<String> largeObjects = getLargeObjectTestIteratorProvider();
		final AbstractFlowIterable<String> emptyObjects = getEmptyObjectTestIteratorProvider();

		assertObjectIteratorAsExpected(
				asList(LongWith.of(0, "10"), LongWith.of(1, "11")),
				createZipIteratorProviderFrom(populatedLongs, smallObjects));

		assertObjectIteratorAsExpected(
				asList(LongWith.of(0, "0"), LongWith.of(1, "1"), LongWith.of(2, "2"), LongWith.of(3, "3"), LongWith.of(4, "4")),
				createZipIteratorProviderFrom(populatedLongs, midObjects));

		assertObjectIteratorAsExpected(
				asList(LongWith.of(0, "10"), LongWith.of(1, "11"), LongWith.of(2, "12"), LongWith.of(3, "13"), LongWith.of(4, "14")),
				createZipIteratorProviderFrom(populatedLongs, largeObjects));

		assertObjectIteratorAsExpected(asList(), createZipIteratorProviderFrom(emptyLongs, emptyObjects));
		assertObjectIteratorAsExpected(asList(), createZipIteratorProviderFrom(emptyLongs, smallObjects));
	}

	private <E> AbstractFlowIterable<LongWith<E>> createZipIteratorProviderFrom(final AbstractIterableLongs first, final AbstractFlowIterable<E> second)
	{
		return new AbstractFlowIterable<LongWith<E>>() {
			@Override
			public AbstractFlow<LongWith<E>> iterator() {
				return first.iterator().zipWith(second.iterator());
			}
		};
	}
}
