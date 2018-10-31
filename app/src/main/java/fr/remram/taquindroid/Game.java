package fr.remram.taquindroid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

public class Game extends Activity implements GameView.EndGameListener {

    private Uri m_SelectedImage = null;
    private int m_Width;
    private int m_Height;

    private final Stopwatch m_GameTime = new Stopwatch();

    /** Called when the activity is first created. */
    @Override
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
            image = ImageTools.getBitmap(this, m_SelectedImage);

        if(image == null)
            image = BitmapFactory.decodeResource(getResources(), R.drawable.image);

        // Set up the view
        GameView game = new GameView(this, image, m_Width, m_Height);
        game.setEndGameListener(this);
        setContentView(game);
    }

    @Override
    protected void onPause() {
        super.onPause();
        m_GameTime.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        m_GameTime.start();
    }

    @Override
    public void onGameEnded(int moves)
    {
        Intent end_game = new Intent();
        end_game.setClass(this, EndGame.class);
        end_game.setData(m_SelectedImage);
        end_game.putExtra("width", m_Width);
        end_game.putExtra("height", m_Height);
        end_game.putExtra("time", m_GameTime.milliseconds());
        end_game.putExtra("moves", moves);
        startActivity(end_game);
        finish();
    }

}
