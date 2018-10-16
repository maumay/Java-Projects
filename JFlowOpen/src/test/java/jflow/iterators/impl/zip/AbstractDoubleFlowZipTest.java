/**
 *
 */
package jflow.iterators.impl.zip;

import static java.util.Arrays.asList;

import org.junit.jupiter.api.Test;

import jflow.iterators.AbstractFlow;
import jflow.iterators.misc.DoublePair;
import jflow.iterators.misc.DoubleWith;
import jflow.testutilities.AbstractFlowIterable;
import jflow.testutilities.AbstractIterableDoubles;
import jflow.testutilities.IteratorExampleProvider;
import jflow.testutilities.IteratorTest;

/**
 * @author ThomasB
 *
 */
class AbstractDoubleFlowZipTest extends IteratorExampleProvider implements IteratorTest
{
	@Test
	void testZipWithDouble()
	{
		final AbstractIterableDoubles small = getSmallDoubleTestIteratorProvider();
		final AbstractIterableDoubles mid = getDoubleTestIteratorProvider();
		final AbstractIterableDoubles large = getLargeDoubleTestIteratorProvider();
		final AbstractIterableDoubles empty = getEmptyDoubleTestIteratorProvider();

		assertObjectIteratorAsExpected(
				asList(DoublePair.of(0, 0), DoublePair.of(1, 1), DoublePair.of(2, 2), DoublePair.of(3, 3), DoublePair.of(4, 4)),
				createZipIteratorProviderFrom(mid, mid));

		assertObjectIteratorAsExpected(
				asList(DoublePair.of(0, 10), DoublePair.of(1, 11), DoublePair.of(2, 12), DoublePair.of(3, 13), DoublePair.of(4, 14)),
				createZipIteratorProviderFrom(mid, large));

		assertObjectIteratorAsExpected(
				asList(DoublePair.of(0, 10), DoublePair.of(1, 11)),
				createZipIteratorProviderFrom(mid, small));

		assertObjectIteratorAsExpected(asList(), createZipIteratorProviderFrom(mid, empty));
		assertObjectIteratorAsExpected(asList(), createZipIteratorProviderFrom(empty, empty));
		assertObjectIteratorAsExpected(asList(), createZipIteratorProviderFrom(empty, mid));
	}

	private AbstractFlowIterable<DoublePair> createZipIteratorProviderFrom(final AbstractIterableDoubles first, final AbstractIterableDoubles second)
	{
		return new AbstractFlowIterable<DoublePair>() {
			@Override
			public AbstractFlow<DoublePair> iterator() {
				return first.iterator().zipWith(second.iterator());
			}
		};
	}

	@Test
	void testZipWithObject()
	{
		final AbstractIterableDoubles populatedDoubles = getDoubleTestIteratorProvider();
		final AbstractIterableDoubles emptyDoubles = getEmptyDoubleTestIteratorProvider();

		final AbstractFlowIterable<String> smallObjects = getSmallObjectTestIteratorProvider();
		final AbstractFlowIterable<String> midObjects = getObjectTestIteratorProvider();
		final AbstractFlowIterable<String> largeObjects = getLargeObjectTestIteratorProvider();
		final AbstractFlowIterable<String> emptyObjects = getEmptyObjectTestIteratorProvider();

		assertObjectIteratorAsExpected(
				asList(DoubleWith.of(0, "10"), DoubleWith.of(1, "11")),
				createZipIteratorProviderFrom(populatedDoubles, smallObjects));

		assertObjectIteratorAsExpected(
				asList(DoubleWith.of(0, "0"), DoubleWith.of(1, "1"), DoubleWith.of(2, "2"), DoubleWith.of(3, "3"), DoubleWith.of(4, "4")),
				createZipIteratorProviderFrom(populatedDoubles, midObjects));

		assertObjectIteratorAsExpected(
				asList(DoubleWith.of(0, "10"), DoubleWith.of(1, "11"), DoubleWith.of(2, "12"), DoubleWith.of(3, "13"), DoubleWith.of(4, "14")),
				createZipIteratorProviderFrom(populatedDoubles, largeObjects));

		assertObjectIteratorAsExpected(asList(), createZipIteratorProviderFrom(emptyDoubles, emptyObjects));
		assertObjectIteratorAsExpected(asList(), createZipIteratorProviderFrom(emptyDoubles, smallObjects));
	}

	private <E> AbstractFlowIterable<DoubleWith<E>> createZipIteratorProviderFrom(final AbstractIterableDoubles first, final AbstractFlowIterable<E> second)
	{
		return new AbstractFlowIterable<DoubleWith<E>>() {
			@Override
			public AbstractFlow<DoubleWith<E>> iterator() {
				return first.iterator().zipWith(second.iterator());
			}
		};
	}
}
