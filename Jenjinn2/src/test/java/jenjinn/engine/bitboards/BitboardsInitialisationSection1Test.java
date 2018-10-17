/**
 *
 */
package jenjinn.engine.bitboards;

import static jenjinn.engine.base.Square.A1;
import static jenjinn.engine.base.Square.A2;
import static jenjinn.engine.base.Square.A3;
import static jenjinn.engine.base.Square.A4;
import static jenjinn.engine.base.Square.A5;
import static jenjinn.engine.base.Square.A6;
import static jenjinn.engine.base.Square.A7;
import static jenjinn.engine.base.Square.A8;
import static jenjinn.engine.base.Square.B1;
import static jenjinn.engine.base.Square.B3;
import static jenjinn.engine.base.Square.B4;
import static jenjinn.engine.base.Square.B5;
import static jenjinn.engine.base.Square.B6;
import static jenjinn.engine.base.Square.B7;
import static jenjinn.engine.base.Square.C1;
import static jenjinn.engine.base.Square.C4;
import static jenjinn.engine.base.Square.C5;
import static jenjinn.engine.base.Square.C6;
import static jenjinn.engine.base.Square.D1;
import static jenjinn.engine.base.Square.E1;
import static jenjinn.engine.base.Square.E2;
import static jenjinn.engine.base.Square.F1;
import static jenjinn.engine.base.Square.F3;
import static jenjinn.engine.base.Square.G1;
import static jenjinn.engine.base.Square.G3;
import static jenjinn.engine.base.Square.G6;
import static jenjinn.engine.base.Square.H1;
import static jenjinn.engine.base.Square.H2;
import static jenjinn.engine.base.Square.H3;
import static jenjinn.engine.base.Square.H4;
import static jenjinn.engine.base.Square.H5;
import static jenjinn.engine.base.Square.H6;
import static jenjinn.engine.base.Square.H7;
import static jenjinn.engine.base.Square.H8;
import static jenjinn.engine.bitboards.Bitboards.antiDiagonalBitboard;
import static jenjinn.engine.bitboards.Bitboards.diagonalBitboard;
import static jenjinn.engine.bitboards.Bitboards.fileBitboard;
import static jenjinn.engine.bitboards.Bitboards.rankBitboard;
import static jenjinn.engine.bitboards.Bitboards.singleOccupancyBitboard;
import static jenjinn.engine.pieces.ChessPiece.BLACK_BISHOP;
import static jenjinn.engine.pieces.ChessPiece.BLACK_KING;
import static jenjinn.engine.pieces.ChessPiece.BLACK_KNIGHT;
import static jenjinn.engine.pieces.ChessPiece.BLACK_PAWN;
import static jenjinn.engine.pieces.ChessPiece.BLACK_QUEEN;
import static jenjinn.engine.pieces.ChessPiece.BLACK_ROOK;
import static jenjinn.engine.pieces.ChessPiece.WHITE_BISHOP;
import static jenjinn.engine.pieces.ChessPiece.WHITE_KING;
import static jenjinn.engine.pieces.ChessPiece.WHITE_KNIGHT;
import static jenjinn.engine.pieces.ChessPiece.WHITE_PAWN;
import static jenjinn.engine.pieces.ChessPiece.WHITE_QUEEN;
import static jenjinn.engine.pieces.ChessPiece.WHITE_ROOK;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import jenjinn.engine.base.Dir;
import jenjinn.engine.base.Square;
import jenjinn.engine.pieces.ChessPiece;
import jenjinn.engine.pieces.PieceMovementDirections;
import jflow.iterators.factories.IterRange;
import jflow.seq.Seq;

/**
 * @author ThomasB
 *
 */
class BitboardsInitialisationSection1Test
{
	@Test
	void testSingleOccupancyBitboard()
	{
		IterRange.to(64).forEach(i -> assertEquals(1L << i, singleOccupancyBitboard(i)));
	}

	@Test
	void testRankBitboard()
	{
		long[] expectedRanks = Seq.of(A1, A2, A3, A4, A5, A6, A7, A8)
				.map(square -> square.getAllSquares(Dir.E, 8).insert(square))
				.mapToLong(BitboardUtils::bitwiseOr);

		IterRange.to(8).forEach(i -> assertEquals(expectedRanks[i], rankBitboard(i)));
	}

	@Test
	void testFileBitboard()
	{
		long[] expectedFiles = Seq.of(H1, G1, F1, E1, D1, C1, B1, A1)
				.map(square -> square.getAllSquares(Dir.N, 8).insert(square))
				.mapToLong(BitboardUtils::bitwiseOr);

		IterRange.to(8).forEach(i -> assertEquals(expectedFiles[i], fileBitboard(i)));
	}

	@Test
	void testDiagonalBitboard()
	{
		long[] expectedDiagonals = Seq.of(H1, G1, F1, E1, D1, C1, B1, A1, A2, A3, A4, A5, A6, A7, A8)
				.map(square -> square.getAllSquares(Dir.NE, 8).insert(square))
				.mapToLong(BitboardUtils::bitwiseOr);

		IterRange.to(15).forEach(i -> assertEquals(expectedDiagonals[i], diagonalBitboard(i)));
	}

	@Test
	void testAntiDiagonalBitboard()
	{
		long[] expectedDiagonals = Seq.of(A1, B1, C1, D1, E1, F1, G1, H1, H2, H3, H4, H5, H6, H7, H8)
				.map(square -> square.getAllSquares(Dir.NW, 8).insert(square))
				.mapToLong(BitboardUtils::bitwiseOr);

		IterRange.to(15).forEach(i -> assertEquals(expectedDiagonals[i], antiDiagonalBitboard(i)));
	}

	@ParameterizedTest
	@MethodSource
	void testEmptyBoardMovesetBitboard(ChessPiece piece, Square location, Seq<Square> expectedMoveLocations)
	{
		assertEquals(BitboardUtils.bitwiseOr(expectedMoveLocations), Bitboards.emptyBoardMoveset(piece, location));
	}

	static Stream<Arguments> testEmptyBoardMovesetBitboard()
	{
		return Stream.of(
				Arguments.of(WHITE_PAWN, A2, Seq.of(A3, A4)),
				Arguments.of(WHITE_PAWN, B3, Seq.of(B4)),
				Arguments.of(WHITE_KNIGHT, C5, C5.getAllSquares(PieceMovementDirections.KNIGHT, 1)),
				Arguments.of(WHITE_BISHOP, F3, F3.getAllSquares(PieceMovementDirections.BISHOP, 8)),
				Arguments.of(WHITE_ROOK, B3, B3.getAllSquares(PieceMovementDirections.ROOK, 8)),
				Arguments.of(WHITE_QUEEN, H2, H2.getAllSquares(PieceMovementDirections.QUEEN, 8)),
				Arguments.of(WHITE_KING, E2, E2.getAllSquares(PieceMovementDirections.KING, 1)),

				Arguments.of(BLACK_PAWN, A2, Seq.of(A1)),
				Arguments.of(BLACK_PAWN, B7, Seq.of(B6, B5)),
				Arguments.of(BLACK_KNIGHT, C5, C5.getAllSquares(PieceMovementDirections.KNIGHT, 1)),
				Arguments.of(BLACK_BISHOP, F3, F3.getAllSquares(PieceMovementDirections.BISHOP, 8)),
				Arguments.of(BLACK_ROOK, B3, B3.getAllSquares(PieceMovementDirections.ROOK, 8)),
				Arguments.of(BLACK_QUEEN, H2, H2.getAllSquares(PieceMovementDirections.QUEEN, 8)),
				Arguments.of(BLACK_KING, E2, E2.getAllSquares(PieceMovementDirections.KING, 1))
				);
	}

	@ParameterizedTest
	@MethodSource
	void testEmptyBoardAttacksetBitboard(ChessPiece piece, Square location, Seq<Square> expectedMoveLocations)
	{
		assertEquals(BitboardUtils.bitwiseOr(expectedMoveLocations), Bitboards.emptyBoardAttackset(piece, location));
	}

	static Stream<Arguments> testEmptyBoardAttacksetBitboard()
	{
		return Stream.of(
				Arguments.of(WHITE_PAWN, A2, Seq.of(B3)),
				Arguments.of(WHITE_PAWN, B3, Seq.of(C4, A4)),
				Arguments.of(WHITE_PAWN, H5, Seq.of(G6)),
				Arguments.of(WHITE_KNIGHT, C5, C5.getAllSquares(PieceMovementDirections.KNIGHT, 1)),
				Arguments.of(WHITE_BISHOP, F3, F3.getAllSquares(PieceMovementDirections.BISHOP, 8)),
				Arguments.of(WHITE_ROOK, B3, B3.getAllSquares(PieceMovementDirections.ROOK, 8)),
				Arguments.of(WHITE_QUEEN, H2, H2.getAllSquares(PieceMovementDirections.QUEEN, 8)),
				Arguments.of(WHITE_KING, E2, E2.getAllSquares(PieceMovementDirections.KING, 1)),

				Arguments.of(BLACK_PAWN, A2, Seq.of(B1)),
				Arguments.of(BLACK_PAWN, B7, Seq.of(C6, A6)),
				Arguments.of(BLACK_PAWN, H4, Seq.of(G3)),
				Arguments.of(BLACK_KNIGHT, C5, C5.getAllSquares(PieceMovementDirections.KNIGHT, 1)),
				Arguments.of(BLACK_BISHOP, F3, F3.getAllSquares(PieceMovementDirections.BISHOP, 8)),
				Arguments.of(BLACK_ROOK, B3, B3.getAllSquares(PieceMovementDirections.ROOK, 8)),
				Arguments.of(BLACK_QUEEN, H2, H2.getAllSquares(PieceMovementDirections.QUEEN, 8)),
				Arguments.of(BLACK_KING, E2, E2.getAllSquares(PieceMovementDirections.KING, 1))
				);
	}
}
