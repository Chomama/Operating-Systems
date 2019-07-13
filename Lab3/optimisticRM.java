import java.util.*;;

public class optimisticRM {
	
	public static void main(String[] args) {
	
	}
	
	//main function that runs the optimistic RM
	public static void runOptimisticRM(task[] tasks, resource[] resourceList) {
		int cycle=0;
		int[] available=new int[resourceList.length];
		int[][] allocated=new int[tasks.length][resourceList.length];
		ArrayList<task> blockedTasks = new ArrayList<>(); 
		ArrayList<task> finishedTasks=new ArrayList<>();
		int[] finishTimes=new int[tasks.length];
		int[] waitTimes=new int[tasks.length];

		boolean deadlocked=false;
		
//		System.out.println("HIHI");
//		for(task task: tasks) {
//			task.printCommands();
		
		for(int i=0; i<resourceList.length; i++) {
			available[i]=resourceList[i].units;  //populates the available array
			
		}
		
		
		//main loop that runs until finished processes is less than the number of tasks
		while(finishedTasks.size()<tasks.length) {
			
			//check blocked tasks
		
//		

			ArrayList<task> tasksToRemove = new ArrayList<>();  //this Arraylist holds the processes to remove from the blocked list at the end of the cycle
			//go through tasks 
				if(blockedTasks.size()>0) {   
					for(int i=0; i< blockedTasks.size(); i++) {  //goes throught list of blocked tasks
						String[] command= blockedTasks.get(i).commands.get(blockedTasks.get(i).commandPointer);  //gets the command
						int requestedResource=Integer.parseInt(command[2]);
						int numUnits=Integer.parseInt(command[3]);
						if(available[requestedResource-1]>=numUnits) {  //if the number of available units is greater than or equal to the number of units requested, decrement available, allocate the units to the task, and move the command pointer
							available[requestedResource-1]-=numUnits;
							allocated[blockedTasks.get(i).taskNumber-1][requestedResource-1]+=numUnits;
							blockedTasks.get(i).commandPointer++; 
							tasksToRemove.add(blockedTasks.get(i));

						}

						waitTimes[blockedTasks.get(i).taskNumber-1]+=1;  //increment the wait time for the task

					}
					
					
				}
				int[] releasedResources=new int[resourceList.length];  //copy of available to store units that are to be released at the end of the cycle
				boolean resourcesReleasedNextCycle= false;  //boolean to check at end of cycle to see if there are resources to release
				for(int i=0; i< tasks.length; i++) {  
					String[] command= tasks[i].commands.get(tasks[i].commandPointer);
				//	System.out.println(command[0]);

					if(tasks[i].computeCycles!=0) {  //checks if the task is computing and modifies the computeCycles field in the task
						if(tasks[i].computeCycles-1==0) {
							tasks[i].commandPointer++; 
							
						}

						tasks[i].computeCycles-=1;

					}
					else if (!blockedTasks.contains(tasks[i]) && !finishedTasks.contains(tasks[i])){ //if the current task is not blocked or finished
						if(command[0].equals("initiate")) {
							tasks[i].commandPointer++;  
						}
						//command for request
						if(command[0].equals("request")) {   
							int requestedResource=Integer.parseInt(command[2]);
							int numUnits=Integer.parseInt(command[3]);
							if(available[requestedResource-1]>=numUnits) {  //if the number of available units is greater than or equal to the number of units requested, decrement available, allocate the units to the task, and move the command pointer
								available[requestedResource-1]-=numUnits;
								allocated[i][requestedResource-1]+=numUnits;
								tasks[i].commandPointer++; 
	
							}
							else {  //if there are not enough units available, add the task to blocked tasks
								blockedTasks.add(tasks[i]);  
							}
							
						}
						//command for release
						if(command[0].equals("release")) { 
							int resourceToRelease=Integer.parseInt(command[2]);
							int numUnits=Integer.parseInt(command[3]);
							

							releasedResources[resourceToRelease-1]+=numUnits;  //adds the released resources 
							resourcesReleasedNextCycle=true;
							allocated[i][resourceToRelease-1]-=numUnits;  //releases the resources
							tasks[i].commandPointer++;
						}
						//command for compute
						if(command[0].equals("compute")) {
							int numCycles=Integer.parseInt(command[2]);
							if(tasks[i].computeCycles==0) {  //modifies the compute variable within the task
								tasks[i].computeCycles=numCycles;
							}
							tasks[i].computeCycles-=1;
							if(tasks[i].computeCycles==0) {
								tasks[i].commandPointer++;
							}
						}
					}
					String[] command2= tasks[i].commands.get(tasks[i].commandPointer);
					//command for terminate
					if(command2[0].equals("terminate") && !finishedTasks.contains(tasks[i])) {
						finishedTasks.add(tasks[i]);  //adds the task to the finished tasks arrayList
						finishTimes[i]+=cycle+1;  //adds the cycle to finish times
						
						//System.out.println(" and finishes at " + cycle);						
					}
				}
				if(tasksToRemove.size()>0) {  //checks if the tasks to remove list is not empty and if it is not then it takes the task out of the blocked list
					for(int i=0; i<tasksToRemove.size(); i++) {
						if(blockedTasks.contains(tasksToRemove.get(i))) {
							blockedTasks.remove(tasksToRemove.get(i));
							i--;
						}
					}
				}
				
				//checks for deadlock state by checking if the number of blocked tasks is equal to the number of running tasks and there are no resources being released next cycle and there are still running tasks
				if(blockedTasks.size()==(tasks.length-finishedTasks.size()) && resourcesReleasedNextCycle==false && (tasks.length-finishedTasks.size()!=0)) {  
					//System.out.println("Yeah we deadlockked.");
					Boolean deadlock=true;
					while (deadlock) {
						task abort=blockedTasks.get(0);  //temp variable for the task to abort
						for(int i=0; i<blockedTasks.size(); i++) {  //replaces the temp variable with the blocked task with the lowest tasknumber
							if(blockedTasks.get(i).taskNumber<abort.taskNumber && !finishedTasks.contains(blockedTasks.get(i))) {
								abort=blockedTasks.get(i);
							}
						}
						finishedTasks.add(abort);  //adds the task to abort to finished tasks
						blockedTasks.remove(abort);  //removes the abort task from blocked tasks
						for(int n=0; n< resourceList.length; n++){  //adds the allocated resources from the aborted task
							available[n]+=allocated[abort.taskNumber-1][n];  //
							//System.out.println("Resources available AFTER: " +available[n]);
						}
					//	System.out.println("ABORTED"+ abort.taskNumber);
						for(int i=0; i< blockedTasks.size(); i++) {  //rechecks if there is still deadlock
							String[] command= blockedTasks.get(i).commands.get(blockedTasks.get(i).commandPointer);
							if(command[0].equals("request")) {   
								int requestedResource=Integer.parseInt(command[2]);
								int numUnits=Integer.parseInt(command[3]);
							//	System.out.println("TASK " + (i+1) + ": requests" + numUnits+ " OF" + requestedResource);
								if(available[requestedResource-1]>=numUnits) {  //if the number of available units is greater than or equal to the number of units requested, decrement available, allocate the units to the task, and move the command pointer
									deadlock=false;
		  
								}
								else {  //if there are not enough units available, add the task to blocked tasks
									deadlock=true;
								//	System.out.println("FIXED");
								}
								
							}
						}

						
					}
				}
//				for(int i=0; i<finishedTasks.size(); i++) {
//					System.out.println("FINISHED TASKS: " + finishedTasks.get(i).taskNumber);
//				}
//				for(int n=0; n< resourceList.length; n++){
//					for(int w=0; w<tasks.length; w++) {
//						System.out.println((w+1) + " HAD "+allocated[w][n]+ " OF RESOURCE " + n);
//
//					}
//				}

			
				for(int u=0; u<resourceList.length; u++) {  //releases the resources
					available[u]+=releasedResources[u];
				}
				cycle++;  //increments the cycle
				//System.out.println();
			}
			
		//output
		System.out.println("FIFO");
		int totalTime=0;
		int waitTotal=0;
		for(int i=0; i<tasks.length; i++) {
			if(finishTimes[i]==0) {
				waitTimes[i]=0;
			}
		}
		for(int i=0; i<tasks.length; i++) {
			totalTime+=finishTimes[i];
			waitTotal+=waitTimes[i];
		}
		
		for(int i=0; i<tasks.length; i++) {
			System.out.println("Task" + (i+1) + "      " + finishTimes[i] + "    " +waitTimes[i] + "    " + ((waitTimes[i]/(double)(finishTimes[i]))*100) + "%" );
		}
		System.out.println("total         " + totalTime + "    " + waitTotal + "   " +  ((waitTotal/(double)totalTime)*100) + "%");
	//}
			
		//}
		
		
	}
	
	

}
