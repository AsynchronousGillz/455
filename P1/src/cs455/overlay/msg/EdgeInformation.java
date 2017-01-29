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
		 * @param weight
		 * 			connection information int form
		 */
		public EdgeInformation(int weight) {
			super();
			this.setType("SINGLE_WEIGHT");
			convertMessage(weight);
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
		public void convertMessage(int weight) {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(output);
			try {
				out.writeInt(weight);
			} catch (IOException e) {
				e.printStackTrace();
			}
			message = output.toByteArray();
		}
		
		/**
		 * TODO
		 */
		public int getWeight() {
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
			int ret = 0;
			ByteArrayInputStream input = new ByteArrayInputStream(message);
			DataInputStream in = new DataInputStream(input);
			try {
				ret = in.readInt();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return new String(ret+"");
		}
}
