/**
 *
 */
package jflow.iterators.impl;

import java.util.function.DoubleFunction;
import java.util.function.IntFunction;
import java.util.function.LongFunction;

import jflow.iterators.AbstractFlow;
import jflow.iterators.DoubleFlow;
import jflow.iterators.IntFlow;
import jflow.iterators.LongFlow;

/**
 * @author ThomasB
 *
 */
public final class MapToObjectFlow
{
	private MapToObjectFlow() {}

	public static class FromLong<E> extends AbstractFlow<E>
	{
		private final LongFlow sourceFlow;
		private final LongFunction<? extends E> mappingFunction;

		public FromLong(LongFlow src, LongFunction<? extends E> mappingFunction)
		{
			super(src.size());
			this.sourceFlow = src;
			this.mappingFunction = mappingFunction;
		}

		@Override
		public boolean hasNext()
		{
			return sourceFlow.hasNext();
		}

		@Override
		public E next()
		{
			return mappingFunction.apply(sourceFlow.nextLong());
		}

		@Override
		public void skip()
		{
			sourceFlow.skip();
		}
	}

	public static class FromInt<E> extends AbstractFlow<E>
	{
		private final IntFlow sourceFlow;
		private final IntFunction<? extends E> mappingFunction;

		public FromInt(final IntFlow src, final IntFunction<? extends E> mappingFunction)
		{
			super(src.size());
			this.sourceFlow = src;
			this.mappingFunction = mappingFunction;
		}

		@Override
		public boolean hasNext()
		{
			return sourceFlow.hasNext();
		}

		@Override
		public E next()
		{
			return mappingFunction.apply(sourceFlow.nextInt());
		}

		@Override
		public void skip()
		{
			sourceFlow.skip();
		}
	}

	public static class FromDouble<E> extends AbstractFlow<E>
	{
		private final DoubleFlow sourceFlow;
		private final DoubleFunction<? extends E> mappingFunction;

		public FromDouble(DoubleFlow src, DoubleFunction<? extends E> mappingFunction)
		{
			super(src.size());
			this.sourceFlow = src;
			this.mappingFunction = mappingFunction;
		}

		@Override
		public boolean hasNext()
		{
			return sourceFlow.hasNext();
		}

		@Override
		public E next()
		{
			return mappingFunction.apply(sourceFlow.nextDouble());
		}

		@Override
		public void skip()
		{
			sourceFlow.skip();
		}
	}
}
