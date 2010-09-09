package fr.supelec.rez_gif;

import android.app.Activity;
import android.os.Bundle;

public class Game extends Activity {

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        // Set up the view
        GameView game = new GameView(this, 3, 3);
        setContentView(game);
    }
}
