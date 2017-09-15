package com.example.hackaton.transfapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    static WebView myWeb;
    static Button btnRefresh;
    static Button btnYes;
    static Button btnNo;
    static String baseUrl = "http://10.3.81.53/RptServer/";
    private static String idFactura = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myWeb = (WebView) findViewById(R.id.webview);
        //myWeb.getSettings().setJavaScriptEnabled(true);

        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        //btnRefresh.setText("Refresh");
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetId();
            }
        });

        btnYes = (Button) findViewById(R.id.btnYes);
        btnYes.setVisibility(View.GONE);
        //btnYes.setText("Yes");
        btnYes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myWeb.loadUrl(baseUrl + "TransferAction.aspx?ID=" + idFactura + "&Result=1");
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
                btnYes.setVisibility(View.GONE);
                btnNo.setVisibility(View.GONE);
                btnRefresh.setVisibility(View.GONE);
            }
        });
    }

    public static void ShowRequest(String idFact) {
        idFactura = idFact;
        myWeb.loadUrl(baseUrl + "Default.aspx?ID=" + idFactura);
        btnRefresh.setVisibility(View.GONE);
        btnYes.setVisibility(View.VISIBLE);
        btnNo.setVisibility(View.VISIBLE);
    }

    private void GetId() {
        try {
            URL url = new URL(baseUrl + "InvoicePooling.aspx");
            new MyAsyncTask().execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private class MyAsyncTask extends AsyncTask<URL, Integer, String> {

        protected String doInBackground(URL... urls) {
            URL url;
            String result = "";
            HttpURLConnection urlConnection = null;
            BufferedReader br = null;
            try {
                url = urls[0];
                URLConnection conn = url.openConnection();
                if (!(conn instanceof HttpURLConnection)) {
                    throw new IOException();
                }
                urlConnection = (HttpURLConnection) conn;
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                br = new BufferedReader(new InputStreamReader(in));
                result = br.readLine();
/*            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();*/
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
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
            return result;
        }

        @Override
        protected void onPostExecute(String sId) {
            super.onPostExecute(sId);
            MainActivity.ShowRequest(sId);
        }

    }
}