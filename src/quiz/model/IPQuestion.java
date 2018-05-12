package quiz.model;

import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import quiz.util.Difficulty;
import quiz.util.Theme;

public class IPQuestion extends Question {
	
	public final static int START_PIXELSIZE = 400;

	private BufferedImage bufImage;
	private Image fxImage;
	private String[] answers;
	private int pixelSize = START_PIXELSIZE;
	private int correctAnswer = -1;

	// Constructor
	public IPQuestion(int questionID, BufferedImage bufImage, boolean asFX, String[] answers) {
		super(questionID);
		if(asFX) {
			this.fxImage = SwingFXUtils.toFXImage(bufImage, null);
			this.bufImage = null;
		} else {
			this.bufImage = bufImage;
			this.fxImage = null;
		}
		this.answers = answers;
	}
	
	public IPQuestion(int questionID, Theme theme, Difficulty difficulty, BufferedImage bufImage, boolean asFX, String[] answers, int correctAnswer) {
		super(questionID, theme, difficulty);
		
		if(asFX) {
			this.fxImage = SwingFXUtils.toFXImage(bufImage, null);
			this.bufImage = null;
		} else {
			this.bufImage = bufImage;
			this.fxImage = null;
		}
		this.answers = answers;
		this.correctAnswer = correctAnswer;
	}

	// Getters
	public BufferedImage getBufferedImage() {
		return bufImage;
	}
	
	public Image getFullFXImage() {
		return fxImage;
	}
	
	public WritableImage getPixelatedFXImage() {
		PixelReader pR = fxImage.getPixelReader();
		
		WritableImage pixelatedCopy = new WritableImage((int) fxImage.getWidth(), (int) fxImage.getHeight());
		PixelWriter pW = pixelatedCopy.getPixelWriter();
		
		for(int y=0; y < (int) fxImage.getHeight(); y+=pixelSize) {
			for(int x=0; x < (int) fxImage.getWidth(); x+=pixelSize) {
				Color res = pR.getColor(x, y);
				int count = 0;
				for(int yInc=0; yInc < pixelSize && y + yInc < fxImage.getHeight(); yInc++) {	// Calculate average colour within each new pixel
					for(int xInc=0; xInc < pixelSize && x + xInc < fxImage.getWidth(); xInc++) {
						count++;
						double t = 1/count;
						res = res.interpolate(pR.getColor(x + xInc, y + yInc), t);
					}
				}
				
				for(int yInc=0; yInc < pixelSize && y + yInc < fxImage.getHeight(); yInc++) {	// Set all pixels within "bigger pixel" to the calculated average colour
					for(int xInc=0; xInc < pixelSize && x + xInc < fxImage.getWidth(); xInc++) {
						pW.setColor(x + xInc, y + yInc, res);
					}
				}
			}
		}
		
		return pixelatedCopy;
	}
	
	public int getPixelSize() {
		return this.pixelSize;
	}
	
	public void setPixelSize(int pixelSize) {
		this.pixelSize = pixelSize;
	}

	public String[] getAnswers() {
		return answers;
	}

	public int getCorrectAnswer() {
		return correctAnswer;
	}

	// Methods
	public boolean checkCorrectAnswer(int answer) {
		return correctAnswer == answer;
	}

}
