package com.hyperon.smsall;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class GroupTabFragment extends Fragment {
	private JSONObject mGroupInfoJSON;
	private String TAG = "GroupTabFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	return inflater.inflate(R.layout.fragment_group_main, container, false);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG, "onActivityCreated()");
//        new APIClient(getActivity(), new GetInfoCallback()).doGetGroupInfo(((GroupInfoActivity)getActivity()).getGroup());
    }
    
	public class GetInfoCallback extends AsyncCallback {
		public void onTaskComplete(String response) {
			try {
				mGroupInfoJSON = new JSONObject(response);
				JSONObject dictionary = mGroupInfoJSON.getJSONObject(Constants.JSON_DICTIONARY_NAME);

				((TextView) getActivity().findViewById(R.id.group_name)).setText(((GroupInfoActivity)getActivity()).getGroup());

				String desc = Constants.NOT_AVAILABLE;
				try { desc = dictionary.getString("description"); }
				catch (JSONException e){ Log.e(TAG, e.toString()); }				
				((TextView) getActivity().findViewById(R.id.group_description)).setText(desc);

				String creator = Constants.NOT_AVAILABLE;
				try { creator = dictionary.getString("creator"); }
				catch (JSONException e){ Log.e(TAG, e.toString()); }
				((TextView) getActivity().findViewById(R.id.group_creator)).setText(creator);
				
				String created_on = Constants.NOT_AVAILABLE;
				try { created_on = dictionary.getString("date_created"); }
				catch (JSONException e){ Log.e(TAG, e.toString()); }				
				((TextView) getActivity().findViewById(R.id.group_created_on)).setText(created_on);
				
				String member_count = Constants.NOT_AVAILABLE;
				try { member_count = dictionary.getString("member_count"); }
				catch (JSONException e){ Log.e(TAG, e.toString()); }				
				((TextView) getActivity().findViewById(R.id.group_member_count)).setText(member_count);
				
				String membership = Constants.NOT_AVAILABLE;
				try { membership = dictionary.getString("membership"); }
				catch (JSONException e){ Log.e(TAG, e.toString()); }				
				((TextView) getActivity().findViewById(R.id.group_membership)).setText(membership);

				String traffic = Constants.NOT_AVAILABLE;
				try { traffic = dictionary.getString("traffic_control"); }
				catch (JSONException e){ Log.e(TAG, e.toString()); }				
				((TextView) getActivity().findViewById(R.id.group_traffic)).setText(traffic);

				String type = Constants.NOT_AVAILABLE;
				try { type = dictionary.getString("type"); }
				catch (JSONException e){ Log.e(TAG, e.toString()); }
				((TextView) getActivity().findViewById(R.id.group_type)).setText(type);

				String status = "false";
				try { type = dictionary.getString("suspended"); }
				catch (JSONException e){ Log.e(TAG, e.toString()); }
				((TextView) getActivity().findViewById(R.id.group_status)).setText(status == "false" ? "resumed" : "suspended");
				
			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(getActivity(), "Group Information could not be loaded!", Toast.LENGTH_LONG).show();
			}
		}
		@Override
		public void onTaskCancelled() {
		}
		@Override
		public void onPreExecute() {
			// TODO Auto-generated method stub
			
		}
	}
}

