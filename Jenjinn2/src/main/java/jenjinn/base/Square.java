package jenjinn.base;

import static java.util.Arrays.asList;

import java.util.Optional;

import jflow.iterators.factories.Repeatedly;
import jflow.iterators.misc.Exceptions;
import jflow.seq.Seq;

/**
 * Enumeration of the 64 chess squares on a chess board.
 * Ordered the same as the order in which the single
 * occupancy bitboards are generated.
 *
 * @author TB
 * @date 30 Nov 2016
 */
public enum Square
{
	// DON'T CHANGE ORDER
	H1, G1, F1, E1, D1, C1, B1, A1,
	H2, G2, F2, E2, D2, C2, B2, A2,
	H3, G3, F3, E3, D3, C3, B3, A3,
	H4, G4, F4, E4, D4, C4, B4, A4,
	H5, G5, F5, E5, D5, C5, B5, A5,
	H6, G6, F6, E6, D6, C6, B6, A6,
	H7, G7, F7, E7, D7, C7, B7, A7,
	H8, G8, F8, E8, D8, C8, B8, A8;
	
	public static final Seq<Square> ALL = Seq.of(values());
	
	public final int index, rank, file;
	public final long bitboard;
	
	private Square()
	{
		index = ordinal();
		rank = ordinal() / 8;
		file = ordinal() % 8;
		bitboard = 1L << ordinal();
	}

	public int getNumberOfSquaresLeft(Dir direction)
	{
		return (int) Repeatedly.apply(sq -> sq.flatMap(x -> x.getNextSquare(direction)), Optional.of(this))
				.drop(1)
				.takeWhile(Optional::isPresent).count();
	}

	public Optional<Square> getNextSquare(Dir direction)
	{
		int newRank = rank + direction.rankIndexChange;
		int newFile = file + direction.fileIndexChange;

		if (0 <= newRank && newRank < 8 && 0 <= newFile && newFile < 8) {
			return Optional.of(fromRankAndFileIndices(newRank, newFile));
		}
		else {
			return Optional.empty();
		}
	}

	public Seq<Square> getAllSquares(Iterable<Dir> directions, int maxDist)
	{
		Exceptions.requireArgument(maxDist >= 0);
		return Seq.copy(directions).flatMap(dir -> {
			return Repeatedly.apply(sq -> sq.flatMap(x -> x.getNextSquare(dir)), Optional.of(this))
					.drop(1)
					.takeWhile(Optional::isPresent)
					.take(maxDist)
					.map(x -> x.get());
		});
	}

	public Seq<Square> getAllSquares(Dir direction, int maxDist)
	{
		return getAllSquares(asList(direction), maxDist);
	}

	public static Square of(int index)
	{
		return values()[index];
	}

	public static Square fromRankAndFileIndices(int rankIndex, int fileIndex)
	{
		return values()[fileIndex + 8*rankIndex];
	}
}
