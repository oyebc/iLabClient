package org.iLab.client;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;

public class Client extends Activity implements OnClickListener {
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
               
        View newButton = findViewById(R.id.new_button);
        newButton.setOnClickListener(this);
        View aboutButton = findViewById(R.id.about_button);
        aboutButton.setOnClickListener(this);
        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);
    }

	@Override
	public void onClick(View arg) {
		// TODO Auto-generated method stub
		
		switch (arg.getId()) {
		case R.id.about_button:
			Intent aboutIntent = new Intent(this, About.class);
			startActivity(aboutIntent);
			break;
		case R.id.new_button:
			Intent newIntent = new Intent(this, Overview.class);
			startActivity(newIntent);
			break;
		case R.id.exit_button:
			finish();
			break;
		
		}
		
	}
}