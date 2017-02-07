package cs455.overlay.msg;

public class RegistationMessage extends ProtocolMessage {

	/**
	 * Convert from a {@link ProtocolMessage} to a {@link RegistationMessage}
	 * @param message
	 * 			message in bye form
	 * @param type
	 * 			0 for REGISTER_REQUEST
	 * 			1 for DEREGISTER_REQUEST
	 * 			2 for REGISTER_RESPONSE
	 * 			3 for TASK_COMPLETE
	 * 			4 for PULL_TRAFFIC_SUMMARY
	 */
	public RegistationMessage(byte[] message, int type) {
		super();
		switch(type) {
			case 0: 
				this.setType("REGISTER_REQUEST");
				break;
			case 1:
				this.setType("DEREGISTER_REQUEST");
				break;
			case 2:
				this.setType("REGISTER_RESPONSE");
				break;
			case 3:
				this.setType("TASK_COMPLETE");
				break;
			case 4:
				this.setType("PULL_TRAFFIC_SUMMARY");
				break;
		}
		this.setMessage(message);
	}
	
	/**
	 * 
	 * @param message
	 * @param type
	 * 			0 for REGISTER_REQUEST
	 * 			1 for DEREGISTER_REQUEST
	 * 			2 for REGISTER_RESPONSE
	 * 			3 for TASK_COMPLETE
	 * 			4 for PULL_TRAFFIC_SUMMARY
	 */
	public RegistationMessage(String message, int type) {
		super();
		switch(type) {
			case 0: 
				this.setType("REGISTER_REQUEST");
				break;
			case 1:
				this.setType("DEREGISTER_REQUEST");
				break;
			case 2:
				this.setType("REGISTER_RESPONSE");
				break;
			case 3:
				this.setType("TASK_COMPLETE");
				break;
			case 4:
				this.setType("PULL_TRAFFIC_SUMMARY");
				break;
		}
		this.setMessage(message.getBytes());
	}
	
	public String getMessageString() {
		return new String(message);
	}
	
	public String toString() {
		return " id :"+type+", type: " + Types[type] + ", message: " + getMessageString();
	}
	
}
