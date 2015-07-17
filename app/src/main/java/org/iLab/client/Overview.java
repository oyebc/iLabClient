package org.iLab.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class Overview extends Activity implements OnClickListener {
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.overview);
	        
	        View bb_button = findViewById(R.id.bb_button);
	        bb_button.setOnClickListener(this);
	 }

	@Override
	public void onClick(View arg) {
		// TODO Auto-generated method stub
		
		switch (arg.getId()) {
		case R.id.bb_button:
			Intent circuitIntent = new Intent(this, Circuit.class);
			startActivity(circuitIntent);
			break;
		
		}
	 
	}
}
