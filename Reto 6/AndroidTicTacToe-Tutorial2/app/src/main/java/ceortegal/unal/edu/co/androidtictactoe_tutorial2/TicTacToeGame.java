package ceortegal.unal.edu.co.androidtictactoe_tutorial2;

import android.os.Handler;

import java.util.Random;

/**
 * Created by Christian Ortega on 22/08/2016.
 */
public class TicTacToeGame {
    // Current difficulty level
    private DifficultyLevel mDifficultyLevel = DifficultyLevel.Expert;

    private char mBoard[] = {'1','2','3','4','5','6','7','8','9'};
    private int mHumanWins = 0;
    private int mBugdroidWins = 0;
    private int mTies = 0;

    private char mPlayerTurn;

    public static final int BOARD_SIZE = 9;
    public static final char HUMAN_PLAYER = 'X';
    public static final char COMPUTER_PLAYER = 'O';
    public static final char NO_BODY_PLAYER = '/';
    public static final char OPEN_SPOT = ' ';

    private Random mRand;

    public TicTacToeGame() {

        // Seed the random number generator
        mRand = new Random();
    }

    /** Clear the board of all X's and O's by setting all spots to OPEN_SPOT. */
    public void clearBoard() {
        for (int i = 0; i < mBoard.length; i++) {
            mBoard[i] = TicTacToeGame.OPEN_SPOT;
        }
        mPlayerTurn = HUMAN_PLAYER;
    }

    /**
     * Gets a blocking move for the computer player respect human player.
     *
     * @return blocking move to do.
     */
    private int getBlockingMove(){
        int move = -1;

        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER) {
                char curr = mBoard[i];   // Save the current number
                mBoard[i] = HUMAN_PLAYER;
                if (checkForWinner() == 2) {
                    move = i;
                }
                mBoard[i] = curr;
            }
        }

        return move;
    }

    /**
     * Gets a random movement for the computer player.
     *
     * @return random move to do.
     */
    private int getRandomMove(){
        int move;

        do {
            move = mRand.nextInt(BOARD_SIZE);
        } while (mBoard[move] == HUMAN_PLAYER || mBoard[move] == COMPUTER_PLAYER);

        return move;
    }

    /**
     * Gets a winning movement for the computer player.
     *
     * @return winning move to do.
     */
    private int getWinningMove(){
        int move = -1;

        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER) {
                char curr = mBoard[i];
                mBoard[i] = COMPUTER_PLAYER;
                if (checkForWinner() == 3) {
                    move = i;
                }
                mBoard[i] = curr;
            }
        }

        return move;
    }

    /** Set the given player at the given location on the game board.
     *  The location must be available, or the board will not be changed.
     *
     * @param player - The HUMAN_PLAYER or COMPUTER_PLAYER
     * @param location - The location (0-8) to place the move
     * @return {@code true} if a movement was committed, {@code false} in other case.
     */
    public boolean setMove(char player, int location) {
        boolean var = false;
        if(player == mPlayerTurn && mBoard[location] == TicTacToeGame.OPEN_SPOT) {
            mBoard[location] = player;
            if(mPlayerTurn == HUMAN_PLAYER) {
                mPlayerTurn = COMPUTER_PLAYER;
            } else {
                mPlayerTurn = HUMAN_PLAYER;
            }
            var = true;
        }

        return var;
    }

    /** Return the best move for the computer to make. You must call setMove()
     * to actually make the computer move to that location.
     * @return The best move for the computer to make (0-8).
     */
    public int getComputerMove() {
        int move = -1;

        if (mDifficultyLevel == DifficultyLevel.Easy)
            move = getRandomMove();
        else if (mDifficultyLevel == DifficultyLevel.Harder) {
            move = getWinningMove();
            if (move == -1)
                move = getRandomMove();
        }
        else if (mDifficultyLevel == DifficultyLevel.Expert) {

            // Try to win, but if that's not possible, block.
            // If that's not possible, move anywhere.
            move = getWinningMove();
            if (move == -1)
                move = getBlockingMove();
            if (move == -1)
                move = getRandomMove();
        }

        return move;
    }

    /**
     * Check for a winner and return a status value indicating who has won.
     * @return Return 0 if no winner or tie yet, 1 if it's a tie, 2 if X won,
     * or 3 if O won.
     */
    public int checkForWinner() {
        // Check horizontal wins
        for (int i = 0; i <= 6; i += 3)	{
            if (mBoard[i] == HUMAN_PLAYER &&
                    mBoard[i+1] == HUMAN_PLAYER &&
                    mBoard[i+2]== HUMAN_PLAYER)
                return 2;
            if (mBoard[i] == COMPUTER_PLAYER &&
                    mBoard[i+1]== COMPUTER_PLAYER &&
                    mBoard[i+2] == COMPUTER_PLAYER)
                return 3;
        }

        // Check vertical wins
        for (int i = 0; i <= 2; i++) {
            if (mBoard[i] == HUMAN_PLAYER &&
                    mBoard[i+3] == HUMAN_PLAYER &&
                    mBoard[i+6]== HUMAN_PLAYER)
                return 2;
            if (mBoard[i] == COMPUTER_PLAYER &&
                    mBoard[i+3] == COMPUTER_PLAYER &&
                    mBoard[i+6]== COMPUTER_PLAYER)
                return 3;
        }

        // Check for diagonal wins
        if ((mBoard[0] == HUMAN_PLAYER &&
                mBoard[4] == HUMAN_PLAYER &&
                mBoard[8] == HUMAN_PLAYER) ||
                (mBoard[2] == HUMAN_PLAYER &&
                        mBoard[4] == HUMAN_PLAYER &&
                        mBoard[6] == HUMAN_PLAYER))
            return 2;
        if ((mBoard[0] == COMPUTER_PLAYER &&
                mBoard[4] == COMPUTER_PLAYER &&
                mBoard[8] == COMPUTER_PLAYER) ||
                (mBoard[2] == COMPUTER_PLAYER &&
                        mBoard[4] == COMPUTER_PLAYER &&
                        mBoard[6] == COMPUTER_PLAYER))
            return 3;

        // Check for tie
        for (int i = 0; i < BOARD_SIZE; i++) {
            // If we find a number, then no one has won yet
            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER)
                return 0;
        }

        // If we make it through the previous loop, all places are taken, so it's a tie
        return 1;
    }

    /**
     * Marks as winner some of players ({@link #HUMAN_PLAYER} or {@link #COMPUTER_PLAYER}) or marks a tie (if player
     * is {@link #NO_BODY_PLAYER}).
     * @param player game's winner: {@link #COMPUTER_PLAYER} or {@link #HUMAN_PLAYER}. If {@link #NO_BODY_PLAYER}
     *               then a tie is marked.
     * @return Player victories (ot ties if {@code player} is {@link #NO_BODY_PLAYER}).
     */
    public int markWinner(char player) {
        int valor = 0;
        if(player == TicTacToeGame.HUMAN_PLAYER) {
            this.mHumanWins++;
            valor = this.mHumanWins;
        } else if(player == TicTacToeGame.COMPUTER_PLAYER) {
            this.mBugdroidWins++;
            valor = this.mBugdroidWins;
        } else {
            this.mTies++;
            valor = this.mTies;
        }

        return valor;
    }


    // PROPERTIES ////

    public char getBoardOccupant(int position) {
        return mBoard[position];
    }

    public char[] getBoardState() {
        return mBoard;
    }

    public void setBoardState(char[] board) {
        this.mBoard = board.clone();
    }

    public int getmBugdroidWins() {
        return mBugdroidWins;
    }

    public void setmBugdroidWins(int mBugdroidWins) {
        this.mBugdroidWins = mBugdroidWins;
    }

    public DifficultyLevel getDifficultyLevel() {
        return mDifficultyLevel;
    }

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        mDifficultyLevel = difficultyLevel;
    }

    public int getmHumanWins() {
        return mHumanWins;
    }

    public void setmHumanWins(int mHumanWins) {
        this.mHumanWins = mHumanWins;
    }

    public char getmPlayerTurn() {
        return mPlayerTurn;
    }

    public void setmPlayerTurn(char mPlayerTurn) {
        this.mPlayerTurn = mPlayerTurn;
    }

    public int getmTies() {
        return mTies;
    }

    public void setmTies(int mTies) {
        this.mTies = mTies;
    }


    // ENUMERATIONS ///

    // The computer's difficulty levels
    public enum DifficultyLevel {Easy, Harder, Expert};
}
