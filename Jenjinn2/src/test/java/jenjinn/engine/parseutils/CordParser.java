/**
 *
 */
package jenjinn.engine.parseutils;

import static jflow.utilities.CollectionUtil.head;
import static jflow.utilities.CollectionUtil.last;
import static jflow.utilities.Strings.allMatches;

import java.util.List;

import jenjinn.engine.base.Square;
import jenjinn.engine.base.Dir;
import jenjinn.engine.pgn.CommonRegex;
import jflow.iterators.factories.Iter;

/**
 * @author ThomasB
 *
 */
public final class CordParser
{
	private CordParser()
	{
	}

	/**
	 * Given a string in the form {@code A2->A5} we construct a list of boardsquares
	 * connecting the two squares in a straight line cord.
	 *
	 * @param encodedCord
	 *            The string defining the cord.
	 * @return A list of boardsquares connecting the start and end squares
	 *         (inclusive).
	 * @throws IllegalArgumentException If no cord connects the two squares.
	 */
	public static List<Square> parse(String encodedCord)
	{
		String ec = encodedCord.trim();
		if (!ec.matches(CommonRegex.CORD)) {
			throw new IllegalArgumentException(encodedCord);
		}

		List<Square> squares = allMatches(ec, CommonRegex.SINGLE_SQUARE)
				.map(String::toUpperCase)
				.map(Square::valueOf)
				.toList();

		Square start = head(squares), end = last(squares);
		Dir dir = Dir.ofLineBetween(start, end)
				.orElseThrow(() -> new IllegalArgumentException(encodedCord));

		return Iter.over(start.getAllSquares(dir, 10))
				.takeWhile(sq -> sq != end)
				.insert(start)
				.append(end)
				.toList();
	}
}
