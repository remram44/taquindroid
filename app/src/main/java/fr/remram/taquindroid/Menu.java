package fr.remram.taquindroid;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends Activity {

    private static final int CHANGE_OPTIONS = 1;
    private Uri m_ImageFile = null;
    private int m_Width = 3;
    private int m_Height = 3;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        final Button play = (Button) findViewById(R.id.menu_play);
        play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent launch_game = new Intent();
                launch_game.setClass(Menu.this, Game.class);
                launch_game.setData(m_ImageFile);
                launch_game.putExtra("width", m_Width);
                launch_game.putExtra("height", m_Height);
                startActivity(launch_game);
            }
        });

        final Button options = (Button) findViewById(R.id.menu_options);
        options.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent change_options = new Intent();
                change_options.setClass(Menu.this, Options.class);
                // Pass the current parameters
                change_options.setData(m_ImageFile);
                change_options.putExtra("width", m_Width);
                change_options.putExtra("height", m_Height);
                startActivityForResult(change_options, CHANGE_OPTIONS);
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

    @Override
    protected void onActivityResult(int request_code, int result_code, Intent data)
    {
        super.onActivityResult(request_code, result_code, data);

        switch(request_code)
        {
        case CHANGE_OPTIONS:
            if(result_code == RESULT_OK)
            {
                m_ImageFile = data.getData();
                m_Width = data.getIntExtra("width", Options.DEFAULT_SIZE);
                m_Height = data.getIntExtra("height", Options.DEFAULT_SIZE);
            }
            break;
        }
    }

}
