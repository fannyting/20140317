package com.example.takephoto;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.xml.sax.helpers.ParserFactory;

import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.SaveCallback;
import com.parse.ParseException;

import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private  ImageView imageview;
	private static final int REQUEST_CODE_TAKE_PHOTO = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Parse.initialize(this,  "nlIQe4IIlQBDWDgk3k64XyFsQEzI2EYnuOXtsDr1", "f5Wa8GRUoApMlfKVE9KxjrjuCvjYc2CJg6X0MXoP");
		
		setContentView(R.layout.activity_main);
		imageview = (ImageView) findViewById(R.id.imageView1);
		
		}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch (item.getItemId()) {
		case R.id.action_settings:
			Log.d("debug", "settings");
			return true;
		case R.id.action_takephoto:
			Intent intent = new Intent();
			intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);			
			//Log.d("debug", "takephoto");
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
			if (resultCode == RESULT_OK){
				Bitmap image = (Bitmap) data.getExtras().get("data");
				save(image);
				imageview.setImageBitmap(image);
			}
		}
		
	}
	
	private void save(Bitmap bitmap) {
		File imageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

		if (!imageDir.exists()) {
			imageDir.mkdir();
		}
		
		File imageFile = new File(imageDir, "photo.png");
		
		try {
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(imageFile));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, bos);
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, baos);
			
			bos.flush();
			bos.close();
			baos.flush();
			baos.close();
			Log.d("debug", "filePath=" + imageFile.getAbsolutePath());
			
			final ParseFile file = new ParseFile("photo.png", baos.toByteArray());
			file.saveInBackground(new SaveCallback() {
				@Override
				public void done(ParseException e) {
					Log.d("debug", file.getUrl());
				}
				
				});
				Log.d("debug", "filePath=" + imageFile.getAbsolutePath());
		
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	catch(IOException e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
