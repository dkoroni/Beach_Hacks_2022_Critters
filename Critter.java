package Beach_Hacks_2022_Critters;

public class Critter extends Organic {

	private int view;
	
	public Critter(int x, int y) {
		setHealth(10);
		setFood(false);
		this.x = x;
		this.y = y;
	}
	
	public Critter(int x, int y, int health) {
		this(x, y);
		setHealth(health);
	}
	
	public Critter(int x, int y, int health, int view) {
		this(x, y, health);
		setView(view);
	}
	
	public void setView(int view) {this.view  = view;}
	
	@Override
	public void act() {
		// TODO Auto-generated method stub
		
	}

}
