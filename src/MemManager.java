public abstract class MemManager extends Object {

	char[] _memory;
	volatile int _largestSpace;
	volatile boolean _changed;

	public MemManager(int s) {
		// Initialising variables
		this._memory = new char[s];
		this._largestSpace = s;
		this._changed = true;

		for (int i = 0; i < _memory.length; i++) {
			_memory[i] = '.';
		}
	}

	public boolean isChanged() {
		return _changed;
	}

	protected abstract int findSpace(int s);

	public synchronized int countFreeSpacesAt(int pos) {
		int consecSpaces = 0;
		for (int i = pos; i < _memory.length; i++) {
			if (_memory[i] == '.') {
				consecSpaces++;
			} else {
				return consecSpaces;
			}
		}
		return consecSpaces;
	}

	public synchronized void allocate(Process p) throws InterruptedException {
		while (_largestSpace < p.getSize()) {
			wait();
		}

		int memAddress = findSpace(p.getSize());
		p.setAddress(memAddress);
		for (int i = memAddress; i < (memAddress + p.getSize()); i++) {
			_memory[i] = p.getID();
		}

		resetLargestSpace();
		_changed = true;
		notifyAll();
	}

	public synchronized void free(Process p) {
		char pId = p.getID();
		for (int i = 0; i < _memory.length; i++) {
			if (pId == _memory[i]) {
				_memory[i] = '.';
			}
		}

		p.setAddress(-1);
		resetLargestSpace();
		_changed = true;
		notifyAll();

	}

	public void resetLargestSpace() {
		int freeSpaces = 0;
		int largestSpace = 0;

		for (int i = 0; i < _memory.length; i++) {
			if (_memory[i] == '.') {
				freeSpaces = countFreeSpacesAt(i);
				if (freeSpaces >= largestSpace) {
					largestSpace = freeSpaces;
				}
			}
		}
		_largestSpace = largestSpace;
	}

	public String toString() {

		int rowNumber = 0;
		int counter = 0;

		StringBuilder to_return = new StringBuilder();

		// initial start of toString
		if (_memory.length > 100 && rowNumber < 100) {
			to_return.append("  0  |");
		} else {
			to_return.append("   0|");
		}

		for (int i = 0; i < _memory.length; i++) {
			if (counter == 20) {
				// row number increments by 20
				rowNumber = rowNumber + 20;
				// new line when counter is 20
				if (_memory.length > 100 && rowNumber < 100) {
					to_return.append("|" + "\n" + "  " + Integer.toString(rowNumber) + " |");
				} else {
					to_return.append("|" + "\n" + "  " + Integer.toString(rowNumber) + "|");
				}
				counter = 0; // reset counter
			}
			// adding data to toString
			to_return.append(_memory[i]);
			counter++;
		}
		to_return.append("|" + "\n ls:" + _largestSpace);
		_changed = false;
		return to_return.toString();

	}
}
