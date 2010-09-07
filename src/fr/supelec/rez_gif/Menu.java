package fr.supelec.rez_gif;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Menu extends Activity {
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        final Button play = (Button) findViewById(R.id.menu_play);
        play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Toast.makeText(Menu.this, "TODO", Toast.LENGTH_SHORT).show();
            }
        });

        final Button scores = (Button) findViewById(R.id.menu_scores);
        scores.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Toast.makeText(Menu.this, "TODO", Toast.LENGTH_SHORT).show();
            }
        });

        final Button quit = (Button) findViewById(R.id.menu_quit);
        quit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Menu.this.finish();
            }
        });
    }
}
