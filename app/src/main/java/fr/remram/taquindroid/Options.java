package fr.remram.taquindroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Options extends Activity {

    protected static final int REQ_PICK_IMAGE = 1;
    protected Uri m_SelectedImage = null;
    private int m_Size;
    public static final int MIN_SIZE = 3;
    public static final int MAX_SIZE = 8;
    public static final int DEFAULT_SIZE = 3;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);

        // Load the current parameters
        Intent intent = getIntent();
        m_SelectedImage = intent.getData();
        updateImage();
        m_Size = intent.getIntExtra("width", DEFAULT_SIZE);
        if(m_Size < MIN_SIZE || m_Size > MAX_SIZE)
            m_Size = DEFAULT_SIZE;

        // The control used to change the grid's size
        final Button change_size = (Button) findViewById(R.id.change_size);
        final String[] sizes = getResources().getStringArray(R.array.change_size);
        change_size.setText(sizes[m_Size - MIN_SIZE]);
        change_size.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v)
           {
               // Choose the new size
               AlertDialog.Builder builder = new AlertDialog.Builder(Options.this);
               builder.setTitle(R.string.change_size_title);
               builder.setItems(sizes, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int item)
                   {
                       m_Size = MIN_SIZE + item;
                       change_size.setText(sizes[item]);
                   }
               });
               builder.create().show();
           }
        });

        final Button change_image = (Button) findViewById(R.id.change_image);
        change_image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                // http://stackoverflow.com/a/25268677/711380
                if(android.os.Build.VERSION.SDK_INT >= 19)
                {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQ_PICK_IMAGE);
                }
                else
                {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQ_PICK_IMAGE);
                }
            }
        });

        final Button reset_image = (Button) findViewById(R.id.reset_image);
        reset_image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                m_SelectedImage = null;
                updateImage();
            }
        });

        final Button ok = (Button) findViewById(R.id.options_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent data = new Intent();
                data.setData(m_SelectedImage);
                data.putExtra("width", m_Size);
                data.putExtra("height", m_Size);
                Options.this.setResult(RESULT_OK, data);
                Options.this.finish();
            }
        });
    }

    /** Called when the image have been selected. */
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
        case REQ_PICK_IMAGE:
            if(resultCode == RESULT_OK)
            {
                m_SelectedImage = imageReturnedIntent.getData();
                updateImage();
            }
        }
    }

    /** Function used to display the right image in the preview area. */
    protected void updateImage()
    {
        ImageView image = (ImageView) findViewById(R.id.preview_image);
        if(m_SelectedImage != null)
            image.setImageURI(m_SelectedImage);
        else
            image.setImageDrawable(getResources().getDrawable(R.drawable.image));
    }

}
