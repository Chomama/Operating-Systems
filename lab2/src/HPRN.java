import java.util.*;

public class HPRN {
	public static void execute(ArrayList <Process> processList, boolean Verbose) {
		int cycle = 0;
		int CPURunning=0;
		int IOTime=0;
		double averageTurnaround=0;
		double averageWaitingTime=0;
		Queue<Process> readyProcesses = new LinkedList<>(); 
		ArrayList <Process> notStartedProcesses =new ArrayList<Process>();
		ArrayList<Process> blockedProcesses = new ArrayList<Process>();
		ArrayList<Process> finishedProcesses= new ArrayList<Process>();
		Process currentProcess = null;
		
		ArrayList <Process> FCFSList=new ArrayList<>();
		ArrayList <Process> copy=new ArrayList<>();
		for(int i=0; i<processList.size(); i++) {
			Process copiedProcesses=new Process(processList.get(i));
			copy.add(copiedProcesses);
		}
		//ArrayList <Integer> states= new ArrayList <Integer>();
		//0=unstarted 1=ready 2=blocked 3=running
		for(int i=0; i<copy.size(); i++) {
			if(copy.get(i).arrivalTime==0) {
				readyProcesses.add(copy.get(i));
			}
			else {
				notStartedProcesses.add(copy.get(i));
			}
		}
		System.out.print("The sorted input is: ");
		for(int i=0; i<copy.size(); i++) {
			System.out.print(" ("+copy.get(i).arrivalTime+ "," + copy.get(i).B + "," + copy.get(i).TotalCPUTime+ "," + copy.get(i).M+")  ");
		}
		System.out.println();
		System.out.println();
		System.out.println("The scheduling algorithm used was Highest Penalty Ratio");
		System.out.println();
		//System.out.println("+readyProcesses);
		//System.out.println("NOT STARTED"+notStartedProcesses);	
		if(Verbose==true) {
			System.out.print("Before cycle    0:   ");
			for(int i=0; i<processList.size(); i++) {
				System.out.print("unstarted  0  ");
			}
			System.out.println();
		}
		while(finishedProcesses.size() < copy.size()) {  //loops for as long as there are unfinished processes
		//while(cycle<50) { 
			boolean sameArrival=false;

			int readyCount=0;
			if(blockedProcesses.size()>0) {
				IOTime++;
			}
			
			for(int i=0; i<blockedProcesses.size(); i++) {  
				blockedProcesses.get(i).blockedIOTime++;
				blockedProcesses.get(i).blockedTime--; 
				if(blockedProcesses.get(i).blockedTime==0) {  //if blocked time is 0, then adds the process to the ready queue
					readyProcesses.add(blockedProcesses.get(i));
					blockedProcesses.remove(i);
					i--;
				}
			}

			for(int i =0; i< notStartedProcesses.size(); i++ ) {  //if cycle is at arrival time, move unstarted process to the ready queue
				if(cycle==notStartedProcesses.get(i).arrivalTime) { //******
				//	System.out.println(notStartedProcesses.get(i).arrivalTime);
					readyProcesses.add(notStartedProcesses.get(i));
					notStartedProcesses.remove(notStartedProcesses.get(i));
					i--;
				}
			}
			cycle++; 
			
			//System.out.println(readyProcesses);
			if(currentProcess!= null && currentProcess.currentCPUBurst==0) {  //if the burst time is 0 on the current process, gets the blocked time, changes currentProcess to Null, and adds the process to the blocked list
				//System.out.println("adding to blocked");
				blockedProcesses.add(currentProcess);
				currentProcess.blockedTime=currentProcess.CPUBurstTime*currentProcess.M;
				currentProcess=null;
				
			} 
			
			//System.out.println("READY BEFORE: "+readyProcesses);
			ArrayList<Process> temp = new ArrayList<Process>();
			while(!readyProcesses.isEmpty()){
				temp.add(readyProcesses.poll());
			}
			double highestPR=0;
			for(int i=0; i< temp.size(); i++) {  //finds the highest PR in readyProcesses
				temp.get(i).getPR(cycle);
				if(i==0) {
					highestPR=temp.get(i).penaltyRatio;		
				}
				else {
					if(temp.get(i).penaltyRatio>highestPR) {
						highestPR=temp.get(i).penaltyRatio;
					}
				}
			}
			//System.out.println("TEMP"+temp);
			boolean PRTie=false;
			int count=0;
			ArrayList<Process> notHighestPR= new ArrayList <Process>();
			for(int i=0; i< temp.size(); i++) {
				if(temp.get(i).penaltyRatio == highestPR) {
					count++;
					//System.out.println("highest PR: "+highestPR+" Process: " + temp.get(i) + " TIME IN SYSTEM: " + (cycle-temp.get(i).arrivalTime)+" CURRENT RUNNING:  " +  temp.get(i).currentRunning);
				}
				else {
					notHighestPR.add(temp.get(i));
					temp.remove(i);
					i--;
				}
			}
			if(count>1) {
				PRTie=true;
			}
			
			if(PRTie) {
				//System.out.println("PR TIE!");
				Process[] temp3=new Process[copy.size()];
				//for(int x=temp.size()-1; x>=0; x--) {
				for(int x=0; x<temp.size(); x++) {
					for(int i=0; i<copy.size(); i++) {
						if(copy.get(i)==temp.get(x)) {
							//System.out.println(x);
							//System.out.println(temp.get(x));
							temp3[i]=(temp.get(x));
						}
					}
				}
				
				for(int i=0; i< temp3.length; i++) {
					//System.out.println (temp3[i]);
					if(temp3[i]!=null) {
						readyProcesses.add(temp3[i]);
					}
				}
				for(int i=0; i< notHighestPR.size(); i++) {
					readyProcesses.add(notHighestPR.get(i));
				}
			}
			else {
				//System.out.println("NO PR TIE");
				for(int i=0; i< temp.size(); i++) {
					if(temp.get(i).penaltyRatio == highestPR) {
						readyProcesses.add(temp.get(i));
					}
				}
				for(int x=0; x< notHighestPR.size(); x++) {
					readyProcesses.add(notHighestPR.get(x));
					
				}
			}
			//System.out.println("Ready AFter Everything" + readyProcesses);

//			if(sameArrival) {
//				ArrayList<Process> temp = new ArrayList<>();
//				ArrayList<Process> wereInRP = new ArrayList<>();
//				System.out.println(blockedProcesses.size());
//				int ignoreVals = readyProcesses.size()-readyCount;
//				System.out.println("IGNORE VALS: " + ignoreVals);
//				System.out.println("Ready BEfore Everything" + readyProcesses);
////				while(readyProcesses.size()>0) {
////					wereInRP.add(readyProcesses.remove());
////				}
////				System.out.println("WERE"+wereInRP);
//				if(ignoreVals>0) {
//	 				for(int x=0; x<ignoreVals; x++) {
//		
//						wereInRP.add(readyProcesses.remove());
//					
//					}
//				}
////				System.out.println(readyProcesses);
//				for(int i =0; i<=readyProcesses.size(); i++) {
//					temp.add(readyProcesses.remove());
//				}
//				System.out.println("TEMP" + temp);
//				if(readyProcesses.size()>0) {
//					temp.add(readyProcesses.remove());
//				}
//
//				Process[] temp3=new Process[processList.size()];
//				//for(int x=temp.size()-1; x>=0; x--) {
//				for(int x=0; x<temp.size(); x++) {
//					for(int i=0; i<processList.size(); i++) {
//						if(processList.get(i)==temp.get(x)) {
//							System.out.println(x);
//							System.out.println(temp.get(x));
//							temp3[i]=(temp.get(x));
//						}
//					}
//				}
//				for(int i=0; i< wereInRP.size(); i++) {
//					readyProcesses.add(wereInRP.get(i));
//				}
//				for(int i=0; i< temp3.length; i++) {
//					System.out.println (temp3[i]);
//					if(temp3[i]!=null) {
//						readyProcesses.add(temp3[i]);
//					}
//				}
//				System.out.println("Ready AFter Everything" + readyProcesses);
//
//			}
			
			if (currentProcess == null && readyProcesses.size()!=0) {  //if there is no current process, takes the next process from the ready queue
				currentProcess = readyProcesses.remove();
				//System.out.println("Current process: " + currentProcess);
				currentProcess.getBurst(); //gets the CPU burst time for the current process
				//System.out.println("AFTER BURST");
				//System.out.println("Current Burst YAYAYAY: "+currentProcess.CPUBurstTime);
			}
		
			
			
			
			if(readyProcesses.size()>0) {
				for(Process process:readyProcesses) {
					process.waitingTime++;
				}
			}
 			
			 
			if(Verbose==true) {
				System.out.print("Before cycle    "+cycle+":     ");
				for(int i =0; i<copy.size(); i++) {
					if(currentProcess==copy.get(i)) {
						System.out.print("running  " + currentProcess.currentCPUBurst+"      ");
					}
					else if(readyProcesses.contains(copy.get(i)) && cycle>0) {
						System.out.print("ready  0      ");
					}
					else if(blockedProcesses.contains(copy.get(i))) {
						System.out.print("blocked  "+copy.get(i).blockedTime+"    ");
					}
					else if(notStartedProcesses.contains(copy.get(i)) || (readyProcesses.contains(copy.get(i)) && cycle==0) ) {
						System.out.print("unstarted    0    ");
					}
					else if(finishedProcesses.contains(copy.get(i))) {
						System.out.print("terminated  0    ");
					}
				}
				System.out.println();
			}
			if(currentProcess!=null) {
				CPURunning++;
				currentProcess.execute();  //executes the current process
				currentProcess.currentRunning++;
				//System.out.println("CYCLE: " + cycle+ "CPUTIME: " + currentProcess.CPUTime);
				if(currentProcess.CPUTime == currentProcess.TotalCPUTime) {
					finishedProcesses.add(currentProcess);
					currentProcess.finishingTime=cycle;
					currentProcess=null;
				}
				

			}
		}
		randomNumbers.setCount();
		System.out.println();
		for(int i=0; i< copy.size(); i++) {
			printProcess.print(copy.get(i), i);
		}
		
		System.out.println();
		System.out.println("Summary Data:");
		System.out.println("	Finishing Time: " + cycle);
		System.out.println("	CPU Utilization: " + (((double)CPURunning/ cycle)));
		System.out.println("	I/O Utilization: " + (((double)IOTime/cycle)));
		System.out.println("	Throughput: " + ((double)copy.size()/cycle)*100);
		int sum=0;
		int sum2=0;
		for(int i=0; i< copy.size(); i++) {
			sum+=copy.get(i).turnaroundTime;
			sum2+=copy.get(i).waitingTime;
		}
		averageTurnaround= (double)sum/copy.size();
		averageWaitingTime= (double)sum2/copy.size();
		System.out.println("	Average turnaround time: " + averageTurnaround);
		System.out.println("	Average waiting time: " + averageWaitingTime);
	}
	

}