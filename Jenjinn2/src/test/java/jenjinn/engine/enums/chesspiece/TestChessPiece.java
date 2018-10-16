/**
 *
 */
package jenjinn.engine.enums.chesspiece;

import static java.util.Arrays.asList;
import static jenjinn.engine.base.Dir.E;
import static jenjinn.engine.base.Dir.N;
import static jenjinn.engine.base.Dir.NE;
import static jenjinn.engine.base.Dir.NEE;
import static jenjinn.engine.base.Dir.NNE;
import static jenjinn.engine.base.Dir.NNW;
import static jenjinn.engine.base.Dir.NW;
import static jenjinn.engine.base.Dir.NWW;
import static jenjinn.engine.base.Dir.S;
import static jenjinn.engine.base.Dir.SE;
import static jenjinn.engine.base.Dir.SEE;
import static jenjinn.engine.base.Dir.SSE;
import static jenjinn.engine.base.Dir.SSW;
import static jenjinn.engine.base.Dir.SW;
import static jenjinn.engine.base.Dir.SWW;
import static jenjinn.engine.base.Dir.W;
import static jenjinn.engine.bitboards.BitboardUtils.bitboardsIntersect;
import static jenjinn.engine.bitboards.BitboardUtils.bitwiseOr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jenjinn.engine.base.Square;
import jenjinn.engine.base.Dir;
import jenjinn.engine.pieces.Moveable;
import jflow.iterators.Flow;
import jflow.iterators.factories.Iter;

/**
 * @author ThomasB
 */
public enum TestChessPiece implements Moveable
{
	WHITE_PAWN
	{
		@Override
		public long getMoves(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			final long allPieces = whitePieces | blackPieces;
			final List<Square> pushSquares = new ArrayList<>();
			final Square firstPush = currentLocation.getNextSquare(N);
			if (firstPush != null && !bitboardsIntersect(firstPush.asBitboard(), allPieces))
			{
				pushSquares.add(firstPush);
				final int locIndex = currentLocation.ordinal();
				if (7 < locIndex && locIndex < 16)
				{
					final Square secondPush = firstPush.getNextSquare(N);
					if (!bitboardsIntersect(secondPush.asBitboard(), allPieces))
					{
						pushSquares.add(secondPush);
					}
				}
			}
			return bitwiseOr(pushSquares) | getAttacks(currentLocation, whitePieces, blackPieces);
		}

		@Override
		public long getAttacks(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			return getSquaresOfControl(currentLocation, whitePieces, blackPieces) & blackPieces;
		}

		@Override
		public long getSquaresOfControl(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			final List<Dir> directions = asList(NE, NW);
			return bitwiseOr(currentLocation.getAllSquares(directions, 1));
		}
	},

	WHITE_KNIGHT
	{
		@Override
		public long getMoves(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			return getSquaresOfControl(currentLocation, whitePieces, blackPieces) & (~whitePieces);
		}

		@Override
		public long getAttacks(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			return getSquaresOfControl(currentLocation, whitePieces, blackPieces) & blackPieces;
		}

		@Override
		public long getSquaresOfControl(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			final List<Dir> directions = asList(NNE, NEE, SEE, SSE, SSW, SWW, NWW, NNW);
			return bitwiseOr(currentLocation.getAllSquares(directions, 1));
		}
	},

	WHITE_BISHOP
	{
		@Override
		public long getMoves(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			return getSquaresOfControl(currentLocation, whitePieces, blackPieces) & (~whitePieces);
		}

		@Override
		public long getAttacks(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			return getSquaresOfControl(currentLocation, whitePieces, blackPieces) & blackPieces;
		}

		@Override
		public long getSquaresOfControl(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			final List<Dir> directions = asList(NE, SE, SW, NW);
			return getSlidingPieceSquaresOfControl(whitePieces | blackPieces, currentLocation, directions);
		}
	},

	WHITE_ROOK
	{
		@Override
		public long getMoves(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			return getSquaresOfControl(currentLocation, whitePieces, blackPieces) & (~whitePieces);
		}

		@Override
		public long getAttacks(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			return getSquaresOfControl(currentLocation, whitePieces, blackPieces) & blackPieces;
		}

		@Override
		public long getSquaresOfControl(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			final List<Dir> directions = asList(Dir.N, Dir.S, Dir.W, Dir.E);
			return getSlidingPieceSquaresOfControl(whitePieces | blackPieces, currentLocation, directions);
		}
	},

	WHITE_QUEEN
	{
		@Override
		public long getMoves(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			return getSquaresOfControl(currentLocation, whitePieces, blackPieces) & (~whitePieces);
		}

		@Override
		public long getAttacks(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			return getSquaresOfControl(currentLocation, whitePieces, blackPieces) & blackPieces;
		}

		@Override
		public long getSquaresOfControl(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			return WHITE_BISHOP.getSquaresOfControl(currentLocation, whitePieces, blackPieces)
					| WHITE_ROOK.getSquaresOfControl(currentLocation, whitePieces, blackPieces);
		}
	},

	WHITE_KING
	{
		@Override
		public long getMoves(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			return getSquaresOfControl(currentLocation, whitePieces, blackPieces) & (~whitePieces);
		}

		@Override
		public long getAttacks(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			return getSquaresOfControl(currentLocation, whitePieces, blackPieces) & blackPieces;
		}

		@Override
		public long getSquaresOfControl(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			final List<Dir> directions = asList(N, NE, E, SE, S, SW, W, NW);
			return bitwiseOr(currentLocation.getAllSquares(directions, 1));
		}
	},

	BLACK_PAWN
	{
		@Override
		public long getMoves(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			final long allPieces = whitePieces | blackPieces;
			final List<Square> pushSquares = new ArrayList<>();
			final Square firstPush = currentLocation.getNextSquare(Dir.S);
			if (firstPush != null && !bitboardsIntersect(firstPush.asBitboard(), allPieces))
			{
				pushSquares.add(firstPush);
				final int locIndex = currentLocation.ordinal();
				if (47 < locIndex && locIndex < 56)
				{
					final Square secondPush = firstPush.getNextSquare(Dir.S);
					if (!bitboardsIntersect(secondPush.asBitboard(), allPieces))
					{
						pushSquares.add(secondPush);
					}
				}
			}
			return bitwiseOr(pushSquares) | getAttacks(currentLocation, whitePieces, blackPieces);
		}

		@Override
		public long getAttacks(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			return getSquaresOfControl(currentLocation, whitePieces, blackPieces) & whitePieces;
		}

		@Override
		public long getSquaresOfControl(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			final List<Dir> directions = asList(SE, SW);
			return bitwiseOr(currentLocation.getAllSquares(directions, 1));
		}
	},

	BLACK_KNIGHT
	{
		@Override
		public long getMoves(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			return getSquaresOfControl(currentLocation, whitePieces, blackPieces) & (~blackPieces);
		}

		@Override
		public long getAttacks(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			return getSquaresOfControl(currentLocation, whitePieces, blackPieces) & whitePieces;
		}

		@Override
		public long getSquaresOfControl(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			final List<Dir> directions = asList(NNE, NEE, SEE, SSE, SSW, SWW, NWW, NNW);
			return bitwiseOr(currentLocation.getAllSquares(directions, 1));
		}
	},

	BLACK_BISHOP
	{
		@Override
		public long getMoves(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			return getSquaresOfControl(currentLocation, whitePieces, blackPieces) & (~blackPieces);
		}

		@Override
		public long getAttacks(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			return getSquaresOfControl(currentLocation, whitePieces, blackPieces) & whitePieces;
		}

		@Override
		public long getSquaresOfControl(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			final List<Dir> directions = asList(NE, SE, SW, NW);
			return getSlidingPieceSquaresOfControl(whitePieces | blackPieces, currentLocation, directions);
		}
	},

	BLACK_ROOK
	{
		@Override
		public long getMoves(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			return getSquaresOfControl(currentLocation, whitePieces, blackPieces) & (~blackPieces);
		}

		@Override
		public long getAttacks(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			return getSquaresOfControl(currentLocation, whitePieces, blackPieces) & whitePieces;
		}

		@Override
		public long getSquaresOfControl(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			final List<Dir> directions = asList(N, E, S, W);
			return getSlidingPieceSquaresOfControl(whitePieces | blackPieces, currentLocation, directions);
		}
	},

	BLACK_QUEEN
	{
		@Override
		public long getMoves(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			return getSquaresOfControl(currentLocation, whitePieces, blackPieces) & (~blackPieces);
		}

		@Override
		public long getAttacks(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			return getSquaresOfControl(currentLocation, whitePieces, blackPieces) & whitePieces;
		}

		@Override
		public long getSquaresOfControl(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			return BLACK_BISHOP.getSquaresOfControl(currentLocation, whitePieces, blackPieces)
					| BLACK_ROOK.getSquaresOfControl(currentLocation, whitePieces, blackPieces);
		}
	},

	BLACK_KING
	{
		@Override
		public long getMoves(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			return getSquaresOfControl(currentLocation, whitePieces, blackPieces) & (~blackPieces);
		}

		@Override
		public long getAttacks(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			return getSquaresOfControl(currentLocation, whitePieces, blackPieces) & whitePieces;
		}

		@Override
		public long getSquaresOfControl(final Square currentLocation, final long whitePieces, final long blackPieces)
		{
			final List<Dir> directions = asList(N, NE, E, SE, S, SW, W, NW);
			return bitwiseOr(currentLocation.getAllSquares(directions, 1));
		}
	};

	private static long getSlidingPieceSquaresOfControl(final long allPieces, final Square startSquare, final List<Dir> movementDirections)
	{
		final List<Square> controlSquares = new ArrayList<>(64);
		Iter.over(movementDirections).forEach(direction ->
		{
			Square current = startSquare.getNextSquare(direction);

			while (current != null && !bitboardsIntersect(current.asBitboard(), allPieces)) {
				controlSquares.add(current);
				current = current.getNextSquare(direction);
			}
			if (current != null) {
				controlSquares.add(current);
			}
		});
		return bitwiseOr(controlSquares);
	}

	public static List<TestChessPiece> valuesAsList()
	{
		return Arrays.asList(values());
	}

	public static Flow<TestChessPiece> iterateAll()
	{
		return Iter.over(valuesAsList());
	}
}
