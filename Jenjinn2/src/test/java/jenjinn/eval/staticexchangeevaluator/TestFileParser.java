/**
 *
 */
package jenjinn.eval.staticexchangeevaluator;

import static jflow.utilities.CollectionUtil.drop;
import static jflow.utilities.CollectionUtil.take;

import java.util.List;

import org.junit.jupiter.params.provider.Arguments;

import jenjinn.parseutils.AbstractTestFileParser;
import jenjinn.parseutils.BoardParser;
import jflow.iterators.factories.Iter;

/**
 * @author ThomasB
 *
 */
final class TestFileParser extends AbstractTestFileParser
{
	@Override
	public Arguments parse(String fileName)
	{
		List<String> lines = loadFile(fileName);
		return Arguments.of(BoardParser.parse(take(9, lines)), parseIndividualCases(drop(9, lines)));
	}

	private List<IndividualStateCase> parseIndividualCases(List<String> caseLines)
	{
		if (caseLines.isEmpty()) {
			throw new IllegalArgumentException();
		}
		return Iter.over(caseLines).map(IndividualStateCase::from).toList();
	}
}
