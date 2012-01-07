package com.github.prm;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.ContentResolver;
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

	/** The image to be projected */
	private Bitmap image;
    
	/** A place to show a thumbnail of image */
    private ImageView preview;
    
    /** A place to show what is going to be projected */
    private ImageView projectionView;
    
    /** Request types that onActivityResult will be called back with */
    static final int PHOTO_CHOSEN_REQUEST = 0;
    
	/** Image chooser */
	private Intent pictureChooser;
	
	/** Handler for click on "Choose image" button  */
	private OnClickListener chooseButtonClickListener;
	
    /** Called when the activity is first created */
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
        
        projectionView = (ImageView) findViewById(R.id.projection_view);
        
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
    				ContentResolver cr = getContentResolver();
    				
    				image = BitmapFactory.decodeStream
    						(cr.openInputStream(imageUri));
    				
    				preview.setImageBitmap(image);
    				
    			} catch (FileNotFoundException e) {
    				e.printStackTrace();
    			}
    		}
    		break;
    	}
    }
    
}