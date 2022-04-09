package Beach_Hacks_2022_Critters;

import java.util.ArrayList;

/**
 * public class app contains the main method for running the project. instantiates the field and places objects
 * 
 * @author darius
 *
 */
public class App {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		char field[][] = new char[10][10];
		for (int i = 0; i < field.length; i++)
			for(int j = 0; j < field[0].length; j++)
				field[i][j] = '.';
		
		ArrayList<Organic> organisms = new ArrayList<>();
		organisms.add(new Critter(1,1));
		
		populateField(organisms, field);
		printCharArray(field);
		
	}
	
	/**
	 * prints the input array as long as its a char
	 * 
	 * @param array array to be printed
	 */
	public static void printCharArray(char[][] array) {
		for (int i = 0; i < array.length; i++) {
			for(int j = 0; j < array[0].length; j++) {
				System.out.print(array[i][j] + " ");
			}
			System.out.println();
		}
	}
	
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

}
