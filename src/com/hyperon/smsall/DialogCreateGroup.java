package com.hyperon.smsall;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class DialogCreateGroup extends DialogFragment {
	private String TAG = "DialogCreateGroup";
//	TextView messageTextView;
//	String mMessage;
	public interface ComposeDialogListener {
        public void onDialogPositiveClick(String groupName);
        public void onDialogNegativeClick();
    }
	ComposeDialogListener mListener;
    
	 public static DialogCreateGroup newInstance() {
		DialogCreateGroup dialog = new DialogCreateGroup();
//        Bundle bundle = new Bundle();
//        bundle.putString(Constants.PARAM_GROUP_NAME, title);
////        Constants.groupMessage = message;
//        bundle.putString("message", message);
//        dialog.setArguments(bundle);
        return dialog;
	}
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            mListener = (ComposeDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
    
    
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    
	    builder.setView(inflater.inflate(R.layout.create_group_dialog_layout, null))
	           .setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
	        	   
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   EditText groupNameEditText = (EditText) getDialog().findViewById(R.id.groupNameEditeText);
	            	   String groupName = "";
	            	   groupName = groupNameEditText.getText().toString();
//	            	   messageTextView.setText(mMessage);
//	            	   Log.v("Dialoge", mMessage);
//	            	   messageTextView = (TextView) getDialog().findViewById(R.id.group_message);
//	            	   messageTextView.setText(mMessage);
//	            	   Log.v("Dialoge", mMessage);
//	            	   CheckBox checkBox = (CheckBox) getDialog().findViewById(R.id.reply_all);
//	            	   Log.d(TAG, "Return message to main activity: " + messageTextView.getText().toString());
	                   mListener.onDialogPositiveClick(groupName);
	                   
	               }
	           })
	           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   Log.d(TAG, "Cancel clicked");
	            	   mListener.onDialogNegativeClick();
	                   DialogCreateGroup.this.getDialog().cancel();
	               }
	           });
	    return builder.create();    
	}
	
//	public void setMessage(String mes){
//		mMessage=mes;
//		Log.v("Dialoge", mMessage);
////		messageTextView.setText(mMessage);
//	}
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = super.onCreateView(inflater, container, savedInstanceState);
		getDialog().setTitle("Create A Group");
//		setMessage(getArguments().getString("message"));
//		messageTextView.setText(getArguments().getString("message"));
		
		return view;		
	}
}
