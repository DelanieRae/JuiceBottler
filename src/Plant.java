public class Plant implements Runnable {
	// How long do we want to run the juice processing
	public static final long PROCESSING_TIME = 5 * 1000;

	private static final int NUM_PLANTS = 2;

	public static void main(String[] args) {
		// Startup the plants
		Plant[] plants = new Plant[NUM_PLANTS];
		for (int i = 0; i < NUM_PLANTS; i++) {
			plants[i] = new Plant(i + 1);
			plants[i].startPlant(i);
		}

		// Give the plants time to do work
		delay(PROCESSING_TIME, "Plant malfunction");

		// Stop the plant, and wait for it to shutdown
		for (Plant p : plants) {
			p.stopPlant();
			p.waitToStop();
		}

		// Summarize the results
		int oranges = 0;
		int bottles = 0;
		int waste = 0;
		for (Plant p : plants) {
			oranges += p.getOranges();
			bottles += p.getBottles();
			waste += p.getWaste();
		}
		System.out.println("Total processed = " + oranges);
		System.out.println("Created " + bottles + ", wasted " + waste + " oranges");
	}

	private static void delay(long time, String errMsg) {
		long sleepTime = Math.max(1, time);
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			System.err.println(errMsg);
		}
	}

	public final int ORANGES_PER_BOTTLE = 3;
	public int workNum;

	private static final int NUM_LINES = 1;
	private static final int WORKERS_PER_LINE = Orange.State.values().length - 2;
	private static final int NUM_WORKERS = NUM_LINES * WORKERS_PER_LINE;

	private final Thread[] workers;
	public final int plantNum;
	private volatile boolean timeToWork;
	private volatile int orangesProcessed;
	//creates worker threads
	Plant(int plantNum) {
		this.plantNum = plantNum;
		workers = new Thread[NUM_WORKERS];
		orangesProcessed = 0;
	}
	//Creates worker threads and starts the threads calling the run method
	public void startPlant(int workNum) {
		timeToWork = true;
		for (int i = 0; i < NUM_WORKERS; i++) {
			this.workNum = workNum;
			workers[i] = new Thread(this, "Plant[" + plantNum + "] Worker[" + (workNum + 1) + "]");

			workNum++;
			workers[i].start();
		}
	}
	
	//sets timeToWork to false
	public void stopPlant() {
		timeToWork = false;
	}

	//waits for the previous worker thread to stop
	public void waitToStop() {
		for (Thread worker : workers) {
			try {
				worker.join();
			} catch (InterruptedException e) {
				System.err.println(worker.getName() + " stop malfunction");
			}
		}
	}

	// This is run once per worker
	public void run() {
		System.out.print(Thread.currentThread().getName() + " Processing oranges");
		Worker worker = new Worker(this);
		while (timeToWork) {
			worker.processEntireOrange(new Orange());
			System.out.print(".");
		}
		System.out.println("");
	}

	// Does the final step of the system.
	public void completeOrange(Orange o) {
		// Do one final check on the orange
		if (o.getState() == Orange.State.Bottled) {
			o.runProcess();
			synchronized (this) {
				orangesProcessed++;
			}
		}

	}

	// Returns orangesProcessed
	public int getOranges() {
		return orangesProcessed;
	}

	// Calculates the number of bottles
	public int getBottles() {
		return orangesProcessed / ORANGES_PER_BOTTLE;
	}

	// Calculates the number of Oranges wasted
	public int getWaste() {
		return orangesProcessed % ORANGES_PER_BOTTLE;
	}
}