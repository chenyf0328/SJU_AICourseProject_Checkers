/* ********************************************************************
   CState
   Forouraghi
***********************************************************************/

import java.util.*; 

//********************************************************************
//*** this aux class represents an ADT for checkers states
class CState
{

      //*** the evaluation function e(n)
      private int e;

      //*** node type: MAX or MIN
      private String type;

      //*** some board configuration
      private int [][] state;
      
      private int x,y,newX,newY;
      
      private int middleX,middleY;

      //**************************************************************
      CState(int [][] state, String type)
      {
          this.state = state;
          this.type  = type;
      }
      
      CState(int[][] state, int x, int y, int newX, int newY, int middleX, int middleY){
    	  this.state = state;
          this.x = x;
          this.y = y;
          this.newX = newX;
          this.newY  = newY;
          this.middleX=middleX;
          this.middleY=middleY;
      }

      //**************************************************************
      //*** evaluate a state based on the evaluation function we
      //*** discussed in class
      int evalState()
      {
    	  //*** add your own necessary logic here to properly evaluate a state
    	  //*** I am just assigning some random numbers for demonstration purposes
    	  //e = Math.random()*10;
    	  Map<String,Integer> advMap=new HashMap<String,Integer>();
    	  advMap.put("bs", 0);
    	  advMap.put("bk", 0);
    	  advMap.put("rs", 0);
    	  advMap.put("rk", 0);
    	  for (int i=0;i<8;i++){
    		  for (int j=0;j<8;j++){
    			  if (this.state[i][j]==1)
    				  advMap.put("bs", advMap.get("bs")+1);
    			  else if (this.state[i][j]==2)
    				  advMap.put("bk", advMap.get("bk")+1);
    			  else if (this.state[i][j]==3)
    				  advMap.put("rs", advMap.get("rs")+1);
    			  else if (this.state[i][j]==4)
    				  advMap.put("rk", advMap.get("rk")+1);
    		  }
    	  }
    	  this.e=(5*advMap.get("bk")+advMap.get("bs"))-(5*advMap.get("rk")+advMap.get("rs"));
    	  return e;
      }

      //**************************************************************
      //*** get a node's E() value
      double getE()
      {
         return e;
      }

      //**************************************************************
      //*** get a node's type
      String getType()
      {
         return type;
      }
      
      int getX(){
    	  return this.x;
      }
      
      int getY(){
    	  return this.y;
      }
      
      int getNewX(){
    	  return this.newX;
      }
      
      int getNewY(){
    	  return this.newY;
      }
      
      int getMiddleX(){
    	  return this.middleX;
      }
      
      int getMiddleY(){
    	  return this.middleY;
      }

      int[][] getStat(){
    	  return this.state;
      }
      
      //**************************************************************
      //*** get a state
      String getState()
      {
         String result = "";

         for (int i=0; i<state.length; i++)
           {
            for (int j=0; j<state.length; j++)
                result = result + state[i][j] + " ";

            result += "\n";
           }

         return result;
      }
      
      @Override
      public String toString(){
    	  for (int i=0;i<8;i++){
    		  for (int j=0;j<8;j++)
    			  System.out.print(this.state[i][j]+" ");
    		  System.out.println();
    	  }
    	  return "";
      }
}
