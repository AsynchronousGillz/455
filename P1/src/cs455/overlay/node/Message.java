// File name Message.java
package cs455.overlay.node;

public class Message {

	public int type;
	private Object payload;
	
	private final String[] Types = { 
            "REGISTER_REQUEST",
            "REGISTER_RESPONSE",
            "DEREGISTER_REQUEST",
            "MESSAGING_NODES_LIST",
            "LINK_WEIGHTS",
            "TASK_INITIATE",
            "TASK_COMPLETE",
            "PULL_TRAFFIC_SUMMARY",
            "TRAFFIC_SUMMARY"
		};
	
	public Message(Object payload) {
		this.payload = payload;
	}

	public String getType() {
		return Types[type];
	}

	public void setType(String type) throws IllegalArgumentException {
		int index = -1;
		for (int i = 0; i < Types.length; i++) {
			if (Types[i].equals(type)) {
				index = i;
				break;
			}
		}
		if (index == -1)
			throw new IllegalArgumentException("Invalid type of Message.");
		this.type = index;
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}
	
	public String toString() {
		return "Message [type=" + Types[type] + ", type=" + type + "]";
	}

}
