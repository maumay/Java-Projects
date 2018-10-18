/**
 *
 */
package jenjinn.fx;

import static java.util.Comparator.comparing;
import static jenjinn.bitboards.BitboardUtils.bitboardsIntersect;

import java.util.Optional;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import jenjinn.base.Side;
import jenjinn.base.Square;
import jenjinn.boardstate.BoardState;
import jenjinn.boardstate.calculators.LegalMoves;
import jenjinn.moves.ChessMove;
import jenjinn.pieces.ChessPieces;
import jenjinn.pieces.Piece;
import jflow.iterators.misc.Pair;
import jflow.seq.Seq;

/**
 * @author ThomasB
 *
 */
public final class ChessBoard
{
	private final ColorScheme colors;
	private final BoardState state;
	private final VisualBoard board = new VisualBoard();

	private BoardSquareLocations squareLocations = BoardSquareLocations.getDefault();
	private Side boardPerspective = Side.WHITE;
	private Optional<Square> selectedSquare = Optional.empty();

	public ChessBoard(ColorScheme colors, BoardState stateToWatch)
	{
		this.colors = colors;
		this.state = stateToWatch;
		board.widthProperty().addListener((x, y, z) -> Platform.runLater(this::redraw));
		board.heightProperty().addListener((x, y, z) -> Platform.runLater(this::redraw));
	}

	public void redraw()
	{
		squareLocations = calculateBoardPoints(board.getBoardSize());
		redrawBackground();
		redrawSquares();
		redrawMarkers();
		redrawPieces();
	}

	public void redrawBackground()
	{
		double size = board.getBackingSize();
		GraphicsContext gc = board.getBackingGC();
		gc.clearRect(0, 0, size, size);
		gc.setFill(colors.backingColor);
		gc.fillRect(0, 0, size, size);
	}

	public void redrawSquares()
	{
		double size = board.getBoardSize(), sqSize = size / 8;
		GraphicsContext gc = board.getBoardGC();
		gc.clearRect(0, 0, size, size);
		Square.ALL.forEach(square -> {
			Point2D c = squareLocations.get(square);
			boolean lightSquare = (square.index + square.rank) % 2 == 0;
			gc.setFill(lightSquare ? colors.lightSquares : colors.darkSquares);
			gc.fillRect(c.getX() - sqSize / 2, c.getY() - sqSize / 2, sqSize, sqSize);
		});
	}

	public void redrawMarkers()
	{
		double size = board.getBoardSize(), sqSize = size / 8;
		GraphicsContext gc = board.getMarkerGC();
		gc.clearRect(0, 0, size, size);
		if (selectedSquare.isPresent()) {
			Square sq = selectedSquare.get();
			drawLocationMarker(sq, gc, sqSize);
			Seq<ChessMove> legalMoves = LegalMoves.getAllMoves(state)
					.filter(mv -> mv.getSource().equals(sq))
					.toSeq();

			long allPieces = state.getPieceLocations().getAllLocations();
			legalMoves.forEach(mv -> {
				if (bitboardsIntersect(allPieces, mv.getTarget().bitboard)) {
					drawAttackMarker(mv.getTarget(), gc, sqSize);
				}
				else {
					drawMovementMarker(mv.getTarget(), gc, sqSize);
				}
			});
		}
	}

	private void drawAttackMarker(Square square, GraphicsContext gc, double size)
	{
		Point2D loc = squareLocations.get(square);
		Bounds renderBounds = RenderUtils.getSquareBounds(loc, size, 1);
		RenderUtils.strokeTarget(gc, renderBounds, colors.attackMarker);
	}

	private void drawMovementMarker(Square square, GraphicsContext gc, double size)
	{
		Point2D centre = squareLocations.get(square);
		Bounds locBounds = RenderUtils.getSquareBounds(centre, size, 0.9);
		RenderUtils.strokeOval(gc, locBounds, size / 20, colors.moveMarker);
	}

	private void drawLocationMarker(Square square, GraphicsContext gc, double size)
	{
		gc.setFill(colors.locationMarker);
		Point2D centre = squareLocations.get(square);
		Bounds locBounds = RenderUtils.getSquareBounds(centre, size, 0.9);
		gc.fillOval(locBounds.getMinX(), locBounds.getMinY(), locBounds.getWidth(), locBounds.getHeight());
	}

	public void redrawPieces()
	{
		double size = board.getBoardSize(), sqSize = size / 8;
		GraphicsContext gc = board.getPieceGC();
		gc.clearRect(0, 0, size, size);

		for (Piece piece : ChessPieces.ALL) {
			Image image = ImageCache.INSTANCE.getImageOf(piece);
			state.getPieceLocations().iterateLocs(piece).forEach(sq -> {
				Point2D loc = squareLocations.get(sq);
				Bounds b = RenderUtils.getSquareBounds(loc, sqSize, 1);
				gc.drawImage(image, b.getMinX(), b.getMinY(), b.getWidth(), b.getHeight());
			});
		}
	}

	/**
	 * Calculates an association of BoardSquares to the centre point of their
	 * required visual bounds relative to the local coordinate space of the canvas
	 * they will be drawn onto.
	 *
	 * @param width
	 *            The width of the board canvas.
	 */
	private BoardSquareLocations calculateBoardPoints(double width)
	{
		double squareWidth = width / 8;
		BoardSquareLocations locs = Square.ALL.flow()
				.map(sq -> Pair.of(sq, new Point2D((7.5 - sq.file) * squareWidth, (7.5 - sq.rank) * squareWidth)))
				.build(BoardSquareLocations::new);

		if (boardPerspective.isWhite()) {
			return locs;
		} else {
			return locs.rotate(width);
		}
	}

	public void setPerspective(Side side)
	{
		if (!side.equals(boardPerspective)) {
			boardPerspective = side;
			squareLocations = calculateBoardPoints(board.getBoardSize());
			Platform.runLater(this::redraw);
		}
	}

	public void switchPerspective()
	{
		setPerspective(boardPerspective.otherSide());
	}

	public Optional<Square> getSelectedSquare()
	{
		return selectedSquare;
	}

	public void setSelectedSquare(Optional<Square> selectedSquare)
	{
		this.selectedSquare = selectedSquare;
		Platform.runLater(this::redrawMarkers);
	}

	public Square getClosestSquare(Point2D query)
	{
		return Square.ALL.min(comparing(sq -> query.distance(squareLocations.get(sq)))).get();
	}

	public VisualBoard getFxComponent()
	{
		return board;
	}
}
