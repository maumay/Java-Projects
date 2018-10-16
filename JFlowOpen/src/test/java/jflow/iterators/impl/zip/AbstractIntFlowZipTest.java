/**
 *
 */
package jflow.iterators.impl.zip;

import static java.util.Arrays.asList;

import org.junit.jupiter.api.Test;

import jflow.iterators.AbstractFlow;
import jflow.iterators.misc.IntPair;
import jflow.iterators.misc.IntWith;
import jflow.testutilities.AbstractFlowIterable;
import jflow.testutilities.AbstractIterableInts;
import jflow.testutilities.IteratorExampleProvider;
import jflow.testutilities.IteratorTest;

/**
 * @author ThomasB
 */
class AbstractIntFlowZipTest extends IteratorExampleProvider implements IteratorTest
{
	@Test
	void testZipWithInt()
	{
		final AbstractIterableInts small = getSmallIntTestIteratorProvider();
		final AbstractIterableInts mid = getIntTestIteratorProvider();
		final AbstractIterableInts large = getLargeIntTestIteratorProvider();
		final AbstractIterableInts empty = getEmptyIntTestIteratorProvider();

		assertObjectIteratorAsExpected(
				asList(IntPair.of(0, 0), IntPair.of(1, 1), IntPair.of(2, 2), IntPair.of(3, 3), IntPair.of(4, 4)),
				createZipIteratorProviderFrom(mid, mid));

		assertObjectIteratorAsExpected(
				asList(IntPair.of(0, 10), IntPair.of(1, 11), IntPair.of(2, 12), IntPair.of(3, 13), IntPair.of(4, 14)),
				createZipIteratorProviderFrom(mid, large));

		assertObjectIteratorAsExpected(
				asList(IntPair.of(0, 10), IntPair.of(1, 11)),
				createZipIteratorProviderFrom(mid, small));

		assertObjectIteratorAsExpected(asList(), createZipIteratorProviderFrom(mid, empty));
		assertObjectIteratorAsExpected(asList(), createZipIteratorProviderFrom(empty, empty));
		assertObjectIteratorAsExpected(asList(), createZipIteratorProviderFrom(empty, mid));
	}

	private AbstractFlowIterable<IntPair> createZipIteratorProviderFrom(final AbstractIterableInts first, final AbstractIterableInts second)
	{
		return new AbstractFlowIterable<IntPair>() {
			@Override
			public AbstractFlow<IntPair> iterator() {
				return first.iterator().zipWith(second.iterator());
			}
		};
	}

	@Test
	void testZipWithObject()
	{
		final AbstractIterableInts populatedInts = getIntTestIteratorProvider();
		final AbstractIterableInts emptyInts = getEmptyIntTestIteratorProvider();

		final AbstractFlowIterable<String> smallObjects = getSmallObjectTestIteratorProvider();
		final AbstractFlowIterable<String> midObjects = getObjectTestIteratorProvider();
		final AbstractFlowIterable<String> largeObjects = getLargeObjectTestIteratorProvider();
		final AbstractFlowIterable<String> emptyObjects = getEmptyObjectTestIteratorProvider();

		assertObjectIteratorAsExpected(
				asList(IntWith.of(0, "10"), IntWith.of(1, "11")),
				createZipIteratorProviderFrom(populatedInts, smallObjects));

		assertObjectIteratorAsExpected(
				asList(IntWith.of(0, "0"), IntWith.of(1, "1"), IntWith.of(2, "2"), IntWith.of(3, "3"), IntWith.of(4, "4")),
				createZipIteratorProviderFrom(populatedInts, midObjects));

		assertObjectIteratorAsExpected(
				asList(IntWith.of(0, "10"), IntWith.of(1, "11"), IntWith.of(2, "12"), IntWith.of(3, "13"), IntWith.of(4, "14")),
				createZipIteratorProviderFrom(populatedInts, largeObjects));

		assertObjectIteratorAsExpected(asList(), createZipIteratorProviderFrom(emptyInts, emptyObjects));
		assertObjectIteratorAsExpected(asList(), createZipIteratorProviderFrom(emptyInts, smallObjects));
	}

	private <E> AbstractFlowIterable<IntWith<E>> createZipIteratorProviderFrom(final AbstractIterableInts first, final AbstractFlowIterable<E> second)
	{
		return new AbstractFlowIterable<IntWith<E>>() {
			@Override
			public AbstractFlow<IntWith<E>> iterator() {
				return first.iterator().zipWith(second.iterator());
			}
		};
	}
}
