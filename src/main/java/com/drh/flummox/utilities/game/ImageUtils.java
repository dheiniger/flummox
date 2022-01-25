package com.drh.flummox.utilities.game;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ImageUtils {

	/**
	 * Mirrors a list of images horizontally.
	 * @param images The images to be mirrored.
	 * @return A new list mirrored images.
	 */
	public static List<BufferedImage> mirrorImages(List<BufferedImage> images) {
		List<BufferedImage> mirroredImages = new ArrayList<>();
		for(BufferedImage image : images) {
			AffineTransform affineTransform = AffineTransform.getScaleInstance(-1, 1);
			affineTransform.translate(-image.getWidth(), 0);
			AffineTransformOp op = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			mirroredImages.add(op.filter(image, null));
		}
		return mirroredImages;
	}
}
