package edu.illinois.cs242.effects;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/* A class to implement the various pixel effects.
*
* @author Jon Tedesco
*/
public class SimpleEffects {

    /**
     * Copy a given image
     *
     * @param source A pixel representation of the image to modify
     * @return The copy of the image
     */
	public static int[][] copy(int[][] source) {
		int[][] result = new int[source.length][source[0].length];
		for(int row = 0; row<source.length; row++){
			for(int col = 0; col<source[0].length; col++){
				result[row][col] = source[row][col];
			}
		}
	return result;
	}

    /**
     * Resize an image to a height and width
     *
     * @param source A pixel representation of the image to modify
     * @return The modified image
     */
	public static int[][] resize(int[][] source, int newWidth, int newHeight) {
		double wRatio = source.length/(double)newWidth;
		double hRatio = source[0].length/(double)newHeight;
		int[][] result = new int[newWidth][newHeight];

		for(int x = 0; x<newWidth; x++){
			for(int y = 0; y<newHeight; y++){
				result[x][y] = source[(int)(x*wRatio)][(int)(y*hRatio)];
			}
		}
		
		return result;
	}

    /**
     * Flip an image vertically
     *
     * @param source A pixel representation of the image to modify
     * @return The modified image
     */
	public static int[][] flip(int[][] source) {
		int[][] result = new int[source.length][source[0].length];
		for(int row = 0; row<source.length; row++){
			for(int col = 0; col<source[0].length; col++){
				result[row][col] = source[row][result[row].length-1-col];
			}
		}
		return result;
	}
	
    /**
     * Flip an image horizontally
     *
     * @param source A pixel representation of the image to modify
     * @return The modified image
     */
	public static int[][] mirror(int[][] source) {
		int[][] result = new int[source.length][source[0].length];
		for(int row = 0; row<source.length; row++){
			for(int col = 0; col<source[0].length; col++){
				result[row][col] = source[result.length-1-row][col];
			}
		}
		return result;
	}
	
    /**
     * Rotate a given image left
     *
     * @param source A pixel representation of the image to modify
     * @return The modified image
     */

	public static int[][] rotateLeft(int[][] source) {
		int[][] result = new int[source[0].length][source.length];
		for(int row = 0; row<source.length; row++){
			for(int col = 0; col<source[0].length; col++){
				result[col][row] = source[row][col];
			}
		}
		result = flip(result);
		return result;
	}

    /**
     * Rotate a given image by a certain degree
     *
     * @param image The image to rotate
     * @param degrees The degree by which to rotate the image
     *
     * @return The final, rotated image
     */
    public static BufferedImage rotate(BufferedImage image, double degrees) {
        if(degrees < 0){
            degrees = degrees % 360d;
        }
        double rads = Math.toRadians(degrees);

        int width = image.getWidth();
        int height = image.getHeight();

        double w = Math.abs(width * Math.cos(rads));
        double z = Math.abs(width * Math.sin(rads));
        double x = Math.abs(height * Math.sin(rads));
        double y = Math.abs(height * Math.cos(rads));

        int rectWidth = (int)Math.ceil(w+x);
        int rectHeight = (int)Math.ceil(y+z);

        BufferedImage result = new BufferedImage(rectWidth,rectHeight,BufferedImage.TYPE_INT_ARGB);
        AffineTransform transform = new AffineTransform();

        transform.setToTranslation((rectWidth - width)/2, (rectHeight-height)/2);
        transform.rotate(rads,width/2,height/2);

        AffineTransformOp op = new AffineTransformOp(transform,AffineTransformOp.TYPE_BICUBIC);
        op.filter(image,result);

        return result;
    }
}
