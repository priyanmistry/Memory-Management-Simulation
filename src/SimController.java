import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SimController extends Object implements Runnable {

	// Declaring ThreadPoolExecutor Object
	static ThreadPoolExecutor threadPoolExe;

	// Instance of MemManger
	static MemManager m;

	static Thread watcher;
	static Thread queueHThread;
	
	static int arrSize = 0;


	public SimController() {
	}

	public void run() {
		int finished = 0;
		
		while (threadPoolExe.getActiveCount() >= 0) {

			try {
				
				Thread.sleep(1000);
				if (m.isChanged() == true) {
					System.out.println(m.toString());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (threadPoolExe.getActiveCount() == 0 && finished == 1) {
				try {
					threadPoolExe.shutdown();
					threadPoolExe.awaitTermination(5L, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
				}
				System.out.println("All threads have terminated.");
				System.exit(1);
			}
			finished = 1;
		}

	}

	public static void main(String[] args) {

		String policy = args[0];
		int memSizeAv = Integer.parseInt(args[1]);
		String fileName = args[2];
		
		arrSize = memSizeAv;

		SimController sc;
		QueueHandler qh;

		switch (policy) {
		case "b":

			System.out.println("Policy: BEST fit");
			m = new BestFitMemManager(memSizeAv);
			threadPoolExe = (ThreadPoolExecutor) Executors.newCachedThreadPool();

			// starting the SimController start method
			sc = new SimController();
			watcher = new Thread(sc);
			watcher.start();

			qh = new QueueHandler(threadPoolExe, m, fileName);
			queueHThread = new Thread(qh);
			queueHThread.start();

			// joining
			try {
				queueHThread.join();
				watcher.join();

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;

		case "w":
			System.out.println("Policy: WORST fit");
			m = new WorstFitMemManager(memSizeAv);
			threadPoolExe = (ThreadPoolExecutor) Executors.newCachedThreadPool();

			// starting the SimController start method
			sc = new SimController();
			watcher = new Thread(sc);
			watcher.start();

			qh = new QueueHandler(threadPoolExe, m, fileName);
			queueHThread = new Thread(qh);
			queueHThread.start();

			// joining
			try {
				queueHThread.join();
				watcher.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;

		case "f":
			System.out.println("Policy: FIRST fit");
			m = new FirstFitMemManager(memSizeAv);
			threadPoolExe = (ThreadPoolExecutor) Executors.newCachedThreadPool();

			// starting the SimController start method
			sc = new SimController();
			watcher = new Thread(sc);
			watcher.start();

			qh = new QueueHandler(threadPoolExe, m, fileName);
			queueHThread = new Thread(qh);
			queueHThread.start();

			// joining
			try {
				queueHThread.join();
				watcher.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		}

	}

}
