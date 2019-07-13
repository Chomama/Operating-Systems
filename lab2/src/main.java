import java.io.*;
import java.util.*;
public class main {

	public static void main(String[] args) {
	//	System.out.println(randomNumbers.randomOS(12)); 
		boolean verbose=false;
		File input;
		int numProcesses;
		ArrayList <Process> processes= new ArrayList<Process> ();
		
		
		if (args[0].equals("--verbose")) {
			verbose=true;
			input = new File(args[1]);	
			System.out.println("VERBOSE FLAG ACTIVATED");
		}
		else {
			input = new File(args[0]);
		}
		
		try {
			Scanner run1 = new Scanner(input);
			numProcesses=run1.nextInt();
			//System.out.println(numProcesses);
			for(int i=0; i<numProcesses; i++) {
				String temp = run1.next();
				
				int A= Integer.parseInt(temp.substring(1));
				//System.out.println("arrival time: " +A);
				int B=run1.nextInt();
				int C=run1.nextInt();
				String temp2=run1.next();
				int M=Integer.parseInt(temp2.substring(0, temp2.length() - 1));
				//System.out.println("M"+M);
//				System.out.println("A: "+A);
//				System.out.println("B: "+B);
//				System.out.println("C: "+C);
//				System.out.println("M: "+M);
				Process newProcess=new Process(A,B,C,M);
				processes.add(newProcess);
			
				
				
			}
		}
		
		catch (IOException exception) {
			System.out.println("Error: File not found.\n");
		}
		
		//System.out.println(processes);

		Collections.sort(processes);
		System.out.println();
		System.out.println("_____________________________________________________________________________________________________________");
		System.out.print("The original input was: ");
		for(int i=0; i<processes.size(); i++) {
			System.out.print(" ("+processes.get(i).arrivalTime+ "," + processes.get(i).B + "," + processes.get(i).TotalCPUTime+ "," + processes.get(i).M+")  ");
		}
		System.out.println();

		FCFS.execute(processes, verbose);
		System.out.println("_____________________________________________________________________________________________________________");
		System.out.print("The original input was: ");
		for(int i=0; i<processes.size(); i++) {
			System.out.print(" ("+processes.get(i).arrivalTime+ "," + processes.get(i).B + "," + processes.get(i).TotalCPUTime+ "," + processes.get(i).M+")  ");
		}
		System.out.println();

		RR.execute(processes, verbose);
		System.out.println("_____________________________________________________________________________________________________________");
		System.out.print("The original input was: ");
		for(int i=0; i<processes.size(); i++) {
			System.out.print(" ("+processes.get(i).arrivalTime+ "," + processes.get(i).B + "," + processes.get(i).TotalCPUTime+ "," + processes.get(i).M+")  ");
		}
		System.out.println();

		LCFS.execute(processes, verbose);
		System.out.println("_____________________________________________________________________________________________________________");
		System.out.print("The original input was: ");
		for(int i=0; i<processes.size(); i++) {
			System.out.print(" ("+processes.get(i).arrivalTime+ "," + processes.get(i).B + "," + processes.get(i).TotalCPUTime+ "," + processes.get(i).M+")  ");
		}
		System.out.println();

		HPRN.execute(processes, verbose);
		}
	
}
