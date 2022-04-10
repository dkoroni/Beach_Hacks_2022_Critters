package Beach_Hacks_2022_Critters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * public class app contains the main method for running the project. instantiates the field and places objects
 * 
 * @author darius
 *
 */
public class App {

	public static final int maxX = 30;
	public static final int maxY = 30;
	
	public static ArrayList<Organic> organisms = new ArrayList<>();
	public static ArrayList<Food> foods = new ArrayList<>();
	public static ArrayList<Organic> starved = new ArrayList<>();
	public static ArrayList<Integer> healthHistory = new ArrayList<>();
	public static ArrayList<Integer> viewHistory = new ArrayList<>();
	public static char field[][] = new char[maxX][maxY];
	public static int healthGenes[] = new int[5];
	public static int viewGenes[] = new int[5];
	public static boolean fullness;
	
	
	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < field.length; i++)
			for(int j = 0; j < field[0].length; j++)
				field[i][j] = '.';
		
		
		Scanner input = new Scanner(System.in);
		
		System.out.println("How many critters would you like to add?");
		int critterAmount = input.nextInt();
		
		String critterHealthStr;
		System.out.println("Should the critters have random health? (y/n)");
		do{
			critterHealthStr = input.nextLine();
		}
		while(!(critterHealthStr.equals("y") || critterHealthStr.equals("n")));
		
		int critterHealth = 0;
		if (critterHealthStr.equals("n")) {
			System.out.println("How much health should each critter have?");
			critterHealth = input.nextInt();
		}
		else {
			System.out.println("What should be the maximum possible health? (min 10)");
			critterHealth = input.nextInt();
		}
		
		String critterViewStr;
		System.out.println("Should the critters have random view distance? (y/n)");
		do{
			critterViewStr = input.nextLine();
		}
		while(!(critterViewStr.equals("y") || critterViewStr.equals("n")));
		
		int critterView = 0;
		if (critterViewStr.equals("n")) {
			System.out.println("How much view distance should each critter have?");
			critterView = input.nextInt();
		}
		else {
			System.out.println("What should be the maximum possible view distance? (min 10)");
			critterView = input.nextInt();
		}
		
		System.out.println("How much food would you like to add?");
		int foodAmount = input.nextInt();
		
		String foodHealthStr;
		System.out.println("Should the food give random health? (y/n)");
		do{
			foodHealthStr = input.nextLine();
		}
		while(!(foodHealthStr.equals("y") || foodHealthStr.equals("n")));
		
		int foodHealth = 0;
		if (foodHealthStr.equals("n")) {
			System.out.println("How much health should the food give?");
			foodHealth = input.nextInt();
		}
		else {
			System.out.println("What should be the maximum possible health? (min 5)");
			foodHealth = input.nextInt();
		}
		
		System.out.println("How long should the critters search for food?");
		int time = input.nextInt();
		
		String fullnessStr;
		System.out.println("Should fullness be taken into account? A full animal will not search for food unless it has less than half health. (y/n)");
		do{
			fullnessStr = input.nextLine();
		}
		while(!(fullnessStr.equals("y") || fullnessStr.equals("n")));
		
		if (fullnessStr.equals("y"))
			fullness = true;
		else
			fullness = false;
		
		System.out.println("Display field? (y/n)");
		String fieldStr = input.nextLine();
		
		
		
		for(int i = 0; i < critterAmount; i++) {
			organisms.add(new Critter(ThreadLocalRandom.current().nextInt(0, maxX),ThreadLocalRandom.current().nextInt(0, maxY), critterHealthStr, critterViewStr, critterHealth, critterView));
		}
		
		for(int i = 0; i < foodAmount; i++) {
			int x = ThreadLocalRandom.current().nextInt(0, maxX);
			int y = ThreadLocalRandom.current().nextInt(0, maxY);
			while (field[x][y] != '.') {
				x = ThreadLocalRandom.current().nextInt(0, maxX);
				y = ThreadLocalRandom.current().nextInt(0, maxY);
			}
			Food tasty = new Food(x, y);
			if (foodHealthStr.equals("y"))
				tasty.setHealth(ThreadLocalRandom.current().nextInt(5, foodHealth + 1));
			else
				tasty.setHealth(foodHealth);
			organisms.add(tasty);
			foods.add(tasty);
		}
		
		
		populateField(organisms, field);

		act(time, fieldStr);
		
		int genesAmount = Math.min(critterAmount, 5);
		int geneIndex = 0;
		
		System.out.println("Surviving critters had the following attributes:");
		for(Organic org : organisms){
			if (!org.isFood() && !org.isDecay()) {
				System.out.println(org.toString());
			}
			if (geneIndex < genesAmount) {
				healthGenes[geneIndex] = org.getMaxHealth();
				viewGenes[geneIndex] = ((Critter)org).getView();
				geneIndex++;
			}
		}
		System.out.println("\n\nStarved critters had the following attributes: (in order of survival)");
		Collections.reverse(starved);
		for(Organic org : starved){
			if (!org.isFood() && org.isDecay()) {
				System.out.println(org.toString());
				if (geneIndex < genesAmount) {
					healthGenes[geneIndex] = org.getMaxHealth();
					viewGenes[geneIndex] = ((Critter)org).getView();
					geneIndex++;
				}
			}
		}
		
		healthHistory.add(averageHealth());
		viewHistory.add(averageView());
		
		while(true) {
			String generateStr;
			System.out.println("Spawn next generation of critters? Next generation is spawned using 5 surviving or most recently starved critters. (y/n)");
			do{
				generateStr = input.nextLine();
			}
			while(!(generateStr.equals("y") || generateStr.equals("n")));
			
			if (generateStr.equals("y")) {
				actNextGen(input, critterAmount, genesAmount, foodAmount, foodHealthStr, foodHealth, time, fieldStr);
			}
			else
				break;
		}
		
		System.out.println("Here is the average health and view distance of each inherited generation:");
		for (int i = 0; i < healthHistory.size(); i ++) {
			System.out.println("\tGeneration " + i + ": \t\tHealth: " + healthHistory.get(i) + "\tView distance: " + viewHistory.get(i));
		}
		
		input.close();
	}
	
	/**
	 * performs the field process with critters and food, calling each object with their act() procedure
	 * 
	 * @param time	how many loops should the field run for. terminates when there is no food regardless
	 * @param fieldStr	should the field be displayed. only "y" or "n"
	 * @throws InterruptedException	exception should never be thrown, thread is only used to sleep for display purposes
	 */
	public static void act(int time, String fieldStr) throws InterruptedException {
		while(time >= 0) {
			if (fieldStr.equals("y"))
					printCharArray();
			for (Organic org : organisms) {
				if (!org.isDecay())
					org.act();
				else {
					if (field[org.x][org.y] == 'C') {
						field[org.x][org.y] = 'D';
					}
					else
						field[org.x][org.y] = '.';	
				}
			}
			if (foods.size() == 0)
				break;
			if (starved.size() == organisms.size())
				break;
			if(fieldStr.equals("y"))
				Thread.sleep(1000);
			time--;
		}
		printCharArray();
	}
	
	public static void actNextGen(Scanner input, int critterAmount, int genesAmount, int foodAmount, String foodHealthStr, int foodHealth, int time, String fieldStr) throws InterruptedException {
		int geneIndex = 0;
		organisms.clear();
		foods.clear();
		starved.clear();
		for (int i = 0; i < field.length; i++)
			for(int j = 0; j < field[0].length; j++)
				field[i][j] = '.';
		
		for(int i = 0; i < critterAmount; i++) {
			organisms.add(new Critter(ThreadLocalRandom.current().nextInt(0, maxX),ThreadLocalRandom.current().nextInt(0, maxY), healthGenes[geneIndex%genesAmount], viewGenes[geneIndex%genesAmount]));
			geneIndex ++;
		}
		
		for(int i = 0; i < foodAmount; i++) {
			int x = ThreadLocalRandom.current().nextInt(0, maxX);
			int y = ThreadLocalRandom.current().nextInt(0, maxY);
			while (field[x][y] != '.') {
				x = ThreadLocalRandom.current().nextInt(0, maxX);
				y = ThreadLocalRandom.current().nextInt(0, maxY);
			}
			Food tasty = new Food(x, y);
			if (foodHealthStr.equals("y"))
				tasty.setHealth(ThreadLocalRandom.current().nextInt(5, foodHealth + 1));
			else
				tasty.setHealth(foodHealth);
			organisms.add(tasty);
			foods.add(tasty);
		}
		
		populateField(organisms, field);

		act(time, fieldStr);
		geneIndex = 0;
		System.out.println("Surviving critters had the following attributes:");
		for(Organic org : organisms){
			if (!org.isFood() && !org.isDecay()) {
				System.out.println(org.toString());
				if (geneIndex < genesAmount) {
					healthGenes[geneIndex] = org.getMaxHealth();
					viewGenes[geneIndex] = ((Critter)org).getView();
					geneIndex++;
				}
			}
		}
		System.out.println("\n\nStarved critters had the following attributes: (in order of survival)");
		Collections.reverse(starved);
		for(Organic org : starved){
			if (!org.isFood() && org.isDecay()) {
				System.out.println(org.toString());
				if (geneIndex < genesAmount) {
					healthGenes[geneIndex] = org.getMaxHealth();
					viewGenes[geneIndex] = ((Critter)org).getView();
					geneIndex++;
				}
			}
		}
		healthHistory.add(averageHealth());
		viewHistory.add(averageView());
	}
	
	public static int averageHealth() {
		int sum = 0;
		int count = 0;
		for (int val : healthGenes) {
			sum += val;
			count++;
			if (val == 0)
				break;
		}
		return (sum/count);
	}
	
	public static int averageView() {
		int sum = 0;
		int count = 0;
		for (int val : viewGenes) {
			sum += val;
			count++;
			if (val == 0)
				break;
		}
		return (sum/count);
	}
	
	/**
	 * prints the input array as long as its a char
	 * 
	 * @param array array to be printed
	 */
	public static void printCharArray() {
		for (int i = 0; i < field.length*2; i++)
			System.out.print('_');
		System.out.println();
		for (int i = 0; i < field.length; i++) {
			for(int j = 0; j < field[0].length; j++) {
				System.out.print(field[i][j] + " ");
				if (field[i][j] == 'D')
					field[i][j] = '.';
			}
			System.out.println();
		}
	}
	
	/**
	 * places objects listed in organisms into the field
	 * 
	 * @param organisms critters and food to be added to charArray field
	 * @param field charArray initially populated with '.' to be filled with chars representing the critters and food
	 */
	public static void populateField(ArrayList<Organic> organisms, char[][] field) {
		for (Organic org : organisms) {
			if (org.isFood()) {
				field[org.x][org.y] = 'F';
			}
			else {
				field[org.x][org.y] = 'C';
			}
		}
	}

	public static Organic findClosestFood(int x, int y) {
		Food closestFood = null;
		double closestDistance = Math.hypot(maxX, maxY);
		double distance;
		for (Food yum : foods) {
			distance = Math.hypot(x-yum.x, y-yum.y);
			if (closestDistance > distance) {
				closestDistance = distance;
				closestFood = yum;
			}
		}
		return closestFood;
	}

	public static void updateCritterPosition(int x, int y, int i, int j) {
		field[x][y] = '.';
		if (field[i][j] == 'F') {
			for (int k = 0; k < foods.size(); k++) {
				if(foods.get(k).x == i && foods.get(k).y == j) {
					foods.remove(k);
					break;
				}
					
			}
		}
		field[i][j] = 'C';
	}

}
