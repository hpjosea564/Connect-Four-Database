/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//Nombre: Joseph De La Cruz Negrón
//Número de ID: Y00410531
//Sec. 54653
//GAME 3102
//Prof. Henry Bruckman Vargas
//Fecha de Entrega: 11 de abril de 2016

package connectfourdb;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;

/**
 *
 * 
 */
public class ConnectFourDB 
{
   /**************************************************************************\
   |* Game Constants                                                         *|
   \**************************************************************************/

   public static final String RED = "X" ;

   public static final String BLACK = "O" ;

   public static final String EMPTY = " " ;

   public static final int ROWS = 6 ;

   public static final int COLS = 7 ;
   
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://localhost/STUDENTS";
   
   static final String USER = "username";

   /**************************************************************************\
   |* Game State                                                             *|
   \**************************************************************************/

   public String[][] board ;
   public boolean isPlayerOneTurn ;

   /**************************************************************************\
   |* Other Game Data Objects and Components                                 *|
   \**************************************************************************/

   private Scanner keyboard ;

   /**************************************************************************\
   |*                                                                        *|
   \**************************************************************************/

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException 
    {
        
        ConnectFourDB CFDB = new ConnectFourDB() ;
        CFDB.start() ;
        
        try{
        
        String host = "jdbc:derby://localhost:1527/STUDENTS";
        String uName = "Your Username Here";
        String uPass = "Your Password Here";
        
        System.out.println("Connecting to a selected database...");
        Connection con = DriverManager.getConnection(host, uName, uPass);
        System.out.println("Connected database successfully...");
        
        System.out.println("Creating table in given database...");
        Statement stmnt = con.createStatement();
        
        String SQL = "CREATE TABLE Users" +
                     "(id INTEGER not NULL, "+
                     "username VARCHAR(255), "+
                     "WINS INTEGER not NULL, "+
                     "LOSSES INTEGER not NULL, "+
                     "DRAWS INTEGER not NULL, "+
                     "TOTAL INTEGER not NULL, "+
                     "PRIMARY KEY (id))";
        
        ResultSet rs = stmnt.executeQuery(SQL);
        
      stmnt.executeUpdate(SQL);
      System.out.println("Created table in given database...");
        
        while(rs.next())
        {
            int id_col = rs.getInt("ID");
            String username = rs.getString("username: ");
            int WINS = rs.getInt("");
            int LOSSES = rs.getInt("");
            int DRAWS = rs.getInt("");
            int TOTAL = rs.getInt("");
            
            
            System.out.println(id_col + " " + username + " " + WINS + " " + LOSSES + " " + DRAWS + " " + TOTAL);
            
        }
       }
        catch (SQLException err){
            System.out.println(err.getMessage());
        }
        
        System.out.println("Goodbye!");
    }
    
    /**************************************************************************\
   |*                                                                        *|
     * @param user\**************************************************************************/
    
     public static boolean authenticate(String user)
    {
        Connection connection;
        Statement stmnt;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/STUDENTS");

            System.out.println("Connection to setdb successful: " + connection);

            stmnt = connection.createStatement();
            ResultSet rs = stmnt.executeQuery("SELECT name, FROM users WHERE name='" + user + "'");
            
            return rs.next();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**************************************************************************\
   |*                                                                        *|
   \**************************************************************************/
    
    public static void UpdateRecord(String usr1,String usr2, int result)
    {
        Connection connection;
        PreparedStatement pstm = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/STUDENTS");

            System.out.println("Connection to setdb successful: " + connection);

            String sqlstmt = "";
            String sqlstmt2 = "";
            switch(result) {
                case 0:
                    sqlstmt = "UPDATE users SET wins=wins+1 WHERE name='" + usr1 + "'";
                    sqlstmt2 = "UPDATE users SET losses=losses+1 WHERE name='" + usr2 + "'";
                    pstm = connection.prepareStatement(sqlstmt2);
                    pstm.executeUpdate();
                    break;
                case 1:
                    sqlstmt = "UPDATE users SET wins=wins+1 WHERE name='" + usr2 + "'";
                    sqlstmt2 = "UPDATE users SET losses=losses+1 WHERE name='" + usr1 + "'";
                    pstm = connection.prepareStatement(sqlstmt2);
                    pstm.executeUpdate();
                    break;
                case 2:
                    sqlstmt = "UPDATE users SET draws=draws+1 WHERE name='" + usr1 + "' OR name='" + usr2 + "'";
                    break;
            }
            pstm = connection.prepareStatement(sqlstmt);
            pstm.executeUpdate();

            pstm.close();
        } catch(Exception e) {
            e.printStackTrace();
        }


    }
    
    /**************************************************************************\
   |*                                                                        *|
   \**************************************************************************/

   public ConnectFourDB()
   {

   }

 /**************************************************************************\
   |*                                                                        *|
   \**************************************************************************/

   public void start()
   {
      int input ;

      init() ;                           // 1. Initialize Environment and Variables

      showStartScreen() ;                // 2. Show Start Screen

      do
      {
         showBoard() ;                   // 3. Show or Render Board / Scene / Map

         do
         {
            showUserInputOptions() ;     // 4. Show User Input Options

            input = getUserInput() ;     // 5. Get User Input
         }
         while(!checkUserInput(input)) ; // 6. Validate User Input

         processUserInput(input) ;       // 7. Process User Input

         updateGameState() ;             // 8. Update Game State

      } while(!isGameOver()) ;           // 9. Check for Winning / Losing / Termination Conditions

      showGameOverScreen() ;             // 10. Show Game Results
   }

   /**************************************************************************\
   |*                                                                        *|
   \**************************************************************************/

   public void init()
   {
      board = new String[ROWS][COLS] ;

      for(int i=0 ; i<ROWS ; i++)
      {
         for(int j=0 ; j<COLS ; j++)
         {
            board[i][j] = EMPTY ;
         }
      }

     // board[5][1] = BLACK ;
     // board[5][2] = BLACK ;
     // board[5][3] = BLACK ;
     // board[4][2] = BLACK ;
     // board[4][3] = BLACK ;
    //  board[3][3] = BLACK ;

    //  board[5][0] = RED ;
    //  board[4][1] = RED ;
    //  board[3][2] = RED ;

    // board[0][0] = RED ;
    //  board[0][1] = BLACK ;
    //  board[0][2] = RED ;
    //  board[0][3] = BLACK ;
    //  board[0][4] = RED ;
    //  board[0][5] = BLACK ;

      isPlayerOneTurn = true ;

      keyboard = new Scanner(System.in) ;
   }

   /**************************************************************************\
   |*                                                                        *|
   \**************************************************************************/

   public void showStartScreen()
   {
      System.out.println("Welcome to Connect Four!") ;
   }

   /**************************************************************************\
   |*                                                                        *|
   \**************************************************************************/

   public void showBoard()
   {
      String output = " " ;

      for(int x=0 ; x<COLS ; x++)
      {
         output += String.format("%-2d", x) ;
      }

      output += "\n" ;

      for(int i=0 ; i<ROWS ; i++)
      {
         output += "|" ;

         for(int j=0 ; j<COLS ; j++)
         {
            output += board[i][j] + "|" ;
         }
         output += "\n-" ;

         for(int x=0 ; x<COLS ; x++)
         {
            output += "--" ;
         }

         output += "\n" ;
      }

      System.out.println(output) ;
   }

   /**************************************************************************\
   |*                                                                        *|
   \**************************************************************************/

   public void showUserInputOptions()
   {
      String player = (isPlayerOneTurn) ? "1" : "2" ;

      System.out.print("Player " + player + " select any number from 0 to " + (COLS-1) + ": ") ;
   }

   /**************************************************************************\
   |*                                                                        *|
   \**************************************************************************/

   public int getUserInput()
   {
      if(keyboard.hasNextInt())
      {
         return keyboard.nextInt() ;
      }
      else
      {
         keyboard.next() ;
         return -1 ;
      }
   }

   /**************************************************************************\
   |*                                                                        *|
   \**************************************************************************/

   public boolean checkUserInput(int input)
   {
      if(input < 0 || input >= COLS)
      {
         System.out.println("You MORON, READ OR FEAR THE DARK SIDE OF THE FORCE IF NOT THEN SUFFER THE WRATH OF NARUTO UZUMAKI THE NINE TAILS JINCHURIKI") ;

         return false ;
      }

      if(isColumnFull(input))
      {
         System.out.println("That column is full, DUMBASS!") ;

         return false ;
      }

      return true ;
   }

   /**************************************************************************\
   |*                                                                        *|
   \**************************************************************************/

   public boolean isColumnFull(int col)
   {
      return (board[0][col] != EMPTY) ;
   }

   /**************************************************************************\
   |*                                                                        *|
   \**************************************************************************/

   public void processUserInput(int input)
   {
      for(int i=ROWS-1 ; i>=0 ; i--)
      {
         if(isEmpty(i, input))
         {
            board[i][input] = isPlayerOneTurn ? RED : BLACK ;

            break ;
         }
      }
   }

   /**************************************************************************\
   |*                                                                        *|
   \**************************************************************************/

   public boolean isEmpty(int row, int col)
   {
      return (board[row][col] == EMPTY) ;
   }

   /**************************************************************************\
   |*                                                                        *|
   \**************************************************************************/

   public void updateGameState()
   {
      isPlayerOneTurn = !isPlayerOneTurn ;
   }

   /**************************************************************************\
   |*                                                                        *|
   \**************************************************************************/

   public boolean isGameOver()
   {
      System.out.println("Checking if game is over.") ;

      return checkWin(RED) || checkWin(BLACK) || checkDraw() ;
   }

   /**************************************************************************\
   |*                                                                        *|
   \**************************************************************************/

   public boolean checkWin(String m)
   {
      for(int i=0 ; i<ROWS ; i++)
      {
         for(int j=0 ; j<COLS ; j++)
         {
            if(checkColWin(i, j, m) ||
               checkRowWin(i, j, m) ||
               checkUpDiagonalWin(i, j, m) ||
               checkDownDiagonalWin(i, j, m))
            {
               return true ;
            }
         }
      }

      return false ;
   }

   /**************************************************************************\
   |*                                                                        *|
   \**************************************************************************/

   public boolean checkColWin(int r, int c, String m)
   {
      if(r <= ROWS-4)
      {
         return board[r+0][c] == m &&
                board[r+1][c] == m &&
                board[r+2][c] == m &&
                board[r+3][c] == m ;
      }

      return false ;
   }

   /**************************************************************************\
   |*                                                                        *|
   \**************************************************************************/

   public boolean checkRowWin(int r, int c, String m)
   {
      if(c <= COLS-4)
      {
         return board[r][c+0] == m &&
                board[r][c+1] == m &&
                board[r][c+2] == m &&
                board[r][c+3] == m ;
      }

      return false ;
   }

   /**************************************************************************\
   |*                                                                        *|
   \**************************************************************************/

   public boolean checkUpDiagonalWin(int r, int c, String m)
   {
      if(c <= COLS-4 && r >= 3)
      {
         return board[r-0][c+0] == m &&
                board[r-1][c+1] == m &&
                board[r-2][c+2] == m &&
                board[r-3][c+3] == m ;
      }

      return false ;
   }

   /**************************************************************************\
   |*                                                                        *|
   \**************************************************************************/

   public boolean checkDownDiagonalWin(int r, int c, String m)
   {
      if(c <= COLS-4 && r <= ROWS-4)
      {
         return board[r+0][c+0] == m &&
                board[r+1][c+1] == m &&
                board[r+2][c+2] == m &&
                board[r+3][c+3] == m ;
      }

      return false ;
   }

   /**************************************************************************\
   |*                                                                        *|
   \**************************************************************************/

   public boolean checkDraw()
   {
      for(int i=0 ; i<COLS ; i++)
      {
         if(!isColumnFull(i))
         {
            return false ;
         }
      }

      return true ;
   }

   /**************************************************************************\
   |*                                                                        *|
   \**************************************************************************/

   public void showGameOverScreen()
   {
      showBoard() ;

      System.out.println("Game Over!") ;

      if(checkWin(RED))
      {
         System.out.println("Player 1 Won!") ;
      }
      else if(checkWin(BLACK))
      {
         System.out.println("Player 2 Won!") ;
      }
      else
      {
         System.out.println("Draw!") ;
      }
   }
}
