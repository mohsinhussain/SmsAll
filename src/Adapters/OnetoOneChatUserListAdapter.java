package Adapters;

import java.util.ArrayList;

import Classes.UserOnetoOne;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hyperon.smsall.R;

public class OnetoOneChatUserListAdapter extends ArrayAdapter<UserOnetoOne> {
	ArrayList<String> list;
	Activity activity;
	private int selectedPos = -1;
	public OnetoOneChatUserListAdapter(Activity activity,ArrayList<String> list) {
		super(activity,
					R.layout.group_icon);
		this.list = list;
//		totlaItems.setText(Integer.toString(list.size()));
		this.activity = activity;
	}
	public void setSelectedPosition(int pos) {
		selectedPos = pos;
		notifyDataSetChanged();
	}
	public int getSelectedPosition() {
		return selectedPos;
	}

	class ViewHolder {
		protected TextView userName;
	}

	@Override
	public View getView(final int position, View convertView,
			ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflator = activity.getLayoutInflater();
			convertView = inflator.inflate(R.layout.group_icon, null);
			holder = new ViewHolder();
			
//			holder.userName = (TextView) convertView
//					.findViewById(R.id.group_icon_text);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Log.v("Adapter", "Size: "+list.size());
//		Log.v("Adapter", "Name: "+list.get(position).getUserName());
		holder.userName.setText(list.get(position).toString());
		
		
		return convertView;
	}

	public boolean areAllItemsEnabled() {
		return false;
	}

	public boolean isEnabled(int position) {
		return false;
	}
}