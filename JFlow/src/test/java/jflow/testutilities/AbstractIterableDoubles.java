package jflow.testutilities;

import jflow.iterators.AbstractDoubleFlow;
import jflow.iterators.iterables.DoubleFlowIterable;

/**
 * @author ThomasB
 * @since 27 Apr 2018
 */
public abstract class AbstractIterableDoubles implements DoubleFlowIterable
{
	@Override
	public abstract AbstractDoubleFlow iterator();
}
