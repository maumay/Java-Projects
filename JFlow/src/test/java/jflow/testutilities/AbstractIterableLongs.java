package jflow.testutilities;

import jflow.iterators.AbstractLongFlow;
import jflow.iterators.iterables.LongFlowIterable;

/**
 * @author ThomasB
 * @since 27 Apr 2018
 */
public abstract class AbstractIterableLongs implements LongFlowIterable
{
	@Override
	public abstract AbstractLongFlow iterator();
}
