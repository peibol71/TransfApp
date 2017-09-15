package com.example.hackaton.transfapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;



public class MainActivity extends AppCompatActivity {
    boolean bPageLoaded = false;
    Button btnYes;
    Button btnNo;
    String baseUrl = "http://10.3.81.53/RptServer/";
    public static String idFactura = "22";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WebView myWeb = (WebView) findViewById(R.id.webview);
        //myWeb.getSettings().setJavaScriptEnabled(true);

        //myWeb.loadUrl("https://www.exact.com");
        //myWeb.loadUrl("http://lt-17-164/RptServer/Default.aspx?ID=7");
        //myWeb.loadUrl("http://10.3.81.53/RptServer/Default.aspx?ID=7");

        //idFactura = getId();
        try {
            URL url = new URL(baseUrl + "InvoicePooling.aspx");
            new MyAsyncTask().execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        final Button btnRefresh = (Button) findViewById(R.id.btnRefresh);
        //btnRefresh.setText("Refresh");
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bPageLoaded)
                    myWeb.reload();
                else {
                    myWeb.loadUrl(baseUrl + "Default.aspx?ID=" + idFactura);
                    bPageLoaded = true;
                    btnYes.setVisibility(View.VISIBLE);
                    btnNo.setVisibility(View.VISIBLE);
                }
            }
        });

        btnYes = (Button) findViewById(R.id.btnYes);
        btnYes.setVisibility(View.GONE);
        //btnYes.setText("Yes");
        btnYes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myWeb.loadUrl(baseUrl + "TransferAction.aspx?ID=" + idFactura + "&Result=1");
                //myWeb.loadUrl("http://www.exact.es");
                btnYes.setVisibility(View.GONE);
                btnNo.setVisibility(View.GONE);
                btnRefresh.setVisibility(View.GONE);
            }
        });
        btnNo = (Button) findViewById(R.id.btnNo);
        btnNo.setVisibility(View.GONE);
        //btnNo.setText("No");
        btnNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myWeb.loadUrl(baseUrl + "TransferAction.aspx?ID=" + idFactura + "&Result=0");
                // /myWeb.loadUrl("http://www.exact.es");
                //myWeb.reload();
                btnYes.setVisibility(View.GONE);
                btnNo.setVisibility(View.GONE);
                btnRefresh.setVisibility(View.GONE);
            }
        });
    }
}

class MyAsyncTask extends AsyncTask<URL, Integer, Long>
{
    protected Long doInBackground(URL... urls) {
        URL url = null;
        String result = "";
        HttpURLConnection urlConnection = null;
        BufferedReader br = null;
        try {
            url = urls[0];
            URLConnection conn = url.openConnection();
            if (!(conn instanceof HttpURLConnection)) {
                throw new IOException();
            }
            urlConnection = (HttpURLConnection) conn; //url.openConnection();
            InputStream in = urlConnection.getInputStream();
            InputStream in2 = new BufferedInputStream(in);
            //readStream(in);
            br = new BufferedReader(new InputStreamReader(in2));
            result = br.readLine();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        MainActivity.idFactura=result;
        return Long.MAX_VALUE;
    }

}