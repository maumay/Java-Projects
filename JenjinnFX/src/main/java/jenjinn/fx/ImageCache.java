/**
 *
 */
package jenjinn.fx;

import java.util.function.Function;

import javafx.scene.image.Image;
import jenjinn.base.FileUtils;
import jenjinn.pieces.ChessPieces;
import jenjinn.pieces.Piece;
import jflow.collections.FList;

/**
 * @author ThomasB
 */
public enum ImageCache
{
	INSTANCE;

	private final FList<Image> pieceImages;

	private ImageCache()
	{
		Function<Piece, String> nameMap = piece -> {
			String[] xs = piece.name().split("_");
			char[] chars = {xs[0].charAt(0), piece.isKnight()? 'N' : xs[1].charAt(0)};
			return new String(chars) + "64.png";
		};

		pieceImages = ChessPieces.iterate()
				.map(p -> FileUtils.absoluteName(getClass(), nameMap.apply(p)))
				.map(getClass()::getResourceAsStream)
				.map(Image::new)
				.toList();
	}

	public Image getImageOf(Piece piece)
	{
		return pieceImages.get(piece.ordinal());
	}
}
