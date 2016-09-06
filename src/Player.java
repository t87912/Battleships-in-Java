
public class Player {
	
	public String name;
	public boolean computer;
	public int hits;
	
	public Player(String name, boolean computer, int hits) {
	    this.name = name;
	    this.computer = computer;
	    this.hits = hits;
	}
	
	public void printName(){
		System.out.println(this.name);
	}
	
	public void hitTarget(){
		hits++;
	}
	
	public int getNumberOfHits(){
		return hits;
	}

}
