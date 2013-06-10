package com.hyperon.smsall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MembersTabFragment extends ListFragment {
	private ListView mListView;
	private JSONObject mMembersJSON;
	private String TAG = "MemberTabFragment";
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	return inflater.inflate(R.layout.fragment_members_tab, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListView = getListView();
//        new APIClient(getActivity(), new GetMembersCallback()).doGetMembers(((GroupInfoActivity)getActivity()).getGroup());
    }
    
	public class GetMembersCallback extends AsyncCallback {
		public void onTaskComplete(String response) {
			try {
				Log.v(TAG, "GetMembersCallback Response: " + response);
				mMembersJSON = new JSONObject(response);
				JSONArray list = mMembersJSON.getJSONArray(Constants.JSON_LIST_NAME);
				String strArray[] = new String[list.length()];
				
				for (int i = 0; i < list.length(); i++)
					strArray[i] = list.getString(i);
		        mListView.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(),
   					 R.layout.layout_member_item, strArray));
		        
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

