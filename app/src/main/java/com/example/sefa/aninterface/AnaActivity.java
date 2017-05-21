package com.example.sefa.aninterface;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.ksoap2.serialization.SoapObject;



public class AnaActivity extends AppCompatActivity {

    private TextView textView_tc;
    private TextView textView_yetki;
    private TextView textView_ad;
    private TextView textView_soyad;
    private TextView textView_dogum;
    private TextView textView_sehir;
    private TextView textView_kan;
    private FloatingActionButton buton_qrcode;
    private KeepLoginPerson loginPerson;
    private LinearLayout linear_yapilan;
    private LinearLayout linear_cikis;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana);
        buton_qrcode = (FloatingActionButton) findViewById(R.id.buton_qrcode);
        textView_ad= (TextView)findViewById(R.id.textView_ad);
        textView_soyad= (TextView)findViewById(R.id.textView_soyad);
        textView_tc= (TextView)findViewById(R.id.textView_tc);
        textView_yetki= (TextView)findViewById(R.id.textView_yetki);
        textView_dogum= (TextView)findViewById(R.id.textView_dogumtarihi);
        textView_sehir= (TextView)findViewById(R.id.textView_sehir);
        textView_kan= (TextView)findViewById(R.id.textView_kangrub);
        linear_yapilan=(LinearLayout)findViewById(R.id.linear_yapilan);
        linear_cikis=(LinearLayout)findViewById(R.id.linear_cikis);


        loginPerson =(KeepLoginPerson)getIntent().getSerializableExtra("ApploginKisi");
        textView_ad.setText(loginPerson.getAd());
        textView_soyad.setText(loginPerson.getSoyad());
        textView_tc.setText(loginPerson.getTcno());
        textView_yetki.setText(loginPerson.getYetki());
        textView_dogum.setText(loginPerson.getDogumTarihi());
        textView_sehir.setText(loginPerson.getSehir());
        textView_kan.setText(loginPerson.getKangrubu());


        buton_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Vibrator titresim = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                titresim.vibrate(150);

                IntentIntegrator integrator = new IntentIntegrator(AnaActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
                        .setOrientationLocked(false)
                        .setPrompt("Kodu Okutunuz")
                        .setCameraId(0)
                        .setBeepEnabled(false)
                        .setBarcodeImageEnabled(true)
                        .initiateScan();

            }
        });


        linear_yapilan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(AnaActivity.this);
                dialog.setContentView(R.layout.custom_dialog_profil);
                dialog.show();
            }
        });
        linear_cikis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAlertDialog();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result != null) {

            if(result.getContents() == null) {
                //iptal
                Toast.makeText(AnaActivity.this,"Qr Kod okuma başarısız !",Toast.LENGTH_LONG).show();

            } else {
                //Okuma basarili
                final Vibrator titresim = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                titresim.vibrate(50);
                new ConnectService(result.getContents()).execute();
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }



    private class ConnectService extends AsyncTask<Void,Void,Void>{
        ProgressDialog progressDialog = new ProgressDialog(AnaActivity.this);
        String input;
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

        public ConnectService(String input) {
            super();
            this.input=input;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String NAMESPACE ="http://tempuri.org/" ;
            String URL ="http://192.168.1.54/Servis.asmx";
            String METHOD_NAME ="getKisi";
            String SOAP_ACTION = "http://tempuri.org/getKisi";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("inputQRTcno",input);

            HttpGetJSON httpGetJSON=new HttpGetJSON(URL,SOAP_ACTION);
            RESPONSE = httpGetJSON.getJsonString(request);

            if (RESPONSE!=null) {

                valid=true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressDialog.dismiss();

            if(valid){

                Intent intent = new Intent(getApplicationContext(),AcilMudahale.class);
                intent.putExtra("RESPONSE",RESPONSE);
                startActivity(intent);

            }
            else{
                setAlertDialog2();
            }
        }


    }
    private void setAlertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AnaActivity.this);
        alertDialogBuilder
                .setMessage("Çıkmak istediğinize emin misiniz ?")
                .setCancelable(false)
                .setNegativeButton("İptal",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                .setPositiveButton("Çıkış Yap",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                startActivity(intent);
                                AnaActivity.this.finish();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }
    private void setAlertDialog2(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AnaActivity.this);
        alertDialogBuilder
                .setMessage("İlgili QR Kodu sistemde bulunmamaktadır.")
                .setCancelable(false)
                .setNegativeButton("Tamam",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }
}
