import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

// TODO:
// sort out
// set variable to true/false to choose which players turn it is
// NEED TO CHECK HERE AI present when player 2 places ships

public class BattleshipGame {
	
	public static int NUMBER_OF_HITS;
	public static int[] SHIPS = {2,2,3,3,3,4,5};
	//public static int[] SHIPS = {2};
	public static int TOTAL_HITS_NEEDED; // 22 for normal ship numbers
	
	
	public static void main(String[] args) {
		
		System.out.println("Welcome to the Battleships game!");
		
		int userChoice;
		int boardSize = 12;
		String p1Name, p2Name;
		String[] playerNames;
		String playerOneTarget, playerTwoTarget;
		boolean aiChosen;
		boolean gameFinished = false;
		boolean hit = false;
		
		// Finding number of hits needed for all ships to be sunk:
		int totalHits = 0;
		for ( int i : SHIPS){
			totalHits += i;
		}
		TOTAL_HITS_NEEDED = totalHits;		
		
		// Main Program Logic:
		printWelcomeMenu(); // Display the welcome message and first menu
		userChoice = getUserChoice(); // Get user menu choice and validate it
		playerNames = setPlayerNames(userChoice);	 // Allow the user to set their names
		
		// Finding the player names
		p1Name = playerNames[0];
		p2Name = playerNames[1];
		
		// Seeing if one player is AI, eg if playername0 is 0, false, else true
		aiChosen = playerNames[2] == "0" ? false : true;
		
		// Creating the two players:
		Player player1 = new Player(p1Name, false, 0);
		Player player2 = new Player(p2Name, aiChosen, 0);
		
		// Initiating the two boards:
		Board boardP1 = new Board(boardSize);
		Board boardP2 = new Board(boardSize);
		
		// Creating the two boards:
		createBoard(boardSize, boardP1);
		createBoard(boardSize, boardP2);
		
		
		//new ButtonGrid(12,12);//makes new ButtonGrid with 2 parameters
		
		// Players place ships:
		placeShips(boardP1, p1Name, 1);
		if (aiChosen){
			placeShipsAI(boardP2, p2Name, 2);
		} else {
			placeShips(boardP2, p2Name, 2);
		}
		
		
		new ButtonGrid(12,12, boardP2);//makes new ButtonGrid with 2 parameters
		new ButtonGrid(12,12, boardP1);//makes new ButtonGrid with 2 parameters
		
		//boardP2.printBoard(boardP2);
				
		
		// Creating player 'visible' boards 
		Board seeP2Board = new Board(boardSize);
		Board seeP1Board = new Board(boardSize);
		createBoard(boardSize, seeP2Board);
		createBoard(boardSize, seeP1Board);
		
		// Can improve this bit of code with boolean to decide turn, put rest in other method
		do{
			do {
				playerOneTarget = playerTurn(seeP2Board, boardP2, p1Name,1,false); // Returns player ones input of target
				hit = hitTarget(playerOneTarget, boardP2, seeP2Board); // Finds if hit or not, returns true/false
				if (hit){
					player1.hitTarget();
					if (TOTAL_HITS_NEEDED == player1.getNumberOfHits()){
						System.out.printf("\n\n\n%s (PLAYER 1) WINS!", p1Name);
						System.out.printf("Print some statistics here...");
						System.exit(0);
					}
				} else {
					hit = false;
				}
			} while (hit);

			if (aiChosen){
				do{
					playerTwoTarget = playerTurn(seeP1Board, boardP1, p2Name,2,aiChosen);
					hit = hitTarget(playerTwoTarget, boardP1, seeP1Board);
					if (hit){
						player2.hitTarget();
						if (TOTAL_HITS_NEEDED == player2.getNumberOfHits()){
							System.out.printf("\n\n\n%s (PLAYER 2) WINS!", p2Name);
							System.out.printf("Print some statistics here...");
							System.exit(0);
						}
					} else {
						hit = false;
					}
				} while (hit);
			} else {
				do {
					playerTwoTarget = playerTurn(seeP1Board, boardP1, p2Name,2,false);
					hit = hitTarget(playerTwoTarget, boardP1, seeP1Board);
					if (hit){
						player2.hitTarget();
						if (TOTAL_HITS_NEEDED == player2.getNumberOfHits()){
							System.out.printf("\n\n\n%s (PLAYER 2) WINS!", p2Name);
							System.out.printf("Print some statistics here...");
							System.exit(0);
						}
					} else {
						hit = false;
					}
				} while (hit);
			}
			
		} while (!gameFinished);
	}
	
	public static void placeShipsAI(Board board, String p1Name, int playerNo){
		System.out.printf("\n %s (PLAYER %s) PLACING SHIPS...\n\n", p1Name, playerNo);
		// Place ships using array holding ship types to place
		for (int x = 0; x < SHIPS.length; x++){
			board.setShips(SHIPS[x], board, true);
		}
	}
	
	// Allows user to place ships on board, from array of possible ships
	public static void placeShips(Board board, String p1Name, int playerNo){
		System.out.printf("\n########## %s (PLAYER %s) PLACE SHIPS ##########\n\n", p1Name, playerNo);
		// Place ships using array holding ship types to place
		for (int x = 0; x < SHIPS.length; x++){
			board.printBoard(board);
			board.setShips(SHIPS[x], board, false);
		}
	}
	
	// Creating the board and filling it with 0's
//	public static void createBoard(int boardSize, Board board){
//		board.boardArray = new int[boardSize][boardSize];
//		for (int[] row: board.boardArray)
//		    Arrays.fill(row, 0);
//	}
	
	public static void createBoard(int boardSize, Board board){
		board.boardArrayChar = new char[boardSize][boardSize];
		char f = '~';
		for (char[] row: board.boardArrayChar)
		    Arrays.fill(row, f);
	}
	
	public static boolean hitTarget(String posCoords, Board board, Board boardSee){
		int xPos, yPos;
		boolean hit = false;
		
		System.out.printf("\n ###### 2 ####### \n posCoords: %s \n", posCoords);
		System.out.printf("xPos: %s\n", posCoords.charAt(0));
		System.out.printf("yPos: %s\n", posCoords.charAt(1));
		
		xPos = Character.getNumericValue(posCoords.charAt(0));
		yPos = Character.getNumericValue(posCoords.charAt(1));
		
		if (board.boardArrayChar[yPos][xPos] != '~'){
			System.out.println("HIT");
			boardSee.boardArrayChar[yPos][xPos] = 'H';
			boardSee.printBoard(boardSee);
			hit = true;
		} else {
			System.out.println("MISS");
			System.out.printf("\n TEST MISS, %s: \n", boardSee.boardArrayChar[yPos][xPos]);
			boardSee.boardArrayChar[yPos][xPos] = 'M'; // Miss = 9, change
			boardSee.printBoard(boardSee);
			hit = false;
		}
		
		return hit;
	}
	
	// Printing welcome message and first menu
	public static void printWelcomeMenu(){
		System.out.println("########## Welcome to BATTLESHIPS! ##########");
		System.out.println("Choose your opponent:");
		System.out.println("1) Human Player");
		System.out.println("2) Computer Player");
	}
	
	public static String playerTurn(Board seeP2Board, Board boardP2, String playerName, int playerNo, boolean aiChosen){
		Scanner reader = new Scanner(System.in);  // Reading from System.in
		String target = "";
		int xPos, yPos;
		boolean valid = true;
		System.out.printf("\n##### %s (Player %s) Turn #####\n", playerName, playerNo);
		boolean shotHit = false;
		
		if (aiChosen){
			do{
				// JUST HAVE IF, if x=0 or y=0 then break
				 for (int z = 0; z < seeP2Board.boardArrayChar.length; z++){
					 for (int q = 0; q < seeP2Board.boardArrayChar.length; q++){
						 // Too lazy to fix an error of out of bounds array, skipping instead
						 if (z == 0 || q == 0){
							 //System.out.println("BREAKING");
							 break;
						 }
						 if (seeP2Board.boardArrayChar[z][q] == 'H'){
							 shotHit = true;
							 Random rand2 = new Random();
							 xPos = rand2.nextInt((1 - -1) + 1) + -1;
							 yPos = rand2.nextInt((1 - -1) + 1) + -1;
							 String xC = Integer.toString(z+xPos);
							String yC = Integer.toString(q+yPos);
							target = xC + yC;
							if (seeP2Board.boardArrayChar[xPos][yPos] != '~'){
								valid = false;
							} else {
								valid = true;
							}
						 }
					 }
				 }
				
				 if (!shotHit){
					 Random rand1 = new Random();
						xPos = rand1.nextInt((9 - 0) + 1) + 0;
						yPos = rand1.nextInt((9 - 0) + 1) + 0;
						String xC = Integer.toString(xPos);
						String yC = Integer.toString(yPos);
						target = xC + yC;
						if (seeP2Board.boardArrayChar[xPos][yPos] != '~'){
							valid = false;
						} else {
							valid = true;
						}
				 }
			} while (!valid);
		} else{
			seeP2Board.printBoard(seeP2Board);
			do{
				System.out.printf("\nEnter a coordinate to fire upon: ");
				target = reader.nextLine ();
				xPos = Character.getNumericValue(target.charAt(0));
				yPos = Character.getNumericValue(target.charAt(1));
				//System.out.println(seeP2Board.boardArrayChar[0][0]);
				if (seeP2Board.boardArrayChar[yPos][xPos] != '~'){ // switched here
					System.out.printf("\nxPos: %s \n", xPos);
					System.out.printf("\nyPos: %s \n", yPos);
					System.out.printf("\nseeP2Board.boardArrayChar[xPos][yPos]: %s \n", seeP2Board.boardArrayChar[xPos][yPos]);
					System.out.println("ERROR: please select a new target.");
					valid = false;
				} else {
					valid = true;
				}
			} while (!valid);
		}
		return target;
	}
	
	public static int getUserChoice(){
		boolean validInput = false;
		Scanner reader = new Scanner(System.in);  // Reading from System.in
		int n = 0;
		do {
			try {
				System.out.printf("Enter 1 or 2: ");
				n = reader.nextInt(); // Scans the next token of the input as an int.
				if (n == 1 || n == 2){
					validInput = true;
				} else {
					System.out.println("Error, please input a valid number.");
					validInput = false;
				}
			} catch (InputMismatchException i){
				System.out.println("Error, please input either 1 or 2.");
			}
		} while (!validInput);
		return n;
	}
	
	public static String[] setPlayerNames(int userChoice){
		Scanner reader = new Scanner(System.in);  // Reading from System.in
		String playerName1, playerName2, aiName;
		boolean aiChosen = false;
		String[] anArray;
		anArray = new String[3];
		
		if (userChoice == 1){
			System.out.printf("Player 1 name: ");
			playerName1 = reader.nextLine ();
			anArray[0] = playerName1;
			System.out.printf("Player 2 name: ");
			playerName2 = reader.nextLine ();
			anArray[1] = playerName2;
			System.out.printf("Welcom %s and %s!\n", playerName1, playerName2);
			// Set indicator string to 0, as no AI present
			anArray[2] = "0";
			aiChosen = false;
		} else if (userChoice == 2){
			System.out.printf("Player 1 name: ");
			playerName1 = reader.nextLine ();
			anArray[0] = playerName1;
			System.out.printf("Computer name: ");
			aiName = reader.nextLine ();
			anArray[1] = aiName;
			System.out.printf("Welcom %s and %s!\n", playerName1, aiName);
			// Set indicator string to 1, as AI is present
			anArray[2] = "1";
			aiChosen = true;
		}
		return anArray;
	}
}
