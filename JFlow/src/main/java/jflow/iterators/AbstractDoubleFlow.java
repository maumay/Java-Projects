package jflow.iterators;

import java.util.Iterator;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.IntUnaryOperator;

import jflow.iterators.factories.Iter;
import jflow.iterators.factories.Numbers;
import jflow.iterators.impl.AccumulationFlow;
import jflow.iterators.impl.AppendFlow;
import jflow.iterators.impl.DoubleCollectionConsumption;
import jflow.iterators.impl.DoubleMinMaxConsumption;
import jflow.iterators.impl.DoublePredicateConsumption;
import jflow.iterators.impl.DoubleReductionConsumption;
import jflow.iterators.impl.DropFlow;
import jflow.iterators.impl.DropwhileFlow;
import jflow.iterators.impl.FilteredFlow;
import jflow.iterators.impl.InsertFlow;
import jflow.iterators.impl.MapFlow;
import jflow.iterators.impl.MapToIntFlow;
import jflow.iterators.impl.MapToLongFlow;
import jflow.iterators.impl.MapToObjectFlow;
import jflow.iterators.impl.SlicedFlow;
import jflow.iterators.impl.TakeFlow;
import jflow.iterators.impl.TakewhileFlow;
import jflow.iterators.impl.ZipFlow;
import jflow.iterators.misc.DoublePair;
import jflow.iterators.misc.DoubleWith;

/**
 * A skeletal implementation of DoubleFlow, users writing custom DoubleFlows
 * should subclass this class.
 *
 * @author ThomasB
 * @since 23 Apr 2018
 */
public abstract class AbstractDoubleFlow extends AbstractOptionallySized
		implements DoubleFlow
{
	public AbstractDoubleFlow(OptionalInt size)
	{
		super(size);
	}

	@Override
	public AbstractDoubleFlow slice(IntUnaryOperator indexMapping)
	{
		return new SlicedFlow.OfDouble(this, indexMapping);
	}

	@Override
	public AbstractDoubleFlow map(DoubleUnaryOperator f)
	{
		return new MapFlow.OfDouble(this, f);
	}

	@Override
	public <E> AbstractFlow<E> mapToObject(DoubleFunction<? extends E> f)
	{
		return new MapToObjectFlow.FromDouble<>(this, f);
	}

	@Override
	public AbstractLongFlow mapToLong(DoubleToLongFunction f)
	{
		return new MapToLongFlow.FromDouble(this, f);
	}

	@Override
	public AbstractIntFlow mapToInt(DoubleToIntFunction f)
	{
		return new MapToIntFlow.FromDouble(this, f);
	}

	@Override
	public <E> AbstractFlow<DoubleWith<E>> zipWith(Iterator<? extends E> other)
	{
		return new ZipFlow.OfObjectAndDouble<>(other, this);
	}

	@Override
	public AbstractFlow<DoublePair> zipWith(OfDouble other)
	{
		return new ZipFlow.OfDoublePair(this, other);
	}

	@Override
	public AbstractFlow<DoubleWith<Integer>> enumerate()
	{
		return zipWith(Numbers.natural());
	}

	@Override
	public AbstractDoubleFlow take(int n)
	{
		return new TakeFlow.OfDouble(this, n);
	}

	@Override
	public AbstractDoubleFlow takeWhile(DoublePredicate predicate)
	{
		return new TakewhileFlow.OfDouble(this, predicate);
	}

	@Override
	public AbstractDoubleFlow drop(int n)
	{
		return new DropFlow.OfDouble(this, n);
	}

	@Override
	public AbstractDoubleFlow dropWhile(DoublePredicate predicate)
	{
		return new DropwhileFlow.OfDouble(this, predicate);
	}

	@Override
	public AbstractDoubleFlow filter(DoublePredicate predicate)
	{
		return new FilteredFlow.OfDouble(this, predicate);
	}

	@Override
	public AbstractDoubleFlow append(OfDouble other)
	{
		return new AppendFlow.OfDouble(this, other);
	}

	@Override
	public AbstractDoubleFlow append(double... xs)
	{
		return append(Iter.overDoubles(xs));
	}

	@Override
	public AbstractDoubleFlow insert(OfDouble other)
	{
		return new InsertFlow.OfDouble(this, other);
	}

	@Override
	public AbstractDoubleFlow insert(double... xs)
	{
		return insert(Iter.overDoubles(xs));
	}

	@Override
	public AbstractDoubleFlow accumulate(DoubleBinaryOperator accumulator)
	{
		return new AccumulationFlow.OfDouble(this, accumulator);
	}

	@Override
	public AbstractDoubleFlow accumulate(double id, DoubleBinaryOperator accumulator)
	{
		return new AccumulationFlow.OfDouble(this, id, accumulator);
	}

	@Override
	public OptionalDouble min()
	{
		return DoubleMinMaxConsumption.findMin(this);
	}

	@Override
	public double min(double defaultValue)
	{
		return DoubleMinMaxConsumption.findMin(this, defaultValue);
	}

	@Override
	public OptionalDouble max()
	{
		return DoubleMinMaxConsumption.findMax(this);
	}

	@Override
	public double max(double defaultValue)
	{
		return DoubleMinMaxConsumption.findMax(this, defaultValue);
	}

	@Override
	public boolean areAllEqual()
	{
		return DoublePredicateConsumption.allEqual(this);
	}

	@Override
	public boolean allMatch(DoublePredicate predicate)
	{
		return DoublePredicateConsumption.allMatch(this, predicate);
	}

	@Override
	public boolean anyMatch(DoublePredicate predicate)
	{
		return DoublePredicateConsumption.anyMatch(this, predicate);
	}

	@Override
	public boolean noneMatch(DoublePredicate predicate)
	{
		return DoublePredicateConsumption.noneMatch(this, predicate);
	}

	@Override
	public long count()
	{
		return DoubleReductionConsumption.count(this);
	}

	@Override
	public double fold(double id, DoubleBinaryOperator reducer)
	{
		return DoubleReductionConsumption.fold(this, id, reducer);
	}

	@Override
	public double fold(DoubleBinaryOperator reducer)
	{
		return DoubleReductionConsumption.fold(this, reducer);
	}

	@Override
	public OptionalDouble foldOption(DoubleBinaryOperator reducer)
	{
		return DoubleReductionConsumption.foldOption(this, reducer);
	}

	@Override
	public double[] toArray()
	{
		return DoubleCollectionConsumption.toArray(this);
	}

	@Override
	public <K, V> Map<K, V> toMap(DoubleFunction<K> keyMapper,
			DoubleFunction<V> valueMapper)
	{
		return DoubleCollectionConsumption.toMap(this, keyMapper, valueMapper);
	}

	@Override
	public <K> Map<K, double[]> groupBy(DoubleFunction<K> classifier)
	{
		return DoubleCollectionConsumption.groupBy(this, classifier);
	}
}