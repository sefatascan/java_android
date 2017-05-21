package com.example.sefa.aninterface;

import android.os.AsyncTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;


public class HttpGetJSON {

    private String URL;
    private String SOAP_ACTION;
    private String RESPONSE=null;

    public HttpGetJSON( String URL, String SOAP_ACTION){

        this.URL=URL;
        this.SOAP_ACTION=SOAP_ACTION;

    }



    public String getJsonString(SoapObject request){

        try {

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(URL);
            transporte.call(SOAP_ACTION, envelope);
            SoapPrimitive resultado_xml = (SoapPrimitive) envelope.getResponse();
            if(resultado_xml!=null){

                RESPONSE = resultado_xml.toString();

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return RESPONSE;
    }




}
