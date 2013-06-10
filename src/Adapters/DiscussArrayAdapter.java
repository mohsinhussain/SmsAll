package Adapters;

import java.util.ArrayList;
import java.util.List;

import com.hyperon.smsall.R;

import Classes.ChatMessage;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DiscussArrayAdapter extends ArrayAdapter<ChatMessage> {

	private TextView message;
	private TextView userName;
	private TextView timeStamp;
	private List<ChatMessage> chatMessages = new ArrayList<ChatMessage>();
	private LinearLayout wrapper;
	private LinearLayout inner;
	

	@Override
	public void add(ChatMessage object) {
		chatMessages.add(object);
		super.add(object);
	}

	public DiscussArrayAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}

	public int getCount() {
		return this.chatMessages.size();
	}

	public ChatMessage getItem(int index) {
		return this.chatMessages.get(index);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.listitem_discuss, parent, false);
		}

		wrapper = (LinearLayout) row.findViewById(R.id.wrapper);
		inner = (LinearLayout) row.findViewById(R.id.inner);

		ChatMessage chatMessage = getItem(position);

		message = (TextView) row.findViewById(R.id.comment);
		userName = (TextView) row.findViewById(R.id.name);
		timeStamp = (TextView) row.findViewById(R.id.timestamp);

//		String complete = chatMessage.getFrom()+": \n\n"+chatMessage.getMessage()+"\n\n"+chatMessage.getTimeStamp();
		message.setText(chatMessage.getMessage());
		userName.setText(chatMessage.getFrom());
		timeStamp.setText(chatMessage.getTimeStamp());
		
		

		inner.setBackgroundResource(chatMessage.getIdentifier() ? R.drawable.bubble_yellow : R.drawable.bubble_green);
		wrapper.setGravity(chatMessage.getIdentifier() ? Gravity.LEFT : Gravity.RIGHT);

		return row;
	}

	public Bitmap decodeToBitmap(byte[] decodedByte) {
		return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
	}

}