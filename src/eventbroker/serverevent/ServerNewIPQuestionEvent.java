package eventbroker.serverevent;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

@SuppressWarnings("serial")
public class ServerNewIPQuestionEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_NEW_IP_QUESTION";

	private int questionID;
	private int pixelSize;
	private byte[] bytesImage;
	private String[] answers;
	private int correctAnswer;

	// Constructor
	public ServerNewIPQuestionEvent(int questionID, BufferedImage img, int pixelSize,String[] answers, int correctAnswer) {
		super.type = EVENTTYPE;
		this.questionID = questionID;
		this.pixelSize = pixelSize;
		this.answers = answers;
		this.correctAnswer = correctAnswer;
		
		ByteArrayOutputStream bAOS = new ByteArrayOutputStream();
		try {
			ImageIO.write(img, "jpg", bAOS);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.bytesImage = bAOS.toByteArray();
	}

	// Getters
	public int getQuestionID() {
		return questionID;
	}

	public BufferedImage getImage() {
		ByteArrayInputStream bAIS = new ByteArrayInputStream(bytesImage);
		BufferedImage bufImage = null;
		try {
			bufImage = ImageIO.read(bAIS);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bufImage;
	}
	
	public int getPixelSize() {
		return pixelSize;
	}

	public String[] getAnswers() {
		return answers;
	}
	
	public int getCorrectAnswer() {
		return this.correctAnswer;
	}
}
