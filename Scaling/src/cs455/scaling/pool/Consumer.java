
public class Consumer extends Thread {
	private Queue quex;
	private int total_size;
	private int current_block;
	private double sum;

	public Consumer(Queue q, int total_size) {
		quex = q;
		this.current_block = 0;
		this.total_size = total_size;
	}

	public void run() {
		double value = 0;
		for (int i = 0; i < this.total_size; i++) {
			value = quex.get();
			this.sum += value;
			this.current_block++;
			if ((this.current_block % 100000) == 0) {
				System.out.printf("Consumer: Consumed ");
				System.out.format("%,d",this.current_block);
				System.out.printf(" items, Cumulative value of consumed items = %f\n", this.sum);
			}
		}
		System.out.format("Consumer: Finshed consuming %,d times.%n",this.total_size);
	}

}
