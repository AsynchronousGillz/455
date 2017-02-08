package cs455.scaling.pool;

import java.util.Random;

public class Producer extends Thread{
	private Random random;
	private Queue quex;
	private int current_block;
	private int total_size;
	private double sum;

	public Producer(Queue q, int size) {
		quex = q;
		this.total_size = size;
		this.sum = 0;
		this.current_block = 0;
		random = new Random();
	}

	public void run() {
		for (int i = 0; i < this.total_size; i++) {
			Double bufferElement = random.nextDouble() * 100.0;
			this.sum += bufferElement;
			this.current_block++;
			if ((this.current_block % 100000) == 0) {
				System.out.printf("Producer: Generated "); 
				System.out.format("%,d",this.current_block);
				System.out.printf(" items, Cumulative value of generated items = %f\n", this.sum);
			}
			quex.put(bufferElement);
		}
		System.out.format("Producer: Finished generating %,d items.%n", this.total_size);
	}	

}
