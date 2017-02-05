package cs455.overlay.msg;

public class Registation extends Protocol {

	/**
	 * 
	 * @param message
	 * @param type
	 * 			0 for REGISTER_REQUEST
	 * 			1 for DEREGISTER_REQUEST
	 * 			2 for REGISTER_RESPONSE
	 * 			3 for TASK_COMPLETE
	 */
	public Registation(String message, int type) {
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
				byte i = (message.equals("True")) ? (byte)1 : (byte)0 ;
				this.setIndicator(i);
				break;
			case 3:
				this.setType("TASK_COMPLETE");
				break;
		}
		this.setMessage(message.getBytes());
	}
	
	public boolean checkResponse() {
		if (getIndicator() == 0)
			return false;
		else
			return true;
	}
	
	public String getMessageString() {
		return new String(message);
	}
	
	public String toString() {
		return " id :"+type+", type: " + Types[type] + ", message: " + getMessageString();
	}
	
}
