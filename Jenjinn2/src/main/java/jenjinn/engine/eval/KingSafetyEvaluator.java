/**
 *
 */
package jenjinn.engine.eval;

import static java.lang.Long.bitCount;

import jenjinn.engine.base.Square;
import jenjinn.engine.boardstate.BoardState;
import jenjinn.engine.boardstate.DetailedPieceLocations;
import jenjinn.engine.pieces.ChessPiece;
import jenjinn.engine.pieces.ChessPieces;
import jflow.iterators.Flow;
import jflow.seq.Seq;

/**
 * @author ThomasB
 *
 */
public final class KingSafetyEvaluator implements EvaluationComponent
{
	static final Seq<ChessPiece> WKING_ATTACKERS = ChessPieces.black().drop(1).take(4);
	static final Seq<ChessPiece> BKING_ATTACKERS = ChessPieces.white().drop(1).take(4);

	public KingSafetyEvaluator()
	{
	}

	@Override
	public int evaluate(BoardState state)
	{
		KingSafetyTable kst = KingSafetyTable.INSTANCE;
		DetailedPieceLocations pieceLocs = state.getPieceLocations();
		long white = pieceLocs.getWhiteLocations(), black = pieceLocs.getBlackLocations();

		Square wKingLoc = pieceLocs.iterateLocs(ChessPiece.WHITE_KING).next();
		KingSafetyArea wSafetyArea = KingSafetyArea.get(wKingLoc);

		int bAttackUnits = 0;
		for (ChessPiece piece : WKING_ATTACKERS) {
			Flow<Square> locs = pieceLocs.iterateLocs(piece);
			while (locs.hasNext()) {
				long control = piece.getSquaresOfControl(locs.next(), white, black);
				bAttackUnits += bitCount(control & wSafetyArea.getOuterArea()) * kst.getOuterUnitValue(piece);
				bAttackUnits += bitCount(control & wSafetyArea.getInnerArea()) * kst.getInnerUnitValue(piece);
			}
		}

		Square bKingLoc = pieceLocs.iterateLocs(ChessPiece.BLACK_KING).next();
		KingSafetyArea bSafetyArea = KingSafetyArea.get(bKingLoc);

		int wAttackUnits = 0;
		for (ChessPiece piece : BKING_ATTACKERS) {
			Flow<Square> locs = pieceLocs.iterateLocs(piece);
			while (locs.hasNext()) {
				long control = piece.getSquaresOfControl(locs.next(), white, black);
				wAttackUnits += bitCount(control & bSafetyArea.getOuterArea()) * kst.getOuterUnitValue(piece);
				wAttackUnits += bitCount(control & bSafetyArea.getInnerArea()) * kst.getInnerUnitValue(piece);
			}
		}

		return kst.indexSafetyTable(wAttackUnits) - kst.indexSafetyTable(bAttackUnits);
	}
}
