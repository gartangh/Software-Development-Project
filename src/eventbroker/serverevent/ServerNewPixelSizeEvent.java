package eventbroker.serverevent;

@SuppressWarnings("serial")
public class ServerNewPixelSizeEvent extends ServerEvent {
	
	public final static String EVENTTYPE = "SERVER_NEW_PIXEL_SIZE_EVENT";
	
	private int pixelSize;
	private int questionID;
	
	public ServerNewPixelSizeEvent(int questionID, int pixelSize) {
		super.type = EVENTTYPE;
		this.pixelSize = pixelSize;
		this.questionID = questionID;
	}
	
	public int getPixelSize() {
		return this.pixelSize;
	}
	
	public int getQuestionID() {
		return this.questionID;
	}
}
