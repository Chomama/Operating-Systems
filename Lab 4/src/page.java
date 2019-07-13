
public class page {
	int pageNumber;
	int process;
	int lastUsed=0;
	int currentResidency=0;
	int loadedTime=0;
	
	
	public page(int pageNumber, int process) {
		this.pageNumber=pageNumber;
		this.process=process;
	}

}
