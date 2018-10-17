/**
 *
 */
package jenjinn.engine.eval.piecesquaretables;

import java.util.List;

import jenjinn.engine.base.Square;
import jenjinn.engine.boardstate.LocationTracker;
import jenjinn.engine.pieces.ChessPiece;
import jenjinn.engine.pieces.ChessPieces;
import jflow.iterators.factories.Iter;
import jflow.iterators.factories.IterRange;

/**
 * @author ThomasB
 *
 */
public final class PieceSquareTables
{
	private final List<PieceSquareTable> tables;

	public PieceSquareTables(List<PieceSquareTable> whiteTables)
	{
		if (whiteTables.size() != 6 || IterRange.to(6).anyMatch(i -> whiteTables.get(i).getAssociatedPiece().ordinal() != i)) {
			throw new IllegalArgumentException();
		}
		this.tables = Iter.over(whiteTables)
				.append(Iter.over(whiteTables).map(PieceSquareTable::invertValues))
				.toList();
	}

	public int getLocationValue(ChessPiece piece, Square location)
	{
		return tables.get(piece.ordinal()).getValueAt(location);
	}

	public int evaluateLocations(List<LocationTracker> pieceLocations)
	{
		if (pieceLocations.size() != 12) {
			throw new IllegalArgumentException();
		}
		int eval = 0;
		for (int i = 0; i < pieceLocations.size(); i++) {
			PieceSquareTable pieceTable = tables.get(i);
			eval += pieceLocations.get(i).iterator()
					.mapToInt(loc -> pieceTable.getValueAt(loc))
					.fold(0, (a, b) -> a + b);
		}
		return eval;
	}

	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = prime * result + ((tables == null) ? 0 : tables.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PieceSquareTables other = (PieceSquareTables) obj;
		if (tables == null) {
			if (other.tables != null)
				return false;
		} else if (!tables.equals(other.tables))
			return false;
		return true;
	}

	public static PieceSquareTables endgame()
	{
		return new PieceSquareTables(
				ChessPieces.white()
				.map(p -> TableParser.parseFile(p, p.name().substring(6).toLowerCase() + "-endgame"))
				.toList());
	}

	public static PieceSquareTables midgame()
	{
		return new PieceSquareTables(
				ChessPieces.white()
				.map(p -> TableParser.parseFile(p, p.name().substring(6).toLowerCase() + "-midgame"))
				.toList());
	}
}
