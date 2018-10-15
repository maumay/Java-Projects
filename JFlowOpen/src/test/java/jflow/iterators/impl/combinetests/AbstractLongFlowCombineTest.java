package jflow.iterators.impl.combinetests;

import java.util.function.LongBinaryOperator;

import org.junit.jupiter.api.Test;

import jflow.iterators.AbstractLongFlow;
import jflow.testutilities.AbstractIterableLongs;
import jflow.testutilities.IteratorExampleProvider;
import jflow.testutilities.IteratorTest;

/**
 * @author ThomasB
 */
class AbstractLongFlowCombineTest extends IteratorExampleProvider implements IteratorTest
{
	@Test
	void test()
	{
		final AbstractIterableLongs small = getSmallLongTestIteratorProvider();
		final AbstractIterableLongs mid = getLongTestIteratorProvider();
		final AbstractIterableLongs large = getLargeLongTestIteratorProvider();
		final AbstractIterableLongs empty = getEmptyLongTestIteratorProvider();

		assertLongIteratorAsExpected(new long[] {10, 12}, createCombineIteratorProviderFrom(mid, small, (a, b) -> a + b));
		assertLongIteratorAsExpected(new long[] {0, 2, 4, 6, 8}, createCombineIteratorProviderFrom(mid, mid, (a, b) -> a + b));
		assertLongIteratorAsExpected(new long[] {10, 12, 14, 16, 18}, createCombineIteratorProviderFrom(mid, large, (a, b) -> a + b));

		assertLongIteratorAsExpected(new long[0], createCombineIteratorProviderFrom(mid, empty, (a, b) -> a + b));
		assertLongIteratorAsExpected(new long[0], createCombineIteratorProviderFrom(empty, empty, (a, b) -> a + b));
		assertLongIteratorAsExpected(new long[0], createCombineIteratorProviderFrom(empty, mid, (a, b) -> a + b));
	}

	private AbstractIterableLongs createCombineIteratorProviderFrom(final AbstractIterableLongs first, final AbstractIterableLongs second, final LongBinaryOperator combiner)
	{
		return new AbstractIterableLongs() {
			@Override
			public AbstractLongFlow iterator() {
				return first.iterator().combineWith(second.iterator(), combiner);
			}
		};
	}
}
