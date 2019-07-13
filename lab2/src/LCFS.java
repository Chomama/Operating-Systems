import java.util.*;


//temporary stack
public class LCFS {
	public static void execute(ArrayList <Process> processList, boolean Verbose) {
		int cycle = 0;
		int CPURunning=0;
		int IOTime=0;
		double averageTurnaround=0;
		double averageWaitingTime=0;
		Stack<Process> readyProcesses = new Stack<>(); 
		ArrayList <Process> notStartedProcesses =new ArrayList<Process>();
		ArrayList<Process> blockedProcesses = new ArrayList<Process>();
		ArrayList<Process> finishedProcesses= new ArrayList<Process>();
		Process currentProcess = null;
		
		ArrayList <Process> FCFSList=new ArrayList<>();
		ArrayList <Process> copy=new ArrayList<>();
		for(int i=0; i<processList.size(); i++) {
			Process copiedProcesses=new Process(processList.get(i));
			copy.add(copiedProcesses);
			//copy.add(processList.get(i));
		}
//		for(int i=0; i<copy.size(); i++) {
//			System.out.println("Process: "+ copy.get(i).arrivalTime);
//			System.out.println("Process: "+ copy.get(i).B);
//			System.out.println("Process: "+ copy.get(i).TotalCPUTime);
//			System.out.println("Process: "+ copy.get(i).M);
//			System.out.println("Process: "+ copy.get(i).CPUTime);
//			System.out.println();
//		}

		//ArrayList <Integer> states= new ArrayList <Integer>();
		//0=unstarted 1=ready 2=blocked 3=running
		boolean multipleArriving=false;
		for(int i=0; i<copy.size(); i++) {
			if(copy.get(i).arrivalTime==0) {
				multipleArriving=true;
			}
		}
		Stack<Process> tempStack = new Stack<>();
		for(int i=0; i<copy.size(); i++) {
			if(copy.get(i).arrivalTime==0) {
				tempStack.push(copy.get(i));
			}
			else {
				notStartedProcesses.add(copy.get(i));
			}
		}
		for(int i=0; i<tempStack.size(); i++) {
			readyProcesses.push(tempStack.pop());
			i--;
		}
		System.out.print("The sorted input is: ");
		for(int i=0; i<copy.size(); i++) {
			System.out.print(" ("+copy.get(i).arrivalTime+ "," + copy.get(i).B + "," + copy.get(i).TotalCPUTime+ "," + copy.get(i).M+")  ");
		}
		System.out.println();
		System.out.println();
		System.out.println("The scheduling algorithm used was Last Come First Served");
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
		//while(cycle<200) { 
				boolean sameArrival=false;

				int readyCount=0;
				for(int i=0; i<blockedProcesses.size(); i++) {  
					if((blockedProcesses.get(i).blockedTime-1)==0) {	
						readyCount++;
					}
				}
				//System.out.println(readyCount);
				if(readyCount>1) {
					Collections.sort(blockedProcesses);
					
					int currentA=0;
					for(int x=0; x<blockedProcesses.size(); x++) {
						if(x==0) {
							currentA=blockedProcesses.get(x).arrivalTime;
						}
						else {
							if(blockedProcesses.get(x).arrivalTime==currentA) {
								sameArrival=true;
//								System.out.println("SAME ARRIVAL");

								
							}
						}					
					}
					
				}
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

//				if(readyCount>1 && sameArrival==true) {
//					System.out.println("need to modify rq");
//					ArrayList <Integer> indexes = new ArrayList<>();
//					for(Process p: readyProcesses) {
//						indexes.add(copy.indexOf(p));
//					}
//					System.out.println(indexes);
//					for(int i=0; i<indexes.size(); i++ {
//						
//					}
//					Process temp = readyProcesses.remove();
//					readyProcesses.add(temp);
//				}
				//System.out.println(readyProcesses);
				for(int i =0; i< notStartedProcesses.size(); i++ ) {  //if cycle is at arrival time, move unstarted process to the ready queue
					if(cycle==notStartedProcesses.get(i).arrivalTime) { //******
						//System.out.println(notStartedProcesses.get(i).arrivalTime);
						readyProcesses.add(notStartedProcesses.get(i));
						notStartedProcesses.remove(notStartedProcesses.get(i));
						i--;
						readyCount++;
					}
				}
				cycle++; 
				
			//	System.out.println(readyProcesses);
				if(currentProcess!= null && currentProcess.currentCPUBurst==0) {  //if the burst time is 0 on the current process, gets the blocked time, changes currentProcess to Null, and adds the process to the blocked list
					//System.out.println("adding to blocked");
					blockedProcesses.add(currentProcess);
					currentProcess.blockedTime=currentProcess.CPUBurstTime*currentProcess.M;
					currentProcess=null;
					
				} 
				//System.out.println(readyCount);

				if(sameArrival || readyCount>1) {
					ArrayList<Process> temp = new ArrayList<>();
					ArrayList<Process> wereInRP = new ArrayList<>();
					//System.out.println(blockedProcesses.size());
					int ignoreVals = readyProcesses.size()-readyCount;
					//System.out.println("IGNORE VALS: " + ignoreVals);
					//System.out.println("Ready BEfore Everything" + readyProcesses);
//					while(readyProcesses.size()>0) {
//						wereInRP.add(readyProcesses.remove());
//					}
//					System.out.println("WERE"+wereInRP);
					//if(ignoreVals>0) {
		 				for(int x=0; x<(readyProcesses.size()-ignoreVals); x++) {
		 					//System.out.println(x);
		 					//System.out.println(readyProcesses.size());
							temp.add(readyProcesses.pop());
							x--;
						}
					//}
				
					//System.out.println("TEMP" + temp);
//					System.out.println(readyProcesses);
					if(ignoreVals>0) {
						for(int i =0; i<=readyProcesses.size(); i++) {
							wereInRP.add(readyProcesses.pop());
						}
						//System.out.println("Were in RP: " +wereInRP);
						//System.out.println("LEFTOVER" + readyProcesses);
						if(readyProcesses.size()>0) {
							wereInRP.add(readyProcesses.pop());
						}
					}
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
					//System.out.print("List with Indexes:");
					for(int i=0; i<temp3.length; i++) {
						//System.out.print(temp3[i]+",");

					}
					//System.out.println();
					//System.out.println("List with Indexes:" + temp3);
					
					for(int i=0; i< temp3.length; i++) {
						if(temp3[i]!=null) {
							tempStack.push(temp3[i]);
						}
					}
					for(int i=0; i< wereInRP.size(); i++) {
						tempStack.push(wereInRP.get(i));
					}
					//System.out.println("TEMPSTACHU"+tempStack);
//
					for(int i=0; i<tempStack.size(); i++) {
						readyProcesses.push(tempStack.pop());
						i--;
					}
					//System.out.println("Ready AFter Everything" + readyProcesses);

				}
				
				if (currentProcess == null && readyProcesses.size()!=0) {  //if there is no current process, takes the next process from the ready queue
					currentProcess = readyProcesses.pop();
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
					//System.out.println("TOTALCPU: " + currentProcess.TotalCPUTime+ "CPUTIME: " + currentProcess.CPUTime);
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