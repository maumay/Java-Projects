/**
 *
 */
package jenjinn.engine.moves;

import static jenjinn.engine.moves.MoveConstants.BLACK_CASTLE_REMOVALS;
import static jenjinn.engine.moves.MoveConstants.WHITE_CASTLE_REMOVALS;

import java.util.Set;

import jenjinn.engine.base.CastleZone;
import jenjinn.engine.base.DevelopmentPiece;
import jenjinn.engine.base.Side;
import jenjinn.engine.boardstate.BoardState;
import jenjinn.engine.boardstate.MoveReversalData;
import jenjinn.engine.pieces.ChessPiece;

/**
 * @author ThomasB
 *
 */
public final class CastleMove extends AbstractChessMove
{
	private final CastleZone wrappedZone;
	private final Set<CastleZone> rightsRemovedByThisMove;

	public CastleMove(CastleZone wrappedZone)
	{
		super(wrappedZone.kingSource, wrappedZone.kingTarget);
		this.wrappedZone = wrappedZone;
		this.rightsRemovedByThisMove =
				wrappedZone.isWhiteZone()? WHITE_CASTLE_REMOVALS : BLACK_CASTLE_REMOVALS;
	}

	@Override
	void updateDevelopedPieces(BoardState state, MoveReversalData unmakeDataStore)
	{
		unmakeDataStore.setPieceDeveloped(null);
	}

	@Override
	void updatePieceLocations(BoardState state, MoveReversalData unmakeDataStore)
	{
		Side currentActiveSide = state.getActiveSide();
		boolean whiteActive = currentActiveSide.isWhite();

		ChessPiece king = whiteActive ? ChessPiece.WHITE_KING : ChessPiece.BLACK_KING;
		state.getPieceLocations().removePieceAt(wrappedZone.kingSource, king);
		state.getPieceLocations().addPieceAt(wrappedZone.kingTarget, king);

		ChessPiece rook = whiteActive ? ChessPiece.WHITE_ROOK : ChessPiece.BLACK_ROOK;
		state.getPieceLocations().removePieceAt(wrappedZone.rookSource, rook);
		state.getPieceLocations().addPieceAt(wrappedZone.rookTarget, rook);

		unmakeDataStore.setPieceTaken(null);

		// handle enpassant
		unmakeDataStore.setDiscardedEnpassantSquare(state.getEnPassantSquare());
		state.setEnPassantSquare(null);
		// handle half move clock
		unmakeDataStore.setDiscardedHalfMoveClock(state.getHalfMoveClock().getValue());
		state.getHalfMoveClock().incrementValue();
	}

	@Override
	void updateCastlingStatus(BoardState state, MoveReversalData unmakeDataStore)
	{
		super.updateCastlingStatus(state, unmakeDataStore);
		state.getCastlingStatus().setCastlingStatus(wrappedZone);
	}

	@Override
	public void reverseMove(BoardState state, MoveReversalData unmakeDataStore)
	{
		super.reverseMove(state, unmakeDataStore);
		state.getCastlingStatus().removeCastlingStatus(wrappedZone);
	}

	@Override
	void resetPieceLocations(BoardState state, MoveReversalData unmakeDataStore)
	{
		boolean whiteActive = state.getActiveSide().isWhite();
		ChessPiece king = whiteActive ? ChessPiece.WHITE_KING : ChessPiece.BLACK_KING;
		state.getPieceLocations().removePieceAt(wrappedZone.kingTarget, king);
		state.getPieceLocations().addPieceAt(wrappedZone.kingSource, king);

		ChessPiece rook = whiteActive ? ChessPiece.WHITE_ROOK : ChessPiece.BLACK_ROOK;
		state.getPieceLocations().removePieceAt(wrappedZone.rookTarget, rook);
		state.getPieceLocations().addPieceAt(wrappedZone.rookSource, rook);
	}

	@Override
	Set<CastleZone> getAllRightsToBeRemoved()
	{
		return rightsRemovedByThisMove;
	}

	@Override
	DevelopmentPiece getPieceDeveloped()
	{
		return null;
	}

	public CastleZone getWrappedZone()
	{
		return wrappedZone;
	}

	@Override
	public String toString()
	{
		return new StringBuilder("CastleMove[zone=")
				.append(wrappedZone.name())
				.append("]")
				.toString();
	}

	@Override
	public String toCompactString()
	{
		return wrappedZone.getSimpleIdentifier();
	}
}
