/**
 *
 */
package jflow.iterators.impl.scan;

import static java.util.Arrays.asList;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

import org.junit.jupiter.api.Test;

import jflow.iterators.AbstractFlow;
import jflow.testutilities.AbstractFlowIterable;
import jflow.testutilities.IteratorExampleProvider;
import jflow.testutilities.IteratorTest;

/**
 * @author ThomasB
 *
 */
class AbstractFlowAccumulationTest extends IteratorExampleProvider implements IteratorTest
{
	@Test
	void testAccumulationWithoutId()
	{
		final AbstractFlowIterable<String> populated = getObjectTestIteratorProvider();
		final AbstractFlowIterable<String> empty = getEmptyObjectTestIteratorProvider();

		assertObjectIteratorAsExpected(asList("0", "01", "012", "0123", "01234"), createAccumlationWithoutIdIteratorProviderFrom(populated, String::concat));
		assertObjectIteratorAsExpected(asList(), createAccumlationWithoutIdIteratorProviderFrom(empty, String::concat));
	}

	private <E> AbstractFlowIterable<E> createAccumlationWithoutIdIteratorProviderFrom(final AbstractFlowIterable<E> source, final BinaryOperator<E> accumulator)
	{
		return new AbstractFlowIterable<E>() {
			@Override
			public AbstractFlow<E> iterator() {
				return source.iterator().scan(accumulator);
			}
		};
	}

	@Test
	void testAccumulationWithId()
	{
		final AbstractFlowIterable<String> populated = getObjectTestIteratorProvider();
		final AbstractFlowIterable<String> empty = getEmptyObjectTestIteratorProvider();

		assertObjectIteratorAsExpected(asList("x0", "x01", "x012", "x0123", "x01234"), createAccumlationWithIdIteratorProviderFrom(populated, "x", String::concat));
		assertObjectIteratorAsExpected(asList(), createAccumlationWithIdIteratorProviderFrom(empty, "x", String::concat));
	}

	private <E, R> AbstractFlowIterable<R> createAccumlationWithIdIteratorProviderFrom(final AbstractFlowIterable<E> source, final R id, final BiFunction<R, E, R> accumulator)
	{
		return new AbstractFlowIterable<R>() {
			@Override
			public AbstractFlow<R> iterator() {
				return source.iterator().scan(id, accumulator);
			}
		};
	}
}
