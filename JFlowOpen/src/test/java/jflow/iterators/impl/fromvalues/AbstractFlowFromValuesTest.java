package jflow.iterators.impl.fromvalues;

import static java.util.Arrays.asList;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import jflow.iterators.AbstractFlow;
import jflow.iterators.impl.FlowFromValues;
import jflow.testutilities.AbstractFlowIterable;
import jflow.testutilities.IteratorTest;

/**
 * @author ThomasB
 */
class AbstractFlowFromValuesTest implements IteratorTest
{
	@ParameterizedTest
	@MethodSource("creationTestDataProvider")
	void testCreationAsExpected(final String[] source)
	{
		assertObjectIteratorAsExpected(asList(source), getCreationFromValuesIteratorProvider(source));
	}

	static Stream<Arguments> creationTestDataProvider()
	{
		return Stream.of(
				Arguments.of(new Object[] {new String[0]}),
				Arguments.of(new Object[] {new String[] {"1", "1"}})
				);
	}

	<E> AbstractFlowIterable<E> getCreationFromValuesIteratorProvider(final E[] source)
	{
		return new AbstractFlowIterable<E>() {
			@Override
			public AbstractFlow<E> iterator() {
				return new FlowFromValues.OfObject<>(source);
			}
		};
	}
}
