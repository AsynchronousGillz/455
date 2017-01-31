package cs455.overlay.msg;

public class Message extends Protocol {
	
	/**
	 * TODO
	 * @param message
	 */
	public Message(byte[] message) {
		super();
		this.setType("TASK_MESSAGE");
		this.setMessage(message);
	}
	
	/**
	 * 
	 * @param number
	 * 			number of current round.
	 */
	public Message(int number) {
		super();
		this.setType("TASK_MESSAGE");
	}
}
