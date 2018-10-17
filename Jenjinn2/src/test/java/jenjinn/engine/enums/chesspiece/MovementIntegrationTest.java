/**
 *
 */
package jenjinn.engine.enums.chesspiece;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import jenjinn.engine.base.Square;
import jenjinn.engine.base.FileUtils;
import jenjinn.engine.pieces.Piece;
import jenjinn.engine.pieces.ChessPieces;
import jenjinn.engine.utils.BasicPieceLocations;

/**
 * For each
 *
 * @author ThomasB
 */
class MovementIntegrationTest
{
	static final String INPUT_FILE_NAME = "movementIntegrationTestData";

	@Test
	void testNonPawnMoves()
	{
		final List<Piece> nonPawns = ChessPieces.iterate().filter(p -> !p.isPawn()).toList();
		Square.iterateAll().forEach(square -> testMovesAgreeAtSquare(square, nonPawns));
	}

	@Test
	void testPawnMoves()
	{
		final List<Piece> pawns = asList(Piece.WHITE_PAWN, Piece.BLACK_PAWN);
		Square.iterateAll().drop(8).take(48).forEach(square -> testMovesAgreeAtSquare(square, pawns));
	}

	void testMovesAgreeAtSquare(final Square square, final List<Piece> piecesToTest)
	{
		piecesToTest.stream().forEach(piece ->
		{
			FileUtils.cacheResource(getClass(), INPUT_FILE_NAME)
			.map(BasicPieceLocations::reconstructFrom)
			.forEach(locations -> testMovesAreCorrect(piece, square, locations));
		});
	}

	void testMovesAreCorrect(final Piece piece, final Square square, final BasicPieceLocations pieceLocations)
	{
		final TestChessPiece constraintPiece = TestChessPiece.values()[piece.ordinal()];
		final long white = pieceLocations.getWhite(), black = pieceLocations.getBlack();

		assertEquals(
				constraintPiece.getSquaresOfControl(square, white, black),
				piece.getSquaresOfControl(square, white, black),
				"SOC:Piece=" + piece.name() + ", Square=" + square.name() + ", " + pieceLocations.toString()
				);

		assertEquals(
				constraintPiece.getAttacks(square, white, black),
				piece.getAttacks(square, white, black),
				"Attacks:Piece=" + piece.name() + ", Square=" + square.name() + ", " + pieceLocations.toString()
				);

		assertEquals(
				constraintPiece.getMoves(square, white, black),
				piece.getMoves(square, white, black),
				"Moves:Piece=" + piece.name() + ", Square=" + square.name() + ", " + pieceLocations.toString()
				);
	}

	// Debugging the test
//	@SuppressWarnings("unused")
//	public static void main(final String[] args)
//	{
//		final String failString = "SOC:Piece=WHITE_KING, Square=H1, PieceLocations[white:100003001c09101|black:6000a18054000008]";
//		final PieceLocations locs = PieceLocations.reconstructFrom("PieceLocations[white:100003001c09101|black:6000a18054000008]");
//		//		BoardSquare
//		System.out.println(FormatBoard.fromPieceLocations(locs));
//
//		System.out.println(FormatBoard.fromBitboard(ChessPiece.WHITE_KING.getSquaresOfControl(BoardSquare.H1, locs)));
//		System.out.println(FormatBoard.fromBitboard(TestChessPiece.WHITE_KING.getSquaresOfControl(BoardSquare.H1, locs)));
//	}
}
