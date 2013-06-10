package Adapters;

import java.util.ArrayList;

import Classes.ContactDetails;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hyperon.smsall.R;

public class MessageView_CustomAdapter extends BaseAdapter
{
	ArrayList<ContactDetails> namesArrayList;
	Context context;
	public MessageView_CustomAdapter(Context context,
			ArrayList<ContactDetails> namesArrayList) {
		this.context = context;
		this.namesArrayList = namesArrayList;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

//		final Inbox p = getItem(position);
		ViewHolder holder;

		if (convertView == null) {
			convertView = View.inflate(context, R.layout.group_icon, null);

			holder = new ViewHolder();
			holder.textName = (TextView) convertView
					.findViewById(R.id.nameTextView);
			holder.crossButton = (Button) convertView
					.findViewById(R.id.crossButton);

			convertView.setTag(holder);
		} else {
			// Get the ViewHolder back to get fast access to the TextView
			// and the ImageView.
			holder = (ViewHolder) convertView.getTag();
		}

		holder.textName.setText(namesArrayList.get(position).getcontactname());
		holder.crossButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				namesArrayList.remove(position);
				notifyDataSetChanged();
			}
		});
		
		/*holder.message.setText(p.getMessage());
		//holder.textto.setText(p.getTo());
		
		holder.timestamp.setText(""+p.getTimestamp());
	
*/
		return convertView;
	}

	static class ViewHolder {
		TextView textName;
		Button crossButton;
		//TextView timestamp;
		//TextView textto;
		//TextView message;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return namesArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return namesArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

}
