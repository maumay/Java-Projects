/**
 *
 */
package jenjinn.engine.eval.kingsafety;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import jenjinn.engine.boardstate.BoardState;
import jenjinn.engine.eval.KingSafetyEvaluator;
import jflow.iterators.Flow;
import jflow.iterators.factories.Iter;

/**
 * @author ThomasB
 */
class KingSafetyEvaluationTest
{
	@ParameterizedTest
	@MethodSource
	void test(BoardState state, Integer expectedValue)
	{
		assertEquals(expectedValue.intValue(), new KingSafetyEvaluator().evaluate(state));
	}

	static Flow<Arguments> test()
	{
		TestFileParser parser = new TestFileParser();
		return Iter.over("case001").map(parser::parse);
	}
}