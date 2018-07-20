/* *********************************************************************
 * ********* Author¡¯s name(s): Yifan Chen
 * Course Title: Artificial Intelligence
 * Semester: Fall 2017
 * Assignment Number 3
 * Submission Date: 11/5/2017
 * Purpose: This program implement the checker game between computer and computer
 * Input: java Checkers
 * Output: The graphic interface of Checker game and whether win or not
 * Help: I worked alone.
 * ************************************************************************
 * ****** */

import java.awt.*;
import javax.swing.*;
import java.applet.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

//***********************************************************************
public class Checkers extends JFrame
{

   /*   set up an 8 by 8 checkers board with only five pieces
        legends:
                0   - empty
                1/2 = blue  single/king
                3/4 = red   single/king
   */
   private static int [][] boardPlan =
      { 
    	  {0, 0, 0, 0, 0, 0, 0, 0},
          {0, 0, 0, 0, 0, 0, 0, 0},
          {0, 0, 0, 0, 0, 0, 0, 0},
          {0, 0, 0, 0, 0, 0, 0, 0},
          {0, 0, 0, 0, 0, 0, 0, 1},
          {1, 0, 4, 0, 3, 0, 0, 0},
          {0, 0, 0, 0, 0, 0, 0, 0},
          {0, 0, 0, 0, 2, 0, 0, 0}
      };

   //*** the legend strings
   private String[] legend = {"blank", "bs", "bk", "rs", "rk" };

   //*** create the checkers board
   private GameBoard board;

   //*** the xy dimensions of each checkers cell on board
   private int cellDimension;

   //*** pause in seconds between moves
   private static int pauseDuration = 500;
   
   //*** x y change when move
   private int dirX[]={-1,-1,1,1};
   private int dirY[]={-1,1,-1,1};
   
   //** judge win statement
   private boolean judgeWin=false;

   //***********************************************************************
   Checkers()
   {
       //*** set up the initial configuration of the board
       board = new GameBoard(boardPlan);

       //*** each board cell is 70 pixels long and wide
       cellDimension = 70;

       //*** set up the frame containing the board
       getContentPane().setLayout(new GridLayout());
       setSize(boardPlan.length*cellDimension, boardPlan.length*cellDimension);
       setDefaultCloseOperation(EXIT_ON_CLOSE);
       getContentPane().add(board);

       //*** enable viewer
       setVisible(true);

       //*** place all initial pieces on board and pause a bit
       putInitialPieces();
       pause(2*pauseDuration);
   }


   //***********************************************************************
   public void pause(int milliseconds)
   {
      try
         {Thread.sleep(milliseconds);}
      catch (Exception e)
         {}
   }


   //***********************************************************************
   void putPiece(int i, int j, String piece)
   {
      //*** can do error checking here to make sure pieces are bs, bk, rs, rk
      board.drawPiece(i, j, "images/" + piece + ".jpg");
   }


   //***********************************************************************
   void putInitialPieces()
   {
      //*** use legend variables to draw one piece at a time
      for (int i=0; i<boardPlan.length; i++)
         for (int j=0; j<boardPlan.length; j++)
            if (boardPlan[i][j] != 0)
                  board.drawPiece(i, j, "images/" + legend[boardPlan[i][j]] + ".jpg");
   }


   //***********************************************************************
   boolean legalPosition(int i)
   {
      //*** can't go outside board boundaries
      return ((i>=0) && (i<boardPlan.length));
   }
   
   	//****************************************************** 
 	//*** Purpose: Judge if the piece between current state can be captured or not
 	//*** Input:  int i, int j, String type,int dir, int[][] cell
 	//*** Output: true or false
 	//******************************************************
   public boolean judgeCapture(int i, int j, String type,int dir, int[][] cell){
	   int newX,newY;
	   newX=i+dirX[dir];
	   newY=j+dirY[dir];
	   
	   if (newX>=0 && newX<8 && newY>=0 && newY<8){
		   //System.out.println("x="+cell[newX][newY]);
		   if (type=="MAX")
			   if (cell[newX][newY]==3 || cell[newX][newY]==4)
				   return true;
			   else
				   return false;
		   else
			   if (cell[newX][newY]==1 || cell[newX][newY]==2)
				   return true;
			   else
				   return false;
	   }
	   else
		   return false;
   }
   
   	//****************************************************** 
  	//*** Purpose: Go into depth to decide which next move. MAX looks for max evaluation; MIN looks for min evaluation
  	//*** Input:  int depth, String type, int[][] board, CState currentCState
  	//*** Output: int[] {bestScore, currentRow, currentCol, bestRow, bestCol, middleX, middleY};
  	//******************************************************
   public int[] minimax(int depth, String type, int[][] board, CState currentCState) {
	   // Generate possible next moves in a List of int[2] of {row, col}.
	   List<CState> nextMovesList = generateMoves(type,board);
	   
	   // mySeed is maximizing; while oppSeed is minimizing
	   int bestScore = (type == "MAX") ? Integer.MIN_VALUE : Integer.MAX_VALUE;
	   
	   int currentScore;
	   int currentRow=-1;
	   int currentCol=-1;
	   int bestRow = -1;
	   int bestCol = -1;
	   int middleX=-1,middleY=-1;
	   CState state=null;
 
	   if (depth == 0) {
		   // depth reached, evaluate score
		   state=new CState(board,type);
		   
		   bestRow=state.getNewX();
		   bestCol=state.getNewY();
		   currentRow=state.getX();
		   currentCol=state.getY();
		   middleX=state.getMiddleX();
		   middleY=state.getMiddleY();
		   bestScore = state.evalState();
	   }
	   else{
		   for (CState move : nextMovesList){
			   
			   // Try this move for the current "player"
			   if (type == "MAX") {  // mySeed (computer) is maximizing player
				   currentScore = minimax(depth - 1, "MIN", move.getStat(),move)[0];
				   if (currentScore > bestScore) {
	                  bestScore = currentScore;
	                  currentRow=move.getX();
	       		   	  currentCol=move.getY();
	                  bestRow = move.getNewX();
	                  bestCol = move.getNewY();
	                  middleX=move.getMiddleX();
	       		   	  middleY=move.getMiddleY();
				   }
			   } 
			   else {  // oppSeed is minimizing player
	               currentScore = minimax(depth - 1, "MAX", move.getStat(),move)[0];
	               if (currentScore < bestScore) {
	                  bestScore = currentScore;
	                  currentRow=move.getX();
	       		      currentCol=move.getY();
	                  bestRow = move.getNewX();
	                  bestCol = move.getNewY();
	                  middleX=move.getMiddleX();
	       		   	  middleY=move.getMiddleY();
               }
            }
         }
      }
      return new int[] {bestScore, currentRow, currentCol, bestRow, bestCol, middleX, middleY};
   }
   
   	//****************************************************** 
 	//*** Purpose: Copy board
 	//*** Input:  int[][] boardPlan
 	//*** Output: cell
 	//******************************************************
   public int[][] copyBoardPlan(int[][] boardPlan){
	   int cell[][]=new int[8][8];
	   for (int i=0;i<8;i++)
		   for (int j=0;j<8;j++)
			   cell[i][j]=boardPlan[i][j];
	   return cell;
   }
   
   	//****************************************************** 
  	//*** Purpose: Generate new cell after do capture
  	//*** Input:  int[][] cell, int piece, int x, int y, int newX, int newY
  	//*** Output: newcell
  	//******************************************************
   public int[][] generateNewCell(int[][] cell, int piece, int x, int y, int newX, int newY){
	   int newCell[][]=new int[8][8];
	   newCell=copyBoardPlan(cell);
	   newCell[x][y]=0;
	   newCell[newX][newY]=piece;
	   
	   //when bs reach boundary
	   if (piece==1)
		   if (newX==0)
			   newCell[newX][newY]=2;
	   else if (piece==3)
		   if (newX==7)
				newCell[newX][newY]=4;
	   return newCell;
   }
   
   	//****************************************************** 
 	//*** Purpose: make rules to every piece
 	//*** Input:  String type, int[][] boardPlan
 	//*** Output: List<CState>, the possible move to this piece
 	//******************************************************
   public List<CState> generateMoves(String type, int[][] boardPlan){
	   List<CState> nextMovesList = new ArrayList<CState>(); // allocate List
	   int newX,newY,doubleNewX,doubleNewY;
	   
	   // when double move happens, store the middle position
	   int middleX=-1,middleY=-1;
	   
	   int temp;
	   int cell[][]=new int[8][8];
	   int newCell[][]=new int[8][8];
	   
	   // move rules
	   // Search for available moves and add to the List
	   if (type == "MAX"){
		   for (int i=0;i<8;i++){
			   for (int j=0;j<8;j++){
				   // rules for bs, which can only move two directions
				   if (boardPlan[i][j]==1){
					   for (int bsDir = 0; bsDir < 2; bsDir++) {
						   	// copy the board in case it will be changed
							cell = copyBoardPlan(boardPlan);
							newX = i + dirX[bsDir];
							newY = j + dirY[bsDir];
							// if this position do not have capture move, add it into List
							if (!judgeCapture(i,j,type,bsDir,cell)){
								if (legalPosition(newX) && legalPosition(newY) && cell[newX][newY]==0){
									nextMovesList.add(new CState(generateNewCell(cell,1,i,j,newX,newY),i,j,newX,newY,-1,-1));
								}
							}
							else{
								cell[newX][newY] = 0;
								newX = newX + dirX[bsDir];
								newY = newY + dirY[bsDir];
								if (legalPosition(newX) && legalPosition(newY) && cell[newX][newX]==0){
									middleX=newX;
									middleY=newY;
									for (int doubleBsDir = 0; doubleBsDir < 4; doubleBsDir++) {
										if (!judgeCapture(newX,newY,type,doubleBsDir,cell))
											continue;
										else{
											newCell=copyBoardPlan(cell);
											newCell[newX+dirX[doubleBsDir]][newY+dirY[doubleBsDir]] = 0;
											doubleNewX = newX + 2*dirX[doubleBsDir];
											doubleNewY = newY + 2*dirY[doubleBsDir];
											// if this position have double capture moves, add it into List
											if (legalPosition(doubleNewX) && legalPosition(doubleNewY) && newCell[doubleNewX][doubleNewY]==0)
												nextMovesList.add(new CState(generateNewCell(newCell,1,i,j,doubleNewX,doubleNewY),i,j,doubleNewX,doubleNewY,middleX,middleY));
										}
									}
								}
								// if this position have capture move, add it into List
								if (legalPosition(newX) && legalPosition(newY) && cell[newX][newY]==0){
									nextMovesList.add(new CState(generateNewCell(cell,1,i,j,newX,newY),i,j,newX,newY,-1,-1));
								}
							}
						}
				   	}
				   	// rules for bk, which can move four directions
				   	else if (boardPlan[i][j]==2){
				   		middleX=-1;
				   		middleY=-1;
				   		for (int bkDir = 0; bkDir < 4; bkDir++) {
							cell = copyBoardPlan(boardPlan);
							newX = i + dirX[bkDir];
							newY = j + dirY[bkDir];
							if (!judgeCapture(i,j,type,bkDir,cell)){
								if (legalPosition(newX) && legalPosition(newY) && cell[newX][newY]==0){
									nextMovesList.add(new CState(generateNewCell(cell,2,i,j,newX,newY),i,j,newX,newY,-1,-1));
								}
							}
							else{
								cell[newX][newY] = 0;
								newX = newX + dirX[bkDir];
								newY = newY + dirY[bkDir];
								if (legalPosition(newX) && legalPosition(newY) && cell[newX][newX]==0){
									middleX=newX;
									middleY=newY;
									for (int doubleBkDir = 0; doubleBkDir < 4; doubleBkDir++) {
										if (!judgeCapture(newX,newY,type,doubleBkDir,cell))
											continue;
										else{
											newCell=copyBoardPlan(cell);
											newCell[newX+dirX[doubleBkDir]][newY+dirY[doubleBkDir]] = 0;
											doubleNewX = newX + 2*dirX[doubleBkDir];
											doubleNewY = newY + 2*dirY[doubleBkDir];
											if (legalPosition(doubleNewX) && legalPosition(doubleNewY) && newCell[doubleNewX][doubleNewY]==0)
												nextMovesList.add(new CState(generateNewCell(newCell,2,i,j,doubleNewX,doubleNewY),i,j,doubleNewX,doubleNewY,middleX,middleY));
										}
									}
								}
								if (legalPosition(newX) && legalPosition(newY) && cell[newX][newY]==0){
									nextMovesList.add(new CState(generateNewCell(cell,2,i,j,newX,newY),i,j,newX,newY,-1,-1));
								}
							}
						}
				   	}
			   }
		   }
	   }
	   else{
		   for (int i=0;i<8;i++){
			   for (int j=0;j<8;j++){
				   // rules for rs, which can only move two directions
				   if (boardPlan[i][j]==3){
					   for (int rsDir = 2; rsDir < 4; rsDir++) {
							cell = copyBoardPlan(boardPlan);
							newX = i + dirX[rsDir];
							newY = j + dirY[rsDir];
							if (!judgeCapture(i,j,type,rsDir,cell)){
								if (legalPosition(newX) && legalPosition(newY) && cell[newX][newY]==0){
									nextMovesList.add(new CState(generateNewCell(cell,3,i,j,newX,newY),i,j,newX,newY,-1,-1));
								}
							}
							else{
								cell[newX][newY] = 0;
								newX = newX + dirX[rsDir];
								newY = newY + dirY[rsDir];
								if (legalPosition(newX) && legalPosition(newY) && cell[newX][newX]==0){
									middleX=newX;
									middleY=newY;
									for (int doubleRsDir = 2; doubleRsDir < 4; doubleRsDir++) {
										if (!judgeCapture(newX,newY,type,doubleRsDir,cell))
											continue;
										else{
											newCell=copyBoardPlan(cell);
											newCell[newX+dirX[doubleRsDir]][newY+dirY[doubleRsDir]] = 0;
											doubleNewX = newX + 2*dirX[doubleRsDir];
											doubleNewY = newY + 2*dirY[doubleRsDir];
											if (legalPosition(doubleNewX) && legalPosition(doubleNewY) && newCell[doubleNewX][doubleNewY]==0)
												nextMovesList.add(new CState(generateNewCell(newCell,3,i,j,doubleNewX,doubleNewY),i,j,doubleNewX,doubleNewY,middleX,middleY));
										}
									}
								}
								if (legalPosition(newX) && legalPosition(newY) && cell[newX][newY]==0){
									nextMovesList.add(new CState(generateNewCell(cell,3,i,j,newX,newY),i,j,newX,newY,-1,-1));
								}
							}
						}
				   	}
				   	// rules for rk, which can move four directions
				   	else if (boardPlan[i][j]==4){
				   		middleX=-1;
				   		middleY=-1;
				   		for (int rkDir = 0; rkDir < 4; rkDir++) {
							cell = copyBoardPlan(boardPlan);
							newX = i + dirX[rkDir];
							newY = j + dirY[rkDir];
							if (!judgeCapture(i,j,type,rkDir,cell)){
								if (legalPosition(newX) && legalPosition(newY) && cell[newX][newY]==0){
									nextMovesList.add(new CState(generateNewCell(cell,4,i,j,newX,newY),i,j,newX,newY,-1,-1));
								}
							}
							else{
								cell[newX][newY] = 0;
								newX = newX + dirX[rkDir];
								newY = newY + dirY[rkDir];
								if (legalPosition(newX) && legalPosition(newY) && cell[newX][newX]==0){
									middleX=newX;
									middleY=newY;
									for (int doubleRkDir = 0; doubleRkDir < 4; doubleRkDir++) {
										if (!judgeCapture(newX,newY,type,doubleRkDir,cell))
											continue;
										else{
											newCell=copyBoardPlan(cell);
											newCell[newX+dirX[doubleRkDir]][newY+dirY[doubleRkDir]] = 0;
											doubleNewX = newX + 2*dirX[doubleRkDir];
											doubleNewY = newY + 2*dirY[doubleRkDir];
											if (legalPosition(doubleNewX) && legalPosition(doubleNewY) && newCell[doubleNewX][doubleNewY]==0)
												nextMovesList.add(new CState(generateNewCell(newCell,4,i,j,doubleNewX,doubleNewY),i,j,doubleNewX,doubleNewY,middleX,middleY));
										}
									}
								}
								if (legalPosition(newX) && legalPosition(newY) && cell[newX][newY]==0){
									nextMovesList.add(new CState(generateNewCell(cell,4,i,j,newX,newY),i,j,newX,newY,-1,-1));
								}
							}
						}
				   	}
			   }
		   }
	   }
	   
	   return nextMovesList;
   }
   
   //***********************************************************************
   //*** all of the opponent's pieces have been captured or his pieces are blocked in
   public boolean hasWon(){
	   return false;
   }

   //***********************************************************************
   void movePiece(int i1, int j1, int i2, int j2, String piece)
   {
      //*** raise exception if outside the board or moving into a non-empty
      //*** cell
      if ((boardPlan[i2][j2] != 0) || !legalPosition(i1) || !legalPosition(i2)
                                   || !legalPosition(j1) || !legalPosition(j1))
            throw new IllegalMoveException("An illegal move was attempted.");

      //*** informative console messages
      System.out.println("Moved " + piece + " from position [" +
                         i1 + ", " + j1 + "] to [" + i2 + ", " + j2 + "]");

      //*** erase the old cell
      board.drawPiece(i1, j1, "images/blank.jpg");

      //*** draw the new cell
      board.drawPiece(i2, j2, "images/" + piece + ".jpg");

      //*** erase any captured piece from the board
      if ((Math.abs(i1-i2) == 2) && (Math.abs(j1-j2) == 2))
         {
            //*** this handles hops of length 2
            //*** the captured piece is halfway in between the two moves
            int captured_i = i1 + (i2-i1)/2;
            int captured_j = j1 + (j2-j1)/2;

            //*** now wait a bit
            pause(pauseDuration);

            //*** erase the captured cell
            board.drawPiece(captured_i, captured_j, "images/blank.jpg");

            //*** print which piece was captured
            System.out.println("Captured " + legend[boardPlan[captured_i][captured_j]] +
                               " from position [" + captured_i + ", " + captured_j + "]");

            //*** the captured piece is removed from the board with a bang
            boardPlan[captured_i][captured_j] = 0;
            Applet.newAudioClip(getClass().getResource("images/hit.wav")).play();
         }

      //*** update the internal representation of the board by moving the old
      //*** piece into its new position and leaving a blank in its old position
      boardPlan[i2][j2] = boardPlan[i1][j1];
      boardPlan[i1][j1] = 0;

      //*** red single is kinged
      if ( (i2==boardPlan.length-1) && (boardPlan[i2][j2] == 3) )
         {
          boardPlan[i2][j2] = 4;
          putPiece(i2, j2, "rk");
         }

      //*** blue single is kinged
      if ( (i2==0) && (boardPlan[i2][j2] == 1) )
         {
          boardPlan[i2][j2] = 2;
          putPiece(i2, j2, "bk");
         }

      //*** now wait a bit
      pause(pauseDuration);
   }

   //***********************************************************************
   //*** incorporate your MINIMAX algorithm in here
   //***********************************************************************
   public static void main(String [] args)
   {
        //*** create a new game and make it visible
        Checkers game = new Checkers();
        CState state=new CState(game.boardPlan,"MAX");
        int[] test=null;
        int count=0;
        
        try{
    		String player="MAX";
    		
    		while(count<=20){
    			if (player=="MAX"){
    				if (game.generateMoves(player,boardPlan).size()!=0){
        				test=game.minimax(3,"MAX", boardPlan,state);
        				if (test[5]==-1){
            				game.movePiece(test[1], test[2], test[3], test[4], game.legend[boardPlan[test[1]][test[2]]]);
            				state=new CState(game.boardPlan,"MAX");
            				player="MIN";
            				if (Math.abs(test[1]-test[3])==2)
            					count=0;
            				else
            					count++;
        				}
        				else{
        					game.movePiece(test[1], test[2], test[5], test[6], game.legend[boardPlan[test[1]][test[2]]]);
        					game.movePiece(test[5], test[6], test[3], test[4], game.legend[boardPlan[test[5]][test[6]]]);
            				state=new CState(game.boardPlan,"MAX");
            				player="MIN";
            				count=0;
        				}
    				}
    				else
    					break;
    			}
    			else{
    				if (game.generateMoves(player,boardPlan).size()!=0){
    					test=game.minimax(1,"MIN", boardPlan,state);
    					if (test[5]==-1){
            				game.movePiece(test[1], test[2], test[3], test[4], game.legend[boardPlan[test[1]][test[2]]]);
            				state=new CState(game.boardPlan,"MIN");
            				player="MAX";
            				if (Math.abs(test[1]-test[3])==2)
            					count=0;
            				else
            					count++;
    					}
        				else{
        					game.movePiece(test[1], test[2], test[5], test[6], game.legend[boardPlan[test[1]][test[2]]]);
        					game.movePiece(test[5], test[6], test[3], test[4], game.legend[boardPlan[test[5]][test[6]]]);
            				state=new CState(game.boardPlan,"MIN");
            				player="MAX";
            				count=0;
        				}
    				}
    				else
    					break;
    			}
    		}
    		if(count==21)
    			System.out.println("Stalemate! Try best next time.");
    		else{
        		if (player=="MAX")
        			System.out.println("Contragulations! MIN win!");
        		else
        			System.out.println("Contragulations! MAX win!");
    		}
       }

       catch (IllegalMoveException e)
           {e.printStackTrace();}
    }
}
