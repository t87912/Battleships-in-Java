import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Board {
	
	public int boardSize;
	public int[][] boardArray;
	public char[][] boardArrayChar;
	
	
	
	public Board(int boardSize) {
		this.boardSize = boardSize;
	}
	
	// Printing the board
	public void printBoard(Board board){
		char[] alphabet = {'A','B','C','D','E','F','G','H','I','J','K','L'};
		System.out.printf("   ");
		for (int i = 0; i < board.boardSize; i++){
			System.out.printf("%s ", alphabet[i]);
		}
		System.out.printf("\n");
		for (int x = 0; x < board.boardSize; x++){
			for (int y = 0; y < board.boardSize; y++){
				if (y == board.boardSize-1){
					System.out.printf("%s\n", board.boardArrayChar[x][y]);
				} else if (y == 0) {
					if (x < 10){
						System.out.printf("%s  ", x);
					} else {
						System.out.printf("%s ", x);
					}
					System.out.printf("%s ", board.boardArrayChar[x][y]);
				} else {
					if (y > 9){
						System.out.printf("%s ", board.boardArrayChar[x][y]);
					} else {
						System.out.printf("%s ", board.boardArrayChar[x][y]);
					}
				}
			}
		}
	}
	
	// Method to choose ship positions
	public void setShips(int type, Board board, boolean ai){	
		boolean finished = false;
		
		if (ai){
			do{
				if (type == 2){ // Patrol boat
					finished = setShipPos(2, board, "", true);
				} else if (type == 3){ // Battleship and sub
					finished = setShipPos(3, board, "", true);
				} else if (type == 4){ // Destroyer
					finished = setShipPos(4, board, "", true);
				} else if (type == 5){ // Carrier
					finished = setShipPos(5, board, "", true);
				}
			} while (!finished); 
		} else {
			// Instructions to the player:
			System.out.println("\nFormat of inputting ship position:");
			System.out.println("<Coordinates><Direction> e.g. 00E");
			System.out.println("Direction: North (N), South (S), East (E), West (W)\n");
			
			do{
				if (type == 2){ // Patrol boat
					finished = setShipPos(2, board, "Set position of patrol boat (1x2): ", false);
				} else if (type == 3){ // Battleship and sub
					finished = setShipPos(3, board, "Set position of battleship (2) and submarine (1) boat (1x3): ", false);
				} else if (type == 4){ // Destroyer
					finished = setShipPos(4, board, "Set position of destroyer boat (1x4): ", false);
				} else if (type == 5){ // Carrier
					finished = setShipPos(5, board, "Set position of carrier boat (1x5): ", false);
				}
			} while (!finished); 
		}
	}
	
	// Sets the position of the ship
	public boolean setShipPos(int type, Board board, String printMsg, boolean ai){
		Scanner reader = new Scanner(System.in);  // Reading from System.in
		String boat;
		String posCoords;
		char shipDirection;
		boolean valid = false;
		boolean noCollision = true;
		boolean finished = false;
		ArrayList<Integer> coords = new ArrayList<Integer>();
		System.out.printf(printMsg);
		
		if (ai){
			// Generating the direction
			 Random randChar = new Random();
			 int randomNum = randChar.nextInt((3 - 0) + 1) + 0;
			 shipDirection = 'E';
			 switch (randomNum){
			 	case 0:
			 		shipDirection = 'N';
			 		break;
			 	case 1:
			 		shipDirection = 'E';
			 		break;
			 	case 2:
			 		shipDirection = 'S';
			 		break;
			 	case 3:
			 		shipDirection = 'W';
			 		break;
			 }
			 
			// Generating the coordinates:
			//Random rand1 = new Random();
			int xCoord = randChar.nextInt((9 - 0) + 1) + 0;
			int yCoord = randChar.nextInt((9 - 0) + 1) + 0;
			String xC = Integer.toString(xCoord);
			String yC = Integer.toString(yCoord);
			posCoords = xC + yC;
			
			valid = validateShipPos(posCoords, board, shipDirection, type); // for valid placement ie not off map
			if (valid){
				coords = setShipCoords(type, posCoords, shipDirection, board);
				noCollision = checkCoords(coords, board, type);
			}
			
			if (valid && noCollision){
				addCords(coords, board);
				finished = true;
			} else {
				//System.out.println("Error, please input a valid ship position.");
				noCollision = false;
				finished = false;
			}
		} else {
			boat = reader.nextLine ();				
			posCoords = boat.substring(0, 2);
			shipDirection = boat.substring(2, 3).charAt(0);
			valid = validateShipPos(posCoords, board, shipDirection, type); // for valid placement ie not off map
			if (valid){
				coords = setShipCoords(type, posCoords, shipDirection, board);
				noCollision = checkCoords(coords, board, type);
			}
			
			if (valid && noCollision){
				addCords(coords, board);
				finished = true;
			} else {
				System.out.println("Error, please input a valid ship position.");
				noCollision = false;
				finished = false;
			}
		}
		
		
		return finished;
	}
	
	// Checking the coordinates for collisions
	public boolean checkCoords(ArrayList<Integer> coords, Board board, int type){
		int xPos, yPos;
		boolean valid = true;
		for (int x = 0; x < coords.size(); x+=2){
			xPos = (int) coords.get(x);
			yPos = (int) coords.get(x+1);
			if (board.boardArrayChar[xPos][yPos] != '~'){
				//System.out.println(board.boardArrayChar[xPos][yPos]);
				valid = false;
			}
		}
		return valid;
	}
	
	// Checking ships are not placed off map
	public boolean validateShipPos(String posCoords, Board board, char shipDirection, int type){
		boolean validPlacement = true;
		int xPos, yPos;
		int boardSize = board.boardSize;
		xPos = Character.getNumericValue(posCoords.charAt(0));
		yPos = Character.getNumericValue(posCoords.charAt(1));
		
		// Checking if ship placement will go off the board
		switch (shipDirection){
			case 'N': // North
				if (yPos-(type-1) < 0){
					//System.out.println("OFF BOARD NORTH");
					validPlacement = false;
				}
				break;
			case 'E': // East
				if (xPos+(type-1) > boardSize-1){
					//System.out.println("OFF BOARD EAST");
					validPlacement = false;
				}
				break;
			case 'S': // South
				if (yPos+(type-1) > boardSize-1){
					//System.out.println("OFF BOARD SOUTH");
					validPlacement = false;
				}
				break;
			case 'W': // West
				if (xPos-(type-1) < 0){
					//System.out.println("OFF BOARD WEST");
					validPlacement = false;
				}
				break;
		}
		return validPlacement;
	}
	
//	public boolean checkCoordinates(Board board, int xPos, int yPos){
//		boolean valid = true;
//		valid = board.boardArray[xPos][yPos] != 0 ? false : true;
//		return valid;
//	}
	
	public void addCords(ArrayList<Integer> coords, Board board){
		int xPos, yPos;

		for (int x = 0; x < coords.size(); x+=2){
			xPos = (int) coords.get(x);
			yPos = (int) coords.get(x+1);
			board.boardArrayChar[xPos][yPos] = 'S';
		}
	}
	
	
	// ERRORS here, return
	public ArrayList<Integer> setShipCoords(int type, String posCoords, char shipDirection, Board board){
		char dirChar = shipDirection;
		int xPos, yPos;
		ArrayList<Integer> coords = new ArrayList<Integer>(); 
		xPos = Character.getNumericValue(posCoords.charAt(0));
		yPos = Character.getNumericValue(posCoords.charAt(1));
		
		switch (shipDirection){
			case 'E': // East 
				coords.add(yPos);
				coords.add(xPos);
				if (type > 2){
					for (int x = 2; x < type; x++){
						coords.add(yPos);
						coords.add(xPos+(x-1));
					}
				}
				coords.add(yPos);
				coords.add(xPos+(type-1));
				break;
			case 'N': // North 
				coords.add(yPos);
				coords.add(xPos);
				if (type > 2){
					for (int x = 2; x < type; x++){
						coords.add(yPos-(x-1));
						coords.add(xPos);
					}
				}
				coords.add(yPos-(type-1));
				coords.add(xPos);
				break;
			case 'W': // West 
				coords.add(yPos);
				coords.add(xPos);
				if (type > 2){
					for (int x = 2; x < type; x++){
						coords.add(yPos);
						coords.add(xPos-(x-1));
					}
				}
				coords.add(yPos);
				coords.add(xPos-(type-1));
				break;
			case 'S': //  
				coords.add(yPos);
				coords.add(xPos);
				if (type > 2){
					for (int x = 2; x < type; x++){
						coords.add(yPos+(x-1));
						coords.add(xPos);
					}
				}
				coords.add(yPos+(type-1));
				coords.add(xPos);
				break;
		}
		return coords;
	}
}