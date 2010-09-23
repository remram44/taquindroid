package fr.supelec.rez_gif;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Options extends Activity {

    protected static final int REQ_PICK_IMAGE = 1;
    protected Uri m_SelectedImage = null;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);
        
        final Button change_image = (Button) findViewById(R.id.change_image);
        change_image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQ_PICK_IMAGE);
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
                data.putExtra("width", 3);
                data.putExtra("height", 3);
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
            if(resultCode == RESULT_OK){  
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
