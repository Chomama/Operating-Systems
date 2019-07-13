import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class randomNumbers {
	static ArrayList <String> numberList = new ArrayList<String>();
	private static int count=0;
	
	static {
		//Input File
		
		File input = new File("random-numbers");
		try {
			Scanner run1 = new Scanner(input);
			while (run1.hasNext()) {
				numberList.add(run1.next());
			}
		}
		
		catch (Exception IOException) {
			System.out.println("Error: File not found. \n");
		}
			
	}

	public static void setCount() {
		count=0;
	}
	public static int randomOS() {	
		int randomInt = Integer.parseInt(numberList.get(count));
		//System.out.println(randomInt);
		count++;
		return randomInt;
	}

}