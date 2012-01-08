package com.github.prm;

import java.io.FileNotFoundException;

import com.github.prm.PrmActivity.Projector;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PrmActivity extends Activity {

	/** The image to be projected */
	private Bitmap image;
    
	/** A place to show a thumbnail of image */
    private ImageView preview;
    
    /** Request types that onActivityResult will be called back with */
    static final int PHOTO_CHOSEN_REQUEST = 0;
    
	/** Image chooser */
	private Intent pictureChooser;
	
	/** Handler for click on "Choose image" button  */
	private OnClickListener chooseButtonClickListener;

	private Projector projector;
	
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
                
        projector = new Projector((Button) findViewById(R.id.start_button),
        		(ImageView) findViewById(R.id.projection_view),
        		(TextView) findViewById(R.id.projector_coordinates));
        
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
    
    class Projector implements SensorEventListener, OnClickListener {
    	
    	private Button startButton;
		private ImageView projectionView;
		private TextView projectionTextView;
		private boolean isProjecting;
		private SensorManager sensorManager;
		private Sensor rotationSensor;

		public Projector(Button startButton, ImageView projectionView, TextView projectionTextView) {
    		this.startButton = startButton;
    		this.projectionView = projectionView;
    		this.projectionTextView = projectionTextView;
    		this.isProjecting = false;
    		this.init();
    	}
		
		protected void init() {
    		startButton.setOnClickListener(this);
    		projectionView.setBackgroundColor(android.R.color.darker_gray);
    		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    		rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    		if (rotationSensor == null)
    			projectionTextView.setText("Error: no rotation sensor");
		}
    	
    	synchronized public void start() {
    		startButton.setText(getString(R.string.stop_button_text));
    		projectionTextView.setText("Projecting...");
    		projectionView.setBackgroundColor(android.R.color.white);
    		sensorManager.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_UI);
    		this.isProjecting = true;
    	}
    	
    	synchronized public void stop() {
    		startButton.setText(getString(R.string.start_button_text));
    		projectionTextView.setText("Stopped");
    		this.isProjecting = false;
    	}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// Do nothing
		}

		public void onSensorChanged(SensorEvent event) {
			String debugText = "Sensor: ";
			for (int i = 0; i < event.values.length; i++)
				debugText += event.values[i] + " ";
			projectionTextView.setText(debugText);
		}

		public void onClick(View v) {
			if(this.isProjecting)
				this.stop();
			else
				this.start();
		}
    	
    }
    
    protected void onPause() {
    	super.onPause();
    	projector.stop();
    }
        
}