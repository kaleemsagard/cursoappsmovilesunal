package ceortegal.unal.edu.co.androidtictactoe_tutorial2;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AndroidTicTacToeActivity extends AppCompatActivity {

    static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_QUIT_ID = 1;
    static final int DIALOG_ABOUT_ID = 2;
    static final int DIALOG_SOUND_ID = 3;

    // Represents the internal state of the game
    private TicTacToeGame mGame;
    private boolean mGameOver = false;
    private SoundLevel mSoundLevel = SoundLevel.ON;

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
                    mInfoTextView.setText(R.string.turno_bugdroid);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            int move = mGame.getComputerMove();
                            //System.out.println("Bugdroid se está moviendo hacia " + (move + 1));
                            setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                            updateInfoGameStatus(mGame.checkForWinner());
                        }
                    }, 2000);
                } else {
                    updateInfoGameStatus(winner);
                }
            }

            // So we aren't notified of continued events when finger is moved
            return false;
        }
    };

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

        startNewGame();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch(id) {
            case DIALOG_SOUND_ID:
                builder.setTitle(R.string.sound_choose);
                final CharSequence[] soundLevels = {
                        getResources().getString(R.string.sound_off),
                        getResources().getString(R.string.sound_on)};
                int soundSelected = mSoundLevel.ordinal();
                builder.setSingleChoiceItems(soundLevels, soundSelected,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                dialog.dismiss();   // Close dialog

                                if(item == 0) {
                                    mSoundLevel = SoundLevel.OFF;
                                } else {
                                    mSoundLevel = SoundLevel.ON;
                                }

                                // Display the selected difficulty level
                                Toast.makeText(getApplicationContext(), soundLevels[item],
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                dialog = builder.create();

                break;

            case DIALOG_DIFFICULTY_ID:

                builder.setTitle(R.string.difficulty_choose);

                final CharSequence[] levels = {
                        getResources().getString(R.string.difficulty_easy),
                        getResources().getString(R.string.difficulty_harder),
                        getResources().getString(R.string.difficulty_expert)};

                // selected is the radio button that should be selected.
                int selected = mGame.getDifficultyLevel().ordinal();

                builder.setSingleChoiceItems(levels, selected,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                dialog.dismiss();   // Close dialog

                                if(item == 0) {
                                    mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
                                } else if(item == 1) {
                                    mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
                                } else {
                                    mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);
                                }

                                // Display the selected difficulty level
                                Toast.makeText(getApplicationContext(), levels[item],
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                dialog = builder.create();

                break;

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
            case R.id.sound:
                showDialog(DIALOG_SOUND_ID);
                return true;
            case R.id.ai_difficulty:
                showDialog(DIALOG_DIFFICULTY_ID);
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


    // PRIVATE METHODS ////
    private boolean setMove(char player, int location) {
        boolean var = false;
        if (mGame.setMove(player, location)) {
            mBoardView.invalidate();   // Redraw the board
            if(mSoundLevel.equals(SoundLevel.ON)) {
                if (player == TicTacToeGame.HUMAN_PLAYER) {
                    mHumanMediaPlayer.start();
                } else {
                    mComputerMediaPlayer.start();
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
            mTiesScoreTextView.setText(String.valueOf(mGame.markWinner(TicTacToeGame.NO_BODY_PLAYER)));
            mGameOver = true;
        } else if (winner == 2) {
            mInfoTextView.setText(R.string.estado_humano_gana);
            mHumanScoreTextView.setText(String.valueOf(mGame.markWinner(TicTacToeGame.HUMAN_PLAYER)));
            mGameOver = true;
        } else {
            mInfoTextView.setText(R.string.estado_bugdroid_gana);
            mComputerScoreTextView.setText(String.valueOf(mGame.markWinner(TicTacToeGame.COMPUTER_PLAYER)));
            mGameOver = true;
        }
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

    // The computer's difficulty levels
    public enum SoundLevel {OFF, ON};
}
