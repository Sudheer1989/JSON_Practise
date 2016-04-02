package com.mytorch.sweety.json_practise;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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

public class MainActivity extends AppCompatActivity {


    ArrayList<Contacts> al;
    MyAdapter adp;

    ListView lv;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn= (Button) findViewById(R.id.button);
        lv= (ListView) findViewById(R.id.listView);

        al=new ArrayList<Contacts>();
        adp=new MyAdapter();

        lv.setAdapter(adp);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyTask m=new MyTask();
                m.execute();
            }
        });
    }

    private class MyTask extends AsyncTask<Void,Void,String>

    {


        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection con = null;
            try{
                URL url  = new URL("http://api.androidhive.info/contacts/");
                con = (HttpURLConnection) url.openConnection();
                InputStream i=con.getInputStream();
                InputStreamReader ir= new InputStreamReader(i);
                BufferedReader br=new BufferedReader(ir);

                StringBuilder s= new StringBuilder();
                String str= br.readLine();

                while(str !=null){
                    s.append(str);
                    str=br.readLine();
                }
                return s.toString();

            }
            catch(MalformedURLException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                JSONObject obj= new JSONObject(s);
                JSONArray contacts=obj.getJSONArray("contacts");
                for(int i=1;i<contacts.length();i++){
                JSONObject contact=contacts.getJSONObject(i);
                    String name=contact.getString("name");
                    String email=contact.getString("email");
                    JSONObject ob =contact.getJSONObject("phone");
                    String mobile=ob.getString("mobile");

                    Contacts obj1=new Contacts();
                    obj1.setName(name);
                    obj1.setEmail(email);
                    obj1.setPhone(mobile);

                    al.add(obj1);
                }
                adp.notifyDataSetChanged();

            }catch(JSONException e){
                e.printStackTrace();
            }

            super.onPostExecute(s);
        }
    }



    private class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {

            return al.size();
        }

        @Override
        public Object getItem(int position) {
            return al.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            View v= getLayoutInflater().inflate(R.layout.row,null);
            TextView Ltxt= (TextView) v.findViewById(R.id.textView);
            TextView mtxt= (TextView) v.findViewById(R.id.textView2);
            TextView stxt= (TextView) v.findViewById(R.id.textView3);

            Contacts contac=al.get(position);
             String name=contac.getName();
            String email=contac.getEmail();
            String mobile=contac.getPhone();

            Ltxt.setText(name);
            mtxt.setText(email);
            stxt.setText(mobile);

            return v;

            }
    }
}


