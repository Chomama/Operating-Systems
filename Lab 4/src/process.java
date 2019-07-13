
public class process {
	double A;
	double B;
	double C;
	int numReferences;
	int processNumber;
	boolean first=true;
	static int quantum=3;
	int currentWord=0;
	int numPageFaults=0;
	int residencyTime=0;
	int numEvictions=0;
	int evictedTime=0;
	int nextReference=0;
	boolean usedNextReference=false;
	int runningSum=0;
	
	public process(double A, double B, double C, int numReferences, int processNumber) {
		this.A=A;
		this.B=B;
		this.C=C;
		this.numReferences=numReferences;
		this.processNumber=processNumber;
	}

	public void notFirst() {
		first=false;
	}
	
	public void decrement_quantum() {
		quantum--;
	}
	public void reset_quantum() {
		quantum=3;
	}
}
