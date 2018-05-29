package com.example.nikhil.jsonfetchurl;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    //TextView t;
    ListView listview;
    ArrayList<PojoClass> pojoClasseslist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = findViewById(R.id.listview);
//        t = (TextView) findViewById(R.id.text);
        MyTask mt = new MyTask();
        mt.execute();
    }

    public class MyTask extends AsyncTask<String, String, String> {
        String msg = "";
//        String msgparse = "";

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("https://api.myjson.com/bins/ol5wa");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                msg = bufferedReader.readLine();

                //we need to paarse the data so needs json arrray and json object to store them
                Log.e("msg:msg:",""+msg);
                
                 JSONArray jsonArray = new JSONArray(msg);
                for (int i = 0; i < jsonArray.length(); i++) {
                    PojoClass pojoClass = new PojoClass();
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                    pojoClass.setName(jsonObject.get("name").toString());
                    pojoClass.setAge(jsonObject.get("age").toString());
                    pojoClass.setCity(jsonObject.get("city").toString());

                    pojoClasseslist.add(pojoClass);

                 /*   String singleparsed = "Name :" + jsonObject.get("name") + "\n" +
                            "age :" + jsonObject.get("age") + "\n" +
                            "city :" + jsonObject.get("city") + "\n";*/

                   /* msgparse = msgparse + singleparsed + "\n"; //for multiple objects*/
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s)   //For UI puepose if to want to cahnge
        {
            super.onPostExecute(s);
//            t.setText(msgparse);
            listview.setAdapter(new CustomAdapter());
        }
    }


    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount()
        {
            return pojoClasseslist.size();
        }

        @Override
        public Object getItem(int i)
        {
            return pojoClasseslist.get(i);
        }

        @Override
        public long getItemId(int i)
        {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.list_item, null);
            TextView t1 = (TextView) view.findViewById(R.id.text1);
            TextView t2 = (TextView) view.findViewById(R.id.text2);
            TextView t3 = (TextView) view.findViewById(R.id.text3);
            t1.setText(pojoClasseslist.get(i).getName());
            t2.setText(pojoClasseslist.get(i).getAge());
            t3.setText(pojoClasseslist.get(i).getCity());
            return view;
        }
    }
}