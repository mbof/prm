package com.github.prm;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class PrmActivity extends Activity {
	
	/** Image chooser */
	private Intent pictureChooser;
	
	/** Handler for click on image picker */
	private OnClickListener chooseButtonClickListener;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Intent pictureChoosingIntent = new Intent(Intent.ACTION_GET_CONTENT)
			.addCategory(Intent.CATEGORY_OPENABLE)
			.setType("image/*");
      
        pictureChooser = Intent.createChooser(pictureChoosingIntent,
        		getString(R.string.pick_image_action_title));
        
        chooseButtonClickListener = new OnClickListener() {
        	public void onClick(View view) {
        		Log.d("Prm", "Choosing photo");
        		startActivityForResult(pictureChooser, PHOTO_CHOSEN_REQUEST);
        	}
        };
        
        findViewById(R.id.pick_image_button)
        	.setOnClickListener(chooseButtonClickListener);        
        
        preview = (ImageView) findViewById(R.id.preview);
        
        Log.d("Prm", "All done");
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);

		System.err.println("Activity result");
    	
    	switch (requestCode) {
    	case PHOTO_CHOSEN_REQUEST:
    		if (resultCode == RESULT_OK) {
    			System.err.println("Photo chosen");
    			Uri imageUri = data.getData();
    			try {
    				image = BitmapFactory.decodeStream(
    							getContentResolver()
    								.openInputStream(imageUri)
    						);
    				
    				preview.setImageBitmap(image);
    				
    			} catch (FileNotFoundException e) {
    				e.printStackTrace();
    			}
    		}
    		break;
    	}
    }
    
    private Bitmap image;
    
    private ImageView preview;
    
    static final int PHOTO_CHOSEN_REQUEST = 0;
    
}