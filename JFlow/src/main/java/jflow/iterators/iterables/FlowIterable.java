package jflow.iterators.iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import jflow.iterators.Flow;
import jflow.iterators.misc.Bool;


/**
 * Abstraction of iterable object which can construct enhanced iterators
 * ({@link Flow}).
 *
 * @param <E>
 *            The type of element this object can iterate over.
 *
 * @author ThomasB
 */
@FunctionalInterface
public interface FlowIterable<E> extends Iterable<E>
{
	/**
	 * @return A Flow over the elements in this iterable.
	 */
	Flow<E> flow();
	
	@Override
	default Flow<E> iterator()
	{
		return flow();
	}
	
	/**
	 * Finds the first element (if any) which satisfies a given predicate.
	 * 
	 * @param predicate
	 *            The predicate which will be used to test elements.
	 * @return The first element to pass the predicate test if there is one, nothing
	 *         otherwise.
	 */
	default Optional<E> findFirst(Predicate<? super E> predicate)
	{
		return flow().filter(predicate).nextOption();
	}

	/**
	 * A convenience method which spawns a Flow and delegates to its
	 * {@link Flow#min(Comparator)} method.
	 */
	default Optional<E> min(Comparator<? super E> orderingFunction)
	{
		return flow().min(orderingFunction);
	}

	/**
	 * A convenience method which spawns a Flow and delegates to its
	 * {@link Flow#max(Comparator)} method.
	 */
	default Optional<E> max(Comparator<? super E> orderingFunction)
	{
		return flow().max(orderingFunction);
	}

	/**
	 * A convenience method which spawns a Flow and delegates to its
	 * {@link Flow#areAllEqual()} method.
	 */
	default boolean areAllEqual()
	{
		return flow().areAllEqual();
	}

	/**
	 * A convenience method which spawns a Flow and delegates to its
	 * {@link Flow#allMatch(Predicate)} method.
	 */
	default boolean allMatch(Predicate<? super E> condition)
	{
		return flow().allMatch(condition);
	}

	/**
	 * A convenience method which spawns a Flow and delegates to its
	 * {@link Flow#allMatch2(Predicate)} method.
	 */
	default Bool allMatch2(Predicate<? super E> condition)
	{
		return flow().allMatch2(condition);
	}

	/**
	 * A convenience method which spawns a Flow and delegates to its
	 * {@link Flow#anyMatch(Predicate)} method.
	 */
	default boolean anyMatch(Predicate<? super E> condition)
	{
		return flow().anyMatch(condition);
	}

	/**
	 * A convenience method which spawns a Flow and delegates to its
	 * {@link Flow#anyMatch2(Predicate)} method.
	 */
	default Bool anyMatch2(Predicate<? super E> condition)
	{
		return flow().anyMatch2(condition);
	}

	/**
	 * A convenience method which spawns a Flow and delegates to its
	 * {@link Flow#noneMatch(Predicate)} method.
	 */
	default boolean noneMatch(Predicate<? super E> condition)
	{
		return flow().noneMatch(condition);
	}

	/**
	 * A convenience method which spawns a Flow and delegates to its
	 * {@link Flow#noneMatch(Predicate)} method.
	 */
	default Bool noneMatch2(Predicate<? super E> condition)
	{
		return flow().noneMatch2(condition);
	}

	/**
	 * A convenience method which spawns a Flow and delegates to its
	 * {@link Flow#groupBy(Function)} method.
	 */
	default <K> Map<K, List<E>> groupBy(Function<? super E, K> classifier)
	{
		return flow().groupBy(classifier);
	}
	
	/**
	 * A convenience method which spawns a Flow and delegates to its
	 * {@link Flow#fold(Object, BiFunction)} method.
	 */
	default <R> R fold(R id, BiFunction<R, E, R> reducer)
	{
		return flow().fold(id, reducer);
	}

	/**
	 * A convenience method which spawns a Flow and delegates to its
	 * {@link Flow#foldOption(BinaryOperator)} method.
	 */
	default Optional<E> foldOption(BinaryOperator<E> reducer)
	{
		return flow().foldOption(reducer);
	}
	
	default E fold(BinaryOperator<E> reducer)
	{
		return flow().fold(reducer);
	}
	
	default <K, V> Map<K, V> toMap(Function<? super E, ? extends K> keyMap, Function<? super E, ? extends V> valueMap)
	{
		return flow().toMap(keyMap, valueMap);
	}
	
	default List<E> toList()
	{
		return toCollection(ArrayList::new);
	}
	
	default Set<E> toSet()
	{
		return toCollection(HashSet::new);
	}
	
	default <C extends Collection<E>> C toCollection(Supplier<C> collectionFactory)
	{
		return flow().toCollection(collectionFactory);
	}
}
