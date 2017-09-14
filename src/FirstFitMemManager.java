public class FirstFitMemManager extends MemManager {

	public FirstFitMemManager(int s) {
		super(s);
	}

	protected int findSpace(int s) {		
		
		int memAddress = 0; // to_return
		for (int i = 0; i < _memory.length; i++) {
			if (_memory[i] == '.') {
				if (this.countFreeSpacesAt(i) >= s) {
					memAddress = i;
					return memAddress;
				}
			}
		}
		return memAddress;
	}

}
