package br.com.george.apprfid.Webservice;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

public class Webservice {
    private URL urlJSOn;
    private String url;
    private HttpURLConnection con = null;
    //private final String URL_GRAILS = "http://192.168.0.16:8080/Patrimonio/api/";
    //private final String URL_GRAILS = "http://192.168.1.49:8080/Patrimonio/api/";
    private String URL_GRAILS;
    private final String USER_AGENT = "Mozilla/5.0";
    private String URL_POST;
    private List<String> cookies;
    private HttpURLConnection conn;
    private Context ctx;
    private SharedPreferences prefs;

    public Webservice(Context ctx, String url) {
        prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        URL_GRAILS = prefs.getString("webservice", null);
        this.url = URL_GRAILS + url;
        this.ctx = ctx;
    }

    public String getJSON() {
        try {
            this.urlJSOn = new URL(url);
            con = (HttpURLConnection) this.urlJSOn.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Content-length", "0");
            con.setUseCaches(false);
            con.setAllowUserInteraction(false);
            con.connect();
            int status = con.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    return sb.toString();
            }
        } catch (ProtocolException ex) {
            Log.d("ProtocolException", ex.getMessage());
        } catch (IOException ex) {
            Log.d("IOException", ex.getMessage());
        } finally {
            if (con != null) {
                try {
                    con.disconnect();
                } catch (Exception ex) {
                    Log.d("Exception", ex.getMessage());
                }
            }
        }
        return null;
    }

    public void postJSON(String url_post, String json) {
        URL_POST = URL_GRAILS + url_post;
        sendPost(URL_POST, json);
    }

    public void putJSON(String url_post, String json) {
        URL_POST = URL_GRAILS + url_post;
        sendPut(URL_POST, json);
    }

    private void sendPost(String url, String postParams) {
        try {
            BufferedReader reader=null;

            URL obj = new URL(url);
            conn = (HttpURLConnection) obj.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestMethod("POST");
            conn.connect();


            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(postParams);
            wr.flush();
            wr.close();

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            conn.disconnect();
        }
    }

    private void sendPut(String url, String postParams) {
        try {
            BufferedReader reader=null;

            URL obj = new URL(url);
            conn = (HttpURLConnection) obj.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestMethod("PUT");
            conn.connect();


            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(postParams);
            wr.flush();
            wr.close();

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            conn.disconnect();
        }
    }

}
