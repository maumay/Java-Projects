/**
 *
 */
package jflow.iterators.impl;

import java.util.PrimitiveIterator;
import java.util.function.LongPredicate;

/**
 * @author ThomasB
 */
public final class LongPredicateConsumption
{
	public LongPredicateConsumption() {}

	public static boolean allEqual(final PrimitiveIterator.OfLong source)
	{
		boolean initialised = false;
		long last = -1L;
		while (source.hasNext()) {
			final long next = source.nextLong();
			if (!initialised) {
				initialised = true;
				last = next;
			}
			else if (last == next) {
				last = next;
			}
			else {
				return false;
			}
		}
		return true;
	}

	public static boolean allMatch(final PrimitiveIterator.OfLong source, final LongPredicate predicate)
	{
		while (source.hasNext()) {
			if (!predicate.test(source.nextLong())) {
				return false;
			}
		}
		return true;
	}

	public static boolean anyMatch(final PrimitiveIterator.OfLong source, final LongPredicate predicate)
	{
		while (source.hasNext()) {
			if (predicate.test(source.nextLong())) {
				return true;
			}
		}
		return false;
	}

	public static boolean noneMatch(final PrimitiveIterator.OfLong source, final LongPredicate predicate)
	{
		while (source.hasNext()) {
			if (predicate.test(source.nextLong())) {
				return false;
			}
		}
		return true;
	}
}
