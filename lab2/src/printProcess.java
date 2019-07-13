
public class printProcess {
	public static void print(Process process, int index) {
		System.out.println("Process "+index+":");
		System.out.println("	(A,B,C,M) = ("+ process.arrivalTime+ "," + process.B + "," + process.TotalCPUTime+ "," + process.M+")");
		System.out.println("	Finishing time: "+process.finishingTime);
		System.out.println("	Turnaround time: "+ (process.finishingTime-process.arrivalTime));
		process.turnaroundTime=(process.finishingTime-process.arrivalTime);
		System.out.println("	I/O time: " + process.blockedIOTime);
		System.out.println("	Waiting time: " + process.waitingTime);
	}
}
