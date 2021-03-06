/**
 *
 */
package jenjinn.fx;

import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Point2D;
import jenjinn.base.Square;
import jflow.iterators.Flow;
import jflow.iterators.misc.Pair;

/**
 * @author ThomasB
 */
public final class BoardSquareLocations
{
	private final Map<Square, Point2D> squareToPoint = new HashMap<>();
	private final Map<Point2D, Square> pointToSquare = new HashMap<>();

	public BoardSquareLocations(Flow<? extends Pair<Square, Point2D>> src)
	{
		src.forEach(pair -> put(pair._1, pair._2));
	}

	public static BoardSquareLocations getDefault()
	{
		return Square.ALL.flow()
		.map(square -> Pair.of(square, new Point2D(7 - square.file, 7 - square.rank)))
		.build(BoardSquareLocations::new);
	}

	private void put(Square square, Point2D point)
	{
		squareToPoint.put(square, point);
		pointToSquare.put(point, square);
	}

	public Point2D get(Square square)
	{
		if (!squareToPoint.containsKey(square)) {
			throw new IllegalStateException();
		}
		return squareToPoint.get(square);
	}

	public Square get(Point2D point)
	{
		if (!pointToSquare.containsKey(point)) {
			throw new IllegalStateException();
		}
		return pointToSquare.get(point);
	}

	public BoardSquareLocations rotate(double boardWidth)
	{
		final Point2D translation = new Point2D(boardWidth, boardWidth);
		return Square.ALL.flow()
				.map(sq -> Pair.of(sq, get(sq).multiply(-1).add(translation)))
				.build(BoardSquareLocations::new);
	}
}
