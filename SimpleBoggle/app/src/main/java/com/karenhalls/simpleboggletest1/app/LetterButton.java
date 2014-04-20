package com.karenhalls.simpleboggletest1.app;

import android.content.Context;
import android.widget.Button;

/**
 * This extension of Button adds class variables to store its coordinates within the Boggle grid
 * and a flag to indicate if the user has selected it.
 * Created by hallsk and levoniaz on 4/8/14.
 */
public class LetterButton extends Button {

    int x, y;
    boolean buttonSelected;

    public LetterButton(Context context, int x, int y, boolean buttonSelected) {
        super(context);
        this.x = x;
        this.y = y;
        this.buttonSelected = buttonSelected;
    }
}
