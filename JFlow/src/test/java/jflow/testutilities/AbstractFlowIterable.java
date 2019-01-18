package jflow.testutilities;

import jflow.iterators.AbstractFlow;
import jflow.iterators.iterables.FlowIterable;

/**
 * @author ThomasB
 * @since 27 Apr 2018
 */
public abstract class AbstractFlowIterable<T> implements FlowIterable<T>
{
	// TODO - needs sorting out, flow() should be the abstract method
	
	@Override
	public abstract AbstractFlow<T> iterator();
	
	@Override
	public AbstractFlow<T> flow()
	{
		return iterator();
	}
}
