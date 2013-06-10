package Adapters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Classes.ChatMessage;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyperon.smsall.Inbox;
import com.hyperon.smsall.R;

public class SingleUserDiscussArrayAdapter extends ArrayAdapter<Inbox> {

	private TextView message;
	private TextView userName;
	private TextView timeStamp;
	private List<Inbox> chatMessages = new ArrayList<Inbox>();
	private LinearLayout wrapper;
	private LinearLayout inner;
	private ImageView statusImageView;
	

	@Override
	public void add(Inbox object) {
		chatMessages.add(object);
		super.add(object);
	}

	public SingleUserDiscussArrayAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}

	public int getCount() {
		return this.chatMessages.size();
	}

	public Inbox getItem(int index) {
		return this.chatMessages.get(index);
	}

	public int getLastItemIndex(){
		return this.chatMessages.size()-1;
		
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.listitem_discuss, parent, false);
		}

		wrapper = (LinearLayout) row.findViewById(R.id.wrapper);
		inner = (LinearLayout) row.findViewById(R.id.inner);

		Inbox chatMessage = getItem(position);

		message = (TextView) row.findViewById(R.id.comment);
		userName = (TextView) row.findViewById(R.id.name);
		timeStamp = (TextView) row.findViewById(R.id.timestamp);
		statusImageView = (ImageView) row.findViewById(R.id.statusImageView);
//		String complete = chatMessage.getFrom()+": \n\n"+chatMessage.getMessage()+"\n\n"+chatMessage.getTimeStamp();
		message.setText(chatMessage.getMessage());
		userName.setText(chatMessage.getfrom());
		//timeStamp.setText(Long.toString(chatMessage.getTimestamp()));
		Date d= new Date(chatMessage.getTimestamp()*1000);
		timeStamp.setText(String.valueOf(d.toLocaleString()));
//		if(chatMessage.getStatus().)
		if(chatMessage.getStatus().equalsIgnoreCase("wait")){
			statusImageView.setBackgroundResource(R.drawable.wait);
		}
		else if(chatMessage.getStatus().equalsIgnoreCase("sent")){
			statusImageView.setBackgroundResource(R.drawable.tick);
		}
		else if(chatMessage.getStatus().equalsIgnoreCase("error")){
			statusImageView.setBackgroundResource(R.drawable.cross);
		}
		

		inner.setBackgroundResource(chatMessage.getFlag()==0 ? R.drawable.bubble_client : R.drawable.bubble_host);
		wrapper.setGravity(chatMessage.getFlag()==0 ? Gravity.LEFT : Gravity.RIGHT);

		return row;
	}

	public Bitmap decodeToBitmap(byte[] decodedByte) {
		return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
	}

}