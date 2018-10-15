/**
 *
 */
package jflow.iterators.impl.fromvalues;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import jflow.iterators.AbstractLongFlow;
import jflow.iterators.impl.FlowFromValues;
import jflow.testutilities.AbstractIterableLongs;
import jflow.testutilities.IteratorTest;

/**
 * @author ThomasB
 */
class AbstractLongFlowFromValuesTest implements IteratorTest
{
	@ParameterizedTest
	@MethodSource("creationTestDataProvider")
	void testCreationAsExpected(final long[] source)
	{
		assertLongIteratorAsExpected(source, getCreationFromValuesIteratorProvider(source));
	}

	static Stream<Arguments> creationTestDataProvider()
	{
		return Stream.of(
				Arguments.of(new long[0]),
				Arguments.of(new long[] {1, 2})
				);
	}

	AbstractIterableLongs getCreationFromValuesIteratorProvider(final long[] source)
	{
		return new AbstractIterableLongs() {
			@Override
			public AbstractLongFlow iterator() {
				return new FlowFromValues.OfLong(source);
			}
		};
	}
}
