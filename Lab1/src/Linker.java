import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;

public class Linker{
	
	public static void main(String args[]) throws IOException {
		int numMods;
		int numVarInModule;
		int relativeAddress;
		int absoluteAddress;
		int numVarUsed;
		int numAddresses;
		string symbol;
		
		ArrayList<String> symbolTable = new ArrayList <String>;
		
				
		
		//Input File
		File input = new File(args[0]);	
	//	System.out.println("File name: "+ input.getName());
		
        //Scanner for first pass
		try {
			Scanner run1 = new Scanner(input);
		}
		catch {
			System.out.println("Error: File not found. \n");
		}
		
		//FIRST PASS
		//scan in the number of modules
        numMods=run1.nextInt();
        //System.out.println(numModules);
        for (int i=0; i< numMods; i++) {
        	//gets the number of symbols defined in the module
        	numVarInModule=run1.nextInt();
        	//loops through the symbols and gets the relative address for the symbol table
        	for(int k=0; k< numVarInModule; k++) {
        		symbol=run1.next(); 
        		symbolTable.add(symbol);
        		absoluteAddress=run1.nextInt();
        	}
        	//gets the number of variables used from the use list
        	numvarUsed=run1.nextInt();
        	//loops through the skips because not needed for first pass
        	for(x=0; x<numVarUsed; x++) {
        		run1.next()
        	}
        	//gets the module size for the base addresses
        	numAddresses=run1.nextInt();
        	for(int y=0; y<numAddresses; y++) {
        		run1.next
        	}
        	System.out.println("Number of Modules ", numMods);
        	System.out.println("Number of Symbols ", numVarInModule);
        	System.out.println("Number of Modules ", numMods);
        	System.out.println("Number of Modules ", numMods);
        	System.out.println("Number of Modules ", numMods);
        }
        
        
	}
}
