package xawd.jflow.iterators.misc;

/**
 * @author ThomasB
 * @since 20 Apr 2018
 */
public final class Pair<T, U>
{
	private final T first;
	private final U second;

	public Pair(final T first, final U second)
	{
		this.first = first;
		this.second = second;
	}

	public static <T, U> Pair<T, U> of(final T t, final U u)
	{
		return new Pair<>(t, u);
	}

	public T first()
	{
		return first;
	}

	public U second()
	{
		return second;
	}

	@Override
	public String toString()
	{
		return new StringBuilder("(")
				.append(first.toString())
				.append(", ")
				.append(second.toString())
				.append(")")
				.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Pair<?, ?> other = (Pair<?, ?>) obj;
		if (first == null) {
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (second == null) {
			if (other.second != null)
				return false;
		} else if (!second.equals(other.second))
			return false;
		return true;
	}
}
