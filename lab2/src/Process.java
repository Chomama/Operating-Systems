
public class Process implements Comparable<Process>  {
	public int arrivalTime;
	public int B;
	public int TotalCPUTime;
	public int M;
	public int finishingTime;
	public int turnaroundTime;
	public int blockedIOTime;
	
	public int CPUTime=0;
	public int blockedTime;
	public int waitingTime;
	public int CPUBurstTime;
	public int currentCPUBurst;
	public int quantum;
	public double penaltyRatio;
	public int currentRunning=0;
	

	
	
	public Process(int arrivalTime, int B, int CPUTime, int M) {
		this.arrivalTime=arrivalTime;
		this.B=B;
		this.TotalCPUTime=CPUTime;
		this.M=M;
	}
	public Process(Process other) {
		this.arrivalTime=other.arrivalTime;
		this.B=other.B;
		this.TotalCPUTime=other.TotalCPUTime;
		this.M=other.M;
    }
	
	public String toString()
	{
	      return "Process"+this.arrivalTime;
	}
	
	public void getBurst() {
		CPUBurstTime=randomNumbers.randomOS(B);
		currentCPUBurst=CPUBurstTime;
	}
	
	public void getPR(int cycle) {
		int timeInSystem = cycle-arrivalTime;
		penaltyRatio= (double)timeInSystem/ Math.max(1, currentRunning);

	}

	@Override
	public int compareTo(Process process) {
		return this.arrivalTime-process.arrivalTime;
	}
	
	public void execute() {
		CPUTime++;
		currentCPUBurst--;
		
	}


}
