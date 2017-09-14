public class BestFitMemManager extends MemManager {

	public BestFitMemManager(int s) {
		super(s);
		// s - the total size of memory to be created
	}

	protected synchronized int findSpace(int s) {
	    int bestPos = 0;
	    int bestFitSoFar = _memory.length;
	    for (int i = 0, fs = 0; i <= (_memory.length-s); i += fs+1) {
	      fs = countFreeSpacesAt(i);
	      if (fs>=s && fs<bestFitSoFar) {
	        bestPos=i;
	        bestFitSoFar=fs;
	      }
	    }
	    return bestPos;
	  }

}
