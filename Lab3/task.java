import java.util.*;

//task class that stores several variables and has a print function
public class task {
	ArrayList<String[]> commands = new ArrayList<String[]>();
	int commandPointer=0;
	int taskNumber; 
	int computeCycles=0;
	public task(int taskNumber) {
		this.taskNumber=taskNumber;
	}
	
	public void printCommands() {
		System.out.println("COMMANDS FOR TASK " + taskNumber+": ");
		for(int i=0; i<commands.size(); i++) {
			for(int x=0; x<commands.get(i).length; x++) {
				System.out.println(commands.get(i)[x]);
			}
		}
	}
}
