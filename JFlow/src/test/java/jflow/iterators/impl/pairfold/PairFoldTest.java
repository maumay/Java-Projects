/**
 *
 */
package jflow.iterators.impl.pairfold;

import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;

import java.util.function.BiFunction;

import org.junit.jupiter.api.Test;

import jflow.iterators.AbstractFlow;
import jflow.testutilities.AbstractFlowIterable;
import jflow.testutilities.IteratorExampleProvider;
import jflow.testutilities.IteratorTest;

/**
 * @author ThomasB
 *
 */
class PairFoldTest extends IteratorExampleProvider implements IteratorTest
{
	@Test
	void test()
	{
		final AbstractFlowIterable<String> populated = getObjectTestIteratorProvider();
		final AbstractFlowIterable<String> singleton = getSingletonObjectTestIteratorProvider();
		final AbstractFlowIterable<String> empty = getEmptyObjectTestIteratorProvider();
		final BiFunction<String, String, Integer> foldFunction = (s1, s2) -> parseInt(s1) + parseInt(s2);

		assertObjectIteratorAsExpected(asList(1, 3, 5, 7), createPairFoldIteratorProviderFrom(populated, foldFunction));
		assertObjectIteratorAsExpected(asList(), createPairFoldIteratorProviderFrom(singleton, foldFunction));
		assertObjectIteratorAsExpected(asList(), createPairFoldIteratorProviderFrom(empty, foldFunction));
	}

	private <R> AbstractFlowIterable<R> createPairFoldIteratorProviderFrom(final AbstractFlowIterable<String> source, final BiFunction<String, String, R> foldFunction)
	{
		return new AbstractFlowIterable<R>() {

			@Override
			public AbstractFlow<R> iterator()
			{
				return source.iterator().pairFold(foldFunction);
			}
		};
	}
}
