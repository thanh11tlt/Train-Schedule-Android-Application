package me.dilan.ui;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import me.dilan.R;
import me.dilan.obj.Delays;
import me.dilan.obj.TrainLines;
import me.dilan.ui.ActivityDisplaySchedule.AdapterTrainSchedule;
import me.dilan.ui.ActivitySelectTrainLines.WSGetTrainLines;
import me.dilan.util.Functions;
import me.dilan.webservice.RailwayWebServiceV2;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

public class ActivityDisplayTrainDelays extends Activity {
	
	GridView mListViewDelays;	
	ProgressDialog mProgressDialog;
	Handler mWSGetTrainDelaysHandler;
	Delays mDelays;
	
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_train_delays);
        
        mListViewDelays = (GridView) findViewById(R.id.dispaly_train_delays_gridview_delays);
        
        mProgressDialog = Functions.getProgressDialog(this, getString(R.string.all_retriving_data));
        new WSGetTrainDelays().execute(null,null,null);
        
        mWSGetTrainDelaysHandler = new Handler() { 
         	public void handleMessage(Message message) {        		
    				mListViewDelays.setAdapter(new AdapterTrainDelay());
    				mProgressDialog.dismiss();
    	        }
         };
	}
	
	
	class WSGetTrainDelays extends AsyncTask<Object, Object, Object>{

		@Override
		protected Object doInBackground(Object... params) {
				try {				
					Calendar now = Calendar.getInstance();
					String todayDate = "2010-10-18";// String.format("%1$tY-%1$tm-%1$te", now);
					String todayTime = String.format("%1$tH:%1$tM:%1$tS", now);						
					mDelays = RailwayWebServiceV2.getDelays(todayDate, todayTime);
					mWSGetTrainDelaysHandler.sendMessage(mWSGetTrainDelaysHandler.obtainMessage());
					return null;
				} catch (Exception e) {			
						e.printStackTrace();
				}
			return null;	
		}
    }
	
	
	
	public void onBackPressed() {		
		super.onBackPressed();
		finish();
	}
	
	
class AdapterTrainDelay extends BaseAdapter {		
		
		public int getCount() {			
			return mDelays.getCount();
		}	
		
		
		public Object getItem(int position) {			
			return null;
		}

		public long getItemId(int position) {			
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater layoutInflater = ActivityDisplayTrainDelays.this.getLayoutInflater();
			View customTrainDelay = layoutInflater.inflate(R.layout.custom_train_delay, null);
			LinearLayout layoutTrainSchedule = (LinearLayout) customTrainDelay.findViewById(R.id.custom_train_delay_root);
			TextView textViewTrainName = (TextView) customTrainDelay.findViewById(R.id.custom_train_delay_train_name);
			TextView textViewTrainDelay = (TextView) customTrainDelay.findViewById(R.id.custom_train_delay_train_delay);
						
			textViewTrainName.setText(mDelays.getTrainNo()[position] + " (" + mDelays.getFromStationName()[position] + " - " + mDelays.getToStationName()[position] + ")");
			textViewTrainDelay.setText("Train is delayed for " + mDelays.getDelayTime()[position] + ".");
			if(mDelays.getComments()[position] != null){
				textViewTrainDelay.setText(textViewTrainDelay.getText() + " Comment: " + mDelays.getComments()[position]);
			}
			                                            
			return layoutTrainSchedule;
		}

	}

class DelayViewAdapter extends SimpleExpandableListAdapter{

	public DelayViewAdapter(Context context,
			List<? extends Map<String, ?>> groupData, int groupLayout,
			String[] groupFrom, int[] groupTo,
			List<? extends List<? extends Map<String, ?>>> childData,
			int childLayout, String[] childFrom, int[] childTo) {
		super(context, groupData, groupLayout, groupFrom, groupTo, childData,
				childLayout, childFrom, childTo);
		// TODO Auto-generated constructor stub
	}	
}


}