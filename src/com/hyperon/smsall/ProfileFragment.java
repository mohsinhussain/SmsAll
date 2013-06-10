package com.hyperon.smsall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class ProfileFragment extends Fragment {
    /** (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
	LinearLayout about;
	LinearLayout inviteByMail;
	LinearLayout inviteBySMS;
	LinearLayout inviteByFb;
	LinearLayout privacyPolicy;
	LinearLayout settings;
	LinearLayout exit;
	
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }
        
        View layout = inflater.inflate(R.layout.profile_linear_layout, container, false);
        inviteByMail = (LinearLayout) layout.findViewById(R.id.inviteMailLinearLayout);
        inviteBySMS = (LinearLayout) layout.findViewById(R.id.inviteSMSLinearLayout);
        inviteByFb = (LinearLayout) layout.findViewById(R.id.inviteFacebookLinearLayout);
        privacyPolicy = (LinearLayout) layout.findViewById(R.id.privacyPolicyLinearLayout);
        settings = (LinearLayout) layout.findViewById(R.id.settingLinearLayout);
        exit = (LinearLayout) layout.findViewById(R.id.exitLinearLayout);
        about = (LinearLayout) layout.findViewById(R.id.aboutLinearLayout);
        
        return layout;
    }
    
    @Override
    public void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	about.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), AboutActivity.class);
				startActivity(i);
			}
		});
    	
    	exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
    	
    	
    	settings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), SettingsActivity.class);
				startActivity(i);
			}
		});
    	
    	
    	privacyPolicy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), PrivacyPolicyActivity.class);
				startActivity(i);
			}
		});
    	inviteByFb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_SEND);
				String Body = "Hey,\n\nI started using SMSALL, its a cool app that lets you text for free!\n\n" +
						"SMSALL is easy to use and works so smooth like normal messaging:\n" +
						"-No need to create a username or log in\n" +
						"-Every one can recieve your free text(even the users who do not have this amazing app)\n" +
						"-Works with your phone's existing contact list\n" +
						"-Shows which of your contacts already has it\n\n" +
						"Get SMSALL app Now!  www.smsall.pk\n\n" +
						"Yours Sincerely,\n" +
						Constants.FULL_NAME;
			    intent.setType("text/plain");
			    intent.putExtra(Intent.EXTRA_TEXT, Body);
			    startActivity(Intent.createChooser(intent, "Select an app to share on"));
			}
		});
    	
    	
    	inviteBySMS.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent sendIntent = new Intent(Intent.ACTION_VIEW);
				String Body = "Hey,\n\nI started using SMSALL, its a cool app that lets you text for free!\n\n" +
						"SMSALL is easy to use and works so smooth like normal messaging:\n" +
						"-No need to create a username or log in\n" +
						"-Every one can recieve your free text(even the users who do not have this amazing app)\n" +
						"-Works with your phone's existing contact list\n" +
						"-Shows which of your contacts already has it\n\n" +
						"Get SMSALL app Now!  www.smsall.pk\n\n" +
						"Yours Sincerely,\n" +
						Constants.FULL_NAME;
				sendIntent.putExtra("sms_body", Body); 
				sendIntent.setType("vnd.android-dir/mms-sms");
				startActivity(sendIntent);
			}
		});
    	
    	
    	inviteByMail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String Body = "Hey,\n\nI started using SMSALL, its a cool app that lets you text for free!\n\n" +
						"SMSALL is easy to use and works so smooth like normal messaging:\n" +
						"-No need to create a username or log in\n" +
						"-Every one can recieve your free text(even the users who do not have this amazing app)\n" +
						"-Works with your phone's existing contact list\n" +
						"-Shows which of your contacts already has it\n\n" +
						"Get SMSALL app Now!  www.smsall.pk\n\n" +
						"Yours Sincerely,\n" +
						Constants.FULL_NAME;
//	        	for(int i=0;i<SelectedItemArrayList.size();i++){
//	        		
//        			Body = Body + "Item Name: "+SelectedItemArrayList.get(i).getName()+
//        					"   Item Code: "+SelectedItemArrayList.get(i).getCode()+
//        					"   Item Quantity: "+SelectedItemArrayList.get(i).getQuantity()+"\n";
//        		}
            	final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                
                emailIntent.setType("plain/text");
                
           
//                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, "phi.me.phi@gmail.com");
         
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Invitation to SMSALL");
         
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, Body);
 
              getActivity().startActivity(Intent.createChooser(emailIntent, "Send mail..."));
//               new SendEmail().execute();
            
			}
		});
    }
}
