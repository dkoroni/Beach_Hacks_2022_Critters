package Beach_Hacks_2022_Critters;

import java.util.concurrent.ThreadLocalRandom;

public class Critter extends Organic {

	private int view  = 10;
	
	public Critter(int x, int y) {
		setFood(false);
		this.x = x;
		this.y = y;
	}
	
	public Critter(int x, int y, int health) {
		this(x, y);
		setHealth(ThreadLocalRandom.current().nextInt(health - 1, health + 2));
	}
	
	public Critter(int x, int y, int health, int view) {
		this(x, y, health);
		setView(ThreadLocalRandom.current().nextInt(Math.max(1, view - 3), view + 4));
	}
	
	public Critter(int x, int y, String healthStr, String viewStr, int health, int view) {
		if (healthStr.equals("y"))
			setHealth(ThreadLocalRandom.current().nextInt(10, health + 1));
		else
			setHealth(health);
		if (viewStr.equals("y"))
			setView(ThreadLocalRandom.current().nextInt(4, view + 1));
		else
			setView(view);
		this.x = x;
		this.y = y;
		setFood(false);
	}
	
	public void setView(int view) {this.view  = view;}
	public int getView() {return view;}
	
	@Override
	public void act() {
		Organic food = App.findClosestFood(x,y);
		int xDir;
		int yDir;
		// if food is valid then find x distance and y distance
		if (food != null) {
			xDir = food.x-x;
			yDir = food.y-y;
		}
		else {
			// if there is no food then pretend the food is out of view
			// idk if i need this since i check for food == null later but why not?
			xDir = view;
			yDir = view;
		}
		if (Math.hypot(xDir, yDir) > view ||(App.fullness && getMaxHealth()/getHealth() <= 2) || food == null) {
			// picks a random position, CROSSES BORDER AND WRAPS TO OTHER SIDE
			xDir = ThreadLocalRandom.current().nextInt(-1, 2);
			yDir = ThreadLocalRandom.current().nextInt(-1, 2);
			while (xDir == 0 && yDir == 0) { 
				xDir = ThreadLocalRandom.current().nextInt(-1, 2);
				yDir = ThreadLocalRandom.current().nextInt(-1, 2);
			}
		}
		else {			
			// moves one unit horizontally or vertically towards the target
			if (xDir != 0)
				xDir /= Math.abs(xDir);
			if (yDir != 0)
				yDir /= Math.abs(yDir);
		}
		// uses math modulus to update position, allowing wrapping of coordinates
		int updatedX = (((x + xDir)%App.maxX) + App.maxX) % App.maxX;
		int updatedY = (((y + yDir)%App.maxY) + App.maxY) % App.maxY;
		
		if (food!= null) {
			if (updatedX == food.x && updatedY == food.y) {
				incrementHealth(food.getHealth());
				if (getHealth() > getMaxHealth())
					setHealth(getMaxHealth());				
			}
		}
		
		// checks if the new position can be occupied
		boolean canMove = App.validateCritterPosition(updatedX, updatedY);
		if(canMove) {
			App.updateCritterPosition(x, y, updatedX, updatedY);
			x = updatedX;
			y = updatedY;
		}
		decrementHealth(1);
		// if the critter dies on this turn, then it dies and adds itself to the starved list :(
		if (this.getHealth() <= 0) {
			this.setDecay(true);
			App.starved.add(this);
			App.field[x][y] = 'D';
			return;
		}
	}
	
	@Override
	// this is a string
	public String toString() {
		return "Critter\n\tMax health: " + getMaxHealth() + "\n\tRemaining health: " + getHealth() + "\n\tView: " + getView();
	}

}
