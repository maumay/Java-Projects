/**
 *
 */
package jenjinn.fx;

import java.util.function.Function;

import javafx.scene.image.Image;
import jenjinn.base.FileUtils;
import jenjinn.pieces.ChessPieces;
import jenjinn.pieces.Piece;
import jflow.seq.Seq;

/**
 * @author ThomasB
 */
public enum ImageCache
{
	INSTANCE;

	private final Seq<Image> pieceImages;

	private ImageCache()
	{
		Function<Piece, String> nameMap = piece -> {
			String[] xs = piece.name().split("_");
			char[] chars = {xs[0].charAt(0), piece.isKnight()? 'N' : xs[1].charAt(0)};
			return new String(chars) + "64.png";
		};

		pieceImages = ChessPieces.ALL.flow()
				.map(p -> FileUtils.absoluteName(getClass(), nameMap.apply(p)))
				.map(getClass()::getResourceAsStream)
				.map(Image::new)
				.toSeq();
	}

	public Image getImageOf(Piece piece)
	{
		return pieceImages.get(piece.ordinal());
	}
}
