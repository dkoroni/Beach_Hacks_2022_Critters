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
		setHealth(ThreadLocalRandom.current().nextInt(health - 3, health + 4));
	}
	
	public Critter(int x, int y, int health, int view) {
		this(x, y, health);
		setView(ThreadLocalRandom.current().nextInt(view - 1, view + 2));
	}
	
	public Critter(int x, int y, String healthStr, String viewStr, int health, int view) {
		if (healthStr.equals("y"))
			setHealth(ThreadLocalRandom.current().nextInt(10, health + 1));
		else
			setHealth(health);
		if (viewStr.equals("y"))
			setView(ThreadLocalRandom.current().nextInt(10, view + 1));
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
		if (food != null) {
			xDir = food.x-x;
			yDir = food.y-y;
		}
		else {
			xDir = view;
			yDir = view;
		}
		if (Math.hypot(xDir, yDir) > view ||(App.fullness && getMaxHealth()/getHealth() <= 2) || food == null) {
			
			xDir = ThreadLocalRandom.current().nextInt(-1, 2);
			yDir = ThreadLocalRandom.current().nextInt(-1, 2);
			while (xDir == 0 && yDir == 0) { 
				xDir = ThreadLocalRandom.current().nextInt(-1, 2);
				yDir = ThreadLocalRandom.current().nextInt(-1, 2);
			}
		}
		else {			
			if (xDir != 0)
				xDir /= Math.abs(xDir);
			if (yDir != 0)
				yDir /= Math.abs(yDir);
		}
		int updatedX = (((x + xDir)%App.maxX) + App.maxX) % App.maxX;
		int updatedY = (((y + yDir)%App.maxY) + App.maxY) % App.maxY;
		
		if (food!= null) {
			if (updatedX == food.x && updatedY == food.y) {
				incrementHealth(food.getHealth());
				if (getHealth() > getMaxHealth())
					setHealth(getMaxHealth());				
			}
		}
		
		App.updateCritterPosition(x, y, updatedX, updatedY);
		x = updatedX;
		y = updatedY;
		decrementHealth(1);
		if (this.getHealth() <= 0) {
			this.setDecay(true);
			App.starved.add(this);
			return;
		}
	}
	
	@Override
	public String toString() {
		return "Critter\n\tMax health: " + getMaxHealth() + "\n\tRemaining health: " + getHealth() + "\n\tView: " + getView();
	}

}
