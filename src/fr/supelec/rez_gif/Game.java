package fr.supelec.rez_gif;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Game extends Activity implements GameView.EndGameListener {

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        // Get the wanted image file from the Intent
        Intent intent = getIntent();
        String image_file = intent.getStringExtra("image_file");
        int width = intent.getIntExtra("width", 3);
        int height = intent.getIntExtra("height", 3);
        
        // Set up the view
        GameView game = new GameView(this, image_file, width, height);
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
