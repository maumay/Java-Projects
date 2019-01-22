/**
 *
 */
package jflow.seq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import jflow.iterators.AbstractFlow;
import jflow.iterators.Flow;
import jflow.iterators.factories.IterRange;
import jflow.iterators.misc.Pair;

/**
 * An immutable implementation of {@link Seq} which stores only non null
 * references. This class is very space efficient as it simply wraps a single
 * Object array. When combined with {@link Flow} instances one can write very
 * clean, efficient and safe code code without ever needing to reference this
 * type directly.
 *
 * @param <E> The type of the elements contained in this List.
 *
 * @author ThomasB
 */
public final class VectorSeq<E> implements Seq<E>
{
	private static final Object[] EMPTY = new Object[0];

	private final Object[] data;

	public VectorSeq()
	{
		data = EMPTY;
	}

	public VectorSeq(Object[] cache)
	{
		this.data = cache;
	}

	public VectorSeq(Iterator<? extends E> src)
	{
		if (src instanceof Flow<?> && ((Flow<?>) src).sizeIsKnown()) {
			int size = ((Flow<?>) src).size().getAsInt(), index = 0;
			data = new Object[size];
			while (src.hasNext()) {
				data[index++] = Objects.requireNonNull(src.next());
			}
		} else {
			List<E> mut = new ArrayList<>(10);
			while (src.hasNext()) {
				mut.add(Objects.requireNonNull(src.next()));
			}
			data = mut.toArray();
		}
	}

	public VectorSeq(Collection<? extends E> src)
	{
		data = new Object[src.size()];
		int index = 0;
		for (Iterator<? extends E> itr = src.iterator(); itr.hasNext();) {
			data[index++] = Objects.requireNonNull(itr.next());
		}
	}

	private VectorSeq(Iterator<? extends E> src, int knownSizeUpperBound)
	{
		int sze = knownSizeUpperBound;
		if (src instanceof Flow<?> && ((Flow<?>) src).sizeIsKnown()) {
			sze = Math.min(sze, ((Flow<?>) src).size().getAsInt());
		}

		Object[] initialTry = new Object[sze];
		int index = 0;
		while (src.hasNext()) {
			initialTry[index++] = Objects.requireNonNull(src.next());
		}
		data = index == sze ? initialTry : Arrays.copyOf(initialTry, index);
	}

	@SuppressWarnings("unchecked")
	@Override
	public E get(int index)
	{
		return (E) data[index];
	}

	@Override
	public int size()
	{
		return data.length;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Seq<?>) {
			final Seq<?> other = (Seq<?>) obj;
			if (other.size() == size()) {
				return IterRange.to(size()).allMatch(i -> data[i].equals(other.get(i)));
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public int hashCode()
	{
		return flow().mapToInt(Object::hashCode).fold(1, (res, n) -> 31 * res + n);
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder("Seq[");
		for (int i = 0; i < data.length; i++) {
			sb.append(data[i].toString());
			if (i < data.length - 1) {
				sb.append(", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	@Override
	public Spliterator<E> spliterator()
	{
		return new VectorSeqSpliterator<>(this, 0, size());
	}

	@Override
	public Stream<E> stream()
	{
		return StreamSupport.stream(spliterator(), false);
	}

	@Override
	public Flow<E> rflow()
	{
		return new AbstractFlow<E>(OptionalInt.of(size())) {
			int count = 0;

			@Override
			public boolean hasNext()
			{
				return count < data.length;
			}

			@SuppressWarnings("unchecked")
			@Override
			public E next()
			{
				if (hasNext()) {
					return (E) data[data.length - 1 - count++];
				} else {
					throw new NoSuchElementException();
				}
			}

			@Override
			public void skip()
			{
				if (count++ >= data.length) {
					throw new NoSuchElementException();
				}
			}
		};
	}

	@Override
	public Flow<E> flow()
	{
		return new AbstractFlow<E>(OptionalInt.of(size())) {
			int count = 0;

			@Override
			public boolean hasNext()
			{
				return count < data.length;
			}

			@SuppressWarnings("unchecked")
			@Override
			public E next()
			{
				if (hasNext()) {
					return (E) data[count++];
				} else {
					throw new NoSuchElementException();
				}
			}

			@Override
			public void skip()
			{
				if (count++ >= data.length) {
					throw new NoSuchElementException();
				}
			}
		};
	}

	@Override
	public <R> Seq<R> map(Function<? super E, ? extends R> mappingFunction)
	{
		Object[] mappedData = new Object[data.length];
		for (int i = 0; i < data.length; i++) {
			mappedData[i] = mappingFunction.apply(get(i));
		}
		return new VectorSeq<>(mappedData);
	}

	@Override
	public <R> Seq<R> flatMap(
			Function<? super E, ? extends Iterator<? extends R>> mapping)
	{
		return new VectorSeq<>(iterator().flatMap(mapping));
	}

	@Override
	public Seq<E> filter(Predicate<? super E> predicate)
	{
		return new VectorSeq<>(iterator().filter(predicate), size());
	}

	@Override
	public <R> Seq<R> castTo(Class<R> klass)
	{
		return new VectorSeq<>(iterator().castTo(klass), size());
	}

	@Override
	public Seq<E> append(Iterable<? extends E> other)
	{
		boolean isSeq = other instanceof Seq<?>;

		if (isSeq || other instanceof Collection<?>) {
			int osize = isSeq ? ((Seq<?>) other).size() : ((Collection<?>) other).size();
			return new VectorSeq<>(iterator().append(other.iterator()), size() + osize);
		} else {
			return new VectorSeq<>(iterator().append(other.iterator()));
		}
	}

	@Override
	public Seq<E> append(E other)
	{
		Object[] newData = new Object[size() + 1];
		System.arraycopy(data, 0, newData, 0, data.length);
		newData[size()] = other;
		return new VectorSeq<>(newData);
	}

	@Override
	public Seq<E> insert(Iterable<? extends E> other)
	{
		boolean isSeq = other instanceof Seq<?>;

		if (isSeq || other instanceof Collection<?>) {
			int osize = isSeq ? ((Seq<?>) other).size() : ((Collection<?>) other).size();
			return new VectorSeq<>(iterator().insert(other.iterator()), size() + osize);
		} else {
			return new VectorSeq<>(iterator().insert(other.iterator()));
		}
	}

	@Override
	public Seq<E> insert(E other)
	{
		Object[] newData = new Object[size() + 1];
		System.arraycopy(data, 0, newData, 1, data.length);
		newData[0] = other;
		return new VectorSeq<>(newData);
	}

	@Override
	public Seq<E> take(int n)
	{
		if (n >= size())
			return this;
		else if (n == 0)
			return new VectorSeq<>();
		else
			return new VectorSeq<>(iterator().take(n));
	}

	@Override
	public Seq<E> takeWhile(Predicate<? super E> predicate)
	{
		return new VectorSeq<>(iterator().takeWhile(predicate), size());
	}

	@Override
	public Seq<E> drop(int n)
	{
		if (n == 0)
			return this;
		else if (n >= size())
			return new VectorSeq<>();
		else
			return new VectorSeq<>(iterator().drop(n));
	}

	@Override
	public Seq<E> dropWhile(Predicate<? super E> predicate)
	{
		return new VectorSeq<>(iterator().dropWhile(predicate), size());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pair<Seq<E>, Seq<E>> span(Predicate<? super E> predicate)
	{
		int split = 0;
		for (Object o : data) {
			if (predicate.test((E) o)) {
				split++;
			} else {
				break;
			}
		}
		Object[] first = new Object[split], second = new Object[data.length - split];
		System.arraycopy(data, 0, first, 0, first.length);
		System.arraycopy(data, first.length, second, 0, second.length);
		return Pair.of(new VectorSeq<>(first), new VectorSeq<>(second));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pair<Seq<E>, Seq<E>> partition(Predicate<? super E> predicate)
	{
		int n = size();
		Object[] tmp = new Object[size()];
		int trueIndex = 0, falseIndex = 0;
		for (Object o : data) {
			if (predicate.test((E) o)) {
				tmp[trueIndex++] = o;
			} else {
				tmp[n - 1 - falseIndex++] = o;
			}
		}
		Object[] passed = new Object[trueIndex], failed = new Object[falseIndex];
		for (int i = 0; i < trueIndex; i++)
			passed[i] = tmp[i];
		for (int i = 0; i < falseIndex; i++)
			failed[i] = tmp[tmp.length - 1 - i];
		return Pair.of(new VectorSeq<>(passed), new VectorSeq<>(failed));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<E> toSet()
	{
		Set<E> set = new HashSet<>(size());
		for (Object o : data) {
			set.add((E) o);
		}
		return set;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> toList()
	{
		List<E> list = new ArrayList<>(size());
		for (Object o : data) {
			list.add((E) o);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Seq<E> sorted(Comparator<? super E> orderingFunction)
	{
		Object[] dcpy = Arrays.copyOf(data, size());
		Arrays.sort(dcpy, (o1, o2) -> orderingFunction.compare((E) o1, (E) o2));
		return new VectorSeq<>(dcpy);
	}
}
