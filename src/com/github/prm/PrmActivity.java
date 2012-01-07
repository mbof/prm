package com.github.prm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

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
        		startActivity(pictureChooser);
        	}
        };
        
        findViewById(R.id.pick_image_button)
        	.setOnClickListener(chooseButtonClickListener);
        
    }
}