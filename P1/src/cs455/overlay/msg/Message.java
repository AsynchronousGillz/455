package cs455.overlay.msg;

public class Message extends Protocol {
	
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
