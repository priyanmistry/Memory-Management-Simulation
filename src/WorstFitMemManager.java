public class WorstFitMemManager extends MemManager {

	public WorstFitMemManager(int s) {

		super(s);
	}

	public int findSpace(int s) {
		int worstFitAddress = 0;

		for (int i = 0; i < _memory.length; i++) {
			if (_memory[i] == '.') {
				if (this.countFreeSpacesAt(i) == _largestSpace) {
					worstFitAddress = i;
					return worstFitAddress;
				}
			}
		}
		return worstFitAddress;
	}

}
