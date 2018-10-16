/**
 *
 */
package jenjinn.engine.boardstate.legalmoves;

import java.util.List;

import org.junit.jupiter.params.provider.Arguments;

import jenjinn.engine.parseutils.AbstractTestFileParser;
import jenjinn.engine.parseutils.BoardParser;
import jflow.iterators.factories.Iter;

/**
 * @author ThomasB
 */
final class TestFileParser extends AbstractTestFileParser
{
	public Arguments parse(final String fileName)
	{
		final List<String> lines = loadFile(fileName);

		final List<String> boardStateAttributes = Iter.over(lines).take(9).toList();
		final List<String> expectedMoveLines = Iter.over(lines)
				.drop(9).takeWhile(s -> !s.startsWith("---")).toList();
		final List<String> expectedAttackLines = Iter.over(lines)
				.dropWhile(s -> !s.startsWith("---")).drop(1).toList();

		return Arguments.of(
				BoardParser.parse(boardStateAttributes),
				parseMoves(expectedMoveLines),
				parseMoves(expectedAttackLines));
	}
}
