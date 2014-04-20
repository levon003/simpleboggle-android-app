package com.karenhalls.simpleboggletest1.app;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * LetterButtonTouchListener controls user interaction with the set of Letter Buttons.
 * It passes pressed letters on to the Word Builder and controls highlighting of pressed buttons
 * and the validation of appropriate next button presses.
 * Created by hallsk and levoniaz on 4/8/14.
 */
public class LetterButtonTouchListener implements View.OnTouchListener {

    LetterButton previouslyPressed = null;

    WordBuilder wordBuilder;

    public LetterButtonTouchListener(WordBuilder builder) {
        wordBuilder = builder;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (!wordBuilder.isLoaded())
            return false;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            LetterButton source = (LetterButton) view;
            if (source.buttonSelected)
                return false;
            if (previouslyPressed == null ||
                    (Math.abs(previouslyPressed.x - source.x) == 0 && Math.abs(previouslyPressed.y - source.y) == 1) ||
                    (Math.abs(previouslyPressed.x - source.x) == 1 && Math.abs(previouslyPressed.y - source.y) == 0)) {
                if (previouslyPressed != null) previouslyPressed.setBackgroundColor(Color.rgb(52, 209, 178));
                previouslyPressed = source;
                source.setBackgroundColor(Color.rgb(0, 163, 131));
                source.buttonSelected = true;
                wordBuilder.addLetter(source.getText().charAt(0));
            } else {
                return false;
            }
        }
        return true;
    }
}
