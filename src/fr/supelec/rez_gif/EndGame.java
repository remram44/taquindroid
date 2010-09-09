package fr.supelec.rez_gif;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EndGame extends Activity {

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        TextView t = new TextView(this);
        t.setText("TODO !");
        LinearLayout layout = new LinearLayout(this);
        layout.addView(t);
        setContentView(layout);
    }
}
