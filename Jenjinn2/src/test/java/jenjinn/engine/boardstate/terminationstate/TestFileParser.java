/**
 *
 */
package jenjinn.engine.boardstate.terminationstate;

import org.junit.jupiter.params.provider.Arguments;

import jenjinn.engine.base.GameTermination;
import jenjinn.engine.boardstate.BoardState;
import jenjinn.engine.parseutils.AbstractTestFileParser;
import jenjinn.engine.parseutils.BoardParser;
import jflow.seq.Seq;

/**
 * @author ThomasB
 *
 */
final class TestFileParser extends AbstractTestFileParser
{

	@Override
	public Arguments parse(String fileName)
	{
		Seq<String> lines = loadFile(fileName);

		if (lines.size() == 11) {
			BoardState state = BoardParser.parse(lines.take(9));
			boolean hasLegalMoves = Boolean.parseBoolean(lines.get(9).toLowerCase().trim());
			GameTermination expectedTermination = GameTermination.valueOf(lines.last().toUpperCase().trim());
			return Arguments.of(state, hasLegalMoves, expectedTermination);
		}
		else {
			throw new IllegalArgumentException("Bad file format");
		}
	}
}
