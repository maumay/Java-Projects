/**
 *
 */
package jflow.iterators;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.PrimitiveIterator.OfDouble;
import java.util.PrimitiveIterator.OfInt;
import java.util.PrimitiveIterator.OfLong;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import jflow.iterators.factories.Numbers;
import jflow.iterators.impl.AccumulationFlow;
import jflow.iterators.impl.AppendFlow;
import jflow.iterators.impl.DropFlow;
import jflow.iterators.impl.DropwhileFlow;
import jflow.iterators.impl.FilteredFlow;
import jflow.iterators.impl.FlattenedFlow;
import jflow.iterators.impl.InsertFlow;
import jflow.iterators.impl.MapFlow;
import jflow.iterators.impl.MapToDoubleFlow;
import jflow.iterators.impl.MapToIntFlow;
import jflow.iterators.impl.MapToLongFlow;
import jflow.iterators.impl.ObjectMinMaxConsumption;
import jflow.iterators.impl.ObjectPredicateConsumption;
import jflow.iterators.impl.ObjectReductionConsumption;
import jflow.iterators.impl.PairFoldFlow;
import jflow.iterators.impl.SlicedFlow;
import jflow.iterators.impl.TakeFlow;
import jflow.iterators.impl.TakewhileFlow;
import jflow.iterators.impl.ZipFlow;
import jflow.iterators.misc.DoubleWith;
import jflow.iterators.misc.IntWith;
import jflow.iterators.misc.LongWith;
import jflow.iterators.misc.Pair;

/**
 * A skeletal implementation of a Flow, users writing custom Flows should
 * subclass this class.
 *
 * @param <E>
 *            The type of elements produced by this Flow.
 *
 * @author ThomasB
 */
public abstract class AbstractFlow<E> extends AbstractOptionallySized implements Flow<E>
{
	public AbstractFlow(OptionalInt size)
	{
		super(size);
	}

	@Override
	public <R> AbstractFlow<R> map(Function<? super E, ? extends R> f)
	{
		return new MapFlow.OfObject<>(this, f);
	}

	@Override
	public AbstractIntFlow mapToInt(ToIntFunction<? super E> f)
	{
		return new MapToIntFlow.FromObject<>(this, f);
	}

	@Override
	public AbstractDoubleFlow mapToDouble(ToDoubleFunction<? super E> f)
	{
		return new MapToDoubleFlow.FromObject<>(this, f);
	}

	@Override
	public AbstractLongFlow mapToLong(ToLongFunction<? super E> f)
	{
		return new MapToLongFlow.FromObject<>(this, f);
	}

	@Override
	public <R> AbstractFlow<R> flatMap(Function<? super E, ? extends Iterator<? extends R>> mapping)
	{
		return new FlattenedFlow.OfObject<>(this, mapping);
	}

	@Override
	public AbstractIntFlow flatMapToInt(Function<? super E, ? extends IntFlow> mapping)
	{
		return new FlattenedFlow.OfInt<>(this, mapping);
	}

	@Override
	public AbstractLongFlow flatMapToLong(Function<? super E, ? extends LongFlow> mapping)
	{
		return new FlattenedFlow.OfLong<>(this, mapping);
	}

	@Override
	public AbstractDoubleFlow flatMapToDouble(Function<? super E, ? extends DoubleFlow> mapping)
	{
		return new FlattenedFlow.OfDouble<>(this, mapping);
	}

	@Override
	public <R> AbstractFlow<Pair<E, R>> zipWith(Iterator<? extends R> other)
	{
		return new ZipFlow.OfObjects<>(this, other);
	}

	@Override
	public AbstractFlow<IntWith<E>> zipWith(OfInt other)
	{
		return new ZipFlow.OfObjectAndInt<>(this, other);
	}

	@Override
	public AbstractFlow<DoubleWith<E>> zipWith(OfDouble other)
	{
		return new ZipFlow.OfObjectAndDouble<>(this, other);
	}

	@Override
	public AbstractFlow<LongWith<E>> zipWith(OfLong other)
	{
		return new ZipFlow.OfObjectAndLong<>(this, other);
	}

	@Override
	public AbstractFlow<IntWith<E>> enumerate()
	{
		return zipWith(Numbers.natural());
	}

	@Override
	public AbstractFlow<E> slice(IntUnaryOperator f)
	{
		return new SlicedFlow.OfObject<>(this, f);
	}

	@Override
	public AbstractFlow<E> take(int n)
	{
		return new TakeFlow.OfObject<>(this, n);
	}

	@Override
	public AbstractFlow<E> takeWhile(Predicate<? super E> predicate)
	{
		return new TakewhileFlow.OfObject<>(this, predicate);
	}

	@Override
	public AbstractFlow<E> drop(int n)
	{
		return new DropFlow.OfObject<>(this, n);
	}

	@Override
	public AbstractFlow<E> dropWhile(Predicate<? super E> predicate)
	{
		return new DropwhileFlow.OfObject<>(this, predicate);
	}

	@Override
	public AbstractFlow<E> filter(Predicate<? super E> predicate)
	{
		return new FilteredFlow.OfObject<>(this, predicate);
	}

	@Override
	public AbstractFlow<E> append(Iterator<? extends E> other)
	{
		return new AppendFlow.OfObject<>(this, other);
	}

	@Override
	public AbstractFlow<E> insert(Iterator<? extends E> other)
	{
		return new InsertFlow.OfObject<>(this, other);
	}

	@Override
	public AbstractFlow<E> scan(BinaryOperator<E> accumulator)
	{
		return new AccumulationFlow.OfObject<>(this, accumulator);
	}

	@Override
	public <R> AbstractFlow<R> scan(R id, BiFunction<R, E, R> accumulator)
	{
		return new AccumulationFlow.OfObjectWithMixedTypes<>(this, id, accumulator);
	}

	@Override
	public <R> AbstractFlow<R> pairFold(BiFunction<? super E, ? super E, R> foldFunction)
	{
		return new PairFoldFlow.OfObject<>(this, foldFunction);
	}

	@Override
	public Optional<E> min(Comparator<? super E> orderingFunction)
	{
		return ObjectMinMaxConsumption.findMin(this, orderingFunction);
	}

	@Override
	public Optional<E> max(Comparator<? super E> orderingFunction)
	{
		return ObjectMinMaxConsumption.findMax(this, orderingFunction);
	}

	@Override
	public boolean areAllEqual()
	{
		return ObjectPredicateConsumption.allEqual(this);
	}

	@Override
	public boolean allMatch(Predicate<? super E> predicate)
	{
		return ObjectPredicateConsumption.allMatch(this, predicate);
	}

	@Override
	public boolean anyMatch(Predicate<? super E> predicate)
	{
		return ObjectPredicateConsumption.anyMatch(this, predicate);
	}

	@Override
	public boolean noneMatch(Predicate<? super E> predicate)
	{
		return ObjectPredicateConsumption.noneMatch(this, predicate);
	}

	@Override
	public long count()
	{
		return ObjectReductionConsumption.count(this);
	}

	@Override
	public <R> R fold(R id, BiFunction<R, E, R> reducer)
	{
		return ObjectReductionConsumption.fold(this, id, reducer);
	}

	@Override
	public Optional<E> foldOption(BinaryOperator<E> reducer)
	{
		return ObjectReductionConsumption.reduceOption(this, reducer);
	}
	
	@Override
	public E fold(BinaryOperator<E> reducer)
	{
		return ObjectReductionConsumption.reduce(this, reducer);
	}
}
