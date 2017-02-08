
public class Driver {

	public static void main(String[] args) {
		int run_size = 1000000;
		Queue c = new Queue(1000);
		Producer p1 = new Producer(c, run_size);
		Consumer c1 = new Consumer(c, run_size);
		p1.start();
		c1.start();
		try {
			p1.join();
			c1.join();
		} catch (InterruptedException e) {}
		System.out.println("Exiting!");
	}

}
