package jflow.testutilities;

import jflow.iterators.AbstractIntFlow;
import jflow.iterators.iterables.IntFlowIterable;

/**
 * @author ThomasB
 * @since 27 Apr 2018
 */
public abstract class AbstractIterableInts implements IntFlowIterable
{
	@Override
	public abstract AbstractIntFlow iterator();
}
