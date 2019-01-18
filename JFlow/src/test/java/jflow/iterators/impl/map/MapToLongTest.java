package jflow.iterators.impl.map;

import java.util.function.DoubleToLongFunction;
import java.util.function.IntToLongFunction;
import java.util.function.ToLongFunction;

import org.junit.jupiter.api.Test;

import jflow.iterators.AbstractLongFlow;
import jflow.testutilities.AbstractFlowIterable;
import jflow.testutilities.AbstractIterableDoubles;
import jflow.testutilities.AbstractIterableInts;
import jflow.testutilities.AbstractIterableLongs;
import jflow.testutilities.IteratorExampleProvider;
import jflow.testutilities.IteratorTest;

public class MapToLongTest extends IteratorExampleProvider implements IteratorTest
{
	@Test
	public void testAbstractFlowMapToLong() 
	{
		final AbstractFlowIterable<String> populated = getObjectTestIteratorProvider();
		final AbstractFlowIterable<String> empty = getEmptyObjectTestIteratorProvider();
		final ToLongFunction<String> mapper = Long::parseLong;
		
		assertLongIteratorAsExpected(new long[] {0, 1, 2, 3, 4}, createMapToLongIteratorProviderFrom(populated, mapper));
		assertLongIteratorAsExpected(new long[] {}, createMapToLongIteratorProviderFrom(empty, mapper));
	}
	
	private <T> AbstractIterableLongs createMapToLongIteratorProviderFrom(final AbstractFlowIterable<T> src, final ToLongFunction<T> mapper)
	{
		return new AbstractIterableLongs() {
			@Override
			public AbstractLongFlow iterator() {
				return src.iterator().mapToLong(mapper);
			}
		};
	}

	@Test
	public void testAbstractIntFlowMapToLong()
	{
		final AbstractIterableInts populated = getIntTestIteratorProvider();
		AbstractIterableInts empty = getEmptyIntTestIteratorProvider();
		final IntToLongFunction mapper = x -> (long) x + 1;
		
		assertLongIteratorAsExpected(new long[] {1, 2, 3, 4, 5}, createIntMapToLongIteratorProviderFrom(populated, mapper));
		assertLongIteratorAsExpected(new long[] {}, createIntMapToLongIteratorProviderFrom(empty, mapper));
	}
	
	private AbstractIterableLongs createIntMapToLongIteratorProviderFrom(AbstractIterableInts src, IntToLongFunction mapper)
	{
		return new AbstractIterableLongs() {
			@Override
			public AbstractLongFlow iterator() {
				return src.iterator().mapToLong(mapper);
			}
		};
	}
	
	@Test
	public void testAbstractDoubleFlowMapToLong()
	{
		final AbstractIterableDoubles populated = getDoubleTestIteratorProvider();
		final AbstractIterableDoubles empty = getEmptyDoubleTestIteratorProvider();
		final DoubleToLongFunction mapper = x -> (long) (x + 1.6);
		
		assertLongIteratorAsExpected(new long[] {1, 2, 3, 4, 5}, createDoubleMapToLongIteratorProviderFrom(populated, mapper));
		assertLongIteratorAsExpected(new long[] {}, createDoubleMapToLongIteratorProviderFrom(empty, mapper));
	}
	
	private AbstractIterableLongs createDoubleMapToLongIteratorProviderFrom(AbstractIterableDoubles src, DoubleToLongFunction mapper)
	{
		return new AbstractIterableLongs() {
			@Override
			public AbstractLongFlow iterator() {
				return src.iterator().mapToLong(mapper);
			}
		};
	}
}
