package fr.supelec.rez_gif;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EndGame extends Activity {

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
        
        // TODO : Display game time
        // TODO : Draw the original image, one way or the other

        TextView t = new TextView(this);
        t.setText(getString(R.string.end_infos, width, height));
        LinearLayout layout = new LinearLayout(this);
        layout.addView(t);
        setContentView(layout);
    }
}
