package com.hyperon.smsall;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CacheAccess extends Activity{
	public String TEMP_FILE_NAME = "wpta_temp_file1.txt";
	File maptext ;
 	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		Intent i = getIntent();
		
		
		String msg=Integer.toString(i.getExtras().size());
		
		Log.v("intentsize", msg);
		
		if(i.getExtras().size() == 3) {
			String key=i.getStringExtra("key");
			String value=i.getStringExtra("value");
			String time=i.getStringExtra("time");
			
			//value=value+"%"+time;
			
		    System.out.println(key);
		    System.out.println(value);
		    System.out.println(time);
		    
		    Map<String, String> mapp = new HashMap<String, String>();
		    mapp.put(key,value);
			
			 File cDir = getBaseContext().getCacheDir();
			// File cacheDir = Context.getCacheDir();
	        
	        /** Getting a reference to temporary file, if created earlier */
	        maptext = new File(cDir.getPath() + "/" + TEMP_FILE_NAME) ;
       
       
	    FileOutputStream fos;
		try {
			fos = new FileOutputStream(maptext);
	        PrintWriter pw=new PrintWriter(fos,true);

	        for(Map.Entry<String,String> m :mapp.entrySet())
	        {
	            pw.println(m.getKey()+"="+m.getValue());
	        }

	        pw.flush();
	        pw.close();
	        fos.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		    System.out.println("executeddddd");
		   
		    
		}
		else
		{
			String key=i.getStringExtra("key");
			Messenger messngr= i.getParcelableExtra("handler");
		    try {
		    	
				String feed =readcachefile(key);
				
				System.out.println("writefile"+feed);
				Message message= Message.obtain();
				
				message.arg1=1;
				message.obj= feed;
				messngr.send(message);
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	public  String  readcachefile(String k) throws IOException
	{
 		
	
		 File cDir = getBaseContext().getCacheDir();
	       
	       /** Getting a reference to temporary file, if created earlier */
	       maptext = new File(cDir.getPath() + "/" + TEMP_FILE_NAME) ;
	    
       StringBuilder text1 = new StringBuilder();
   	
       FileInputStream fis=new FileInputStream(maptext);

       Scanner sc=new Scanner(fis);

       HashMap<String,String> mapInFile=new HashMap<String,String>();

       //read data from file line by line:
       String currentLine;
       while(sc.hasNextLine())
       {
           currentLine=sc.nextLine();
           text1.append(currentLine+"\n");
           Log.v("readstring", text1.toString());
           //now tokenize the currentLine:
           StringTokenizer st=new StringTokenizer(currentLine,"=",false);
           //put tokens ot currentLine in map
           mapInFile.put(st.nextToken(),st.nextToken());
       }
       
       fis.close();
     
       if(mapInFile.get(k) == null)
       {
    	   return null;
    	   
       }
       
       else
     
       {
      /*
    	   String [] valuendtime = mapInFile.get(k).split("%");
      String value=valuendtime[0];
      int time = Integer.parseInt(valuendtime[1]);
      
      int currenttime =(int) (System.currentTimeMillis() / 1000);
      int validtime=currenttime + time;
      
      if(currenttime<validtime)
      {
    	*/
    	  //Log.v("Value is", value);
    	//  return mapInFile.get(k);
    	   return mapInFile.get(k);
    	   
      //}
      
      
	}
    //   return null; 
	}
	
}
