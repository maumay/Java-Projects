package jflow.iterators.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.OptionalInt;
import java.util.function.Function;

import jflow.iterators.AbstractDoubleFlow;
import jflow.iterators.AbstractFlow;
import jflow.iterators.AbstractIntFlow;
import jflow.iterators.AbstractLongFlow;
import jflow.iterators.DoubleFlow;
import jflow.iterators.Flow;
import jflow.iterators.IntFlow;
import jflow.iterators.LongFlow;

/**
 * @author t
 */
public final class FlattenedFlow
{
	private FlattenedFlow() {}

	public static class OfObject<E, R> extends AbstractFlow<R>
	{
		private final Flow<E> src;
		private final Function<? super E, ? extends Iterator<? extends R>> mapping;

		private Iterator<? extends R> currentSubFlow;

		public OfObject(final Flow<E> src, final Function<? super E, ? extends Iterator<? extends R>> mapping)
		{
			super(OptionalInt.empty());
			this.src = src;
			this.mapping = mapping;
		}

		private void init()
		{
			while (src.hasNext() && (currentSubFlow == null || !currentSubFlow.hasNext())) {
				currentSubFlow = mapping.apply(src.next());
			}
		}

		@Override
		public boolean hasNext()
		{
			if (currentSubFlow == null) {
				init();
			}
			return currentSubFlow != null && currentSubFlow.hasNext();
		}

		@Override
		public R next()
		{
			if (currentSubFlow == null) {
				init();
			}

			if (currentSubFlow == null) {
				throw new NoSuchElementException();
			}
			else {
				final R next = currentSubFlow.next();
				while (!currentSubFlow.hasNext() && src.hasNext()) {
					currentSubFlow = mapping.apply(src.next());
				}
				return next;
			}
		}

		@Override
		public void skip()
		{
			next();
		}
	}

	public static class OfLong<E> extends AbstractLongFlow
	{
		private final Flow<E> src;
		private final Function<? super E, ? extends LongFlow> mapping;

		private LongFlow currentSubFlow;

		public OfLong(final Flow<E> src, final Function<? super E, ? extends LongFlow> mapping)
		{
			super(OptionalInt.empty());
			this.src = src;
			this.mapping = mapping;
		}

		private void init()
		{
			while (src.hasNext() && (currentSubFlow == null || !currentSubFlow.hasNext())) {
				currentSubFlow = mapping.apply(src.next());
			}
		}

		@Override
		public boolean hasNext()
		{
			if (currentSubFlow == null) {
				init();
			}
			return currentSubFlow != null && currentSubFlow.hasNext();
		}

		@Override
		public long nextLong()
		{
			if (currentSubFlow == null) {
				init();
			}

			if (currentSubFlow == null) {
				throw new NoSuchElementException();
			}
			else {
				final long next = currentSubFlow.nextLong();
				while (!currentSubFlow.hasNext() && src.hasNext()) {
					currentSubFlow = mapping.apply(src.next());
				}
				return next;
			}
		}

		@Override
		public void skip()
		{
			nextLong();
		}
	}

	public static class OfDouble<E> extends AbstractDoubleFlow
	{
		private final Flow<E> src;
		private final Function<? super E, ? extends DoubleFlow> mapping;

		private DoubleFlow currentSubFlow;

		public OfDouble(final Flow<E> src, final Function<? super E, ? extends DoubleFlow> mapping)
		{
			super(OptionalInt.empty());
			this.src = src;
			this.mapping = mapping;
		}

		private void init()
		{
			while (src.hasNext() && (currentSubFlow == null || !currentSubFlow.hasNext())) {
				currentSubFlow = mapping.apply(src.next());
			}
		}

		@Override
		public boolean hasNext()
		{
			if (currentSubFlow == null) {
				init();
			}
			return currentSubFlow != null && currentSubFlow.hasNext();
		}

		@Override
		public double nextDouble()
		{
			if (currentSubFlow == null) {
				init();
			}

			if (currentSubFlow == null) {
				throw new NoSuchElementException();
			}
			else {
				final double next = currentSubFlow.nextDouble();
				while (!currentSubFlow.hasNext() && src.hasNext()) {
					currentSubFlow = mapping.apply(src.next());
				}
				return next;
			}
		}

		@Override
		public void skip()
		{
			nextDouble();
		}
	}

	public static class OfInt<E> extends AbstractIntFlow
	{
		private final Flow<E> src;
		private final Function<? super E, ? extends IntFlow> mapping;

		private IntFlow currentSubFlow;

		public OfInt(final Flow<E> src, final Function<? super E, ? extends IntFlow> mapping)
		{
			super(OptionalInt.empty());
			this.src = src;
			this.mapping = mapping;
		}

		private void init()
		{
			while (src.hasNext() && (currentSubFlow == null || !currentSubFlow.hasNext())) {
				currentSubFlow = mapping.apply(src.next());
			}
		}

		@Override
		public boolean hasNext()
		{
			if (currentSubFlow == null) {
				init();
			}
			return currentSubFlow != null && currentSubFlow.hasNext();
		}

		@Override
		public int nextInt()
		{
			if (currentSubFlow == null) {
				init();
			}

			if (currentSubFlow == null) {
				throw new NoSuchElementException();
			}
			else {
				final int next = currentSubFlow.nextInt();
				while (!currentSubFlow.hasNext() && src.hasNext()) {
					currentSubFlow = mapping.apply(src.next());
				}
				return next;
			}
		}

		@Override
		public void skip()
		{
			nextInt();
		}
	}
}
