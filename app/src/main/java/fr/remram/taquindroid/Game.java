package fr.remram.taquindroid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.IOException;

public class Game extends Activity implements GameView.EndGameListener {

    private Uri m_SelectedImage = null;
    private int m_Width;
    private int m_Height;
    
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        // Get the parameters from the Indent
        Intent intent = getIntent();
        m_Width = intent.getIntExtra("width", Options.DEFAULT_SIZE);
        m_Height = intent.getIntExtra("height", Options.DEFAULT_SIZE);
        
        // Find the requested image
        m_SelectedImage = intent.getData();
        Bitmap image = null;
        if(m_SelectedImage != null)
        {
            try {
                ParcelFileDescriptor descriptor = getContentResolver().openFileDescriptor(m_SelectedImage, "r");
                if(descriptor != null)
                {
                    FileDescriptor fileDescriptor = descriptor.getFileDescriptor();
                    image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                    descriptor.close();
                }
            } catch(IOException e) {
                Log.e("TAQUINDROID", "Couldn't open requested image file", e);
            }
        }

        if(image == null)
            image = BitmapFactory.decodeResource(getResources(), R.drawable.image);
        
        // Set up the view
        GameView game = new GameView(this, image, m_Width, m_Height);
        game.setEndGameListener(this);
        setContentView(game);
    }

    public void onGameEnded()
    {
        Intent end_game = new Intent();
        end_game.setClass(this, EndGame.class);
        end_game.setData(m_SelectedImage);
        end_game.putExtra("width", m_Width);
        end_game.putExtra("height", m_Height);
        startActivity(end_game);
        finish();
    }

}
