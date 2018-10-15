/**
 *
 */
package jflow.iterators.impl.reversefromvalues;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import jflow.iterators.AbstractDoubleFlow;
import jflow.iterators.impl.ReverseFlowFromValues;
import jflow.testutilities.AbstractIterableDoubles;
import jflow.testutilities.IteratorTest;

/**
 * @author ThomasB
 */
class AbstractDoubleFlowReverseFromValuesTest implements IteratorTest
{
	@ParameterizedTest
	@MethodSource("creationTestDataProvider")
	void testCreationAsExpected(final double[] source)
	{
		assertDoubleIteratorAsExpected(reverse(source), getCreationFromValuesIteratorProvider(source));
	}

	static Stream<Arguments> creationTestDataProvider()
	{
		return Stream.of(
				Arguments.of(new double[0]),
				Arguments.of(new double[] {1, 2})
				);
	}

	double[] reverse(final double[] toReverse)
	{
		final double[] reversed = new double[toReverse.length];
		for (int i = 0; i < toReverse.length; i++) {
			reversed[i] = toReverse[toReverse.length - 1 - i];
		}
		return reversed;
	}

	AbstractIterableDoubles getCreationFromValuesIteratorProvider(final double[] source)
	{
		return new AbstractIterableDoubles() {
			@Override
			public AbstractDoubleFlow iterator() {
				return new ReverseFlowFromValues.OfDouble(source);
			}
		};
	}
}
