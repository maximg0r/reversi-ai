package com.maximgorshkov.reversi;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Lob;
import javax.persistence.CascadeType;

/*
1) Does our code capture situations in which there are no valid moves
2) does our code link up parents to the same child state if there is more than one way of achieveing a specific outcome
*/

@Entity
public class State // implements Comparable<State>
{
    private int             boardSize;

    @Lob
    private int[][]         board;        // 0=empty, 1=black, 2=white

    private int             currPlayer;   // 1=black, 2=white
    // private int numberOfCurrentPlayerTiles;
    // private int numberOfEnemyPlayerTiles;
    private int             num_of_blacks;
    private int             num_of_whites;

    @OneToMany(targetEntity=Coordinate.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Coordinate> emptySpots;
    // private int util = Integer.MIN_VALUE;

    @Id @GeneratedValue
    private Long id;
    
    public int getBoardSize()
    {
        return boardSize;
    }
    
    public int[][] getBoard()
    {
        return board;
    }
    
    public int getCurrPlayer()
    {
        return currPlayer;
    }
    
    public void setCurrPlayer(int currPlayer)
    {
        this.currPlayer = currPlayer;
    }
    
    /*
    public int getUtility()
    {
        return util;
    }
    
    public void setUtility(int util)
    {
        this.util = util;
    }
    */

    public int getNumberOfBlackTiles()
    {
        return num_of_blacks;
    }
    
    public int getNumberOfWhiteTiles()
    {
        return num_of_whites;
    }
    
    public int getNumberOfCurrentPlayerTiles()
    {
        return (currPlayer==1)? num_of_blacks:num_of_whites;
    }
    
    public int getNumberOfEnemyPlayerTiles()
    {
        return (currPlayer!=1)? num_of_blacks:num_of_whites;
    }

    public State() {}

    public State(int boardSize)
    {
        this.boardSize                              = boardSize;
        this.currPlayer                             = 1;
        
        board                                       = new int[boardSize][boardSize];
        // initialize board
        board[boardSize / 2][boardSize / 2]         = 1;
        board[boardSize / 2 - 1][boardSize / 2 - 1] = 1;
        board[boardSize / 2 - 1][boardSize / 2]     = 2;
        board[boardSize / 2][boardSize / 2 - 1]     = 2;
        this.emptySpots                             = new HashSet<Coordinate>();
        
        for (int i = 0; i < boardSize; i++)
        {
            for (int j = 0; j < boardSize; j++)
            {
                if (board[i][j] == 0)
                {
                    int        row   = i;
                    int        col   = j;
                    
                    Coordinate empty = new Coordinate(row, col);
                    this.emptySpots.add(empty);
                }
                
            }
        }
        
        
        num_of_blacks = num_of_whites = 2;
    }

    public State(int boardSize, int currPlayer)
    {
        this.boardSize                              = boardSize;
        this.currPlayer                             = currPlayer;
        
        board                                       = new int[boardSize][boardSize];
        // initialize board
        board[boardSize / 2][boardSize / 2]         = 1;
        board[boardSize / 2 - 1][boardSize / 2 - 1] = 1;
        board[boardSize / 2 - 1][boardSize / 2]     = 2;
        board[boardSize / 2][boardSize / 2 - 1]     = 2;
        this.emptySpots                             = new HashSet<Coordinate>();
        
        for (int i = 0; i < boardSize; i++)
        {
            for (int j = 0; j < boardSize; j++)
            {
                if (board[i][j] == 0)
                {
                    int        row   = i;
                    int        col   = j;
                    
                    Coordinate empty = new Coordinate(row, col);
                    this.emptySpots.add(empty);
                }
                
            }
        }
        
        
        num_of_blacks = num_of_whites = 2;
    }
    
    public State(State s)
    {
        this.boardSize     = s.boardSize;
        this.currPlayer    = s.currPlayer;
        this.num_of_blacks = s.num_of_blacks;
        this.num_of_whites = s.num_of_whites;
        //this.util          = s.util;
        
        board              = new int[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++)
        {
            for (int j = 0; j < boardSize; j++)
            {
                board[i][j] = s.board[i][j];
            }
        }
        
        this.emptySpots = new HashSet<Coordinate>();
        this.emptySpots.addAll(s.emptySpots);
    }
    
    /*
    @Override
    public int compareTo(State otherState)
    {
        Integer mine  = this.computeUtility();
        Integer other = otherState.computeUtility();
        
        return mine.compareTo(other);
    }
    */
    
    public static State applyAction(State s, Action action)
    {
        State currentState = new State(s);
        
        currentState.board[action.row][action.col] = action.player_value;
        Coordinate placedTile = new Coordinate(action.row, action.col);
        currentState.emptySpots.remove(placedTile);

        if (currentState.currPlayer == 1)
        {
            currentState.num_of_blacks++;
            currentState.currPlayer = 2;
        } else
        {
            currentState.num_of_whites++;
            currentState.currPlayer = 1;
        }
        
        for (Coordinate coordinate : action.coordinates_to_flip)
        {
            currentState.board[coordinate.r][coordinate.c] = s.currPlayer;
            if (s.currPlayer == 1)
            {
                currentState.num_of_blacks++;
                currentState.num_of_whites--;
            } else
            {
                currentState.num_of_whites++;
                currentState.num_of_blacks--;
            }
        }
        
        return currentState;
    }
    
    public ArrayList<Action> getPossibleActions()
    {
        ArrayList<Action> actions = new ArrayList<Action>();
        for (Coordinate emptySpot : emptySpots)
        {
            Action a = new Action(emptySpot.r, emptySpot.c, currPlayer);
            if (isValidAction(a))
            {
                actions.add(a);
            }
            
        }
        return actions;
    }

    public TreeMap<State, Action> getPossibleActionsMap(int player_color)
    {
        // compare by heuristic from AB_H_MinimaxAgent
        TreeMap<State, Action> map = new TreeMap<State, Action>(new Comparator<State>() {
            @Override
            public int compare(State s1, State s2) {
                Integer s1_val = AB_H_MinimaxAgent.evaluate_heuristic(s1, player_color);
                Integer s2_val = AB_H_MinimaxAgent.evaluate_heuristic(s2, player_color);
                return s2_val.compareTo(s1_val);
            }
        });
        for (Coordinate emptySpot : emptySpots)
        {
            Action a = new Action(emptySpot.r, emptySpot.c, currPlayer);
            if (isValidAction(a))
            {
                State s = State.applyAction(this, a);
                map.put(s, a);
            }
            
        }
        return map;
    }
    
    public enum eDirection
    {
        // (row, col)
        NN(1, 0), NW(1, -1), NE(1, 1), SS(-1, 0), SW(-1, -1), SE(-1, 1), WW(0, -1), EE(0, 1);
        
        public final int row_increment;
        public final int col_increment;
        
        eDirection(int row_increment, int col_increment)
        {
            this.row_increment = row_increment;
            this.col_increment = col_increment;
        }
        
        
    }
    
    
    /*
     * public boolean isValidAction(Action action){
     * 
     * int number_of_valid_moves=0; // change to boolean
     * 
     * for (eDirection dirCheck : eDirection.values()) { int check_row = action.row + dirCheck.row_increment; int check_col = action.col + dirCheck.col_increment;
     * 
     * if (isWithinBoard(check_row,check_col)) { int val_check = board[check_row][check_col]; if(val_check != 0 && val_check !=currPlayer) { Set<Pair<Integer, Integer>> captured_coordinates = new HashSet<>(); //surrounding box with enemy tile found. Now check if there is a team tile further in that direction while(isWithinBoard(check_row, check_col) && board[check_row][check_col]!=0) { //
     * change to properly reflect to number of valid moves checked [Sid will handle] val_check = board[check_row][check_col]; if(val_check == currPlayer) { action.coordinates_to_flip.addAll(captured_coordinates); // Is this allowed? capturing all X's before the last O (*)XXOXXOXX()(), contiguous or not? number_of_valid_moves++; // if not catching all on first catch }else {
     * captured_coordinates.add(new Pair<check_row, check_col>()); check_row+=dirCheck.row_increment; check_col+=dirCheck.col_increment; } } captured_coordinates.clear();
     * 
     * 
     * } }
     * 
     * } return number_of_valid_moves>0; }
     */
    
    
    // We pay our respects to the first chunk of code that ran this project:
    
    
    public boolean isValidAction(Action action)
    {
        boolean bValidMovePresent = false;
        for (eDirection dirCheck : eDirection.values())
        {
            int             check_row                  = action.row + dirCheck.row_increment;
            int             check_col                  = action.col + dirCheck.col_increment;
            boolean         exploringEnemyTileSequence = false;
            // do something like DFA for matching the pattern of valid sequence of tiles
            Set<Coordinate> captured_coordinates       = new HashSet<>();
            while (isWithinBoard(check_row, check_col))
            {
                if (!exploringEnemyTileSequence)
                {
                    if (board[check_row][check_col] > 0 && board[check_row][check_col] != currPlayer)
                    {
                        exploringEnemyTileSequence = true;
                    } else
                    {
                        break;
                    }
                } else
                {
                    if (board[check_row][check_col] == 0)
                    {
                        break;
                    } else if (board[check_row][check_col] == currPlayer)
                    {
                        action.coordinates_to_flip.addAll(captured_coordinates);
                        captured_coordinates.clear();
                        bValidMovePresent = true;
                        break;
                    }
                }
                // update row and col to move to the next symbol
                captured_coordinates.add(new Coordinate(check_row, check_col));
                check_row += dirCheck.row_increment;
                check_col += dirCheck.col_increment;
            }
        }
        return bValidMovePresent;
    }
    
    public boolean isWithinBoard(int row, int col)
    {
        return !(row < 0 || row >= boardSize || col < 0 || col >= boardSize);
    }
    
    public boolean isTerminal()
    {
        if (this.getPossibleActions().isEmpty())
        {
            State enemyCopy = new State(this);
            enemyCopy.setCurrPlayer(currPlayer == 1 ? 2 : 1);
            if (enemyCopy.getPossibleActions().isEmpty())
            {
                return true;
            }
        }
        return false;
    }
    
    // check if terminal state before calling
    public int computeUtility()
    {
        if (getNumberOfCurrentPlayerTiles() > getNumberOfEnemyPlayerTiles() )
        {
            return 1;
        } else if (getNumberOfCurrentPlayerTiles() < getNumberOfEnemyPlayerTiles())
        {
            return -1;
        } else
        {
            return 0;
        }
    }

    public int computeUtility(int player)
    {
        if (player == 1) {
            if (getNumberOfBlackTiles() > getNumberOfWhiteTiles()) {
                return 1;
            } else if (getNumberOfBlackTiles() < getNumberOfWhiteTiles())
            {
                return -1;
            } else
            {
                return 0;
            }
        }
        else if (player == 2) {
            if (getNumberOfWhiteTiles() > getNumberOfBlackTiles()) {
                return 1;
            } else if (getNumberOfWhiteTiles() < getNumberOfBlackTiles())
            {
                return -1;
            } else
            {
                return 0;
            }
        } else {
            throw new IllegalArgumentException();
        }
    }
}
