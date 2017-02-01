package cs455.overlay.msg;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class EdgeInformation extends Protocol {
	
	//******************************************************************************
	
		/**
		 * Used when creating the message from a Protocol. SINGLE_WEIGHT.
		 * @param port
		 * 			the port in int form 
		 * @param weight
		 * 			connection information int form
		 */
		public EdgeInformation(int port, int cost) {
			super();
			this.setType("SINGLE_WEIGHT");
			convertMessage(port, cost);
		}
		
		/**
		 * Used when converting the message from a Protocol. SINGLE_WEIGHT.
		 * @param message
		 * 			connection information in byte form.
		 */
		public EdgeInformation(byte[] message) {
			super();
			this.setType("TASK_MESSAGE");
			this.setMessage(message);
		}
		
		/**
		 * TODO
		 * @param nodes
		 */
		public void convertMessage(int port, int cost) {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(output);
			try {
				out.writeInt(cost);
				out.writeInt(port);
			} catch (IOException e) {
				e.printStackTrace();
			}
			message = output.toByteArray();
		}
		
		public int getPort() {
			int ret = 0;
			ByteArrayInputStream input = new ByteArrayInputStream(message);
			DataInputStream in = new DataInputStream(input);
			try {
				in.readInt();
				ret = in.readInt();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return ret;
		}
		
		/**
		 * TODO
		 */
		public int getCost() {
			int ret = 0;
			ByteArrayInputStream input = new ByteArrayInputStream(message);
			DataInputStream in = new DataInputStream(input);
			try {
				ret = in.readInt();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return ret;
		}
		
		/**
		 * TODO
		 */
		public String toString() {
			String ret = "";
			ByteArrayInputStream input = new ByteArrayInputStream(message);
			DataInputStream in = new DataInputStream(input);
			try {
				ret = in.readInt()+" "+in.readInt();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return ret;
		}
}
