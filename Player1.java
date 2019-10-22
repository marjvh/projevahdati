
package othello;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Player1 extends Player {

    
    private int Depth = 6 ;
    
    @Override
    public Action play() {
        try {
  
            
            Othello thisGame = deepClone(Gameplay.GetGame())  ;
            
            return firstMove(thisGame);
        } catch (Exception ex) {
            Logger.getLogger(Player1.class.getName()).log(Level.SEVERE, null, ex);
        }
       System.out.println("othello.Player1.play() _ have exeption !!");
       return null; 
    }
    

    public Action firstMove(Othello _currentGame) throws CloneNotSupportedException, Exception{
        int index = -1 ;
        int utility  = Integer.MIN_VALUE ;
        
        ArrayList<Action> list = new ArrayList<>();
        list = GetPosibleMoveof(_currentGame  ,_currentGame.getBoard() , 1 ) ;
        if(_currentGame.isOver()) 
            return null;
        for(int i = 0 ; i < list.size() ; i++){
            
            Othello GameNew = deepClone(_currentGame);
         
            Action s = list.get(i);
            int value = 0 ;
            if(s == null) break;
            if(GameNew.getBoard()[s.row][s.col] == 0){
                 GameNew.put(list.get(i));
                 value = MiniMax(GameNew, 2 , Depth - 1) ;
            }
            if(value > utility){
                index = i ;
            }
            utility = Math.min(value, utility) ;
            
        }
        
    
        return list.get(index) ;
    }
    
    
    public int MiniMax(Othello _currentGame , int PlayerTurn , int Depth1) throws CloneNotSupportedException, Exception{
       int value   = 0;
        if(Depth1 == 0 && PlayerTurn == 1 ) {
            return _currentGame.getPlayer1Pieces();
        }
        if(Depth1 == 1 && PlayerTurn == 2)
            return _currentGame.getPlayer2Pieces();
        
       if(PlayerTurn == 1){
           // max 
           value = Integer.MIN_VALUE;
           ArrayList<Action> list = new ArrayList<>();
           list = GetPosibleMoveof(_currentGame  ,_currentGame.getBoard() , 1 ) ;
           
           if(list.isEmpty()) 
               return Integer.MIN_VALUE;
           
           for(int i = 0 ; i < list.size() ; i++){
               Othello GameTemp;
               GameTemp = deepClone(_currentGame);
               Action action = list.get(i) ;
               if(GameTemp.canPut(action, 1)){
                    GameTemp.put(action);
                    int min = MiniMax(GameTemp, 2, Depth1 - 1) ;
                    if(value < min){
                        value  = min;
                    }
               
               }
           }
           
       }
       else if(PlayerTurn == 2){
           // min
           value = Integer.MAX_VALUE;
           ArrayList<Action> list = new ArrayList<>();
           list = GetPosibleMoveof(_currentGame  ,_currentGame.getBoard() , 2 ) ;
            for(int i = 0 ; i < list.size() ; i++){
               Othello GameTemp ;
               GameTemp = deepClone(_currentGame) ;
               Action action = list.get(i) ;
               if(GameTemp.canPut(action, 2) ){
                    GameTemp.put(list.get(i));
                    int max = MiniMax(GameTemp, 1 , Depth1 - 1) ;
                    if(value < max){
                        value  = max;
                    }
               }
           }
           
       }
        return value ;
    }
    
    
    public ArrayList<Action> GetPosibleMoveof(Othello game , int[][] Board , int turn ) throws CloneNotSupportedException{
         ArrayList<Action> temp = new ArrayList<>();
         Othello TempGame = deepClone(game) ;
         
         for(int i = 0 ; i < 8 ; i++){
             for(int j = 0 ; j < 8 ; j++){
                 Action action  = new Action(i, j);
                 if(Board[i][j] == 0 )
                   if(TempGame.canPut(action, turn))
                       temp.add(action);
               
             }
         }
         
        
         return temp;
    }
    

    public static  Othello deepClone(Othello object){
    try {
          ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
          ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
          objectOutputStream.writeObject(object);
          ByteArrayInputStream bais = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
          ObjectInputStream objectInputStream = new ObjectInputStream(bais);
            return (Othello) objectInputStream.readObject();
      }
      catch (IOException | ClassNotFoundException e) {
        return null;
      }
  }
}
