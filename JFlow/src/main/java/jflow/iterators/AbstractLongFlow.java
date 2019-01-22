/**
 *
 */
package jflow.iterators;

import java.util.Iterator;
import java.util.Map;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.IntUnaryOperator;
import java.util.function.LongBinaryOperator;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.LongUnaryOperator;

import jflow.iterators.factories.Iter;
import jflow.iterators.factories.Numbers;
import jflow.iterators.impl.AccumulationFlow;
import jflow.iterators.impl.AppendFlow;
import jflow.iterators.impl.DropFlow;
import jflow.iterators.impl.DropwhileFlow;
import jflow.iterators.impl.FilteredFlow;
import jflow.iterators.impl.InsertFlow;
import jflow.iterators.impl.LongCollectionConsumption;
import jflow.iterators.impl.LongMinMaxConsumption;
import jflow.iterators.impl.LongPredicateConsumption;
import jflow.iterators.impl.LongReductionConsumption;
import jflow.iterators.impl.MapFlow;
import jflow.iterators.impl.MapToDoubleFlow;
import jflow.iterators.impl.MapToIntFlow;
import jflow.iterators.impl.MapToObjectFlow;
import jflow.iterators.impl.SlicedFlow;
import jflow.iterators.impl.TakeFlow;
import jflow.iterators.impl.TakewhileFlow;
import jflow.iterators.impl.ZipFlow;
import jflow.iterators.misc.LongPair;
import jflow.iterators.misc.LongWith;

/**
 * A skeletal implementation of a LongFlow, users writing custom LongFlows
 * should subclass this class.
 *
 * @author ThomasB
 */
public abstract class AbstractLongFlow extends AbstractOptionallySized implements LongFlow
{
	public AbstractLongFlow(OptionalInt size)
	{
		super(size);
	}

	@Override
	public AbstractLongFlow slice(IntUnaryOperator indexMapping)
	{
		return new SlicedFlow.OfLong(this, indexMapping);
	}

	@Override
	public AbstractLongFlow map(LongUnaryOperator f)
	{
		return new MapFlow.OfLong(this, f);
	}

	@Override
	public <E> AbstractFlow<E> mapToObject(LongFunction<? extends E> f)
	{
		return new MapToObjectFlow.FromLong<>(this, f);
	}

	@Override
	public AbstractDoubleFlow mapToDouble(LongToDoubleFunction f)
	{
		return new MapToDoubleFlow.FromLong(this, f);
	}

	@Override
	public AbstractIntFlow mapToInt(LongToIntFunction f)
	{
		return new MapToIntFlow.FromLong(this, f);
	}

	@Override
	public <E> AbstractFlow<LongWith<E>> zipWith(Iterator<? extends E> other)
	{
		return new ZipFlow.OfObjectAndLong<>(other, this);
	}

	@Override
	public AbstractFlow<LongPair> zipWith(OfLong other)
	{
		return new ZipFlow.OfLongPair(this, other);
	}

	@Override
	public AbstractFlow<LongWith<Integer>> enumerate()
	{
		return zipWith(Numbers.natural());
	}

	@Override
	public AbstractLongFlow take(int n)
	{
		return new TakeFlow.OfLong(this, n);
	}

	@Override
	public AbstractLongFlow takeWhile(LongPredicate predicate)
	{
		return new TakewhileFlow.OfLong(this, predicate);
	}

	@Override
	public AbstractLongFlow drop(int n)
	{
		return new DropFlow.OfLong(this, n);
	}

	@Override
	public AbstractLongFlow dropWhile(LongPredicate predicate)
	{
		return new DropwhileFlow.OfLong(this, predicate);
	}

	@Override
	public AbstractLongFlow filter(LongPredicate predicate)
	{
		return new FilteredFlow.OfLong(this, predicate);
	}

	@Override
	public AbstractLongFlow append(OfLong other)
	{
		return new AppendFlow.OfLong(this, other);
	}

	@Override
	public AbstractLongFlow append(long... xs)
	{
		return append(Iter.overLongs(xs));
	}

	@Override
	public AbstractLongFlow insert(OfLong other)
	{
		return new InsertFlow.OfLong(this, other);
	}

	@Override
	public AbstractLongFlow insert(long... xs)
	{
		return insert(Iter.overLongs(xs));
	}

	@Override
	public AbstractLongFlow scan(LongBinaryOperator accumulator)
	{
		return new AccumulationFlow.OfLong(this, accumulator);
	}

	@Override
	public AbstractLongFlow scan(long id, LongBinaryOperator accumulator)
	{
		return new AccumulationFlow.OfLong(this, id, accumulator);
	}

	@Override
	public OptionalLong min()
	{
		return LongMinMaxConsumption.findMin(this);
	}

	@Override
	public long min(long defaultValue)
	{
		return LongMinMaxConsumption.findMin(this, defaultValue);
	}

	@Override
	public OptionalLong max()
	{
		return LongMinMaxConsumption.findMax(this);
	}

	@Override
	public long max(long defaultValue)
	{
		return LongMinMaxConsumption.findMax(this, defaultValue);
	}

	@Override
	public boolean areAllEqual()
	{
		return LongPredicateConsumption.allEqual(this);
	}

	@Override
	public boolean allMatch(LongPredicate predicate)
	{
		return LongPredicateConsumption.allMatch(this, predicate);
	}

	@Override
	public boolean anyMatch(LongPredicate predicate)
	{
		return LongPredicateConsumption.anyMatch(this, predicate);
	}

	@Override
	public boolean noneMatch(LongPredicate predicate)
	{
		return LongPredicateConsumption.noneMatch(this, predicate);
	}

	@Override
	public long count()
	{
		return LongReductionConsumption.count(this);
	}

	@Override
	public long fold(long id, LongBinaryOperator reducer)
	{
		return LongReductionConsumption.fold(this, id, reducer);
	}

	@Override
	public long fold(LongBinaryOperator reducer)
	{
		return LongReductionConsumption.fold(this, reducer);
	}

	@Override
	public OptionalLong foldOption(LongBinaryOperator reducer)
	{
		return LongReductionConsumption.foldOption(this, reducer);
	}

	@Override
	public long[] toArray()
	{
		return LongCollectionConsumption.toArray(this);
	}

	@Override
	public <K, V> Map<K, V> toMap(LongFunction<K> keyMapper, LongFunction<V> valueMapper)
	{
		return LongCollectionConsumption.toMap(this, keyMapper, valueMapper);
	}

	@Override
	public <K> Map<K, long[]> groupBy(LongFunction<K> classifier)
	{
		return LongCollectionConsumption.groupBy(this, classifier);
	}
}
