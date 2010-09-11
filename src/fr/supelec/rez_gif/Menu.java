package fr.supelec.rez_gif;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Menu extends Activity {

    private static final int CHANGE_OPTIONS = 1;
    private String m_ImageFile = null;
    private int m_Width = 3;
    private int m_Height = 3;

    /** Called when the activity is first created. */
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
                launch_game.putExtra("image_file", m_ImageFile);
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
                startActivityForResult(change_options, CHANGE_OPTIONS);
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

    protected void onActivityResult(int request_code, int result_code, Intent data)
    {
        super.onActivityResult(request_code, result_code, data);
        
        switch(request_code)
        {
        case CHANGE_OPTIONS:
            if(result_code == RESULT_OK)
            {
                Uri selectedImage = data.getData();
                if(selectedImage == null)
                    m_ImageFile = null;
                else
                {
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    m_ImageFile = cursor.getString(columnIndex);
                    cursor.close();
                }

                m_Width = data.getIntExtra("width", 3);
                m_Height = data.getIntExtra("height", 3);
            }
            break;
        }
    }

}
