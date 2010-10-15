package fr.supelec.rez_gif;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

public class Game extends Activity implements GameView.EndGameListener {

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        // Get the parameters from the Indent
        Intent intent = getIntent();
        int width = intent.getIntExtra("width", 3);
        int height = intent.getIntExtra("height", 3);
        
        // Find the requested image
        Uri selectedImage = intent.getData();
        String image_file = null;
        if(selectedImage != null)
        {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            image_file = cursor.getString(columnIndex);
            cursor.close();
        }
        
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
