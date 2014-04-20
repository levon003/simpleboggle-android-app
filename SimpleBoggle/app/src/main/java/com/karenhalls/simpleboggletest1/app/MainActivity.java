/*
 * SimpleBoggle
 * Play a game of boggle - without the stress of the clock!
 * 
 * Written by Zachary Levonian and Karen Halls
 */

package com.karenhalls.simpleboggletest1.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends Activity {

    LetterButton[] buttons = new LetterButton[16];
    ArrayAdapter<String> wordListAdapter;
    WordBuilder wordBuilder;

    SharedPreferences preferences;

    LetterButtonTouchListener letterButtonListener;

    Handler handler = new Handler();

    public final static int LETTER_BUTTON_BACKGROUND = Color.rgb(144, 239, 226);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = this.getSharedPreferences("com.karenhalls.simpleboggletest1.app", Context.MODE_PRIVATE);

        Button oopsButton = (Button) this.findViewById(R.id.oops_button);
        oopsButton.setOnTouchListener(new OopsButtonListener());

        ArrayList<String> wordList = new ArrayList<String>();

        // if game was previously saved, reload the list of words already made
        String savedWord;
        int key = 0;
        while ((savedWord = preferences.getString("l"+String.valueOf(key), null)) != null) {
            wordList.add(savedWord);
            key++;
        }
        wordListAdapter = new ArrayAdapter(this.findViewById(R.id.list_view).getContext(), R.layout.wordlist_cell, R.id.wordlist_cell_textview, wordList);
        ((ListView)this.findViewById(R.id.list_view)).setAdapter(wordListAdapter);

        wordBuilder = new WordBuilder(wordListAdapter, this);
        letterButtonListener = new LetterButtonTouchListener(wordBuilder);

        // makes the boggle buttons
        GridLayout grid = (GridLayout) findViewById(R.id.grid_layout);
        for (int i = 0; i < 16; i++) {
            LetterButton newButton = new LetterButton(this, i / 4, i % 4, false);
            buttons[i] = newButton;
            grid.addView(newButton, i);
            newButton.setBackgroundColor(LETTER_BUTTON_BACKGROUND);
            newButton.setOnTouchListener(letterButtonListener);
        }
        if (preferences.getString(String.valueOf(0), null) == null) {
            startNewGame();
        } else {
            for (int i = 0; i < 16; i++)
                buttons[i].setText(preferences.getString(String.valueOf(i), "-"));
        }
    }

    /**
     * Saves the current board and word state whenever the activity pauses.
     */
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        for (int i = 0; i < 16; i++) {
            editor.putString(String.valueOf(i), buttons[i].getText().toString());
        }
        for (int i = 0; i < wordListAdapter.getCount(); i++) {
            editor.putString("l" + i, wordListAdapter.getItem(i));
        }

        editor.commit();
    }

    /**
     * Inflates the options menu.
     * @param menu The menu to inflate
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Enables the handling of action bar item clicks
     * @param item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.new_game) {
            startNewGame();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Start a new game of Boggle, clearing the list of found words and resetting the board.
     */
    private void startNewGame() {
        clearSelections();
        for (Button b : buttons) {
            // random capital ascii value generated
            Random random = new Random();
            int randomNum = random.nextInt((90 - 65) + 1) + 65;
            char randomLetter = (char)randomNum;
            b.setText(String.valueOf(randomLetter));
        }
        wordBuilder.clearWord();
        wordListAdapter.clear();
    }

    /**
     * Clears all selected letters
     */
    public void clearSelections() {
        letterButtonListener.previouslyPressed = null;
        for (LetterButton b : buttons) {
            b.setBackgroundColor(LETTER_BUTTON_BACKGROUND);
            b.buttonSelected = false;
        }
    }

    /**
     * This listener clears the current letter selections when clicked
     */
    private class OopsButtonListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                clearSelections();
                wordBuilder.clearWord();
            }
            return true;
        }
    }
}
