package cs455.overlay.msg;

public class Registation extends Protocol {

	/**
	 * 
	 * @param message
	 * @param type
	 * 			0 for REGISTER_REQUEST
	 * 			1 for DEREGISTER_REQUEST
	 * 			2 for REGISTER_RESPONSE
	 */
	public Registation(String message, int type) {
		super();
		switch(type) {
			case 0: 
				this.setType("REGISTER_REQUEST");
				this.setMessage(message.getBytes());
				break;
			case 1:
				this.setType("DEREGISTER_REQUEST");
				this.setMessage(message.getBytes());
				break;
			case 2:
				this.setType("REGISTER_RESPONSE");
				byte i = (message.equals("True")) ? (byte)1 : (byte)0 ;
				this.setIndicator(i);
				this.setMessage(message.getBytes());
				break;
		}
		
	}
	
	public String getMessageString() {
		return new String(this.getMessage());
	}

}
