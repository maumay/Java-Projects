package jflow.iterators.impl.skip;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import jflow.iterators.AbstractFlow;
import jflow.iterators.misc.Pair;
import jflow.testutilities.AbstractFlowIterable;
import jflow.testutilities.IteratorExampleProvider;
import jflow.testutilities.IteratorTest;

/**
 * @author t
 */
class AbstractFlowSkipwhileTest extends IteratorExampleProvider implements IteratorTest
{
	@Test
	void test()
	{
		final AbstractFlowIterable<String> populated = getObjectTestIteratorProvider();
		final AbstractFlowIterable<String> empty = getEmptyObjectTestIteratorProvider();

		final List<Pair<List<String>, Predicate<String>>> testData = asList(
				Pair.of(asList(), string -> !string.equals("5")),
				Pair.of(asList("3", "4"), string -> !string.equals("3")),
				Pair.of(asList("0", "1", "2", "3", "4"), string -> !string.equals("0"))
				);

		testData.stream().forEach(testCase ->
		{
			assertObjectIteratorAsExpected(testCase._1(), createSkipwhileIteratorProviderFrom(populated, testCase._2()));
			assertObjectIteratorAsExpected(asList(), createSkipwhileIteratorProviderFrom(empty, testCase._2()));
		});
	}

	private <T> AbstractFlowIterable<T> createSkipwhileIteratorProviderFrom(AbstractFlowIterable<T> src, Predicate<T> predicate)
	{
		return new AbstractFlowIterable<T>()
		{
			@Override
			public AbstractFlow<T> iterator()
			{
				return src.iterator().dropWhile(predicate);
			}
		};
	}
}
