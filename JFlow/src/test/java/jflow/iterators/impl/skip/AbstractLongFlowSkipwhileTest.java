package jflow.iterators.impl.skip;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.function.LongPredicate;

import org.junit.jupiter.api.Test;

import jflow.iterators.AbstractLongFlow;
import jflow.iterators.misc.Pair;
import jflow.testutilities.AbstractIterableLongs;
import jflow.testutilities.IteratorExampleProvider;
import jflow.testutilities.IteratorTest;

/**
 * @author t
 */
class AbstractLongFlowSkipwhileTest extends IteratorExampleProvider implements IteratorTest
{
	@Test
	void test()
	{
		final AbstractIterableLongs populated = getLongTestIteratorProvider();
		final AbstractIterableLongs empty = getEmptyLongTestIteratorProvider();

		final List<Pair<long[], LongPredicate>> testData = asList(
				Pair.of(new long[] {0, 1, 2, 3, 4}, x -> x < -0.1),
				Pair.of(new long[] {4}, x -> x < 3.1),
				Pair.of(new long[] {}, x -> x < 5)
				);

		testData.stream().forEach(testCase ->
		{
			assertLongIteratorAsExpected(testCase._1(), createSkipwhileIteratorProviderFrom(populated, testCase._2()));
			assertLongIteratorAsExpected(new long[] {}, createSkipwhileIteratorProviderFrom(empty, testCase._2()));
		});
	}

	private  AbstractIterableLongs createSkipwhileIteratorProviderFrom(AbstractIterableLongs src, LongPredicate predicate)
	{
		return new AbstractIterableLongs()
		{
			@Override
			public AbstractLongFlow iterator()
			{
				return src.iterator().dropWhile(predicate);
			}
		};
	}
}
