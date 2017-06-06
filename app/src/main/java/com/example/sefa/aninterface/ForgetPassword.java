package com.example.sefa.aninterface;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.Calendar;


public class ForgetPassword extends AppCompatActivity {
    private EditText editText_Tc;
    private EditText editText_Isim;
    private EditText editText_Soyisim;
    private EditText editText_Dogum;
    private EditText editText_Il;
    private int dogrulamaId;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sifre_degistirme,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menuBack){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_forget_password);
        setSupportActionBar(myToolbar);

        editText_Tc =(EditText)findViewById(R.id.editText_yenileme_Tc);
        editText_Isim =(EditText)findViewById(R.id.editText_yenileme_isim);
        editText_Soyisim =(EditText)findViewById(R.id.editText_yenileme_soyisim);
        editText_Dogum =(EditText)findViewById(R.id.editText_yenileme_dogum);
        editText_Il =(EditText)findViewById(R.id.editText_yenileme_il);

        editText_Dogum.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String ggaayyyy = "ggaayyyy";
            private Calendar cal = Calendar.getInstance();
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if(!charSequence.toString().equals(current)){

                    String clean = charSequence.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int j = 2; j <= cl && j < 6; j += 2) {
                        sel++;
                    }
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ggaayyyy.substring(clean.length());
                    }else{
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    editText_Dogum.setText(current);
                    editText_Dogum.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        Button button = (Button)findViewById(R.id.button_yenileme_Dogrula);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(valid()){
                    new ConnectService().execute();
                }
                else{
                    setAlertDialog();
                }
            }
        });

    }
    private class ConnectService extends AsyncTask<Void,Void,Void> {
        ProgressDialog progressDialog = new ProgressDialog(ForgetPassword.this);

        String tcno=editText_Tc.getText().toString();
        String ad =editText_Isim.getText().toString();
        String soyad=editText_Soyisim.getText().toString();
        String dogum=editText_Dogum.getText().toString();
        String sehir=editText_Il.getText().toString();

        String RESPONSE;

        boolean valid=false;


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
            String URL ="http://192.168.43.24/Servis.asmx";
            String METHOD_NAME ="getControlPerson";
            String SOAP_ACTION = "http://tempuri.org/getControlPerson";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("inputTc",tcno);
            request.addProperty("inputAd",ad);
            request.addProperty("inputSoyad",soyad);
            request.addProperty("inputDtarih",dogum);
            request.addProperty("inputSehir",sehir);


            HttpGetJSON httpGetJSON=new HttpGetJSON(URL,SOAP_ACTION);
            RESPONSE = httpGetJSON.getJsonString(request);

            if (RESPONSE!=null) {

                createJsonParser(RESPONSE);
                valid=true;
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

            if(valid){
                //şifre dogru yeni intent kur
                Intent intent = new Intent(ForgetPassword.this,NewPassword.class);
                intent.putExtra("ApploginId",dogrulamaId);
                startActivity(intent);
                ForgetPassword.this.finish();
            }
            else{
              setAlertDialogYanlis();
            }




        }
    }
    private void createJsonParser(String RESPONSE){
        try {

            JSONArray contacts = new JSONArray(RESPONSE);

            JSONObject c = contacts.getJSONObject(0);

            dogrulamaId = c.getInt("ApploginId");



        } catch (final JSONException e) {

            Toast.makeText(getApplicationContext(),"Json parse etmedi",Toast.LENGTH_LONG).show();


        }
    }
    private boolean valid(){
        boolean valid=true;

        if(editText_Tc.getText().toString().length()<11)
        {
            editText_Tc.setError("T.C Numaranızı giriniz.");
            valid=false;
        }
        if(editText_Isim.getText().toString().isEmpty())
        {
            editText_Isim.setError("İsminizi giriniz.");
            valid=false;
        }
        if(editText_Soyisim.getText().toString().isEmpty())
        {
            editText_Soyisim.setError("Soyisminizi giriniz.");
            valid=false;
        }
        if(editText_Dogum.getText().toString().isEmpty())
        {
            editText_Dogum.setError("Dogum tarihinizi gg/aa/yyyy olarak giriniz.");
            valid=false;
        }
        if(editText_Il.getText().toString().isEmpty())
        {
            editText_Il.setError("İlinizi giriniz.");
            valid=false;
        }
        return valid;
    }
    private void setAlertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ForgetPassword.this);
        alertDialogBuilder
                //.setTitle("Uyarı")
                .setMessage("Lütfen gerekli alanları doldurunuz.")
                .setPositiveButton("Tamam",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }
    private void setAlertDialogYanlis(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ForgetPassword.this);
        alertDialogBuilder
                //.setTitle("Uyarı")
                .setMessage("Girdiğiniz bilgiler uyuşmamaktadır..")
                .setPositiveButton("Tamam",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

}


