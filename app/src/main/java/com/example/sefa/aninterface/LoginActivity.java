package com.example.sefa.aninterface;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;


public class LoginActivity extends AppCompatActivity {
    private Button button_giris;
    private EditText editText_TC;
    private EditText editText_Sifre;
    private TextView textView_sUnuttum;
    private KeepLoginPerson loginPerson;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        if( checkInternetConnection() == false){
           setAlertDialogConnectNetwork();
        }

        editText_TC = (EditText)findViewById(R.id.editText_TC);
        editText_Sifre = (EditText)findViewById(R.id.editText_Sifre);
        button_giris = (Button) findViewById(R.id.button_giris);
        textView_sUnuttum = (TextView)findViewById(R.id.textView_sUnuttum);




        editText_TC.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    if( editText_TC.getText().toString().length()<11) {
                        editText_TC.setError("T.C. Kimlik No 11 haneli olmalıdır.");
                    }
                }
            }
        });
        editText_Sifre.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    if( editText_Sifre.getText().toString().length()<4) {
                        editText_Sifre.setError("Şifre en az 4 haneli olmalıdır.");
                    }
                }
            }
        });

        textView_sUnuttum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ForgetPassword.class);
                startActivity(intent);

            }
        });

        button_giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editText_TC.getText().toString().length()<11 || editText_Sifre.getText().toString().length()<4){
                    setAlertDialog();
                }
                else{

                    new ConnectService().execute();


                }

            }
        });

    }
    private void setAlertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
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
    private void setAlertDialogSifreYanlis(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        alertDialogBuilder
                //.setTitle("Uyarı")
                .setMessage("T.C No ve Şifre hatalıdır.")
                .setPositiveButton("Tamam",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }
    private void setAlertDialogConnectNetwork(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        alertDialogBuilder
                .setCancelable(false)
                //.setTitle("Uyarı")
                .setMessage("İnternet erişiminiz bulunmamaktadır." +
                        "Lütfen internet bağlantınızı kontrol ediniz.")
                .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                finish();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }
    private boolean checkInternetConnection(){

        ConnectivityManager check = (ConnectivityManager)getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = check.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();


    }

    private class ConnectService extends AsyncTask<Void,Void,Void>{
        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);

        String text_tc =editText_TC.getText().toString();
        String text_sifre =editText_Sifre.getText().toString();

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
            String URL ="http://192.168.1.35/Servis.asmx";
            String METHOD_NAME ="getapplogin";
            String SOAP_ACTION = "http://tempuri.org/getapplogin";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("inputTcno", text_tc);
            request.addProperty("sifre", text_sifre);


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
                // şifre doğru, login ol
                Intent intent = new Intent(getApplicationContext(),AnaActivity.class);
                intent.putExtra("ApploginKisi", loginPerson);
                startActivity(intent);
                LoginActivity.this.finish();
            }
            else{
                setAlertDialogSifreYanlis();
            }




        }
    }

    private void createJsonParser(String RESPONSE){
        try {

            JSONArray contacts = new JSONArray(RESPONSE);

            JSONObject c = contacts.getJSONObject(0);

            loginPerson=new KeepLoginPerson();
            loginPerson.setApploginId(c.getInt("ApploginId"));
            loginPerson.setAd(c.getString("Ad"));
            loginPerson.setTcno(c.getString("Tcno"));
            loginPerson.setSoyad(c.getString("Soyad"));
            loginPerson.setDogumTarihi(c.getString("DogumTarihi"));
            loginPerson.setSehir(c.getString("Sehir"));
            loginPerson.setKangrubu(c.getString("Kangrubu"));
            loginPerson.setYetki(c.getString("Yetki"));






        } catch (final JSONException e) {

            Toast.makeText(getApplicationContext(),"Json parse etmedi",Toast.LENGTH_LONG).show();


        }
    }





}
