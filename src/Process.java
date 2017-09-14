public class Process extends Object implements Runnable {

	char id;
	int s;
	int runTime;
	int memAddress;
	MemManager m;

	public Process(MemManager m, char i, int s, int r) {

		this.id = i;
		this.m = m;
		this.s = s;
		this.runTime = r;
		this.memAddress = -1;

	}

	public int getSize() {
		return s;
	}

	public char getID() {
		return id;
	}

	public void setAddress(int a) {
		this.memAddress = a;
	}

	public int getAddress() {
		return memAddress;
	}

	public void run() {

		try {
			System.out.println(this.toString() + " waiting to run");

			m.allocate(this);

			System.out.println(this.toString() + " running");

			Thread.sleep(100 * runTime);

			m.free(this);

			System.out.println(this.toString() + " has finished");

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String toString() {

		if (memAddress == -1) {
			return id + ":   U+" + s;
		} else {
			return id + ":  " + memAddress + "+" + s;
		}

	}

}
