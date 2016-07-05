package fr.remram.taquindroid;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.IOException;

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
        {
            try {
                ParcelFileDescriptor descriptor = getContentResolver().openFileDescriptor(m_SelectedImage, "r");
                if(descriptor != null)
                {
                    FileDescriptor fileDescriptor = descriptor.getFileDescriptor();
                    image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                    descriptor.close();

                    int orientation = getOrientation(m_SelectedImage);
                    if(orientation > 0)
                    {
                        Matrix matrix = new Matrix();
                        matrix.postRotate(orientation);
                        image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
                    }
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

    // From http://stackoverflow.com/a/8661569/711380
    public int getOrientation(Uri photoUri)
    {
        Cursor cursor = getContentResolver().query(photoUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

        int result = -1;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = cursor.getInt(0);
            }
            cursor.close();
        }

        return result;
    }

}
