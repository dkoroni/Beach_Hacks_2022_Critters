package Beach_Hacks_2022_Critters;

public class Food extends Organic{

	private boolean rotten = false;
	
	public Food(int x, int y) {
		setHealth(10);
		setFood(true);
		this.x = x;
		this.y = y;
	}
	
	public Food(int x, int y, int health) {
		this(x, y);
		setHealth(health);
	}
	
	public boolean isRotten() {return rotten;}
	
	@Override
	public void act() {
		//decrementHealth(1);
		if (getHealth() <= 0) {
			rotten = true;
		}
		if(rotten) {
			
		}
	}
	
}
