package cs455.overlay.msg;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class EdgeMessage extends ProtocolMessage {
	
	//******************************************************************************
	
		/**
		 * Used when creating the message from a Protocol. SINGLE_WEIGHT.
		 * @param port
		 * 			the port in int form 
		 * @param weight
		 * 			connection information int form
		 */
		public EdgeMessage(int port, int cost) {
			super();
			this.setType("SINGLE_WEIGHT");
			convertMessage(port, cost);
		}
		
		/**
		 * Used when converting the message from a Protocol. SINGLE_WEIGHT.
		 * @param message
		 * 			connection information in byte form.
		 */
		public EdgeMessage(byte[] message) {
			super();
			this.setType("TASK_MESSAGE");
			this.setMessage(message);
		}
		
		/**
		 * {@link EdgeMessage} contains the information when a link between nodes 
		 * is setup and the cost and port of the server is sent over.
		 * @param port
		 * @param cost
		 */
		public void convertMessage(int port, int cost) {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(output);
			try {
				out.writeInt(cost);
				out.writeInt(port);
				output.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			message = output.toByteArray();
		}
		
		/**
		 * Gets the port from the message in int form.
		 * @return port
		 */
		public int getPort() {
			int ret = 0;
			ByteArrayInputStream input = new ByteArrayInputStream(message);
			DataInputStream in = new DataInputStream(input);
			try {
				in.readInt();
				ret = in.readInt();
				input.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return ret;
		}
		
		/**
		 * Gets the cost from the message in int form.
		 *@return cost
		 */
		public int getCost() {
			int ret = 0;
			ByteArrayInputStream input = new ByteArrayInputStream(message);
			DataInputStream in = new DataInputStream(input);
			try {
				ret = in.readInt();
				input.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return ret;
		}
		
		/**
		 * String representation as "cost port"
		 */
		public String toString() {
			String ret = "";
			ByteArrayInputStream input = new ByteArrayInputStream(message);
			DataInputStream in = new DataInputStream(input);
			try {
				ret = in.readInt()+" "+in.readInt();
				input.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return ret;
		}
}
