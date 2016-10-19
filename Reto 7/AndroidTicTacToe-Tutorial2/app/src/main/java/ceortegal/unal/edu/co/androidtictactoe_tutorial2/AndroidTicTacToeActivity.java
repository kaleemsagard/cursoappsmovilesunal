package ceortegal.unal.edu.co.androidtictactoe_tutorial2;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class AndroidTicTacToeActivity extends AppCompatActivity {

    static final int DIALOG_QUIT_ID = 0;
    static final int DIALOG_ABOUT_ID = 1;

    // Represents the internal state of the game
    private TicTacToeGame mGame;
    private boolean mGameOver = false;
    private SoundLevel mSoundLevel = SoundLevel.ON;
    private SharedPreferences mPrefs;

    // Various text displayed
    private TextView mInfoTextView;
    private TextView mHumanScoreTextView;
    private TextView mComputerScoreTextView;
    private TextView mTiesScoreTextView;
    private BoardView mBoardView;

    // Sound variables
    private MediaPlayer mHumanMediaPlayer;
    private MediaPlayer mComputerMediaPlayer;

    // Listen for touches on the board
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {

            // Determine which cell was touched
            int col = (int) event.getX() / mBoardView.getBoardCellWidth();
            int row = (int) event.getY() / mBoardView.getBoardCellHeight();
            int pos = row * 3 + col;

            if (!mGameOver && setMove(TicTacToeGame.HUMAN_PLAYER, pos))	{
                // If no winner yet, let the computer make a move
                int winner = mGame.checkForWinner();
                if (winner == 0) {
                    giveTurnToComputer();
                } else {
                    updateInfoGameStatus(winner);
                }
            }

            // So we aren't notified of continued events when finger is moved
            return false;
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RESULT_CANCELED) {
            // Apply potentially new settings

            mSoundLevel = mPrefs.getBoolean("sound", true) ? SoundLevel.ON : SoundLevel.OFF;

            String difficultyLevel = mPrefs.getString("difficulty_level",
                    getResources().getString(R.string.difficulty_expert));

            if (difficultyLevel.equals(getResources().getString(R.string.difficulty_easy)))
                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
            else if (difficultyLevel.equals(getResources().getString(R.string.difficulty_harder)))
                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
            else
                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_tic_tac_toe);

        mInfoTextView = (TextView) findViewById(R.id.information);
        mHumanScoreTextView = (TextView) findViewById(R.id.state_human_wins_value);
        mComputerScoreTextView = (TextView) findViewById(R.id.state_bugdroid_wins_value);
        mTiesScoreTextView = (TextView) findViewById(R.id.state_ties_value);

        mGame = new TicTacToeGame();
        mBoardView = (BoardView) findViewById(R.id.play_grid);
        mBoardView.setGame(mGame);
        mBoardView.setOnTouchListener(mTouchListener);

        // Load preferences
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Restore the scores
        mGame.setmHumanWins(mPrefs.getInt("humanWins", 0));
        mGame.setmBugdroidWins(mPrefs.getInt("computerWins", 0));
        mGame.setmTies(mPrefs.getInt("ties", 0));

        mSoundLevel = mPrefs.getBoolean("sound", true) ? SoundLevel.ON : SoundLevel.OFF;
        String difficultyLevel = mPrefs.getString("difficulty_level",
                getResources().getString(R.string.difficulty_harder));
        if (difficultyLevel.equals(getResources().getString(R.string.difficulty_easy)))
            mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
        else if (difficultyLevel.equals(getResources().getString(R.string.difficulty_harder)))
            mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
        else
            mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);

        if (savedInstanceState == null) {
            startNewGame();
        }
        else {
            // Restore the game's state
            mGame.setBoardState(savedInstanceState.getCharArray("board"));
            mGameOver = savedInstanceState.getBoolean("gameOver");
            mInfoTextView.setText(savedInstanceState.getCharSequence("info"));
            mGame.setmPlayerTurn(savedInstanceState.getChar("playerTurns"));
        }
        displayScores();
        if(!mGameOver && mGame.getmPlayerTurn() == mGame.COMPUTER_PLAYER) {
            giveTurnToComputer();
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch(id) {
            case DIALOG_ABOUT_ID:
                // Create the about confirmation dialog

                Context context = getApplicationContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.about_dialog, null);
                builder.setView(layout);
                builder.setPositiveButton("OK", null);
                dialog = builder.create();

                break;

            case DIALOG_QUIT_ID:
                // Create the quit confirmation dialog

                builder.setMessage(R.string.quit_question)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                AndroidTicTacToeActivity.this.finish();
                            }
                        })
                        .setNegativeButton(R.string.no, null);
                dialog = builder.create();

                break;
        }

        return dialog;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                startNewGame();
                return true;
            case R.id.reset_game:
                resetGame();
                return true;
            case R.id.settings:
                startActivityForResult(new Intent(this, Settings.class), 0);
                return true;
            case R.id.quit:
                showDialog(DIALOG_QUIT_ID);
                return true;
            case R.id.about:
                showDialog(DIALOG_ABOUT_ID);
                return true;
        }
        return false;

    }

    @Override
    protected void onPause() {
        super.onPause();

        mHumanMediaPlayer.release();
        mComputerMediaPlayer.release();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.aboriginal);
        mComputerMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.conqueror);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharArray("board", mGame.getBoardState());
        outState.putBoolean("gameOver", mGameOver);
        outState.putCharSequence("info", mInfoTextView.getText());
        outState.putChar("playerTurns", mGame.getmPlayerTurn());
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Save the current scores
        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putInt("humanWins", this.mGame.getmHumanWins());
        ed.putInt("computerWins", this.mGame.getmBugdroidWins());
        ed.putInt("ties", this.mGame.getmTies());
        ed.commit();
    }



    // PRIVATE METHODS ////
    private void displayScores() {
        this.mHumanScoreTextView.setText(String.valueOf(this.mGame.getmHumanWins()));
        this.mComputerScoreTextView.setText(String.valueOf(this.mGame.getmBugdroidWins()));
        this.mTiesScoreTextView.setText(String.valueOf(mGame.getmTies()));
    }

    /**
     * Gives the turn to the computer player.
     */
    private void giveTurnToComputer() {
        mInfoTextView.setText(R.string.turno_bugdroid);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                int move = mGame.getComputerMove();
                //System.out.println("Bugdroid se está moviendo hacia " + (move + 1));
                setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                updateInfoGameStatus(mGame.checkForWinner());
            }
        }, 2000);
    }

    private boolean setMove(char player, int location) {
        boolean var = false;
        if (mGame.setMove(player, location)) {
            mBoardView.invalidate();   // Redraw the board
            if(mSoundLevel.equals(SoundLevel.ON)) {
                try {
                    if (player == TicTacToeGame.HUMAN_PLAYER) {
                        mHumanMediaPlayer.start();
                    } else {
                        mComputerMediaPlayer.start();
                    }
                } catch (IllegalStateException ex) {
                    // Se puede presentar al jugar el turno y rotar la pantalla antes de que sea
                    // liberado el reproductor multimedia. El sonido tiene baja prioridad, así
                    // que no se ejecuta ninguna acción.
                }
            }
            var = true;
        }
        return var;
    }

    private void updateInfoGameStatus(int winner) {
        if (winner == 0) {
            mInfoTextView.setText(R.string.turno_humano);
        } else if (winner == 1) {
            mInfoTextView.setText(R.string.estado_empate);
            mGame.markWinner(TicTacToeGame.NO_BODY_PLAYER);
            mGameOver = true;
        } else if (winner == 2) {
            String defaultMessage = getResources().getString(R.string.estado_humano_gana);
            mInfoTextView.setText(mPrefs.getString("victory_message", defaultMessage));
            mGame.markWinner(TicTacToeGame.HUMAN_PLAYER);
            mGameOver = true;
        } else {
            mInfoTextView.setText(R.string.estado_bugdroid_gana);
            mGame.markWinner(TicTacToeGame.COMPUTER_PLAYER);
            mGameOver = true;
        }
        displayScores();
    }

    /**
     * Reset the game values; starts from zero.
     */
    private void resetGame() {
        this.mGame.setmTies(0);
        this.mGame.setmBugdroidWins(0);
        this.mGame.setmHumanWins(0);
        displayScores();
        startNewGame();
    }

    // Set up the game board.
    private void startNewGame() {
        mGame.clearBoard();
        mGameOver = false;

        mGame.clearBoard();
        mBoardView.invalidate();   // Redraw the board

        // Human goes first
        mInfoTextView.setText(R.string.primer_turno_humano);
    }


    // ENUMERATORS ////
    // The computer's difficulty levels
    public enum SoundLevel {OFF, ON};
}
