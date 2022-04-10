# Beach_Hacks_2022_Critters
 Project for BeachHacks 2022

The project asks for user input regarding a specified amount of critters' max health and viewing distance as well as how much food should be provided. Then the critters are placed onto a 2D char array and search for food objects which will restore health within their view. If there is no food spotted, the critter will move in a random direction. The critter loses health each time it moves. Once all food has been depleted, time has run out, or every critter dies, a new generation of critters is spawned inheriting the traits of the 4 most viable critters, viable defined as 4 random surviving critters or the most recently starved if there are not enough surviving critters. There is a small amount of variance with each new generation. Once the user declines new critter generation a list of the average health and view distance for each generation is printed.

