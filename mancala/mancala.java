import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;



public class mancala {
	
	public static PrintWriter pWriter;
	public static int cutOffDepth;
	public static int myPlayer;
	public static int count; // Delete this.Only for debugging.
	public static Board nextState;
	public static float nextStateValue = Float.NEGATIVE_INFINITY;
	
	public Board generateNextMoveGreedy(Board b , int player){
		if (player == 1){
			Board maxUtilityBoard = b.getPlayer1MoveUtility();
			return maxUtilityBoard;
		}
		else{
			Board maxUtilityBoard = b.getPlayer2MoveUtility();
			return maxUtilityBoard;
		}		
	}
	
	public void generateTraverseLogMinMax(Board b , int player ,String outputFileName , String nextMoveFileName) throws FileNotFoundException{
		PrintWriter writer = new PrintWriter(outputFileName);
		String output = "Node,Depth,Value";		
		mancala.pWriter = writer;
		mancala.pWriter.println(output);
		b.parentNodeName = "root";		
		b.value = Float.NEGATIVE_INFINITY;
		b.depth = 0;
		mancala.nextState = b;
		b.miniMaxDecision(player);
		writer.close();
		printNextMove(mancala.nextState, nextMoveFileName);
	}
	
	public void generateTraverseLogAlphaBeta(Board b , int player ,String outputFileName ,String nextMoveFileName) throws FileNotFoundException{
		PrintWriter writer = new PrintWriter(outputFileName);
		String output = "Node,Depth,Value,Alpha,Beta";		
		mancala.pWriter = writer;
		mancala.pWriter.println(output);
		b.parentNodeName = "root";		
		b.value = Float.NEGATIVE_INFINITY;
		b.depth = 0;
		b.alpha = Float.NEGATIVE_INFINITY;
		b.beta = Float.POSITIVE_INFINITY;
		b.alphaBetaSearch(player);
		writer.close();
		printNextMove(mancala.nextState, nextMoveFileName);
	}	
	
	public void printNextMove(Board b , String outputFileName) throws FileNotFoundException{
		PrintWriter writer = new PrintWriter(outputFileName);
		int length = b.A.length;
		String output = "";
		for(int i =1;i < length; i++ ){
			output += b.A[i] + " ";
		}
		writer.println(output);		
		output = "";
		for(int i =0;i < length-1 ; i++ ){
			output += b.B[i] + " ";
		}
		writer.println(output);
		writer.println( b.A[0]);
		writer.println(b.B[length-1]);
		writer.close();
	}
	
	public static void main(String args[]) throws FileNotFoundException {
		//String inputFileName = "F:\\workspace\\java\\AIAssignment1\\manacalaInputs\\input_16.txt";
		//String outputFileName = "F:\\workspace\\java\\AIAssignment1\\manacalaInputs\\output_16.txt";
		
		String inputFileName = "F:\\workspace\\java\\AIAssignment1\\mancalamin_max\\input_14.txt";
		String outputFileName = "F:\\workspace\\java\\AIAssignment1\\mancalamin_max\\traverse_log_14.txt";
		String nextMoveFile = "F:\\workspace\\java\\AIAssignment1\\mancalamin_max\\next_move_14.txt";
		
		/*String inputFileName = "F:\\workspace\\java\\AIAssignment1\\alphabeta\\input_5.txt";
		String outputFileName = "F:\\workspace\\java\\AIAssignment1\\alphabeta\\traverse_log_5.txt";
		String nextMoveFile = "F:\\workspace\\java\\AIAssignment1\\alphabeta\\next_move_5.txt";*/
		
		/*String inputFileName = args[1];
		String outputFileName = "traverse_log.txt";
		String nextMoveFile = "next_state.txt";*/
		
		Scanner sc = new Scanner(new File(inputFileName));
		
		MancalaTestCase t = new MancalaTestCase();
		while(sc.hasNextLine()){	
			
			t.task = Integer.parseInt(sc.nextLine());
			t.myPlayer = Integer.parseInt(sc.nextLine());
			t.cutoffDepth = Integer.parseInt(sc.nextLine());
			t.setBoardTwoValues(sc.nextLine());
			t.setBoardOneValues(sc.nextLine());
			t.countMancalaTwo = Integer.parseInt(sc.nextLine());
			t.countMancalaOne = Integer.parseInt(sc.nextLine());
		}		
		sc.close();
		if(t.task == 1){
			Board b = new Board(t.boardOneValues , t.boardTwoValues , t.countMancalaOne , t.countMancalaTwo);
			mancala m = new mancala();
			Board nextMove = m.generateNextMoveGreedy(b, t.myPlayer);
			m.printNextMove(nextMove , nextMoveFile);
		}
		else if(t.task == 2){
			Board b = new Board(t.boardOneValues , t.boardTwoValues , t.countMancalaOne , t.countMancalaTwo);
			mancala m = new mancala();
			mancala.cutOffDepth = t.cutoffDepth;
			mancala.myPlayer = t.myPlayer;
			m.generateTraverseLogMinMax(b, t.myPlayer , outputFileName , nextMoveFile);
		}
		else if(t.task == 3){
			Board b = new Board(t.boardOneValues , t.boardTwoValues , t.countMancalaOne , t.countMancalaTwo);
			mancala m = new mancala();
			mancala.cutOffDepth = t.cutoffDepth;
			mancala.myPlayer = t.myPlayer;
			m.generateTraverseLogAlphaBeta(b, t.myPlayer , outputFileName,nextMoveFile);
		}
		
	}

}

class MancalaTestCase {
	
	public int task ; // Greedy -1 Minimax -2 Alpha Beta 3 Competition 4	
	public int myPlayer;
	public int cutoffDepth;
	String[] boardOneValues;
	String[] boardTwoValues;
	public int countMancalaOne;
	public int countMancalaTwo;
	
	public void setBoardOneValues(String input){
		boardOneValues = input.split(" ");
	}
	
	public void setBoardTwoValues(String input){
		boardTwoValues = input.split(" ");
	}
}

class Board implements Serializable{	
	private static final long serialVersionUID = 1L;
	//A[0] is Player 2's Mancala . B[length-1] is the player 1's Mancala.
	
	public int[] A; // Player 2 pits
	public int[] B;	// Player 1 pits
	
	//for minimax
	
	float value;
	int depth = -1;
	String parentNodeName;
	float alpha;
	float beta;
	
	ArrayList<Board> boardMoves = new ArrayList<Board>();
	
	public Board(String[] board1 , String[] board2 , int count1, int count2){
		
		int numPits = board1.length;
		int length = numPits +1;
		A = new int[length];
		B = new int[length];
		
		A[0] = count2; // Initialize mancala for player 2
		B[length-1] =  count1; // Initialize mancala for player 1
		
		for(int i=0;i< length-1;i++ ){			
			int val = Integer.parseInt(board1[i]);
			B[i] = val;
		}
		
		for(int i=1;i<= length-1;i++ ){			
			int val = Integer.parseInt(board2[i-1]);
			A[i] = val;
		}		
		
	}	
	
	public int evalMoveUtility(){
		int boardLength = A.length-1;
		return B[boardLength] - A[0];
	}
	
	public int evalMoveUtility2(){
		int boardLength = A.length-1;
		return  A[0] - B[boardLength];
	}
	
	public Board getPlayer1MoveUtility(){
		for(int i=0;i< A.length-1;i++){
			getMoveUtility(B[i] , i);				
		}
		return getMaxUtilityBoard();
	}
	
	public Board getPlayer2MoveUtility(){
		for(int i=1;i< A.length;i++){
			getMoveUtility2(A[i] , i);				
		}
		return getMaxUtilityBoard2();
	}
	
	public void getMoveUtility(int nStones , int index){
		//printBoard();
		int boardLength = A.length-1;
		int numStones = nStones;
		
		Board clonedBoard = getClonedObject();
		clonedBoard.boardMoves.clear();		
		Board maxUtilityBoard = null;
		
		if(numStones < (boardLength - index) ){
			if(numStones !=0){
				clonedBoard.updateBoardAfterPlayer1Move(clonedBoard.B[index], index);
				boardMoves.add(clonedBoard);
			}
			//clonedBoard.printBoard();
			return;
		}
		if(nextMoveGenerated(nStones, index)){
			clonedBoard.updateBoardAfterPlayer1Move(clonedBoard.B[index], index);
			//clonedBoard.printBoard();
			maxUtilityBoard = clonedBoard.getPlayer1MoveUtility();
			boardMoves.add(maxUtilityBoard);
		}
		else{
			clonedBoard.updateBoardAfterPlayer1Move(clonedBoard.B[index], index);
			//clonedBoard.printBoard();
			boardMoves.add(clonedBoard);
		}
		
	}
	
	public void getMoveUtility2(int nStones , int index){
		//printBoard();
		int numStones = nStones;
		
		Board clonedBoard = getClonedObject();
		clonedBoard.boardMoves.clear();		
		Board maxUtilityBoard = null;
		
		if(numStones < index ){
			if(numStones !=0){
				clonedBoard.updateBoardAfterPlayer2Move(clonedBoard.A[index], index);
				boardMoves.add(clonedBoard);
			}
			//clonedBoard.printBoard();
			return;
		}
		if(nextMoveGenerated2(nStones, index)){
			clonedBoard.updateBoardAfterPlayer2Move(clonedBoard.A[index], index);
			maxUtilityBoard = clonedBoard.getPlayer2MoveUtility();
			boardMoves.add(maxUtilityBoard);
		}
		else{
			clonedBoard.updateBoardAfterPlayer2Move(clonedBoard.A[index], index);
			boardMoves.add(clonedBoard);
		}
		
	}	
		
	public Board getMaxUtilityBoard(){
		Board maxUtilityBoard = null;
		int maxUtilityValue = (int) Float.NEGATIVE_INFINITY;
		Iterator<Board> it =  boardMoves.iterator();
		while(it.hasNext()){
			Board b = it.next();
			int value = b.evalMoveUtility();
			if(  value > maxUtilityValue ){
				maxUtilityBoard = b;
				maxUtilityValue = value;
			}			
		}	
		
		return maxUtilityBoard;
	}
	
	public Board getMaxUtilityBoard2(){
		Board maxUtilityBoard = null;
		int maxUtilityValue = (int) Float.NEGATIVE_INFINITY;
		Iterator<Board> it =  boardMoves.iterator();
		while(it.hasNext()){
			Board b = it.next();
			int value = b.evalMoveUtility2();
			if(  value > maxUtilityValue ){
				maxUtilityBoard = b;
				maxUtilityValue = value;
			}			
		}	
		
		return maxUtilityBoard;
	}	
	
	public void updateBoardAfterPlayer1Move(int nStones , int index){
		//printBoard();
		int numOfStones = nStones;
		B[index] = 0;
		for(int i = index+1;i < A.length;i++){
			B[i] ++;
			numOfStones --;
			if(numOfStones == 0){
				// Below condition takes care of the last stone going into an empty pit.
				/*if(B[i] == 1 && i < A.length-1){
					B[A.length -1] = B[A.length-1]+ B[i] + A[i+1];
					B[i] = 0;
					A[i+1] = 0;
				}*/
				executeEmptyPitCondition1(i);
				executeGameEndCondition();
				return;
			}
		}
		
		while(numOfStones != 0){
			for(int i = A.length-1;i >= 1 ;i--){
				if(numOfStones == 0){
					executeGameEndCondition();
					return;
				}
				A[i] ++;
				numOfStones --;
			}
			for(int i = 0;i <= A.length-1;i++){
				if(numOfStones == 0){
					executeGameEndCondition();
					return;
				}
				B[i] ++;
				numOfStones --;
				if(numOfStones == 0){
					executeEmptyPitCondition1(i);
					executeGameEndCondition();
					return;
				}
			}
		}		
		executeGameEndCondition();		
	}
	
	public void executeEmptyPitCondition1(int i){
		if(B[i] == 1 && i < A.length-1){
			B[A.length -1] = B[A.length-1]+ B[i] + A[i+1];
			B[i] = 0;
			A[i+1] = 0;
		}
		//return true;
	}
	
	public void updateBoardAfterPlayer2Move(int nStones , int index){
		//printBoard();
		int numOfStones = nStones;
		A[index] = 0;
		for(int i = index-1;i >= 0;i--){
			A[i] ++;
			numOfStones --;
			if(numOfStones == 0){
				// Below condition takes care of the last stone going into an empty pit.
				/*if(A[i] == 1 && i > 0){
					A[0] = A[0]+ A[i] + B[i-1];
					A[i] = 0;
					B[i-1] = 0;
				}*/
				executeEmptyPitCondition2(i);
				executeGameEndCondition();
				return;
			}
		}
		
		while(numOfStones != 0){
			for(int i = 0;i <= A.length-2;i++){
				if(numOfStones == 0){
					executeGameEndCondition();
					return;
				}
				B[i] ++;
				numOfStones --;
			}
			
			for(int i = A.length-1;i >= 0 ;i--){
				if(numOfStones == 0){
					executeGameEndCondition();
					return;
				}
				A[i] ++;
				numOfStones --;
				if(numOfStones == 0){
					executeEmptyPitCondition2(i);
					executeGameEndCondition();
					return;
				}
			}
			
		}		
		executeGameEndCondition();
		//printBoard();
	}
	
	public void executeEmptyPitCondition2(int i){
		if(A[i] == 1 && i > 0){
			A[0] = A[0]+ A[i] + B[i-1];
			A[i] = 0;
			B[i-1] = 0;
		}
	}
	
	public void executeGameEndCondition(){
		if(checkGameEndConditionPlayer1() == true){
			int sum =0;
			for(int i =1;i< A.length;i++){
				sum = sum + A[i];
				A[i] = 0;
			}
			A[0] = A[0] + sum;			
		}
		
		if(checkGameEndConditionPlayer2() == true){
			int sum =0;
			for(int i =0;i< A.length-1;i++){
				sum = sum + B[i];
				B[i] = 0;
			}
			B[A.length-1] = B[A.length-1] + sum;			
		}	
	}
	
	public boolean checkGameEndConditionPlayer1(){
		
		int sum = 0;
		for(int i =0;i < A.length-1 ; i++){			
			sum += B[i];
		}
		if(sum == 0){
			return true;
		}
		else
			return false;
	}
	
	public boolean checkGameEndConditionPlayer2(){
		
		int sum = 0;
		for(int i =1;i < A.length ; i++){			
			sum += A[i];
		}
		if(sum == 0){
			return true;
		}
		else
			return false;
	}
	
	public boolean nextMoveGenerated(int nStones , int index){
		/*
		 * Next move is generated when the last stone in the move ends in player's pit.For this there might be two conditions.
		 * 1. If the number of stones is exactly equal to how far it is from mancala.
		 * 2. Or after putting all stones in various rounds in both boards the last stone ends in the player's mancala.
		 */		
		int numStones = nStones;
		int distanceFromMancala = (A.length-1) - index;
		if ( numStones == distanceFromMancala || ((numStones - distanceFromMancala) % (2* (A.length) -1)) == 0 )
			return true;		
		
		return false;
	}
	
	public boolean nextMoveGenerated2(int nStones , int index){		
		int numStones = nStones;
		int distanceFromMancala = index;
		
		if ( numStones == distanceFromMancala || ((numStones - distanceFromMancala) % (2* (A.length) - 1)) == 0 )
			return true;		
		
		return false;
	}
	
	/*
	 * Min-Max Functions starting
	 */
	public void miniMaxDecision(int player){
		printTraverseLog();
		maxValue(player,false);
	}		
	
	public void minValue(int player, boolean sameLayer){
		/*
		 * Creating a clone outside so that depth does not get updated multiple times inside
		 * the for loop for a particular level.
		 */
		Board mainClonedBoard = getClonedObject();
		if(!sameLayer){
			mainClonedBoard.depth = depth +1;
		}	
		if(player == 1){			
			int opponent = 2;
			for(int i=0;i< A.length-1;i++){
				if(B[i] == 0){
					continue;
				}
				Board clonedBoard = mainClonedBoard.getClonedObject();
				if(clonedBoard.isTerminalNode(B[i], i)){
					clonedBoard.parentNodeName= "B" + (i+2);
					if(clonedBoard.isGameEndBoardPosition1(B[i], i) && clonedBoard.depth != mancala.cutOffDepth){
						clonedBoard.value = Float.NEGATIVE_INFINITY;
						clonedBoard.printTraverseLog();						
						clonedBoard.value = Float.POSITIVE_INFINITY;
					}
					clonedBoard.updateBoardAfterPlayer1Move(B[i], i);
					//clonedBoard.parentNodeName= "B" + (i+2);
					clonedBoard.value = clonedBoard.evalMoveUtility2();// Check the move utility function used here. i.e. it should be for 1 or 2.
					clonedBoard.printTraverseLog();					
					if(value > clonedBoard.value){
						value = clonedBoard.value;
					}					
					printTraverseLog();
				}
				else{
					if(Double.isInfinite(clonedBoard.value)){
						if(clonedBoard.nextMoveGenerated(B[i], i)){
							clonedBoard.value = Float.POSITIVE_INFINITY;
						}
						else{
							clonedBoard.value = Float.NEGATIVE_INFINITY;
						}
					}
					clonedBoard.parentNodeName= "B" + (i+2);
					clonedBoard.printTraverseLog();
					
					if(clonedBoard.nextMoveGenerated(B[i], i)){
						clonedBoard.updateBoardAfterPlayer1Move(B[i], i);
						clonedBoard.minValue(player , true);
						if(value > clonedBoard.value){
							value = clonedBoard.value;
						}
						printTraverseLog();
					}
					else{
						clonedBoard.updateBoardAfterPlayer1Move(B[i], i);
						clonedBoard.maxValue(opponent, false);
						if(Double.isInfinite(value) || value > clonedBoard.value){
							value = clonedBoard.value;
						}
						printTraverseLog();
					}
				}
			}			
		}
		else{
			int opponent = 1;
			for(int i=1;i< A.length;i++){
				if(A[i] == 0){
					continue;
				}
				Board clonedBoard = mainClonedBoard.getClonedObject();
				if(clonedBoard.isTerminalNode2(A[i], i)){
					clonedBoard.parentNodeName= "A" + (i+1);
					if(clonedBoard.isGameEndBoardPosition2(A[i], i) && clonedBoard.depth != mancala.cutOffDepth){
						clonedBoard.value = Float.NEGATIVE_INFINITY;
						clonedBoard.printTraverseLog();						
						clonedBoard.value = Float.POSITIVE_INFINITY;
					}
					clonedBoard.updateBoardAfterPlayer2Move(A[i], i);
					//clonedBoard.parentNodeName= "A" + (i+1);
					clonedBoard.value = clonedBoard.evalMoveUtility();// Check the move utility function used here.		
					clonedBoard.printTraverseLog();
					if(value > clonedBoard.value){
						value = clonedBoard.value;
					}
					printTraverseLog();
				}
				else{					
					 if(Double.isInfinite(clonedBoard.value)){
						if(clonedBoard.nextMoveGenerated2(A[i], i)){
							clonedBoard.value = Float.POSITIVE_INFINITY;
						}
						else{
							clonedBoard.value = Float.NEGATIVE_INFINITY;
						}
					}
					clonedBoard.parentNodeName= "A" + (i+1);
					clonedBoard.printTraverseLog();
					
					if(clonedBoard.nextMoveGenerated2(A[i], i)){
						clonedBoard.updateBoardAfterPlayer2Move(A[i], i);
						clonedBoard.minValue(player , true);
						if(value > clonedBoard.value){
							value = clonedBoard.value;
						}
						printTraverseLog();
					}
					else{
						clonedBoard.updateBoardAfterPlayer2Move(A[i], i);
						clonedBoard.maxValue(opponent , false);
						if(Double.isInfinite(value) || value > clonedBoard.value){
							value = clonedBoard.value;
						}
						printTraverseLog();
					}
				}				
			}			
		}		
	}	
	
	public void maxValue(int player, boolean sameLayer){
		/*
		 * Creating a clone outside so that depth does not get updated multiple times inside
		 * the for loop for a particular level.
		 */
		Board mainClonedBoard = getClonedObject();
		if(!sameLayer){
			mainClonedBoard.depth = depth +1;
		}	
		if(player == 1){			
			int opponent = 2;
			for(int i=0;i< A.length-1;i++){
				Board clonedBoard = mainClonedBoard.getClonedObject();
				if(B[i] == 0){
					continue;
				}				
				if(clonedBoard.isTerminalNode(B[i], i)){
					clonedBoard.parentNodeName= "B" + (i+2);
					if(clonedBoard.isGameEndBoardPosition1(B[i], i) && clonedBoard.depth != mancala.cutOffDepth){
						clonedBoard.value = Float.POSITIVE_INFINITY;
						clonedBoard.printTraverseLog();
						clonedBoard.value = Float.NEGATIVE_INFINITY;
					}
					clonedBoard.updateBoardAfterPlayer1Move(B[i], i);
					//clonedBoard.parentNodeName= "B" + (i+2);
					clonedBoard.value = clonedBoard.evalMoveUtility();// Check the move utility function used here.					
					clonedBoard.printTraverseLog();
					if(value < clonedBoard.value){
						value = clonedBoard.value;
					}
					printTraverseLog();
				}				
				else{					
					if(Double.isInfinite(clonedBoard.value)){
						if(clonedBoard.nextMoveGenerated(B[i], i)){
							clonedBoard.value = Float.NEGATIVE_INFINITY;
						}
						else{
							clonedBoard.value = Float.POSITIVE_INFINITY;
						}
					}
					
					clonedBoard.parentNodeName= "B" + (i+2);
					clonedBoard.printTraverseLog();
					
					if(clonedBoard.nextMoveGenerated(B[i], i)){
						clonedBoard.updateBoardAfterPlayer1Move(B[i], i);
						clonedBoard.maxValue(player , true);
						if(value < clonedBoard.value){
							value = clonedBoard.value;							
						}
						printTraverseLog();
					}
					else{
						clonedBoard.updateBoardAfterPlayer1Move(B[i], i);
						clonedBoard.minValue(opponent , false);
						if(Double.isInfinite(value) || value < clonedBoard.value){
							value = clonedBoard.value;
						}
						printTraverseLog();
					}
				}
				if(clonedBoard.depth == 1){
					if(clonedBoard.value > mancala.nextStateValue){
						mancala.nextState = clonedBoard;
						mancala.nextStateValue = clonedBoard.value;
					}
				}
				
			}
			
		}
		else{
			int opponent = 1;
			for(int i=1;i< A.length;i++){
				if(A[i] == 0){
					continue;
				}
				Board clonedBoard = mainClonedBoard.getClonedObject();
				if(clonedBoard.isTerminalNode2(A[i], i)){
					clonedBoard.parentNodeName= "A" + (i+1);
					if(clonedBoard.isGameEndBoardPosition2(A[i], i) && clonedBoard.depth != mancala.cutOffDepth){
						clonedBoard.value = Float.POSITIVE_INFINITY;
						clonedBoard.printTraverseLog();
						clonedBoard.value = Float.NEGATIVE_INFINITY;
					}
					clonedBoard.updateBoardAfterPlayer2Move(A[i], i);
					//clonedBoard.parentNodeName= "A" + (i+1);					
					clonedBoard.value = clonedBoard.evalMoveUtility2();// Check the move utility function used here.					
					clonedBoard.printTraverseLog();
					if(value < clonedBoard.value){
						value = clonedBoard.value;
					}
					printTraverseLog();
				}
				else{
					if(Double.isInfinite(clonedBoard.value)){
						if(clonedBoard.nextMoveGenerated2(A[i], i)){
							clonedBoard.value = Float.NEGATIVE_INFINITY;
						}
						else{
							clonedBoard.value = Float.POSITIVE_INFINITY;
						}
					}
					
					clonedBoard.parentNodeName= "A" + (i+1);
					clonedBoard.printTraverseLog();
					
					if(clonedBoard.nextMoveGenerated2(A[i], i)){
						clonedBoard.updateBoardAfterPlayer2Move(A[i], i);
						clonedBoard.maxValue(player , true);
						if(value < clonedBoard.value){
							value = clonedBoard.value;							
						}
						printTraverseLog();
					}
					else{
						clonedBoard.updateBoardAfterPlayer2Move(A[i], i);
						clonedBoard.minValue(opponent , false);						
						
						if(Double.isInfinite(value) || value < clonedBoard.value){
							value = clonedBoard.value;
						}
						printTraverseLog();
					}
				}
				if(clonedBoard.depth == 1){
					if(clonedBoard.value > mancala.nextStateValue){
						mancala.nextState = clonedBoard;
						mancala.nextStateValue = clonedBoard.value;
					}
				}
				
			}			
		}		
	}
	
	
	public int getMinMaxMoveUtility(){
		int boardLength = A.length-1;
		if(mancala.myPlayer == 1){
			return B[boardLength] - A[0];
		}
		else{
			return  A[0] - B[boardLength];
		}
	}
	
	public boolean isTerminalNode(int nStones , int index){
		
		if ((depth == mancala.cutOffDepth && !nextMoveGenerated(nStones, index)) || isGameEndBoardPosition1(nStones , index)){
			return true;
		}
		return false;
	}
	
	public boolean isTerminalNode2(int nStones , int index){
		
		if ((depth == mancala.cutOffDepth && !nextMoveGenerated2(nStones, index)) || isGameEndBoardPosition2(nStones , index)){
			return true;
		}
		return false;
	}
	
	public boolean isGameEndBoardPosition1(int nStones , int index){
		Board tempClonedBoard = getClonedObject();
		tempClonedBoard.updateBoardAfterPlayer1Move(B[index], index);
		
		if(tempClonedBoard.checkGameEndConditionPlayer1()){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean isGameEndBoardPosition2(int nStones , int index){
		Board tempClonedBoard = getClonedObject();
		tempClonedBoard.updateBoardAfterPlayer2Move(A[index], index);
		
		if(tempClonedBoard.checkGameEndConditionPlayer2()){
			return true;
		}
		else{
			return false;
		}
	}
	
	/****************************************Alpha Beta Pruning Starts here***************************************/
	
	public void alphaBetaSearch(int player){
		printTraverseLogAlphaBeta();
		maxValueAlphaBeta(player,false);
	}
	
	
	public void minValueAlphaBeta(int player, boolean sameLayer){
		/*
		 * Creating a clone outside so that depth does not get updated multiple times inside
		 * the for loop for a particular level.
		 */
		Board mainClonedBoard = getClonedObject();
		if(!sameLayer){
			mainClonedBoard.depth = depth +1;
		}	
		if(player == 1){			
			int opponent = 2;
			for(int i=0;i< A.length-1;i++){
				if(B[i] == 0){
					continue;
				}
				Board clonedBoard = mainClonedBoard.getClonedObject();
				if(clonedBoard.isTerminalNode(B[i], i)){
					clonedBoard.parentNodeName= "B" + (i+2);
					if(clonedBoard.isGameEndBoardPosition1(B[i], i) && clonedBoard.depth != mancala.cutOffDepth){
						clonedBoard.value = Float.NEGATIVE_INFINITY;
						clonedBoard.printTraverseLogAlphaBeta();						
						clonedBoard.value = Float.POSITIVE_INFINITY;
					}
					clonedBoard.updateBoardAfterPlayer1Move(B[i], i);
					clonedBoard.value = clonedBoard.evalMoveUtility2();// Check the move utility function used here. i.e. it should be for 1 or 2.
					clonedBoard.printTraverseLogAlphaBeta();					
					if(value > clonedBoard.value){
						value = clonedBoard.value;
					}
					if(value <= alpha){
						printTraverseLogAlphaBeta();
						return;
					}
					
					beta = Math.min(beta, value);
					mainClonedBoard.beta = beta;					
					printTraverseLogAlphaBeta();
				}
				else{
					if(Double.isInfinite(clonedBoard.value)){
						if(clonedBoard.nextMoveGenerated(B[i], i)){
							clonedBoard.value = Float.POSITIVE_INFINITY;
						}
						else{
							clonedBoard.value = Float.NEGATIVE_INFINITY;
						}
					}
					clonedBoard.parentNodeName= "B" + (i+2);
					clonedBoard.printTraverseLogAlphaBeta();
					
					if(clonedBoard.nextMoveGenerated(B[i], i)){
						clonedBoard.updateBoardAfterPlayer1Move(B[i], i);
						clonedBoard.minValueAlphaBeta(player , true);
						if(value > clonedBoard.value){
							value = clonedBoard.value;
						}
						if(value <= alpha){
							printTraverseLogAlphaBeta();
							return;
						}
						beta = Math.min(beta, value);
						mainClonedBoard.beta = beta;
						printTraverseLogAlphaBeta();
					}
					else{
						clonedBoard.updateBoardAfterPlayer1Move(B[i], i);
						clonedBoard.maxValueAlphaBeta(opponent, false);
						if(Double.isInfinite(value) || value > clonedBoard.value){
							value = clonedBoard.value;
						}
						if(value <= alpha){
							printTraverseLogAlphaBeta();
							return;
						}
						beta = Math.min(beta, value);
						mainClonedBoard.beta = beta;
						printTraverseLogAlphaBeta();
					}
				}
			}			
		}
		else{
			int opponent = 1;
			for(int i=1;i< A.length;i++){
				if(A[i] == 0){
					continue;
				}
				Board clonedBoard = mainClonedBoard.getClonedObject();
				if(clonedBoard.isTerminalNode2(A[i], i)){
					clonedBoard.parentNodeName= "A" + (i+1);
					if(clonedBoard.isGameEndBoardPosition2(A[i], i) && clonedBoard.depth != mancala.cutOffDepth){
						clonedBoard.value = Float.NEGATIVE_INFINITY;
						clonedBoard.printTraverseLogAlphaBeta();						
						clonedBoard.value = Float.POSITIVE_INFINITY;
					}
					clonedBoard.updateBoardAfterPlayer2Move(A[i], i);
					//clonedBoard.parentNodeName= "A" + (i+1);
					clonedBoard.value = clonedBoard.evalMoveUtility();// Check the move utility function used here.		
					clonedBoard.printTraverseLogAlphaBeta();
					if(value > clonedBoard.value){
						value = clonedBoard.value;
					}
					if(value <= alpha){
						printTraverseLogAlphaBeta();
						return;
					}
					beta = Math.min(beta, value);
					mainClonedBoard.beta = beta;
					printTraverseLogAlphaBeta();
				}
				else{					
					 if(Double.isInfinite(clonedBoard.value)){
						if(clonedBoard.nextMoveGenerated2(A[i], i)){
							clonedBoard.value = Float.POSITIVE_INFINITY;
						}
						else{
							clonedBoard.value = Float.NEGATIVE_INFINITY;
						}
					}
					clonedBoard.parentNodeName= "A" + (i+1);
					clonedBoard.printTraverseLogAlphaBeta();
					
					if(clonedBoard.nextMoveGenerated2(A[i], i)){
						clonedBoard.updateBoardAfterPlayer2Move(A[i], i);
						clonedBoard.minValueAlphaBeta(player , true);
						if(value > clonedBoard.value){
							value = clonedBoard.value;
						}
						if(value <= alpha){
							printTraverseLogAlphaBeta();
							return;
						}
						beta = Math.min(beta, value);
						mainClonedBoard.beta = beta;
						printTraverseLogAlphaBeta();
					}
					else{
						clonedBoard.updateBoardAfterPlayer2Move(A[i], i);
						clonedBoard.maxValueAlphaBeta(opponent , false);
						if(Double.isInfinite(value) || value > clonedBoard.value){
							value = clonedBoard.value;
						}
						if(value <= alpha){
							printTraverseLogAlphaBeta();
							return;
						}
						beta = Math.min(beta, value);
						mainClonedBoard.beta = beta;
						printTraverseLogAlphaBeta();
					}
				}				
			}			
		}		
	}	
	
	public void maxValueAlphaBeta(int player, boolean sameLayer){
		/*
		 * Creating a clone outside so that depth does not get updated multiple times inside
		 * the for loop for a particular level.
		 */
		Board mainClonedBoard = getClonedObject();
		if(!sameLayer){
			mainClonedBoard.depth = depth +1;
		}	
		if(player == 1){			
			int opponent = 2;
			for(int i=0;i< A.length-1;i++){
				Board clonedBoard = mainClonedBoard.getClonedObject();
				if(B[i] == 0){
					continue;
				}				
				if(clonedBoard.isTerminalNode(B[i], i)){
					clonedBoard.parentNodeName= "B" + (i+2);
					if(clonedBoard.isGameEndBoardPosition1(B[i], i) && clonedBoard.depth != mancala.cutOffDepth){
						clonedBoard.value = Float.POSITIVE_INFINITY;
						clonedBoard.printTraverseLogAlphaBeta();
						clonedBoard.value = Float.NEGATIVE_INFINITY;
					}
					clonedBoard.updateBoardAfterPlayer1Move(B[i], i);
					//clonedBoard.parentNodeName= "B" + (i+2);
					clonedBoard.value = clonedBoard.evalMoveUtility();// Check the move utility function used here.					
					clonedBoard.printTraverseLogAlphaBeta();
					if(value < clonedBoard.value){
						value = clonedBoard.value;
					}
					if(value >= beta){
						printTraverseLogAlphaBeta();
						return;
					}
					alpha = Math.max(alpha, value);
					mainClonedBoard.alpha = alpha;
					printTraverseLogAlphaBeta();
				}				
				else{					
					if(Double.isInfinite(clonedBoard.value)){
						if(clonedBoard.nextMoveGenerated(B[i], i)){
							clonedBoard.value = Float.NEGATIVE_INFINITY;
						}
						else{
							clonedBoard.value = Float.POSITIVE_INFINITY;
						}
					}
					
					clonedBoard.parentNodeName= "B" + (i+2);
					clonedBoard.printTraverseLogAlphaBeta();
					
					if(clonedBoard.nextMoveGenerated(B[i], i)){
						clonedBoard.updateBoardAfterPlayer1Move(B[i], i);
						clonedBoard.maxValueAlphaBeta(player , true);
						if(value < clonedBoard.value){
							value = clonedBoard.value;							
						}
						if(value >= beta){
							printTraverseLogAlphaBeta();
							return;
						}
						alpha = Math.max(alpha, value);
						mainClonedBoard.alpha = alpha;
						printTraverseLogAlphaBeta();
					}
					else{
						clonedBoard.updateBoardAfterPlayer1Move(B[i], i);
						clonedBoard.minValueAlphaBeta(opponent , false);
						if(Double.isInfinite(value) || value < clonedBoard.value){
							value = clonedBoard.value;
						}
						if(value >= beta){
							printTraverseLogAlphaBeta();
							return;
						}
						alpha = Math.max(alpha, value);
						mainClonedBoard.alpha = alpha;
						printTraverseLogAlphaBeta();
					}
				}
				if(clonedBoard.depth == 1){
					if(clonedBoard.value > mancala.nextStateValue){
						mancala.nextState = clonedBoard;
						mancala.nextStateValue = clonedBoard.value;
					}
				}
			}
			
		}
		else{
			int opponent = 1;
			for(int i=1;i< A.length;i++){
				if(A[i] == 0){
					continue;
				}
				Board clonedBoard = mainClonedBoard.getClonedObject();
				if(clonedBoard.isTerminalNode2(A[i], i)){
					clonedBoard.parentNodeName= "A" + (i+1);
					if(clonedBoard.isGameEndBoardPosition2(A[i], i) && clonedBoard.depth != mancala.cutOffDepth){
						clonedBoard.value = Float.POSITIVE_INFINITY;
						clonedBoard.printTraverseLogAlphaBeta();
						clonedBoard.value = Float.NEGATIVE_INFINITY;
					}
					clonedBoard.updateBoardAfterPlayer2Move(A[i], i);
					//clonedBoard.parentNodeName= "A" + (i+1);					
					clonedBoard.value = clonedBoard.evalMoveUtility2();// Check the move utility function used here.					
					clonedBoard.printTraverseLogAlphaBeta();
					if(value < clonedBoard.value){
						value = clonedBoard.value;
					}
					if(value >= beta){
						printTraverseLogAlphaBeta();
						return;
					}
					alpha = Math.max(alpha, value);
					mainClonedBoard.alpha = alpha;
					printTraverseLogAlphaBeta();
				}
				else{
					if(Double.isInfinite(clonedBoard.value)){
						if(clonedBoard.nextMoveGenerated2(A[i], i)){
							clonedBoard.value = Float.NEGATIVE_INFINITY;
						}
						else{
							clonedBoard.value = Float.POSITIVE_INFINITY;
						}
					}
					
					clonedBoard.parentNodeName= "A" + (i+1);
					clonedBoard.printTraverseLogAlphaBeta();
					
					if(clonedBoard.nextMoveGenerated2(A[i], i)){
						clonedBoard.updateBoardAfterPlayer2Move(A[i], i);
						clonedBoard.maxValueAlphaBeta(player , true);
						if(value < clonedBoard.value){
							value = clonedBoard.value;							
						}
						if(value >= beta){
							printTraverseLogAlphaBeta();
							return;
						}
						alpha = Math.max(alpha, value);
						mainClonedBoard.alpha = alpha;
						printTraverseLogAlphaBeta();
					}
					else{
						clonedBoard.updateBoardAfterPlayer2Move(A[i], i);
						clonedBoard.minValueAlphaBeta(opponent , false);						
						
						if(Double.isInfinite(value) || value < clonedBoard.value){
							value = clonedBoard.value;
						}
						if(value >= beta){
							printTraverseLogAlphaBeta();
							return;
						}
						alpha = Math.max(alpha, value);
						mainClonedBoard.alpha = alpha;
						printTraverseLogAlphaBeta();
					}
				}
				if(clonedBoard.depth == 1){
					if(clonedBoard.value > mancala.nextStateValue){
						mancala.nextState = clonedBoard;
						mancala.nextStateValue = clonedBoard.value;
					}
				}
				
			}			
		}		
	}
	
	public Board getClonedObject() {		
		ObjectOutputStream oos = null;
	    ObjectInputStream ois = null;	    
	    
		try{
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
	        oos = new ObjectOutputStream(bos); 
	        // serialize and pass the object
	        oos.writeObject(this);   
	        oos.flush();               
	        ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray()); 
	        ois = new ObjectInputStream(bin);                  
	        // return the new object
	        return (Board) (ois.readObject());
		}
		catch (Exception e){
			return null;
		}
	}	
	
	public void printBoard(){		
		System.out.println("Player 2 Mancala : " + A[0]);
		int length = A.length;
		for(int i =1;i < A.length ; i++ ){
			System.out.print(A[i] + " ");
		}
		System.out.println();
		System.out.println("Player 1 Mancala : " + B[length-1]);		
		for(int i =0;i < A.length-1 ; i++ ){
			System.out.print(B[i] + " ");
		}
		System.out.println();
	}
	
	public void printTraverseLog(){
		/*mancala.count++;
		if(mancala.count == 1134){
			int test = 0;
		}*/
		String output = "";
		if(Float.isInfinite(value)){
			output = output + parentNodeName + "," + depth + "," + value;
		}
		else{
			output = output + parentNodeName + "," + depth + "," + (int) value;
		}
		
		//System.out.println(output);
		mancala.pWriter.println(output);		
	}
	
	public void printTraverseLogAlphaBeta(){
		/*mancala.count++;
		if(mancala.count == 12){
			int test = 0;
		}*/
		String output = "";
		
		output = output + parentNodeName + "," + depth + ",";
		
		if(Float.isInfinite(value)){
			output += value;
		}
		else{
			output += (int) value;
		}
		output += ",";
		
		if(Float.isInfinite(alpha)){
			output += alpha;
		}
		else{
			output += (int) alpha;
		}
		output += ",";
		
		if(Float.isInfinite(beta)){
			output += beta;
		}
		else{
			output += (int) beta;
		}
		
		//System.out.println(output);
		mancala.pWriter.println(output);		
	}
}