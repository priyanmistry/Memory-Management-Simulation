import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.ThreadPoolExecutor;

class QueueHandler extends Object implements Runnable {

	ThreadPoolExecutor threadPoolExe; // second ThreadPoolExecutor
	MemManager m;
	String fileName;

	public QueueHandler(ThreadPoolExecutor e, MemManager m, String f) {
		this.threadPoolExe = e;
		this.m = m;
		this.fileName = f;
	}

	public void run() {

		// Reading lines from the file
		Path fpath = Paths.get(fileName);

		try (Scanner file = new Scanner(fpath)) {

			// Variables for the fields in file
			int delay;
			int size;
			int runTime;
			char pid;

			while (file.hasNextLine()) {

				Scanner line = new Scanner(file.nextLine());
				line.useDelimiter(":");
				delay = line.nextInt();
				pid = line.next().charAt(0);
				size = line.nextInt();
				runTime = line.nextInt();
				line.close();

				try {
					Thread.sleep(delay * 100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				//Creating process and adding it to tpe pool
				Process p = new Process(m, pid, size, runTime);
				threadPoolExe.execute(p);
			}

			file.close();

		} catch (NoSuchFileException e) {
			System.err.println("File not found: " + fileName);
			System.exit(1);
		} catch (IOException e) {
			System.err.println(e);
			System.exit(1);
		}
	}
}
