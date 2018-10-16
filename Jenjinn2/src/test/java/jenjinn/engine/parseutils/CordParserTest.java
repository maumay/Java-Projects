package jenjinn.engine.parseutils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import jenjinn.engine.base.Square;
import jflow.iterators.Flow;
import jflow.iterators.factories.Iter;

/**
 * @author ThomasB
 */
class CordParserTest
{
	@ParameterizedTest
	@MethodSource
	void test(String encodedCord, int[] expectedSquareIndices)
	{
		List<Square> squares = Iter.overInts(expectedSquareIndices).mapToObject(Square::of).toList();
		assertEquals(squares, CordParser.parse(encodedCord));
	}

	static Flow<Arguments> test()
	{
		return Iter.over(
				Arguments.of("g1->D1", new int[] {1, 2, 3, 4}),
				Arguments.of("G1->F3", new int[] {1, 18})
				);
	}

	void testErrors()
	{
		assertThrows(IllegalArgumentException.class, () -> CordParser.parse("g1 -> g2"));
		assertThrows(IllegalArgumentException.class, () -> CordParser.parse("g1 ->"));
		assertThrows(IllegalArgumentException.class, () -> CordParser.parse("g1 -> a2"));
	}
}
