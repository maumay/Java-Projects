/**
 *
 */
package jflow.iterators.impl.fromvalues;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import jflow.iterators.AbstractDoubleFlow;
import jflow.iterators.impl.FlowFromValues;
import jflow.testutilities.AbstractIterableDoubles;
import jflow.testutilities.IteratorTest;

/**
 * @author ThomasB
 */
class AbstractDoubleFlowFromValuesTest implements IteratorTest
{
	@ParameterizedTest
	@MethodSource("creationTestDataProvider")
	void testCreationAsExpected(final double[] source)
	{
		assertDoubleIteratorAsExpected(source, getCreationFromValuesIteratorProvider(source));
	}

	static Stream<Arguments> creationTestDataProvider()
	{
		return Stream.of(
				Arguments.of(new double[0]),
				Arguments.of(new double[] {1, 2})
				);
	}

	AbstractIterableDoubles getCreationFromValuesIteratorProvider(final double[] source)
	{
		return new AbstractIterableDoubles() {
			@Override
			public AbstractDoubleFlow iterator() {
				return new FlowFromValues.OfDouble(source);
			}
		};
	}
}
