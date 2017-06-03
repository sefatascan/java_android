package com.example.sefa.aninterface;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

public class MudahaleEdilen extends AppCompatActivity {

    private List<ArrayList> listData;
    private DuzListeAdapter duzListeAdapter;
    private KeepLoginPerson loginPerson;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mudahale_edilen);

        loginPerson =(KeepLoginPerson)getIntent().getSerializableExtra("ApploginKisi");
        listData = new ArrayList();
        listView = (ListView)findViewById(R.id.listview_mudahale);

        new ConnectService().execute();



    }
    private class ConnectService extends AsyncTask<Void,Void,Void>{
        ProgressDialog progressDialog = new ProgressDialog(MudahaleEdilen.this);
        String RESPONSE;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Kontrol Ediliyor...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String NAMESPACE ="http://tempuri.org/" ;
            String URL ="http://192.168.1.35/Servis.asmx";
            String METHOD_NAME ="getMudahaleEdilenler";
            String SOAP_ACTION = "http://tempuri.org/getMudahaleEdilenler";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("inputId",loginPerson.getApploginId());

            HttpGetJSON httpGetJSON=new HttpGetJSON(URL,SOAP_ACTION);
            RESPONSE = httpGetJSON.getJsonString(request);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if(RESPONSE != null){
                try {

                    JSONArray contacts = new JSONArray(RESPONSE);

                    for (int i=0; i<contacts.length(); i++){
                        JSONObject c = contacts.getJSONObject(i);
                        ArrayList a = new ArrayList();
                        a.add(c.getString("Tcno"));
                        a.add(c.getString("Ad"));
                        a.add(c.getString("Soyad"));
                        a.add(c.getString("Yetki"));
                        a.add(c.getString("Tarih"));
                        a.add(c.getString("Aciklama"));

                        listData.add(a);

                    }

                } catch (final JSONException e) {
                    Toast.makeText(getApplicationContext(),"Json parse etmedi",Toast.LENGTH_LONG).show();
                }
            }
            else{
                ArrayList a = new ArrayList();
                a.add("Yetkilinin mudahale edilen bilgisi bulunmamaktadır.");
                listData.add(a);
            }
            duzListeAdapter = new DuzListeAdapter(MudahaleEdilen.this,listData);
            listView.setAdapter(duzListeAdapter);
        }
    }
}
