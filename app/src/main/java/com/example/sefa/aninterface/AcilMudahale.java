package com.example.sefa.aninterface;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AcilMudahale extends AppCompatActivity {

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<ArrayList>> listDataChild;
    private String RESPONSEQR;
    private int kisiID;

    private TextView textView_tc_H;
    private TextView textView_ad_H;
    private TextView textView_soyad_H;
    private TextView textView_dogum_H;
    private TextView textView_sehir_H;
    private TextView textView_kan_H;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acil_mudahale);

        RESPONSEQR=(String)getIntent().getStringExtra("RESPONSE");

        textView_tc_H=(TextView)findViewById(R.id.textView_tc_H);
        textView_ad_H=(TextView)findViewById(R.id.textView_ad_H);
        textView_soyad_H=(TextView)findViewById(R.id.textView_soyad_H);
        textView_dogum_H=(TextView)findViewById(R.id.textView_dogumtarih_H);
        textView_sehir_H=(TextView)findViewById(R.id.textView_sehir_H);
        textView_kan_H=(TextView)findViewById(R.id.textView_kangrub_H);

        JsonParser(RESPONSEQR);

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<ArrayList>>();

        expListView = (ExpandableListView) findViewById(R.id.expandable_listview);
        new ConnectService().execute();


    }

    private class ConnectService extends AsyncTask<Void,Void,Void>{
        String RESPONSE1,RESPONSE2,RESPONSE3,RESPONSE4,RESPONSE5;

        String NAMESPACE ="http://tempuri.org/" ;
        String URL ="http://192.168.43.24/Servis.asmx";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            doHastalikBilgisi();
            doIlacBilgisi();
            doAlerjiBilgisi();
            doIletisimBilgileri();
            doMudahaleBilgisi();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

                doldurHastalik(RESPONSE1);
                doldurIlac(RESPONSE2);
                doldurAlerji(RESPONSE3);
                doldurIletisim(RESPONSE4);
                doldurAcilMudahale(RESPONSE5);

                listAdapter = new ListeAdapter(AcilMudahale.this, listDataHeader, listDataChild);
                expListView.setAdapter(listAdapter);


        }
        private void doHastalikBilgisi(){

            String METHOD_NAME ="getHastalikBilgisi";
            String SOAP_ACTION = "http://tempuri.org/getHastalikBilgisi";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("inputId",kisiID);

            HttpGetJSON httpGetJSON=new HttpGetJSON(URL,SOAP_ACTION);
            RESPONSE1 = httpGetJSON.getJsonString(request);

        }
        private void doIlacBilgisi(){

            String METHOD_NAME ="getIlacBilgisi";
            String SOAP_ACTION = "http://tempuri.org/getIlacBilgisi";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("inputId",kisiID);

            HttpGetJSON httpGetJSON=new HttpGetJSON(URL,SOAP_ACTION);
            RESPONSE2 = httpGetJSON.getJsonString(request);

        }
        private void doAlerjiBilgisi(){

            String METHOD_NAME ="getAlerjiBilgisi";
            String SOAP_ACTION = "http://tempuri.org/getAlerjiBilgisi";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("inputId",kisiID);

            HttpGetJSON httpGetJSON=new HttpGetJSON(URL,SOAP_ACTION);
            RESPONSE3 = httpGetJSON.getJsonString(request);

        }
        private void doIletisimBilgileri(){

            String METHOD_NAME ="getKisininIletisimBilgileri";
            String SOAP_ACTION = "http://tempuri.org/getKisininIletisimBilgileri";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("inputId",kisiID);

            HttpGetJSON httpGetJSON=new HttpGetJSON(URL,SOAP_ACTION);
            RESPONSE4 = httpGetJSON.getJsonString(request);

        }
        private void doMudahaleBilgisi(){

            String METHOD_NAME ="getAcilMudahale";
            String SOAP_ACTION = "http://tempuri.org/getAcilMudahale";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("inputId",kisiID);

            HttpGetJSON httpGetJSON=new HttpGetJSON(URL,SOAP_ACTION);
            RESPONSE5 = httpGetJSON.getJsonString(request);

        }
    }
    private void doldurHastalik(String RESPONSE){
        listDataHeader.add("Hastalık Bilgileri");
        List<ArrayList> hastalik = new ArrayList<ArrayList>();
        if(RESPONSE != null){
            try {

                JSONArray contacts = new JSONArray(RESPONSE);

                for (int i=0; i<contacts.length(); i++){
                    JSONObject c = contacts.getJSONObject(i);
                    ArrayList a = new ArrayList();
                    a.add(c.getString("HastalikAd"));
                    hastalik.add(a);

                }





            } catch (final JSONException e) {
                Toast.makeText(getApplicationContext(),"Json parse etmedi",Toast.LENGTH_LONG).show();
            }
        }
        else{
            ArrayList a = new ArrayList();
            a.add("Kişinin hastalık bilgisi bulunmamaktadır.");
            hastalik.add(a);
        }
        listDataChild.put(listDataHeader.get(0),hastalik);
    }
    private void doldurIlac(String RESPONSE){
        listDataHeader.add("İlaç Bilgileri");
        List<ArrayList> ilac = new ArrayList<ArrayList>();
        if(RESPONSE != null){
            try {

                JSONArray contacts = new JSONArray(RESPONSE);

                for (int i=0; i<contacts.length(); i++){
                    JSONObject c = contacts.getJSONObject(i);
                    ArrayList a = new ArrayList();
                    a.add(c.getString("IlacAd"));
                    ilac.add(a);

                }





            } catch (final JSONException e) {
                Toast.makeText(getApplicationContext(),"Json parse etmedi",Toast.LENGTH_LONG).show();
            }
        }
        else{
            ArrayList a = new ArrayList();
            a.add("Kişinin ilaç bilgisi bulunmamaktadır.");
            ilac.add(a);
        }
        listDataChild.put(listDataHeader.get(1),ilac);
    }
    private void doldurAlerji(String RESPONSE){
        listDataHeader.add("Alerji Bilgileri");
        List<ArrayList> alerji = new ArrayList<ArrayList>();
        if(RESPONSE != null){
            try {

                JSONArray contacts = new JSONArray(RESPONSE);

                for (int i=0; i<contacts.length(); i++){
                    JSONObject c = contacts.getJSONObject(i);
                    ArrayList a = new ArrayList();
                    a.add(c.getString("AlerjiAd"));
                    alerji.add(a);

                }





            } catch (final JSONException e) {
                Toast.makeText(getApplicationContext(),"Json parse etmedi",Toast.LENGTH_LONG).show();
            }
        }
        else{
            ArrayList a = new ArrayList();
            a.add("Kişinin alerjen bilgisi bulunmamaktadır.");
            alerji.add(a);
        }
        listDataChild.put(listDataHeader.get(2),alerji);
    }
    private void doldurIletisim(String RESPONSE){
        listDataHeader.add("İletişim Bilgileri");
        List<ArrayList> iletisim = new ArrayList<ArrayList>();
        if(RESPONSE != null){
            try {

                JSONArray contacts = new JSONArray(RESPONSE);

                JSONObject c = contacts.getJSONObject(0);
                ArrayList a = new ArrayList();
                a.add(c.getString("Telefon"));
                a.add(c.getString("Mail"));
                a.add(c.getString("YakinAd"));
                a.add(c.getString("YakinSoyad"));
                a.add(c.getString("YakinTel"));
                a.add(c.getString("YakinDerece"));
                a.add(c.getString("Adres"));

                iletisim.add(a);

            } catch (final JSONException e) {
                Toast.makeText(getApplicationContext(),"Json parse etmedi",Toast.LENGTH_LONG).show();
            }
        }
        else{
            ArrayList a = new ArrayList();
            a.add("Kişinin iletişim bilgisi bulunmamaktadır.");
            iletisim.add(a);
        }
        listDataChild.put(listDataHeader.get(3),iletisim);
    }
    private void doldurAcilMudahale(String RESPONSE){
        listDataHeader.add("Acil Mudahale Bilgileri");
        List<ArrayList> mudahale = new ArrayList<ArrayList>();
        if(RESPONSE != null){
            try {

                JSONArray contacts = new JSONArray(RESPONSE);

                for (int i=0; i<contacts.length(); i++){
                    JSONObject c = contacts.getJSONObject(i);
                    ArrayList a = new ArrayList();
                    a.add(c.getString("Ad"));
                    a.add(c.getString("Soyad"));
                    a.add(c.getString("Yetki"));
                    a.add(c.getString("Tarih"));
                    a.add(c.getString("Aciklama"));

                    mudahale.add(a);

                }

            } catch (final JSONException e) {
                Toast.makeText(getApplicationContext(),"Json parse etmedi",Toast.LENGTH_LONG).show();
            }
        }
        else{
            ArrayList a = new ArrayList();
            a.add("Kişinin acil mudahale bilgisi bulunmamaktadır.");
            mudahale.add(a);
        }
        listDataChild.put(listDataHeader.get(4),mudahale);
    }

    private void JsonParser(String RESPONSE){
        try {

            JSONArray contacts = new JSONArray(RESPONSE);

            JSONObject c = contacts.getJSONObject(0);

            kisiID=c.getInt("KimlikId");
            textView_tc_H.setText(c.getString("Tcno"));
            textView_ad_H.setText(c.getString("Ad"));
            textView_soyad_H.setText(c.getString("Soyad"));
            textView_dogum_H.setText(c.getString("DogumTarihi"));
            textView_sehir_H.setText(c.getString("Sehir"));
            textView_kan_H.setText(c.getString("Kangrubu"));

        } catch (final JSONException e) {

            Toast.makeText(getApplicationContext(),"Json parse etmedi",Toast.LENGTH_LONG).show();


        }
    }

}

