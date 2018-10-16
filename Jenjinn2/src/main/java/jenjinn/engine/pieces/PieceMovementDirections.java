package jenjinn.engine.pieces;

import static java.util.Arrays.asList;

import java.util.List;

import jenjinn.engine.base.Dir;

/**
 * Convenience class containing the directions each piece type can move /
 * attack.
 * 
 * @author TB
 */
public class PieceMovementDirections
{
	/** White pawn movement directions */
	public static final List<Dir> WHITE_PAWN_MOVE = asList( Dir.N );

	/** Black pawn movement directions */
	public static final List<Dir> BLACK_PAWN_MOVE = asList( Dir.S );

	/** White Pawn attack directions */
	public static final List<Dir> WHITE_PAWN_ATTACK = asList( Dir.NE, Dir.NW );

	/** Black Pawn attack directions */
	public static final List<Dir> BLACK_PAWN_ATTACK = asList( Dir.SE, Dir.SW );

	/** Bishop directions */
	public static final List<Dir> BISHOP = asList( Dir.NE, Dir.NW, Dir.SE, Dir.SW );

	/** Rook directions */
	public static final List<Dir> ROOK = asList( Dir.N, Dir.W, Dir.S, Dir.E );

	/** Knight directions */
	public static final List<Dir> KNIGHT = asList( Dir.NNE, Dir.NNW, Dir.NWW, Dir.NEE, Dir.SEE, Dir.SSE, Dir.SSW, Dir.SWW );

	/** Queen directions */
	public static final List<Dir> QUEEN = asList( Dir.NE, Dir.NW, Dir.N, Dir.E, Dir.SE, Dir.S, Dir.SW, Dir.W );

	/** King directions */
	public static final List<Dir> KING = QUEEN;
}
