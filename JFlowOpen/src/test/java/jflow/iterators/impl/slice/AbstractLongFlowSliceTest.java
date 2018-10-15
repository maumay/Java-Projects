/**
 *
 */
package jflow.iterators.impl.slice;

import java.util.function.IntUnaryOperator;

import org.junit.jupiter.api.Test;

import jflow.iterators.AbstractLongFlow;
import jflow.testutilities.AbstractIterableLongs;
import jflow.testutilities.IteratorExampleProvider;
import jflow.testutilities.IteratorTest;

/**
 * @author ThomasB
 *
 */
class AbstractLongFlowSliceTest extends IteratorExampleProvider implements IteratorTest
{
	@Test
	void test()
	{
		final AbstractIterableLongs populated = getLongTestIteratorProvider();
		final AbstractIterableLongs empty = getEmptyLongTestIteratorProvider();

		final IntUnaryOperator allSlicedOperator = i -> i;
		assertLongIteratorAsExpected(new long[] {0, 1, 2, 3, 4}, createSlicedIteratorProviderFrom(populated, allSlicedOperator));
		assertLongIteratorAsExpected(new long[] {}, createSlicedIteratorProviderFrom(empty, allSlicedOperator));

		final IntUnaryOperator someSlicedOperator = i -> 2*i;
		assertLongIteratorAsExpected(new long[] {0, 2, 4}, createSlicedIteratorProviderFrom(populated, someSlicedOperator));
		assertLongIteratorAsExpected(new long[] {}, createSlicedIteratorProviderFrom(empty, someSlicedOperator));

		final IntUnaryOperator noneSlicedOperator = i -> i + 5;
		assertLongIteratorAsExpected(new long[] {}, createSlicedIteratorProviderFrom(populated, noneSlicedOperator));
		assertLongIteratorAsExpected(new long[] {}, createSlicedIteratorProviderFrom(empty, noneSlicedOperator));
	}

	private AbstractIterableLongs createSlicedIteratorProviderFrom(final AbstractIterableLongs src, final IntUnaryOperator indexMapping)
	{
		return new AbstractIterableLongs() {
			@Override
			public AbstractLongFlow iterator() {
				return src.iterator().slice(indexMapping);
			}
		};
	}
}
