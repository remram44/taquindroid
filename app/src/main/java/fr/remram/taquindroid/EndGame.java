package fr.remram.taquindroid;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class EndGame extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.endgame);

        // Get the parameters from the Indent
        Intent intent = getIntent();
        int width = intent.getIntExtra("width", 3);
        int height = intent.getIntExtra("height", 3);

        // Find the requested image
        Uri selectedImage = intent.getData();
        ImageView image = (ImageView) findViewById(R.id.endgame_image);
        if(selectedImage != null)
            image.setImageURI(selectedImage);
        else
            image.setImageDrawable(getResources().getDrawable(R.drawable.image));

        float time = intent.getLongExtra("time", 0) / 1000.0f;
        int moves = intent.getIntExtra("moves", 0);

        TextView t = (TextView) findViewById(R.id.endgame_label);
        t.setText(getString(R.string.end_info, width, height, time, moves));
    }

}
