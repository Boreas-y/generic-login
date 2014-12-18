package org.boreas.common.login.captcha.generator;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.Random;

/**
 * A simple implement of {@code ImageGenerator}, can generate an JPG image with
 * defined width and height.
 * 
 * @author boreas
 *
 */
public class SimpleImageGenerator implements ImageGenerator {

	private Font font;
	private Integer width;
	private Integer height;
	private Color backgroundColor = new Color(220, 220, 220);
	private Color characterColor = new Color(40, 40, 40);
	private int puzzleLineCount = 40;
	private Color[] randomColors;
	private Random random = new Random();

	public SimpleImageGenerator(Integer width, Integer height) {
		this.width = width;
		this.height = height;
		font = new Font("Fixedsys", Font.CENTER_BASELINE, (int) (height * 0.8));
		randomColors = new Color[30];
		for (int i = 0; i < randomColors.length; i++) {
			randomColors[i] = new Color(random.nextInt(256),
					random.nextInt(256), random.nextInt(256));
		}
	}

	@Override
	public RenderedImage generateImage(String content) {
		BufferedImage bImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics graphics = bImage.getGraphics();
		generateMonochromeBackground(graphics);
		renderPuzzleLines(graphics);
		renderCharacters(graphics, content);
		graphics.dispose();
		return bImage;
	}

	private void generateMonochromeBackground(Graphics graphics) {
		graphics.setColor(backgroundColor);
		graphics.fillRect(0, 0, width, height);
	}

	private void renderPuzzleLines(Graphics graphics) {
		for (int i = 0; i <= puzzleLineCount; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(13);
			int yl = random.nextInt(15);
			graphics.setColor(getRandomColor());
			graphics.drawLine(x, y, x + xl, y + yl);
		}
	}

	private void renderCharacters(Graphics graphics, String content) {
		graphics.setColor(characterColor);
		graphics.setFont(font);
		for (int i = 0; i < content.length(); i++) {
			graphics.translate(random.nextInt(3), random.nextInt(3));
			graphics.drawString(content.substring(i, i + 1), i * width
					/ content.length(), 15);
		}
	}

	private Color getRandomColor() {
		return randomColors[random.nextInt(randomColors.length)];
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public Color getCharacterColor() {
		return characterColor;
	}

	public void setCharacterColor(Color characterColor) {
		this.characterColor = characterColor;
	}

	public int getPuzzleLineCount() {
		return puzzleLineCount;
	}

	public void setPuzzleLineCount(int puzzleLineCount) {
		this.puzzleLineCount = puzzleLineCount;
	}

	public Color[] getRandomColors() {
		return randomColors;
	}

	public void setRandomColors(Color[] randomColors) {
		this.randomColors = randomColors;
	}

	public Random getRandom() {
		return random;
	}

	public void setRandom(Random random) {
		this.random = random;
	}

}
