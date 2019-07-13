import java.io.*;
import java.util.*;
public class main {
	public static void main(String[] args) throws IOException {
		int numTasks;
		int numResources;
		int numUnits;
		
		//___________________________________________________Data Input_____________________________________________________
		File input = new File(args[0]);
        Scanner scanner = new Scanner(input);
        numTasks=scanner.nextInt(); //scans in the number of tasks
        task[] tasks=new task[numTasks];  //creates an array of tasks
        task[] tasks2=new task[numTasks];
        for(int i=0; i<numTasks; i++) {  //populates the task array with task objects
         //   System.out.println(i);

        	task task=new task(i+1);
        	task task2=new task(i+1);
        	tasks[i]=task;
        	tasks2[i]=task2;
        	 
        }
        numResources=scanner.nextInt();  //scans in number of resources
		resource[] resourceList=new resource[numResources];  //creates an array of resources
		resource[] resourceList2=new resource[numResources];
		
        for(int i=0; i<numResources; i++) {  //populates the resource with resource objects
        	numUnits=scanner.nextInt();
        	resource resource=new resource(i+1, numUnits);
        	resource resource2=new resource(i+1, numUnits);
        	resourceList[i]=resource;
          	resourceList2[i]=resource2;
        }
//		for(resource resource:resourceList) {   
//			System.out.println(resource.toString());
//		}
		scanner.nextLine();
		while(scanner.hasNextLine()) {
			String[] split=scanner.nextLine().split("\\s+");
			//System.out.println(split.length);
//			System.out.println("HI"+ split.length);
//			for(int i=0; i<split.length; i++ ) {
//				System.out.println(split[i]);
//			}
			//System.out.println("size: " +split.length);
			if(split.length>1 ) {
				tasks[Integer.parseInt(split[1])-1].commands.add(split);
				tasks2[Integer.parseInt(split[1])-1].commands.add(split);
			}
		}
		
//		for(int i=0; i<tasks.length; i++) {
//			//System.out.println("Commands for"+i);
//			//tasks[i].printCommands();
//		}
		
		//___________________________________________________Begin Algorithms_____________________________________________________
		int cycle=0;
		
		 
		//Optimistic
		optimisticRM.runOptimisticRM(tasks, resourceList);
		System.out.println();
		
		//Bankers
		Banker_algorithm.runBanker(tasks2, resourceList2);
		
		

		
		
	}
}