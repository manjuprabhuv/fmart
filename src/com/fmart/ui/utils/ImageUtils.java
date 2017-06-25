package com.fmart.ui.utils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ImageUtils {
	
	public static byte[] resizeImage(byte[] imageInByte) {
		InputStream in=null;
		BufferedImage bImageFromConvert = null;
		ByteArrayOutputStream baos=null;
		try {
			 
			// convert byte array back to BufferedImage
			in = new ByteArrayInputStream(imageInByte);
			bImageFromConvert = ImageIO.read(in);
			bImageFromConvert = resize(bImageFromConvert, 200, 200);
			baos = new ByteArrayOutputStream();
			ImageIO.write( bImageFromConvert, "jpg", baos );
			baos.flush();
			imageInByte = baos.toByteArray();
			
		
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}finally{
			try {
				baos.close();
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		}
		return imageInByte;

	}
	private static BufferedImage resize(BufferedImage img, int newW, int newH) {
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage dimg = dimg = new BufferedImage(newW, newH, img.getType());
		Graphics2D g = dimg.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
		g.dispose();
		return dimg;
	}

}
