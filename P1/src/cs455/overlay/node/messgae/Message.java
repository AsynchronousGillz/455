// File name Message.java
package cs455.overlay.messgae;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cs455.overlay.node.NodeAddress;

public abstract class Message {

	public enum MsgTypes {
		ERROR_MSG,
		SUCCESS_MSG,
		REGISTER_NODE_MSG,
		NODE_INFO_MSG,
		NODE_JOIN_MSG,
		ROUTING_INFO_MSG 
	}
	
	private int id;
	public MsgTypes msgType;
	private NodeAddress nodeAddress;
	
	public Message(NodeAddress node, int id) {
		this.id = id;
		nodeAddress.clone(node);
	}
	
	public int getID() {
		return id;
	}

	public MsgTypes getMsgType() {
		return msgType;
	}
	
	public byte[] convertToBytes() throws IOException {
		byte[] bytes = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            oos.flush();
            bytes = bos.toByteArray();
        } finally {
            if (oos != null) {
                oos.close();
            }
            if (bos != null) {
                bos.close();
            }
        }
        return bytes;
	}
	
	public Object convertBytes(byte[] bytes) throws IOException {
		Object obj = null;
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            obj = ois.readObject();
        } catch (ClassNotFoundException e) {
        	System.out.println("Error: Class not found");
        }finally {
            if (bais != null) {
                bais.close();
            }
            if (ois != null) {
                ois.close();
            }
        } 
        return obj;
	}

}
