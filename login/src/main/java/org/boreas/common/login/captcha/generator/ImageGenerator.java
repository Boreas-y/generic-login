package org.boreas.common.login.captcha.generator;

import java.awt.image.RenderedImage;

/**
 * This interface specifies an image generator which can generate an
 * {@code RenderedImage} object using the given string.
 * 
 * @author boreas
 *
 */
public interface ImageGenerator {

	/**
	 * Create a {@code BufferedImage} object with the given content.
	 * 
	 * @return
	 */
	RenderedImage generateImage(String content);
}
