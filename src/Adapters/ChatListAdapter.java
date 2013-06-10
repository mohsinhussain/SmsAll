package Adapters;

import java.util.ArrayList;

import Classes.ChatHistory;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyperon.smsall.R;

class ChatListAdapter extends BaseAdapter {
	 
    private ArrayList mData = new ArrayList();
    private LayoutInflater mInflater;

    public ChatListAdapter(Activity activity, ArrayList<ChatHistory> chatHistoryArrayList) {
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final String item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData.get(position).toString();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        System.out.println("getView " + position + " " + convertView);
        ViewHolder holder = null;
        if (convertView == null) {
//            convertView = mInflater.inflate(R.layout.item1, null);
            holder = new ViewHolder();
//            holder.textView = (TextView)convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
//        holder.textView.setText(mData.get(position));
        return convertView;
    }



}

	class ViewHolder {
    public TextView textView;
}
