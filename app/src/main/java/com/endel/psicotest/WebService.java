package com.endel.psicotest;


import java.util.ArrayList;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.HttpsTransportSE;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;

import com.endel.psicotest.vo.DocumentsIDs;
import com.endel.psicotest.vo.RespuestasUsuarioNM_VO;

public class WebService {

    HttpsTransportSE androidHttpTransport;

    private String NAMESPACE;
    private String URL;
    Context contx;
    public static String ip = "156.35.71.167:8080";

    public WebService(String ip, Context ctx) {
        contx = ctx;
        NAMESPACE = "http://" + ip + "/PsicologoService/";
        URL = "http://" + ip + "/PsicologoService/Service1.asmx";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }

















    public String ObtenerExperimento(String Id) {
        String SOAP_ACTION = "http://192.168.0.1/Android2/ObtenerAgendaIdentificador";
        String METHOD_NAME = "ObtenerExperimentoIdentificador";

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("id", Id);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            int Timeout = 60000;

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, Timeout);
            androidHttpTransport.call(SOAP_ACTION, envelope);

            Object result = (Object) envelope.getResponse();

            return result.toString();
        } catch (Exception e) {
            return "ERROR";
        }
    }



    public boolean EstaDisponible(String ip) {
        String SOAP_ACTION = "http://" + ip + "/PsicologoService/IsAlive";
        String METHOD_NAME = "IsAlive";

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            int Timeout = 60000;

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, Timeout);

            androidHttpTransport.call(SOAP_ACTION, envelope);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }



    public boolean isNetworkAvailable() {
        Context context = contx;
        ConnectivityManager connectivity = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public static boolean enviarTests (ArrayList<RespuestasUsuarioNM_VO> listaRespuestas) {
        String NAMESPACE ="http://"+ip+"/psicologoservice/";
        String URL = "http://"+ip+"/psicologoservice/service1.asmx";

        String SOAP_ACTION1 = "http://" + ip + "/psicologoservice/InsertarResultado2";
        String SOAP_ACTION2 = "http://" + ip + "/PsicologoService/InsertarFichero";
        String METHOD_NAME1 = "InsertarResultado2";
        String METHOD_NAME2 = "InsertarFichero";

        SoapPrimitive result1, result2;

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);

            DocumentsIDs documentIdVector = new DocumentsIDs();
            for (int i = 0; i < listaRespuestas.size(); i++) {
                documentIdVector.add(listaRespuestas.get(i));
            }

            PropertyInfo documentIdsPropertyInfo;
            documentIdsPropertyInfo = new PropertyInfo();
            documentIdsPropertyInfo.setName("resultados");
            documentIdsPropertyInfo.setValue(documentIdVector);
            documentIdsPropertyInfo.setType(documentIdVector.getClass());

            request.addProperty(documentIdsPropertyInfo);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            int Timeout = 60000;

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, Timeout);

            androidHttpTransport.call(SOAP_ACTION1, envelope);

            result1 = (SoapPrimitive) envelope.getResponse();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}

