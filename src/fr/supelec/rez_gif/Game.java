package fr.supelec.rez_gif;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Game extends Activity implements GameView.EndGameListener {

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        // Set up the view
        GameView game = new GameView(this, 3, 3);
        game.setEndGameListener(this);
        setContentView(game);
    }

    public void onGameEnded()
    {
        Intent end_game = new Intent();
        end_game.setClass(this, EndGame.class);
        startActivity(end_game);
        finish();
    }

}
