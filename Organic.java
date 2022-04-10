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
	private int maxHealth;
	private boolean food;
	private boolean decay = false;

	public abstract void act();
	
	public void decrementHealth(int amount) {health -= amount;}
	public void incrementHealth(int amount) {health += amount;}

	public int getHealth() {return health;}
	public int getMaxHealth() {return maxHealth;}
	public void setHealth(int hp) {health = hp; maxHealth = hp;}
	public boolean isFood() {return food;}
	public void setFood(boolean food) {this.food = food;}
	public boolean isDecay() {return decay;}
	public void setDecay(boolean decay) {this.decay = decay;}
	
}

