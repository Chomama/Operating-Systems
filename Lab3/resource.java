
//resource class that stores the number of units and the tasks number
public class resource {
	int units;
	int number;
	public resource(int number, int units) {
		this.units=units;
		this.number=number;
	}
	public String toString() {
		return "Resource "+ number+ " has " +units+" units";
	}
}
