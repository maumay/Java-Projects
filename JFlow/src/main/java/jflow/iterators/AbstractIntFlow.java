package jflow.iterators;

import java.util.Iterator;
import java.util.Map;
import java.util.OptionalInt;
import java.util.function.IntBinaryOperator;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;

import jflow.iterators.factories.Iter;
import jflow.iterators.factories.Numbers;
import jflow.iterators.impl.AccumulationFlow;
import jflow.iterators.impl.AppendFlow;
import jflow.iterators.impl.DropFlow;
import jflow.iterators.impl.DropwhileFlow;
import jflow.iterators.impl.FilteredFlow;
import jflow.iterators.impl.InsertFlow;
import jflow.iterators.impl.IntCollectionConsumption;
import jflow.iterators.impl.IntMinMaxConsumption;
import jflow.iterators.impl.IntPredicateConsumption;
import jflow.iterators.impl.IntReductionConsumption;
import jflow.iterators.impl.MapFlow;
import jflow.iterators.impl.MapToDoubleFlow;
import jflow.iterators.impl.MapToLongFlow;
import jflow.iterators.impl.MapToObjectFlow;
import jflow.iterators.impl.SlicedFlow;
import jflow.iterators.impl.TakeFlow;
import jflow.iterators.impl.TakewhileFlow;
import jflow.iterators.impl.ZipFlow;
import jflow.iterators.misc.IntPair;
import jflow.iterators.misc.IntWith;

/**
 * A skeletal implementation of a IntFlow, users writing custom IntFlows should
 * subclass this class.
 *
 * @author ThomasB
 * @since 23 Apr 2018
 */
public abstract class AbstractIntFlow extends AbstractOptionallySized implements IntFlow
{
	public AbstractIntFlow(OptionalInt size)
	{
		super(size);
	}

	@Override
	public AbstractIntFlow slice(IntUnaryOperator sliceMap)
	{
		return new SlicedFlow.OfInt(this, sliceMap);
	}

	@Override
	public AbstractIntFlow map(IntUnaryOperator f)
	{
		return new MapFlow.OfInt(this, f);
	}

	@Override
	public <E> AbstractFlow<E> mapToObject(IntFunction<? extends E> f)
	{
		return new MapToObjectFlow.FromInt<>(this, f);
	}

	@Override
	public AbstractDoubleFlow mapToDouble(IntToDoubleFunction f)
	{
		return new MapToDoubleFlow.FromInt(this, f);
	}

	@Override
	public AbstractLongFlow mapToLong(IntToLongFunction f)
	{
		return new MapToLongFlow.FromInt(this, f);
	}

	@Override
	public <E> AbstractFlow<IntWith<E>> zipWith(Iterator<? extends E> other)
	{
		return new ZipFlow.OfObjectAndInt<>(other, this);
	}

	@Override
	public AbstractFlow<IntPair> zipWith(OfInt other)
	{
		return new ZipFlow.OfIntPair(this, other);
	}

	@Override
	public AbstractFlow<IntPair> enumerate()
	{
		return zipWith(Numbers.natural());
	}

	@Override
	public AbstractIntFlow take(int n)
	{
		return new TakeFlow.OfInt(this, n);
	}

	@Override
	public AbstractIntFlow takeWhile(IntPredicate predicate)
	{
		return new TakewhileFlow.OfInt(this, predicate);
	}

	@Override
	public AbstractIntFlow drop(int n)
	{
		return new DropFlow.OfInt(this, n);
	}

	@Override
	public AbstractIntFlow dropWhile(IntPredicate predicate)
	{
		return new DropwhileFlow.OfInt(this, predicate);
	}

	@Override
	public AbstractIntFlow filter(IntPredicate predicate)
	{
		return new FilteredFlow.OfInt(this, predicate);
	}

	@Override
	public AbstractIntFlow append(OfInt other)
	{
		return new AppendFlow.OfInt(this, other);
	}

	@Override
	public AbstractIntFlow append(int... xs)
	{
		return append(Iter.overInts(xs));
	}

	@Override
	public AbstractIntFlow insert(OfInt other)
	{
		return new InsertFlow.OfInt(this, other);
	}

	@Override
	public AbstractIntFlow insert(int... xs)
	{
		return insert(Iter.overInts(xs));
	}

	@Override
	public AbstractIntFlow scan(IntBinaryOperator accumulator)
	{
		return new AccumulationFlow.OfInt(this, accumulator);
	}

	@Override
	public AbstractIntFlow scan(int id, IntBinaryOperator accumulator)
	{
		return new AccumulationFlow.OfInt(this, id, accumulator);
	}

	@Override
	public OptionalInt min()
	{
		return IntMinMaxConsumption.findMin(this);
	}

	@Override
	public int min(int defaultValue)
	{
		return IntMinMaxConsumption.findMin(this, defaultValue);
	}

	// @Override
	// public int minByKey(int defaultValue, IntToDoubleFunction key)
	// {
	// return IntMinMaxConsumption.findMin(this, defaultValue, key);
	// }
	//
	// @Override
	// public OptionalInt minByKey(IntToDoubleFunction key)
	// {
	// return IntMinMaxConsumption.findMin(this, key);
	// }

	@Override
	public <C extends Comparable<C>> OptionalInt minByKey(IntFunction<C> key)
	{
		return IntMinMaxConsumption.findMin(this, key);
	}

	@Override
	public OptionalInt max()
	{
		return IntMinMaxConsumption.findMax(this);
	}

	@Override
	public int max(int defaultValue)
	{
		return IntMinMaxConsumption.findMax(this, defaultValue);
	}

	// @Override
	// public int maxByKey(int defaultValue, IntToDoubleFunction key)
	// {
	// return IntMinMaxConsumption.findMax(this, defaultValue, key);
	// }
	//
	// @Override
	// public OptionalInt maxByKey(IntToDoubleFunction key)
	// {
	// return IntMinMaxConsumption.findMax(this, key);
	// }

	@Override
	public <C extends Comparable<C>> OptionalInt maxByKey(IntFunction<C> key)
	{
		return IntMinMaxConsumption.findMax(this, key);
	}

	@Override
	public boolean areAllEqual()
	{
		return IntPredicateConsumption.allEqual(this);
	}

	@Override
	public boolean allMatch(IntPredicate predicate)
	{
		return IntPredicateConsumption.allMatch(this, predicate);
	}

	@Override
	public boolean anyMatch(IntPredicate predicate)
	{
		return IntPredicateConsumption.anyMatch(this, predicate);
	}

	@Override
	public boolean noneMatch(IntPredicate predicate)
	{
		return IntPredicateConsumption.noneMatch(this, predicate);
	}

	@Override
	public long count()
	{
		return IntReductionConsumption.count(this);
	}

	@Override
	public int fold(int id, IntBinaryOperator reducer)
	{
		return IntReductionConsumption.fold(this, id, reducer);
	}

	@Override
	public int fold(IntBinaryOperator reducer)
	{
		return IntReductionConsumption.fold(this, reducer);
	}

	@Override
	public OptionalInt foldOption(IntBinaryOperator reducer)
	{
		return IntReductionConsumption.foldOption(this, reducer);
	}

	@Override
	public int[] toArray()
	{
		return IntCollectionConsumption.toArray(this);
	}

	@Override
	public <K, V> Map<K, V> toMap(IntFunction<? extends K> keyMapper,
			IntFunction<? extends V> valueMapper)
	{
		return IntCollectionConsumption.toMap(this, keyMapper, valueMapper);
	}

	@Override
	public <K> Map<K, int[]> groupBy(IntFunction<? extends K> classifier)
	{
		return IntCollectionConsumption.groupBy(this, classifier);
	}
}