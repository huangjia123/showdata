package com.example.showdata;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MainActivity extends Activity {
	DatabaseHelper database;
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    private SimpleAdapter simpleAdapter = null;
	ListView listview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		String sql1="select * from songtable";
	    //*******************************/
		listview=(ListView) findViewById(R.id.songlist);
		database = new DatabaseHelper(this);
		Cursor cur1 = database.getReadableDatabase().rawQuery(sql1, null);
		//database.getReadableDatabase().
		 InputStream inputStream = getResources().openRawResource(R.raw.num_json);
	     if(cur1.getCount()==0){
	    	 getString(inputStream);
	     }
		 
		show();
	}
	
	public void show(){
		 simpleAdapter = new SimpleAdapter(this, getdata(), R.layout.songtext,
	                new String[] {"song","singer"}, new int[] { R.id.songName,R.id.songerName });
	        //listView绑定adapter
		 listview.setAdapter(simpleAdapter);
	}
	
	
	 private List<Map<String, String>> getdata() {
		 SQLiteDatabase readdb = database.getReadableDatabase();
			String sql="select singer,song from songtable";
			Cursor cursor=readdb.rawQuery(sql, null);
			// 清空list
	        list.clear();
	        // 查询到的数据添加到list集合
	        while (cursor.moveToNext()) {
	            Map<String, String> map = new HashMap<String, String>();
	            map.put("singer", cursor.getString(0)); // 获取name
	            map.put("song", cursor.getString(1)); // 获取name
	            list.add(map);
	        }
	        return list;
	    }
	 
	 //获取JSON插入数据库
	 public  void getString(InputStream inputStream){
		 SQLiteDatabase sd = database.getWritableDatabase();
	        InputStreamReader inputStreamReader = null;
	        try {
	            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        }
	        BufferedReader reader = new BufferedReader(inputStreamReader);
	        String line;
	        String str="";
	        try {
	            while ((line = reader.readLine()) != null){
	            	str=str+line;
	                }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        try {
				JSONArray array = new JSONArray(str);
				 int len = array.length();  
		            for (int i = 0; i < len; i++) {  
		                JSONObject object = array.getJSONObject(i);
		                String singer = object.get("singer").toString();
		                String song = object.get("song").toString();
		                String sql="insert into songtable(singer,song)values('"+singer+"','"+song+"')";
		    			sd.execSQL(sql);
		            }
				
			} catch (JSONException e) {
				e.printStackTrace();
			} 
	    }
}
