/**
 *
 */
package jenjinn.engine.parseutils;

import static java.util.Arrays.asList;
import static jflow.utilities.CollectionUtil.drop;
import static jflow.utilities.CollectionUtil.head;
import static jflow.utilities.CollectionUtil.last;
import static jflow.utilities.Strings.allMatches;

import java.util.List;

import jenjinn.engine.base.Square;
import jenjinn.engine.base.CastleZone;
import jenjinn.engine.moves.CastleMove;
import jenjinn.engine.moves.ChessMove;
import jenjinn.engine.moves.EnpassantMove;
import jenjinn.engine.moves.PromotionMove;
import jenjinn.engine.moves.PromotionResult;
import jenjinn.engine.moves.StandardMove;
import jenjinn.engine.pgn.CommonRegex;
import jflow.iterators.factories.Iterators;
import jflow.iterators.misc.Pair;
import jflow.utilities.Strings;

/**
 * @author ThomasB
 *
 */
public final class ShorthandMoveParser
{
	private ShorthandMoveParser()
	{
	}

	public static List<ChessMove> parse(String encoded)
	{
		String ec = encoded.trim().toUpperCase();
		if (!ec.matches(CommonRegex.SHORTHAND_MOVE)) {
			throw new IllegalArgumentException(encoded);
		}
		switch (ec.charAt(0)) {
		case 'S':
			return parseStandardMoves(ec);
		case 'P':
			return parsePromotionMoves(ec);
		case 'E':
			return asList(parseEnpassantMove(ec));
		case 'C':
			return parseCastlingMoves(ec);
		default:
			throw new RuntimeException();
		}
	}

	private static List<ChessMove> parseStandardMoves(String ec)
	{
		String mtarg = CommonRegex.MULTI_TARGET, cord = CommonRegex.CORD;
		if (ec.matches("S\\[(" + mtarg + "|" + cord + ")\\]")) {
			Pair<Square, Iterable<Square>> moves = parseMultiMove(ec.substring(2, ec.length() - 1));
			return Iterators.wrap(moves.second())
					.map(target -> new StandardMove(moves.first(), target))
					.filterAndCastTo(ChessMove.class)
					.toList();
		}
		else if (ec.matches("S\\[(" + CommonRegex.DOUBLE_SQUARE + ")\\]")) {
			List<Square> squares = Strings.allMatches(ec, CommonRegex.SINGLE_SQUARE)
					.map(s -> Square.valueOf(s.toUpperCase()))
					.toList();

			return asList(new StandardMove(head(squares), last(squares)));
		}
		else {
			throw new IllegalArgumentException(ec);
		}
	}

	private static Pair<Square, Iterable<Square>> parseMultiMove(String ec)
	{
		if (ec.matches(CommonRegex.CORD)) {
			List<Square> squares = CordParser.parse(ec);
			return Pair.of(head(squares), drop(1, squares));
		}
		else if (ec.matches(CommonRegex.MULTI_TARGET)) {
			List<Square> squares = allMatches(ec, CommonRegex.SINGLE_SQUARE)
					.map(String::toUpperCase)
					.map(Square::valueOf)
					.toList();
			return Pair.of(head(squares), drop(1, squares));
		}
		else {
			throw new IllegalArgumentException(ec);
		}
	}

	private static List<ChessMove> parsePromotionMoves(String ec)
	{
		String mtarg = CommonRegex.MULTI_TARGET, cord = CommonRegex.CORD;
		String result = Strings.lastMatch(ec, "[NBRQ]")
				.orElseThrow(() -> new IllegalArgumentException(ec));
		if (ec.matches("P\\[(" + mtarg + "|" + cord + ") " + result + "\\]")) {
			Pair<Square, Iterable<Square>> moves = parseMultiMove(ec.substring(2, ec.length() - 3));
			return Iterators.wrap(moves.second())
					.map(target -> new PromotionMove(moves.first(), target, PromotionResult.valueOf(result)))
					.filterAndCastTo(ChessMove.class)
					.toList();
		}
		else if (ec.matches("P\\[(" + CommonRegex.DOUBLE_SQUARE + ") " + result + "\\]")) {
			List<Square> squares = Strings.allMatches(ec, CommonRegex.SINGLE_SQUARE)
					.map(s -> Square.valueOf(s.toUpperCase()))
					.toList();

			return asList(new PromotionMove(head(squares), last(squares), PromotionResult.valueOf(result)));
		}
		else {
			throw new IllegalArgumentException(ec);
		}
	}

	private static List<ChessMove> parseCastlingMoves(String ec)
	{
		String cz = CommonRegex.CASTLE_ZONE;
		if (ec.matches("[cC]\\[( *" + cz + " *)+\\]")) {
			return allMatches(ec, cz)
					.map(CastleZone::fromSimpleIdentifier)
					.map(CastleMove::new)
					.filterAndCastTo(ChessMove.class)
					.toList();
		}
		else {
			throw new IllegalArgumentException(ec);
		}
	}

	private static ChessMove parseEnpassantMove(String ec)
	{
		String sq = CommonRegex.SINGLE_SQUARE;
		if (ec.matches("[eE]\\[ *" + sq + " +" + sq + " *\\]")) {
			List<Square> sqMatches = allMatches(ec, sq)
					.map(String::toUpperCase)
					.map(Square::valueOf)
					.toList();
			return new EnpassantMove(head(sqMatches), last(sqMatches));
		}
		else {
			throw new IllegalArgumentException(ec);
		}
	}
}
