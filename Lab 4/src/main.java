import java.io.IOException;
import java.util.*;

public class main {
	static ArrayList<process> processList=new ArrayList<process>();
	static int numReferences;
	public static void main(String[] args) throws IOException {
		String M = args[0];  //machine size
		String P= args[1]; //page size
		String S= args[2]; //size of each process
		String J= args[3]; //job mix
		String N= args[4]; //number of references per process
		numReferences=Integer.parseInt(N);
		String R= args[5]; //replacement algo
		int quantum=3;
		page [] frameTable=new page[(Integer.parseInt(M)/Integer.parseInt(P))];
		int numPageFaults=0;
		int totalEvictions=0;
		
		System.out.println("Machine Size: " + M);
		System.out.println("Page Size: " + P);
		System.out.println("Process Size: " + S);
		System.out.println("Job Mix: " + J);
		System.out.println("Number of References: " + N);
		System.out.println("Replacement Algorithm: " + R);
//		System.out.println("Frame Table Size: " +frameTable.length);
		
		
		getProcesses(Integer.parseInt(J));
//		for(int i=0; i<processList.size(); i++) {
//			System.out.println(processList.get(i).processNumber);
//		}
		int processEnum=0;
		int currentWord=0;
		
		ArrayList<page> pageList=new ArrayList<page>();
		Stack<page> pageStack=new Stack<>();
		ArrayList<page> currentEvicted=new ArrayList<>();
		
//MAIN LOOP_________________________________________________________________
	//	for(int i=0; i<40; i++) {
			
		for(int i=0; i<processList.size()*Integer.parseInt(N); i++) {
//			for(int p=0; p<frameTable.length; p++) {
//				if(frameTable[p]!=null && i!=processList.size()*Integer.parseInt(N)) {
//					processList.get(frameTable[p].process-1).residencyTime++;
//					for(int x=0; x<currentEvicted.size(); x++) {
//						if(processList.get(frameTable[p].process-1).processNumber==currentEvicted.get(x).process && frameTable[p].pageNumber==currentEvicted.get(x).pageNumber) {
//							currentEvicted.remove(x);
//						}
//					}

					//System.out.println();
					//System.out.println(processList.get(frameTable[p].process-1).processNumber+" IS RESIDENT THIS CYCLE AND HAS" +processList.get(frameTable[p].process-1).residencyTime + "residency");
//				}
//			}
//			for(int p=0; p<currentEvicted.size(); p++) {
//				processList.get(currentEvicted.get(p).process-1).evictedTime++;
//			}
//		
			process currentProcess= processList.get(processEnum);
			currentProcess.decrement_quantum();
			int currentPageNumber=0;
			page currentPage=new page(1,2);
			if(currentProcess.first) {
				//System.out.println("FIRST CONTEXT for "+currentProcess.processNumber);
				if(i!=0) {
				//	double y=randomNumbers.randomOS()/(Integer.MAX_VALUE + 1d);
					//System.out.println("RANDOM NUMBER USED "+y +" : " +(y*(Integer.MAX_VALUE + 1d)));
				}
				currentWord=(111*currentProcess.processNumber+Integer.parseInt(S))%Integer.parseInt(S);
				currentPageNumber=currentWord/Integer.parseInt(P);
				currentPage=new page(currentPageNumber, currentProcess.processNumber);
				currentProcess.notFirst();
				currentPage.loadedTime=i+1;
				currentProcess.currentWord=currentWord;

//				System.out.println("LOADED TIME: " + (i+1) +" For page " + currentPage + " PROCESS " + currentProcess.processNumber);
			}
			else if(currentProcess.usedNextReference==true) {
				//	System.out.println("USING PRIOR REF");
			//	double y=randomNumbers.randomOS()/(Integer.MAX_VALUE + 1d);
				//randomNumbers.randomOS();
				currentWord=currentProcess.nextReference;
				currentProcess.usedNextReference=false;
				currentProcess.currentWord=currentWord;
				currentPageNumber=currentWord/Integer.parseInt(P);
				boolean newPage=true;
				for(int z=0; z< pageList.size(); z++) {
					if(pageList.get(z).pageNumber==currentPageNumber && pageList.get(z).process==currentProcess.processNumber) {
						newPage=false;
						currentPage=pageList.get(z);

					}
				}
				if(newPage) {
					currentPage=new page(currentPageNumber, currentProcess.processNumber);
					pageList.add(currentPage);
				}
				//currentPage.loadedTime=i+1;
				//System.out.println("LOADED TIME: " + (i+1) +" For page " + currentPage.pageNumber + " PROCESS " + currentProcess.processNumber);


			}
			else {
				//System.out.print(currentProcess.processNumber+ " uses ");
				currentWord=determineNextRef(currentProcess.currentWord, Integer.parseInt(S), currentProcess);
				if(currentProcess.quantum==0 && processList.size()>1) {
					//System.out.println(" YA KNOW THAT QUANTUM IS 0 ____");
					currentProcess.nextReference=determineNextRef(currentWord, Integer.parseInt(S), currentProcess);
					currentProcess.usedNextReference=true;
				}
				currentProcess.currentWord=currentWord;
				currentPageNumber=currentWord/Integer.parseInt(P);
			//	currentPage=new page(currentPageNumber, currentProcess.processNumber);
				for(int z=0; z< pageList.size(); z++) {
					if(pageList.get(z).pageNumber==currentPageNumber && pageList.get(z).process==currentProcess.processNumber) {
						currentPage=pageList.get(z);
					}
					else {
						currentPage=new page(currentPageNumber, currentProcess.processNumber);
					}
				}

			}
			
//			page currentPage=new page(currentPageNumber, currentProcess.processNumber);
			if(pageList.isEmpty()) {
				pageList.add(currentPage);
			}
			boolean usedBefore=false;
			for(int p=0; p<pageList.size(); p++) {
				if(pageList.get(p).pageNumber==currentPageNumber && pageList.get(p).process==currentProcess.processNumber) {
					//System.out.println("THIS PAGE HAS BEEN LOADED BEFORE________________ SIZE: " + pageList.size());
					currentPage=pageList.get(p);
					pageList.get(p).lastUsed=i;
					usedBefore=true;
				}
			}
			if(usedBefore==false) {
					pageList.add(currentPage);
			}
			currentPage.lastUsed=i;
//			System.out.println("CURRENT PAGE" +  currentPage.pageNumber+" current page number" + currentPageNumber);
//			System.out.print(currentProcess.processNumber+ " references word " + currentWord+ " (page " + currentPageNumber+") at time" + (i+1)+ ":");
//			for(int p=0; p<frameTable.length; p++) {
//				if(frameTable[p]!=null) {
//					System.out.println("At Frame " + p + "is page " + frameTable[p].pageNumber + " of Process " + frameTable[p].process);
//				}
//			}
			if(hit(frameTable,currentPage, currentProcess)!=-1) {
//				System.out.println(" Hit in frame" +hit(frameTable,currentPage, currentProcess));
				currentPage.lastUsed=i;
			}
			else {
				if(findHighestFreeFrame(frameTable)==-1) {
					totalEvictions++;
				//	currentProcess.runningSum++;
					//System.out.println("EVICTING MOTHERFUCKAH: " + totalEvictions);
					if(R.equals("lifo")) {
						page pageToEvict=pageStack.pop();
						processList.get(pageToEvict.process-1).runningSum+= (i+1)-pageToEvict.loadedTime;
//						System.out.println("Page" + pageToEvict.pageNumber + " was loaded at " + pageToEvict.loadedTime + " and evicted at " + (i+1) + " for a residency of " + ((i+1)-pageToEvict.loadedTime) );
						currentEvicted.add(pageToEvict);
						processList.get(pageToEvict.process-1).numEvictions++;
						processList.get(pageToEvict.process-1).evictedTime+=i;
//						System.out.println("Fault, evicting page " + pageToEvict.pageNumber + " of " + pageToEvict.process);
						for(int x=0; x<frameTable.length; x++) {
							if(frameTable[x]==pageToEvict) {
								frameTable[x]=currentPage;
								currentPage.loadedTime=i+1;
							}
						}
						numPageFaults+=1;
						currentProcess.numPageFaults+=1;
					}

					else if(R.equals("random")) {
						//System.out.println("WHAT IS GOINT ON?");
						int randomNumber=randomNumbers.randomOS();
					
						
						page pageToEvict=frameTable[randomNumber%frameTable.length];
//						System.out.println("RANDOM USED: " + randomNumber + " to evict " + (randomNumber%frameTable.length));
						processList.get(pageToEvict.process-1).runningSum+= (i+1)-pageToEvict.loadedTime;
//						System.out.println("Page" + pageToEvict.pageNumber + " was loaded at " + pageToEvict.loadedTime + " and evicted at " + (i+1) + " for a residency of " + ((i+1)-pageToEvict.loadedTime) );


						processList.get(pageToEvict.process-1).evictedTime+=i;

						processList.get(pageToEvict.process-1).numEvictions++;
						currentEvicted.add(pageToEvict);
//						System.out.println("PAGE TO EVICT: " + pageToEvict.pageNumber + " " + pageToEvict.process);
						for(int x=0; x<frameTable.length; x++) {
							if(frameTable[x]==pageToEvict) {
								frameTable[x]=currentPage;
								currentPage.loadedTime=i+1;

							}
						}
						numPageFaults+=1;
						currentProcess.numPageFaults+=1;
						

					}
					else if(R.equals("lru")) {
						int currentLast=100000000;
						int frameTablePosition=0;

						for(int j=0; j<frameTable.length; j++) {
							if(frameTable[j].lastUsed<currentLast) {
								currentLast=frameTable[j].lastUsed;
								frameTablePosition=j;
							}
						}
						page pageToEvict=frameTable[frameTablePosition];

						processList.get(pageToEvict.process-1).runningSum+= (i+1)-pageToEvict.loadedTime;
//						System.out.println();
//					    System.out.println("Page" + pageToEvict.pageNumber + " was loaded at " + pageToEvict.loadedTime + " and evicted at " + (i+1) + " for a residency of " + ((i+1)-pageToEvict.loadedTime) );
						//System.out.println("Process" + processList.get(pageToEvict.process-1).processNumber + " has a running sum of " + processList.get(pageToEvict.process-1).runningSum) ;
						processList.get(pageToEvict.process-1).numEvictions++;
						currentEvicted.add(pageToEvict);
						processList.get(pageToEvict.process-1).evictedTime+=i;


						//System.out.println("PAGE TO EVICT: " + pageToEvict.pageNumber + " " + pageToEvict.process);
						//System.out.println(pageToEvict.process+ " has" +processList.get(pageToEvict.process-1).numEvictions);

						int frameNumber = 0;
						for(int x=0; x<frameTable.length; x++) {
							if(frameTable[x]==pageToEvict) {
								//System.out.println("IM EVICTING BAABY");
								frameTable[x]=currentPage;
								frameNumber=x;
								currentPage.loadedTime=i+1;

							}
						}

						numPageFaults+=1;
						currentProcess.numPageFaults+=1;
//						System.out.println("FAULT evicting page" + pageToEvict.pageNumber + " of " + pageToEvict.process+ "From frame "+ frameNumber);
					}

				} 
				else {
				//	totalEvictions++;
					currentPage.loadedTime=i+1;
					numPageFaults+=1;
					currentProcess.numPageFaults+=1;
//					System.out.println(" FAULT, using free frame" + findHighestFreeFrame(frameTable));
					frameTable[findHighestFreeFrame(frameTable)]=currentPage;
				}
				pageStack.add(currentPage);

			}
//			System.out.println("PAGE QUEUE: ");
//			for(int o=0; o< pageQueue.size(); o++) {
//				System.out.print(" " + ((LinkedList<page>) pageQueue).get(o).pageNumber);
//			}
//			System.out.println();

			//System.out.println("CURRENT PROCESS: "+ currentProcess.processNumber+"has "+currentProcess.numReferences+ "left.");
//			if(currentProcess.numReferences==0) {
//				processEnum++;
//				processList.remove(currentProcess);
//			}
			currentProcess.numReferences-=1;

			if(currentProcess.quantum==0 || currentProcess.numReferences==0) {
				currentProcess.reset_quantum();
				processEnum++;
				if(processEnum==processList.size()) {
					processEnum=0;
				}
				
			}

			
		}
		int totalRunning=0;
		for(int i=0; i<processList.size(); i++) {
			if(processList.get(i).numEvictions==0) {
				System.out.println("Process " + processList.get(i).processNumber+ " had " + processList.get(i).numPageFaults + " faults.");
				System.out.println("             With no evictions, the average residency is undefined.");

			}
			else {
				totalRunning+=processList.get(i).runningSum;

			//	System.out.println("Process " + processList.get(i).processNumber+ " had " + processList.get(i).residencyTime + " residency and " + processList.get(i).numEvictions + " evictions.");
//				System.out.println("Process " + processList.get(i).processNumber+ " had " + processList.get(i).numPageFaults + " faults and " +(((float)processList.get(i).evictedTime-(float)processList.get(i).residencyTime)/(float)processList.get(i).numEvictions) + " residency.");
//				System.out.println("Process " + processList.get(i).processNumber+ " had " + processList.get(i).numPageFaults + " faults and " +(((float)(processList.get(i).evictedTime-processList.get(i).residencyTime))/(float)processList.get(i).numEvictions) + " residency.");
				System.out.println("Process " + processList.get(i).processNumber+ " had " + processList.get(i).numPageFaults + " faults and " +(((float)(processList.get(i).runningSum)/(float)processList.get(i).numEvictions) + " residency."));
				//System.out.println("Number of Evictions: " + processList.get(i).numEvictions);
			}
		}
		if(totalEvictions==0) {
			System.out.println("The total number of faults is " + numPageFaults+ " and the overall average residency is undefined");

		}
		else {
//			System.out.println(totalRunning);
			System.out.println("The total number of faults is " + numPageFaults+ " and the overall average residency is "+ ((float)totalRunning/(float)totalEvictions));

		}
		
//END OF MAIN LOOP__________________________________________________________
		
	}
	
	
//Creates process objects based on the possible sets and adds them to processList
	public static void getProcesses(int J) {
		if(J==1) {  //case 1: 1 Process
			process process=new process(1, 0, 0, numReferences, 1);
			processList.add(process);
		}
		else if(J==2) {  //case 2: 4 processes
			for(int i=0; i<4; i++) {
				process process= new process(1,0,0, numReferences, i+1);
				processList.add(process);
				
			}
		}
		else if(J==3) { //case 3: 4 processes fully random references
			for(int i=0; i<4; i++) {
				process process= new process(0,0,0, numReferences, i+1);
				processList.add(process);
				
			}
		}
		else if(J==4) {  //case 4: 4 processes each with different probabilities
			for(int i=0; i<4; i++) {
				if(i==0) {
					process process= new process(.75,.25,0, numReferences, i+1);
					processList.add(process);
				}
				else if(i==1) {
					process process= new process(.75,0,.25, numReferences, i+1);
					processList.add(process);
				}
				else if(i==2) {
					process process= new process(.75,.125,.125, numReferences, i+1);
					processList.add(process);
				}
				else if(i==3) {
					process process= new process(.5,.125,.125, numReferences, i+1);
					processList.add(process);
				}
				
			}
		}
	}
	
	//function that determines the address of the next reference
	public static int determineNextRef(int curWord, int S, process P) {
		double y=(randomNumbers.randomOS()/(Integer.MAX_VALUE + 1d));
		//System.out.println("random number: "+y + ": " + (y*(Integer.MAX_VALUE+1d))+ "A: " +P.A + "B: " + P.B);
		int nextRef = 0;
		if(y<P.A) {
//			System.out.println("HIGH" + curWord);

			nextRef=(curWord+1+S)%S;
//			System.out.println("NEXT "+ nextRef);
		}
		else if(y<P.A+P.B) {
//			System.out.println("LOW");

			nextRef=(curWord-5+S)%S;
//			System.out.println("NEXT "+ nextRef);


		}
		else if(y<P.A+P.B+P.C) {
//			System.out.println("HIGHER");

			nextRef=(curWord+4+S)%S;
//			System.out.println("NEXT "+ nextRef);


		}
		else if((y>P.A+P.B+P.C)){
//			System.out.println("RANDOM");
			nextRef=randomNumbers.randomOS()%S;
//			System.out.println("NEXT "+ nextRef);


		}
		return nextRef;
	}


	public static int findHighestFreeFrame(page [] frameTable) {
		int highestIndex=-1;
		for(int i=0; i<frameTable.length; i++) {
			if(frameTable[i]==null && i>highestIndex) {
				highestIndex=i;
			}
		}
		return highestIndex;
	}
	public static int hit(page [] frameTable, page currentPage, process currentProcess){
		for(int i=0; i<frameTable.length;i++) {
			if(frameTable[i]!=null && frameTable[i].pageNumber==currentPage.pageNumber && frameTable[i].process == currentProcess.processNumber) {
//				System.out.println(frameTable[i].pageNumber+ " is same as " + currentPage.pageNumber);
				return i;
			}
		}
		return -1;
	}
	

}


