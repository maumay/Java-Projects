/**
 *
 */
package jflow.seq;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Stream;

import jflow.iterators.Flow;
import jflow.iterators.iterables.FlowIterable;
import jflow.iterators.misc.Pair;

/**
 * <p>
 * A sequence is an <b>immutable</b> alternative to a {@link List} which
 * provides a myriad of higher order methods for operating on the elements in
 * the style of {@link Stream} but using sequential iterators, see {@link Flow}.
 * That is not to say that streams are not supported, in fact because of the
 * immutability guarantee we can construct a {@link Spliterator} that is well
 * suited for parallelising operations.
 * </p>
 * 
 * @author ThomasB
 */
public interface Seq<E> extends FlowIterable<E>
{
	/**
	 * @return The number of elements in this sequence.
	 */
	int size();

	/**
	 * @return An iteration of the elements in this List in reverse order.
	 */
	Flow<E> rflow();

	/**
	 * @return A sequential stream over the elements of this sequence in order.
	 */
	Stream<E> stream();

	/**
	 * @return The element at the given index, throws
	 *         {@link IndexOutOfBoundsException} if passed invalid index.
	 */
	E get(int index);

	/**
	 * @return A new sequence obtains by mapping this sequence elementwise by the
	 *         parameter function.
	 */
	<R> Seq<R> map(Function<? super E, ? extends R> mappingFunction);

	/**
	 * @return A flattened sequence obtained by concatenating the images of the
	 *         parameter function applied elementwise to this sequence.
	 */
	<R> Seq<R> flatMap(Function<? super E, ? extends Iterator<? extends R>> mapping);

	/**
	 * @return A sequence contained the elements of this sequence which pass the
	 *         supplied predicate with their relative order preserved.
	 */
	Seq<E> filter(Predicate<? super E> predicate);

	/**
	 * @return A sequence with the required parameterisation containing the elements
	 *         of this sequence which can be cast to the given type with their
	 *         relative order preserved.
	 */
	<R> Seq<R> castTo(Class<R> klass);

	/**
	 * @return A sequence containing the elements of this sequence with order
	 *         retained followed by the elements of the parameter iterable whose
	 *         order is defined by the iterator it produces.
	 */
	Seq<E> append(Iterable<? extends E> other);

	/**
	 * @return A sequence containing the elements of this sequence with order
	 *         preserved followed by the parameter element.
	 */
	Seq<E> append(E other);

	/**
	 * @return A sequence containing the elements of the parameter iterable whose
	 *         order is determined by the iterator it produces followed by the
	 *         elements of this sequence whose relative order is preserved.
	 */
	Seq<E> insert(Iterable<? extends E> other);

	/**
	 * @return A sequence containing the parameter element followed by the elements
	 *         of this sequence whose relative order is preserved.
	 */
	Seq<E> insert(E other);

	// TODO implement these
	// Seq<E> slice(int fromIndex, int toIndex, int step);
	//
	// Seq<E> rslice(int from, int to, int step);

	/**
	 * @return A sequence consisting of the first n elements of this sequence with
	 *         their relative order retained. If the requested number of elements is
	 *         larger than the size of this sequence then we will just return this
	 *         sequence, if it is less than zero an exception will be thrown.
	 */
	Seq<E> take(int n);

	/**
	 * @return A sequence consisting of the elements taken from the head of this
	 *         sequence until an element fails the given predicate. The first
	 *         failure <b>is not</b> included.
	 */
	Seq<E> takeWhile(Predicate<? super E> predicate);

	/**
	 * @return A sequence consisting of all but the first n elements of this
	 *         sequence with their relative order retained. If the requested number
	 *         of elements is 0 then we just return this sequence, if it is less
	 *         than zero an exception will be thrown.
	 */
	Seq<E> drop(int n);

	/**
	 * @return A sequence consisting of the elements in this sequence which occur
	 *         after the first element which fails the predicate. This first failure
	 *         <b>is</b> included.
	 */
	Seq<E> dropWhile(Predicate<? super E> predicate);

	/**
	 * @return A pair whose first element is the result of
	 *         {@link Seq#takeWhile(Predicate)} and whose second element is a
	 *         sequence of all elements who were not included in the first sequence.
	 */
	Pair<Seq<E>, Seq<E>> span(Predicate<? super E> predicate);

	/**
	 * @return A pair of sequences whose first element is all the elements of this
	 *         sequence which pass the given predicate, the second is all the
	 *         failures. Relative ordering in these subsequences is preserved.
	 */
	Pair<Seq<E>, Seq<E>> partition(Predicate<? super E> predicate);

	/**
	 * @return A copy of this sequence where the elements are ordered according to
	 *         the supplied comparator.
	 */
	Seq<E> sorted(Comparator<? super E> orderingFunction);

	// Default methods

	/**
	 * @param element The instance to check for membership.
	 * @return true if the given element is in this sequence, false otherwise.
	 */
	default boolean contains(E element)
	{
		return anyMatch(x -> x.equals(element));
	}

	/**
	 * @return A parallel stream over the elements of this sequence in order.
	 */
	default Stream<E> parstream()
	{
		return stream().parallel();
	}

	default Optional<E> getOption(int index)
	{
		return -1 < index && index < size() ? Optional.of(get(index)) : Optional.empty();
	}

	default E head()
	{
		return get(0);
	}

	default Optional<E> headOption()
	{
		return size() > 0 ? Optional.of(head()) : Optional.empty();
	}

	default E last()
	{
		return get(size() - 1);
	}

	default Optional<E> lastOption()
	{
		return size() > 0 ? Optional.of(last()) : Optional.empty();
	}

	default int[] mapToInt(ToIntFunction<? super E> mappingFunction)
	{
		return flow().mapToInt(mappingFunction).toArray();
	}

	default double[] mapToDouble(ToDoubleFunction<? super E> mappingFunction)
	{
		return flow().mapToDouble(mappingFunction).toArray();
	}

	default long[] mapToLong(ToLongFunction<? super E> mappingFunction)
	{
		return flow().mapToLong(mappingFunction).toArray();
	}

	default boolean isEmpty()
	{
		return size() == 0;
	}

	/**
	 * @return An empty sequence
	 */
	static <E> Seq<E> empty()
	{
		return new VectorSeq<>();
	}

	/**
	 * Note that this method is only designed for varargs. If it is passed an array
	 * it won't make a copy.
	 * 
	 * @param elements
	 * @return a seq containing the parameter elements with the ordering retained.
	 */
	@SafeVarargs
	static <E> Seq<E> of(E... elements)
	{
		return new VectorSeq<>(elements);
	}

	static <E> Seq<E> copy(Collection<? extends E> collection)
	{
		return new VectorSeq<>(collection);
	}

	static <E> Seq<E> copy(Iterable<? extends E> iterable)
	{
		return new VectorSeq<>(iterable.iterator());
	}
}
