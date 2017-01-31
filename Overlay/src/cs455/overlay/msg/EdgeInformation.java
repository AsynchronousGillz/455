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
		 * @param address
		 * 			the ip address and port separated by a ':'
		 * @param weight
		 * 			connection information int form
		 */
		public EdgeInformation(String address, int cost) {
			super();
			this.setType("SINGLE_WEIGHT");
			convertMessage(address, cost);
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
		public void convertMessage(String address, int cost) {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(output);
			byte[] bytes = address.getBytes();
			try {
				out.writeInt(cost);
				out.writeInt(bytes.length);
				out.write(bytes, 0, bytes.length);
			} catch (IOException e) {
				e.printStackTrace();
			}
			message = output.toByteArray();
		}
		
		public String getAddress() {
			byte bytes[] = null;
			ByteArrayInputStream input = new ByteArrayInputStream(message);
			DataInputStream in = new DataInputStream(input);
			try {
				in.readInt();
				int len = in.readInt();
				bytes = new byte[len];
				in.read(bytes, 0, len);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return bytes.toString();
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
			byte bytes[] = null;
			try {
				ret = in.readInt()+" ";
				int len = in.readInt();
				bytes = new byte[len];
				in.read(bytes, 0, len);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return ret + bytes.toString();
		}
}
