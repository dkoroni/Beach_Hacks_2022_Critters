package Beach_Hacks_2022_Critters;

/**
 * abstract class for organic materials on the field
 * contains the abstract method act
 * @author darius 
 *
 */
public abstract class Organic {
	public int x;
	public int y;
	private int health;
	private boolean food;

	public abstract void act();
	
	public int getHealth() {return health;}
	public void setHealth(int hp) {health = hp;}
	public boolean isFood() {return food;}
	public void setFood(boolean food) {this.food = food;}
}

