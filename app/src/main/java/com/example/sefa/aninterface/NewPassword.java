package com.example.sefa.aninterface;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

public class NewPassword extends AppCompatActivity {
    private EditText editText_Sifre;
    private EditText editText_SifreDogrula;
    private int ApploginId;
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
        setContentView(R.layout.activity_new_password);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_new_password);
        setSupportActionBar(myToolbar);
        Bundle extra = getIntent().getExtras();
        ApploginId = extra.getInt("ApploginId");


        editText_Sifre = (EditText)findViewById(R.id.editText_yenile_sifre);
        editText_SifreDogrula = (EditText)findViewById(R.id.editText_yenile_sifre_dogrula);

        Button button_iptal = (Button)findViewById(R.id.button_yenile_iptal);
        Button button_onayla = (Button)findViewById(R.id.button_yenile_onayla);
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
        editText_SifreDogrula.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    if( editText_SifreDogrula.getText().toString().length()<4) {
                        editText_SifreDogrula.setError("Şifre en az 4 haneli olmalıdır.");
                    }
                }
            }
        });

        button_iptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        button_onayla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(valid()){
                  if(!editText_Sifre.getText().toString().equals(editText_SifreDogrula.getText().toString())){
                      setAlertDialog2();
                  }
                  else {

                      new ConnectService().execute();
                  }

              } else{
                  setAlertDialog();

              }
            }
        });


    }
    private class ConnectService extends AsyncTask<Void,Void,Void> {
        ProgressDialog progressDialog = new ProgressDialog(NewPassword.this);

        String sifre=editText_Sifre.getText().toString();

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
            String METHOD_NAME ="setPassword";
            String SOAP_ACTION = "http://tempuri.org/setPassword";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("inputId",ApploginId); // gelecek
            request.addProperty("setpassword",sifre);


            HttpGetJSON httpGetJSON=new HttpGetJSON(URL,SOAP_ACTION);
            httpGetJSON.getJsonString(request);



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

                setAlertDialog3();


        }
    }
    private boolean valid(){

        boolean valid=true;

        if(editText_Sifre.getText().toString().length()<4)
        {
            editText_Sifre.setError("Şifrenizi en az 4 haneli giriniz.");
            valid=false;
        }
        if(editText_SifreDogrula.getText().toString().length()<4)
        {
            editText_SifreDogrula.setError("Şifre tekrarını giriniz.");
            valid=false;
        }
        return valid;
    }
    private void setAlertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NewPassword.this);
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
    private void setAlertDialog2(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NewPassword.this);
        alertDialogBuilder
                //.setTitle("Uyarı")
                .setMessage("Şifreler uyuşmamaktadır.")
                .setPositiveButton("Tamam",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }
    private void setAlertDialog3(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NewPassword.this);
        alertDialogBuilder
                //.setTitle("Uyarı")
                .setMessage("Şifreniz başarıyla değiştirilmiştir.")
                .setPositiveButton("Tamam",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                finish();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }
}
