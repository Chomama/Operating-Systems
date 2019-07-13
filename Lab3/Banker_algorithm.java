import java.util.ArrayList;;

public class Banker_algorithm {
	
	//main function that runs the banker algorithm
	public static void runBanker(task[] tasks, resource[] resourceList) {
		
		//declare variables
		int cycle=0;
		int[] available=new int[resourceList.length];
		int[][] allocated=new int[tasks.length][resourceList.length];
		int[][] needs=new int[tasks.length][resourceList.length];
		int[][] max=new int[tasks.length][resourceList.length];
		int[] finishTimes=new int[tasks.length];
		int[] waitTimes=new int[tasks.length];

		ArrayList<task> blockedTasks = new ArrayList<>(); 
		ArrayList<task> finishedTasks=new ArrayList<>();
		boolean deadlocked=false;
		

		
		for(int i=0; i<resourceList.length; i++) {  //populates the available array
			available[i]=resourceList[i].units;
		}
		for(int i=0; i<tasks.length; i++) {
			for(int x=0; x<tasks[i].commands.size(); x++) {
				String[] command=tasks[i].commands.get(x);  
				if(command[0].equals("initiate")) { 		//goes through the commands and gets the resource and claim 
					int resource=Integer.parseInt(command[2]);
					int claim=Integer.parseInt(command[3]);
					needs[i][resource-1]+=claim;  //populate the needs matrix
					max[i][resource-1]+=claim;  //populate the max claim matrix
				}
			}
		}

		
//		for(int n=0; n< resourceList.length; n++){
//			for(int w=0; w<tasks.length; w++) {
//				System.out.println((w+1) + " claimed "+needs[w][n]+ " OF RESOURCE " + n);
//
//			}
//		}
		
		//main loop that runs until finished processes is less than the number of tasks
		while(finishedTasks.size()<tasks.length) {
			
	
	//	for(int x=0; x<20; x++) { 
				//System.out.println("DURING "+cycle+"-"+(cycle+1));
//				for(int i=0; i<resourceList.length; i++) {
//				//.out.println("RESOURCE " + (i+1) +" HAS " + available[i] + " UNITS");
//					
//				}
//
//			
			ArrayList<task> tasksToRemove = new ArrayList<>();   //Arraylist that store teh tasks to remove from the blocked list at the end of the cycle
			//go through tasks 
				if(blockedTasks.size()>0) { 
				//	System.out.println("First Check Blocked Tasks:");
					for(int i=0; i< blockedTasks.size(); i++) {
						//System.out.println("BLOCKED"+blockedTasks.get(i).taskNumber);
						String[] command= blockedTasks.get(i).commands.get(blockedTasks.get(i).commandPointer);
						int requestedResource=Integer.parseInt(command[2]);
						int numUnits=Integer.parseInt(command[3]);
						//System.out.println("TASK " + (i+1) + ": requests" + numUnits+ " OF" + requestedResource);
						if(available[requestedResource-1]>=numUnits) {  //if the number of available units is greater than or equal to the number of units requested, decrement available, allocate the units to the task, and move the command pointer
							
							//makes copies of the available, allocated, and needs arrays for for the banker
							int[] availableCopy=new int[resourceList.length];
							for(int n=0; n< resourceList.length; n++){
								availableCopy[n]+=available[n];
							}
							int[][] allocatedCopy=new int[tasks.length][resourceList.length];
							for(int n=0; n< tasks.length; n++){
								for(int w=0; w<resourceList.length; w++) {
									allocatedCopy[n][w]+=needs[n][w];
								}
							}
							int[][] needsCopy=new int[tasks.length][resourceList.length];
							for(int n=0; n< tasks.length; n++){
								for(int w=0; w<resourceList.length; w++) {
									needsCopy[n][w]+=needs[n][w];
								}
							}
							
							//decrements the available, allocated, and needs arrays to check to see what happens if the banker fulfills the request
							availableCopy[requestedResource-1]-=numUnits;
							allocatedCopy[i][requestedResource-1]+=numUnits;
							needsCopy[blockedTasks.get(i).taskNumber-1][requestedResource-1]-=numUnits;

							//arrayList that store the tasks that can finish if the request is fulfilled
							ArrayList<task> ableToFinish = new ArrayList<>(); 
							ArrayList<task> runningTasks = new ArrayList<>(); 
							for(int t=0; t<tasks.length; t++) {  //adds the running processes to running tasks
								if(!finishedTasks.contains(tasks[t])) {
									 runningTasks.add(tasks[t]);
								}
							}
							
							if(needsCopy[blockedTasks.get(i).taskNumber-1][requestedResource-1]==0) {  
								//System.out.println("rasfagasgsag_----------");
								availableCopy[requestedResource-1]+=numUnits;

							}
							
							//goes through each task 
							for(int u=0; u<runningTasks.size(); u++) {
								for(int n=0; n<resourceList.length; n++) {
									if(Math.abs(needsCopy[runningTasks.get(u).taskNumber-1][n]) <= availableCopy[n]) {	//if the need of this task is less than the number of that resource
										ableToFinish.add(runningTasks.get(u));  //add the task to the able to finish array
										if(n==requestedResource-1) {  
											availableCopy[n]+=allocatedCopy[runningTasks.get(u).taskNumber-1][n];
										}
									}
								}
								
							}
							if(ableToFinish.size()/resourceList.length==runningTasks.size()) {		 //System is in a  if the request is fulfilled safe state
								available[requestedResource-1]-=numUnits;  //takes away the number of requested units 
								allocated[blockedTasks.get(i).taskNumber-1][requestedResource-1]+=numUnits;  //allocates the units
								needs[blockedTasks.get(i).taskNumber-1][requestedResource-1]-=numUnits;  //decrements the needs
								blockedTasks.get(i).commandPointer++;   //increments the tasks command pointer
								tasksToRemove.add(blockedTasks.get(i));  //adds the task to tasksToRemove so that it is taken off the blocked list at the end of the cycle
								//System.out.println("121TASK " + blockedTasks.get(i).taskNumber + "completes its request( resource["+ requestedResource + "]: requested= " + numUnits+ ", remaining= " + available[requestedResource-1] + ")" + "CLAIM" + needs[blockedTasks.get(i).taskNumber-1][requestedResource-1]);
							}
							else {
								//System.out.println("TASK " + blockedTasks.get(i).taskNumber + " cannot be granted(NOT SAFE).");
								//waitTimes[blockedTasks.get(i).taskNumber-1]+=1;
							}
							
							
	//						tasks[i].commandPointer++; 
//
						}
						else {  //if there are not enough units available, add the task to blocked tasks
						//	System.out.println("TASK " + blockedTasks.get(i).taskNumber + " IS WAITING.");

						}
						waitTimes[blockedTasks.get(i).taskNumber-1]+=1;  //adds to the wait time


					}
					
					
				}
				int[] releasedResources=new int[resourceList.length];  //copy of available to store units that are to be released at the end of the cycle
				boolean resourcesReleasedNextCycle= false;
				for(int i=0; i< tasks.length; i++) {
					String[] command= tasks[i].commands.get(tasks[i].commandPointer);
				//	System.out.println(command[0]);

					if(tasks[i].computeCycles!=0) {  //checks if the task is computing
						if(tasks[i].computeCycles-1==0) {
							tasks[i].commandPointer++; 
							//tasks[i].computeCycles=0;
							//System.out.println("TASK " + (i+1) + ": compute" + (tasks[i].computeCycles) +"CYCLES LEFT");
							//System.out.println("WAKA");

							
						}
						else {
							//System.out.println("TASK " + (i+1) + ": compute" + tasks[i].computeCycles +"CYCLES LEFT");
							//System.out.println("FLACKA");
						}
						tasks[i].computeCycles-=1;

					}
					else if (!blockedTasks.contains(tasks[i]) && !finishedTasks.contains(tasks[i])){
						if(command[0].equals("initiate")) {
							if(Integer.parseInt(command[3])>available[Integer.parseInt(command[2])-1]) {
							//	System.out.println("Task" + command[1]+" is aborted (claim exceeds total in system)");
								finishedTasks.add(tasks[i]);
							}
							else {
								//System.out.println("TASK " + (i+1) + ": INITIATE");

							}
							tasks[i].commandPointer++;
						}
						if(command[0].equals("request")) {   //command for request
							//System.out.println("Task" + (i+1)+ "currently has"+ allocated[i][0]+ "units");

							int requestedResource=Integer.parseInt(command[2]);
							int numUnits=Integer.parseInt(command[3]);
							if((allocated[i][requestedResource-1]+numUnits)>max[i][requestedResource-1]) {  //checks for case where the number of units requested is greater than that tasks claim so aborts
								//System.out.println("TASK" + (i+1)+ "would have "+ (allocated[i][requestedResource-1]+numUnits)+ " which is bigger than" + max[i][requestedResource-1]);
								releasedResources[requestedResource-1]+=numUnits;  //adds the released resources 
								resourcesReleasedNextCycle=true;
								finishedTasks.add(tasks[i]);  //aborts the task
								System.out.println("Task Aborted: Request exceeds claim.");
							}
							else {
								//System.out.println("TASK " + (i+1) + ": requests" + numUnits+ " OF" + requestedResource);
								if(available[requestedResource-1]>=numUnits) {  //if the number of available units is greater than or equal to the number of units requested, decrement available, allocate the units to the task, and move the command pointer
	//								available[requestedResource-1]-=numUnits;
	//								allocated[i][requestedResource-1]+=numUnits;
	//								needs[i][requestedResource-1]+=numUnits;
									
									//populates the copied lists
									int[] availableCopy=new int[resourceList.length];
									for(int n=0; n< resourceList.length; n++){
										availableCopy[n]+=available[n];
									}
									int[][] allocatedCopy=new int[tasks.length][resourceList.length];
									for(int n=0; n< tasks.length; n++){
										for(int w=0; w<resourceList.length; w++) {
											allocatedCopy[n][w]+=needs[n][w];
										}
									}
									int[][] needsCopy=new int[tasks.length][resourceList.length];
									for(int n=0; n< tasks.length; n++){
										for(int w=0; w<resourceList.length; w++) {
											needsCopy[n][w]+=needs[n][w];
										}
									}
									
									//decrement the avialable and add to allocated to simulate the request being granted
									availableCopy[requestedResource-1]-=numUnits;
									allocatedCopy[i][requestedResource-1]+=numUnits;
									
									
									//System.out.println("CLAIMB" +needs[i][requestedResource-1]);
	
									needsCopy[i][requestedResource-1]-=numUnits;
									//System.out.println("CLAIM" +needs[i][requestedResource-1]);
									
									if(needsCopy[i][requestedResource-1]==0) {
										//System.out.println("rasfagasgsag_----------");
										availableCopy[requestedResource-1]+=numUnits;

									}
									
									//arrayList that holds the tasks that can't finish if request is fulfilled
									ArrayList<task> ableToFinish = new ArrayList<>(); 
									ArrayList<task> runningTasks = new ArrayList<>(); 
									
									//adds the running tasks
									for(int t=0; t<tasks.length; t++) {
										if(!finishedTasks.contains(tasks[t])) {
											 runningTasks.add(tasks[t]);
										}
									}
						
									for(int u=0; u<runningTasks.size(); u++) {										
										for(int n=0; n<resourceList.length; n++) {
										//	System.out.println("TASK" + runningTasks.get(u).taskNumber + "WANTS" + needsCopy[i][n] + " WITH " +availableCopy[n] + " Resources Left");

											if(Math.abs(needsCopy[i][n]) <= availableCopy[n]) {	  //checks if the task's needs are less than the number of that resources available
											//	System.out.println(runningTasks.get(u).taskNumber+"IS ABLE TO FINISH");
												ableToFinish.add(runningTasks.get(u));  //adds that task to the able to finish list
												if(n==requestedResource-1) {
													availableCopy[n]+=allocatedCopy[runningTasks.get(u).taskNumber-1][n];
												}
											}
										}
								
									}
									//System.out.println(ableToFinish.size()+ " tasks were able to finish");
									if(ableToFinish.size()/resourceList.length==runningTasks.size()) {		 //System is in a safe state
										available[requestedResource-1]-=numUnits;
										allocated[i][requestedResource-1]+=numUnits;
										needs[i][requestedResource-1]-=numUnits;
										tasks[i].commandPointer++; 
									//	System.out.println("TASK " + (i+1) + "completes its request( resource["+ requestedResource + "]: requested= " + numUnits+ ", remaining= " + available[requestedResource-1] + ")");
									}
									else {
										blockedTasks.add(tasks[i]);  
									//	System.out.println("TASK " + (i+1) + " cannot be granted(NOT SAFE).");
									}
									
									
			//						tasks[i].commandPointer++; 
	//	
								}
								else {  //if there are not enough units available, add the task to blocked tasks
									blockedTasks.add(tasks[i]);  
									//System.out.println("TASK " + (i+1) + " IS WAITING.");
								}
							}
							
						}
						if(command[0].equals("release")) { //command for release
							int resourceToRelease=Integer.parseInt(command[2]);
							int numUnits=Integer.parseInt(command[3]);
							releasedResources[resourceToRelease-1]+=numUnits;  //adds the released resources 
							resourcesReleasedNextCycle=true;
							allocated[i][resourceToRelease-1]-=numUnits;  //releases the resources
//							System.out.println("TASK " + (i+1) + ": released" + numUnits+ " OF" + (resourceToRelease-1));
							//System.out.println("Task "+ (i+1)+" completes its release (resource[" + resourceToRelease + "]: released = " + numUnits + ", available next cycle = " + releasedResources[resourceToRelease-1]+  ")");
							tasks[i].commandPointer++;
						}
						if(command[0].equals("compute")) {  //if command equals compute
							int numCycles=Integer.parseInt(command[2]);
							if(tasks[i].computeCycles==0) {  //modifies the compute variable within the task
								tasks[i].computeCycles=numCycles;
							}
							//System.out.println("TASK " + (i+1) + ": compute" + numCycles +"CYCLES LEFT");
							tasks[i].computeCycles-=1;
							//System.out.println(tasks[i].computeCycles + "AFTER");
							if(tasks[i].computeCycles==0) {
								tasks[i].commandPointer++;
							}
						}
					}
					String[] command2= tasks[i].commands.get(tasks[i].commandPointer);
					if(command2[0].equals("terminate") && !finishedTasks.contains(tasks[i])) {  //if command equals terminate adds the task to finished tasks and adds the finished time 
						finishedTasks.add(tasks[i]);
						finishTimes[tasks[i].taskNumber-1]=cycle+1;
						//System.out.println(" and finishes at " + cycle);						
					}
				}
				if(tasksToRemove.size()>0) {  //removes the tasks from this cycle
					for(int i=0; i<tasksToRemove.size(); i++) {
						if(blockedTasks.contains(tasksToRemove.get(i))) {
							blockedTasks.remove(tasksToRemove.get(i));
							i--;
						}
					}
				}
			
//				for(int i=0; i<finishedTasks.size(); i++) {
//					System.out.println("FINISHED TASKS: " + finishedTasks.get(i).taskNumber);
//				}
//				for(int n=0; n< resourceList.length; n++){
//					for(int w=0; w<tasks.length; w++) {
//						//System.out.println((w+1) + " Allocated are now "+allocated[w][n]+ " OF RESOURCE " + (n+1));
//
//					}
//				}

			
				for(int u=0; u<resourceList.length; u++) {
					available[u]+=releasedResources[u];
				}
				cycle++;
				//System.out.println();
			}
			
		
		//Ouput
			System.out.println("BANKER's");
			int totalTime=0;
			int waitTotal=0;
			for(int i=0; i<tasks.length; i++) {
				totalTime+=finishTimes[i];
				waitTotal+=waitTimes[i];
			}
			
			for(int i=0; i<tasks.length; i++) {
				System.out.println("Task" + (i+1) + "      " + finishTimes[i] + "    " +waitTimes[i] + "    " + ((waitTimes[i]/(double)(finishTimes[i]))*100) + "%" );
			}
			System.out.println("total         " + totalTime + "    " + waitTotal + "   " +  ((waitTotal/(double)totalTime)*100) + "%");
	
		
		
	}
	
	

}