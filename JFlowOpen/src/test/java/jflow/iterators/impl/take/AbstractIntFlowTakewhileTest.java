package jflow.iterators.impl.take;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.function.IntPredicate;

import org.junit.jupiter.api.Test;

import jflow.iterators.AbstractIntFlow;
import jflow.iterators.misc.Pair;
import jflow.testutilities.AbstractIterableInts;
import jflow.testutilities.IteratorExampleProvider;
import jflow.testutilities.IteratorTest;

class AbstractIntFlowTakewhileTest extends IteratorExampleProvider implements IteratorTest
{
	@Test
	void test()
	{
		final AbstractIterableInts populated = getIntTestIteratorProvider();
		final AbstractIterableInts empty = getEmptyIntTestIteratorProvider();

		final List<Pair<int[], IntPredicate>> testData = asList(
				Pair.of(new int[] {}, x -> x < -0.1),
				Pair.of(new int[] {0, 1, 2, 3}, x -> x < 3.1),
				Pair.of(new int[] {0, 1, 2, 3, 4}, x -> x < 5)
				);

		testData.stream().forEach(testCase ->
		{
			assertIntIteratorAsExpected(testCase.first(), createTakewhileIteratorProviderFrom(populated, testCase.second()));
			assertIntIteratorAsExpected(new int[] {}, createTakewhileIteratorProviderFrom(empty, testCase.second()));
		});
	}

	private  AbstractIterableInts createTakewhileIteratorProviderFrom(AbstractIterableInts src, IntPredicate predicate)
	{
		return new AbstractIterableInts()
		{
			@Override
			public AbstractIntFlow iterator()
			{
				return src.iterator().takeWhile(predicate);
			}
		};
	}
}
