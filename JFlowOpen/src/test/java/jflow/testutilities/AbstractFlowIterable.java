package jflow.testutilities;

import jflow.iterators.AbstractFlow;
import jflow.iterators.iterables.FlowIterable;

/**
 * @author ThomasB
 * @since 27 Apr 2018
 */
public abstract class AbstractFlowIterable<T> implements FlowIterable<T>
{
	@Override
	public abstract AbstractFlow<T> iterator();
}
