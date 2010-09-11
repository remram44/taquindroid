package fr.supelec.rez_gif;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class Options extends Activity {

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);

        Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show();

        Intent data = new Intent();
        data.setData(null);
        data.putExtra("width", 3);
        data.putExtra("height", 3);
        setResult(RESULT_OK, data);
        //
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run()
            {
                Options.this.finish();
            }
        }, 1500);
        //
        //finish();
    }
}
