
public class Queue {
	private int size;
	private int used;
	private int front;
	private int back;
	private double contents[];

	public Queue(int s) {
		size = s;
		used = front = back = 0;
		contents = new double[size];
	}

	public int get_size() {
		return(size);
	}

	public synchronized double get() {
		while (used == 0) {
			try {
				this.wait();
				//	System.out.printf("Queue is empty. %d = this.size\n");
			} catch (InterruptedException e) {}
		}
		notify();
		this.used--;
		return (contents[this.front++ % this.size]);
	}

	public synchronized void put(double value) {
		while (used == size) {
			try {
				this.wait();
				//	System.out.printf("Queue is full. %d = this.size\n");
			} catch (InterruptedException e) {}
		}
		this.used++;
		contents[this.back++ % this.size] = value;
		notify();
	}
}
