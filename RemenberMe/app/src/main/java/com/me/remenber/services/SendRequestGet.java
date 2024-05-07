package com.me.remenber.services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.me.remenber.entitys.SortData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class SendRequestGet extends AsyncTask<String, Void, String> {

    private String server_response;
    @SuppressLint("StaticFieldLeak")
    private  ManagementService managementService;
    private DTOServices dtoServices;
    private Context context;

    public SendRequestGet() {
        managementService = ManagementService.getInstance();
    }

    public SendRequestGet(Context context, DTOServices dtoServices) {
        managementService = ManagementService.getInstance();
        this.dtoServices = dtoServices;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        server_response = null;
        URL url;
        HttpURLConnection httpConnection = null;
        try {
            url = new URL(strings[0]);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty("Content-length", "0");
            httpConnection.setUseCaches(false);
            httpConnection.setAllowUserInteraction(false);
            httpConnection.setConnectTimeout(100000);
            httpConnection.setReadTimeout(100000);
            httpConnection.connect();
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                server_response = readStream(httpConnection.getInputStream());
                managementService.setServerResponse(server_response);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Thread thread = new Thread() {
            public void run() {
                getServerResponses(server_response);
            }
        };
        thread.start();
    }


    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

    public boolean checkServerResponse() {
        if (server_response != null && !server_response.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public String getServerResponse() {
        return server_response;
    }


    private boolean getServerResponses(String text){
        if(text != null && !text.equals("null") ){
            String response = text;
            List<SortData> listSortData = this.dtoServices.getData(response);
            if (listSortData != null && listSortData.size()>0) {
                for (SortData sortData1: listSortData) {
                    this.saveData(sortData1);
                }
//                Toast.makeText(context, "The Backup is Ready", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "no element for  Backup", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void saveData(SortData sortData1){
        if(sortData1.getKeyr() != null){
            List<SortData> findSortDataByKey = managementService.findByKey(sortData1.getKeyr(),context);
            if(findSortDataByKey.size()>0) {
                managementService.setSortData(sortData1);
                managementService.updateSortData(context);
            }else {
                this.set(sortData1);
            }
        }else {
            this.set(sortData1);
        }
    }


    private void set(SortData sortData1){
        managementService.setSortData(sortData1);
        managementService.insertSortData(context);
    }

}
