/**
 * 
 */
package org.iLab.client;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher.ViewFactory;


/**
 * @author Bisi
 *
 */
public class Circuit extends Activity implements OnClickListener {

	private CircuitView mCircuitView;
	private Button verifyButton;
	private TextSwitcher verifyTextSwitcher;
	private ImageButton previousButton;
	private ImageButton nextButton;
	private ViewFlipper flipper;
	private Button switchButton;
	private Button flipButton;
	private Button deleteButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(new CircuitView(this));
		setContentView(R.layout.circuitframe);
		
		  flipper = (ViewFlipper)(findViewById(R.id.flipper));
		  mCircuitView = (CircuitView)findViewById(R.id.CircuitView);
		  verifyButton = (Button)findViewById(R.id.verifyButton);		  
		  verifyButton.setOnClickListener(this);
		  verifyTextSwitcher = (TextSwitcher)findViewById(R.id.verify_string);
		  verifyTextSwitcher.setFactory(new ViewFactory() {	public View makeView() {return new TextView(Circuit.this);	}});
		  previousButton = (ImageButton)findViewById(R.id.previous_button);
		  previousButton.setOnClickListener(this);
		  nextButton = (ImageButton)findViewById(R.id.next_button);
		  nextButton.setOnClickListener(this);
		  switchButton = (Button)findViewById(R.id.switchComponent);
		  switchButton.setOnClickListener(this);
		  flipButton = (Button)findViewById(R.id.flipMode);
		  flipButton.setOnClickListener(this);
		  deleteButton = (Button)findViewById(R.id.delete);
		  deleteButton.setVisibility(View.INVISIBLE);
		  deleteButton.setOnClickListener(this);
		  flipper.setInAnimation(this,R.anim.push_left_in);
		  flipper.setDisplayedChild(flipper.indexOfChild((View)findViewById(R.id.breadboard_frame)));
	    }

	@Override
	public void onDetachedFromWindow(){
		
		try{
			super.onDetachedFromWindow();
		}
		catch(IllegalArgumentException e)
		{
			flipper.stopFlipping();
		}
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId())
		{
		case R.id.verifyButton:
			verifyTextSwitcher.setText(mCircuitView.verifyConnection());
			break;
		case R.id.previous_button:
			//flipper.setOutAnimation(this,R.anim.push_left_in);
			flipper.setInAnimation(this,R.anim.slide_right);
			flipper.showPrevious();
			break;
		case R.id.next_button:
			flipper.setInAnimation(this,R.anim.push_left_in);
			//flipper.setOutAnimation(this, R.anim.push_left_out);
			flipper.showNext();
			break;
		case R.id.switchComponent:
			mCircuitView.switchComponent();
			break;
		case R.id.flipMode:
			int bbmode = mCircuitView.flipMode();
			deleteButton.setEnabled(false);
			if(bbmode ==0)
				deleteButton.setVisibility(View.INVISIBLE);
			else
				deleteButton.setVisibility(View.VISIBLE);
		case R.id.delete:
				deleteButton.setEnabled(true);
				mCircuitView.deleteEdge();			
			break;
		}
		
	}
	
	 

}