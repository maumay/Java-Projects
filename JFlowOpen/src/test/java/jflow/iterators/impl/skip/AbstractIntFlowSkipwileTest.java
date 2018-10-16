package jflow.iterators.impl.skip;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.function.IntPredicate;

import org.junit.jupiter.api.Test;

import jflow.iterators.AbstractIntFlow;
import jflow.iterators.misc.Pair;
import jflow.testutilities.AbstractIterableInts;
import jflow.testutilities.IteratorExampleProvider;
import jflow.testutilities.IteratorTest;

/**
 * @author t
 */
class AbstractIntFlowSkipwileTest extends IteratorExampleProvider implements IteratorTest
{
	@Test
	void test()
	{
		final AbstractIterableInts populated = getIntTestIteratorProvider();
		final AbstractIterableInts empty = getEmptyIntTestIteratorProvider();

		final List<Pair<int[], IntPredicate>> testData = asList(
				Pair.of(new int[] {0, 1, 2, 3, 4}, x -> x < -0.1),
				Pair.of(new int[] {4}, x -> x < 3.1),
				Pair.of(new int[] {}, x -> x < 5)
				);

		testData.stream().forEach(testCase ->
		{
			assertIntIteratorAsExpected(testCase._1(), createSkipwhileIteratorProviderFrom(populated, testCase._2()));
			assertIntIteratorAsExpected(new int[] {}, createSkipwhileIteratorProviderFrom(empty, testCase._2()));
		});
	}

	private  AbstractIterableInts createSkipwhileIteratorProviderFrom(AbstractIterableInts src, IntPredicate predicate)
	{
		return new AbstractIterableInts()
		{
			@Override
			public AbstractIntFlow iterator()
			{
				return src.iterator().dropWhile(predicate);
			}
		};
	}
}
