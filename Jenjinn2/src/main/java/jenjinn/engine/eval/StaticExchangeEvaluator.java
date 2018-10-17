/**
 *
 */
package jenjinn.engine.eval;

import static java.lang.Math.max;
import static jenjinn.engine.bitboards.BitboardUtils.bitboardsIntersect;
import static jenjinn.engine.bitboards.Bitboards.emptyBoardAttackset;

import jenjinn.engine.base.Side;
import jenjinn.engine.base.Square;
import jenjinn.engine.bitboards.BitboardIterator;
import jenjinn.engine.boardstate.BoardState;
import jenjinn.engine.boardstate.DetailedPieceLocations;
import jenjinn.engine.pieces.ChessPiece;
import jenjinn.engine.pieces.ChessPieces;
import jflow.iterators.Flow;

/**
 * @author ThomasB
 *
 */
public class StaticExchangeEvaluator
{
	private long target, source, attadef, xrays;

	public boolean isGoodExchange(Square sourceSquare, Square targetSquare, BoardState state)
	{
		// Make sure all instance variables set correctly first
		DetailedPieceLocations pieceLocs = state.getPieceLocations();
		source = sourceSquare.bitboard;
		target = targetSquare.bitboard;
		generateAttackDefenseInfo(pieceLocs);
		long knightLocs = pieceLocs.locationsOf(ChessPiece.WHITE_KNIGHT)
				| pieceLocs.locationsOf(ChessPiece.BLACK_KNIGHT);

		int d = 0;
		int[] gain = new int[32];
		gain[d] = PieceValues.MIDGAME.valueOf(pieceLocs.getPieceAt(targetSquare));
		ChessPiece attPiece = pieceLocs.getPieceAt(source);

		Side activeSide = state.getActiveSide();
		do {
			d++;
			activeSide = activeSide.otherSide();
			gain[d] = PieceValues.MIDGAME.valueOf(attPiece) - gain[d - 1];
			if (max(-gain[d - 1], gain[d]) < 0) {
				break;
			}

			attadef ^= source;
			// If a knight moves to attack or defend it can't open an x-ray.
			if (!bitboardsIntersect(source, knightLocs)) {
				updateXrays(pieceLocs);
			}
			source = getLeastValuablePiece(state.getPieceLocations(), activeSide);
			attPiece = pieceLocs.getPieceAt(source);
		} while (source != 0);

		while (--d > 0) {
			gain[d - 1] = -Math.max(-gain[d - 1], gain[d]);
		}
		return gain[0]>= 0;
	}

	private void updateXrays(DetailedPieceLocations pieceLocs)
	{
		if (xrays != 0) {
			Flow<Square> xrayLocs = BitboardIterator.from(xrays);
			long white = pieceLocs.getWhiteLocations(), black = pieceLocs.getBlackLocations();
			while (xrayLocs.hasNext()) {
				Square loc = xrayLocs.next();
				ChessPiece p = pieceLocs.getPieceAt(loc);
				if (bitboardsIntersect(p.getSquaresOfControl(loc, white, black), target)) {
					long locBitboard = loc.bitboard;
					xrays ^= locBitboard;
					attadef ^= locBitboard;
				}
			}
		}
	}

	private void generateAttackDefenseInfo(DetailedPieceLocations locationProvider)
	{
		attadef = 0L;
		xrays = 0L;
		long white = locationProvider.getWhiteLocations();
		long black = locationProvider.getBlackLocations();

		for (ChessPiece p : ChessPieces.all()) {
			Flow<Square> locations = locationProvider.iterateLocs(p);
			while (locations.hasNext()) {
				Square loc = locations.next();
				long control = p.getSquaresOfControl(loc, white, black);
				if (bitboardsIntersect(control, target)) {
					attadef |= loc.bitboard;
				}
				else if (p.isSlidingPiece() && bitboardsIntersect(emptyBoardAttackset(p, loc), target)) {
					xrays |= loc.bitboard;
				}
			}
		}
	}

	private long getLeastValuablePiece(DetailedPieceLocations locationProvider, Side fromSide)
	{
		for (ChessPiece p : ChessPieces.ofSide(fromSide)) {
			long intersection = attadef & locationProvider.locationsOf(p);
			if (intersection != 0) {
				return (intersection & -intersection);
			}
		}
		return 0L;
	}
}
