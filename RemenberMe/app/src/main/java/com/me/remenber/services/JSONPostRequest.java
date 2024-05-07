package com.me.remenber.services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.me.remenber.dto.BackupDTO;
import com.me.remenber.entitys.SortData;
import com.me.remenber.entitys.User;
import com.me.remenber.pojo.JSONResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class JSONPostRequest extends AsyncTask<String, Void, String> {

    private static final String TAG = "JSONPostRequest";
    ManagementService managementService;
    private JSONObject data;
    private String url;
    private DTOServices dtoServices;
    private User user;

    private Context context;

    SortData sortData;
    public static String response;

    public JSONPostRequest(JSONObject data, String url) {
        this.data = data;
        this.url = url;
    }

    public JSONPostRequest(SortData sortData, User user, Context context) {

        if (user != null && user.getBackUpUrl2() != null) {
            this.user = user;
            this.url = this.user.getBackUpUrl2();
            this.dtoServices = new DTOServices(this.user);
        }
        if (sortData != null && context != null) {
            this.sortData = sortData;
            this.context = context;
            managementService = ManagementService.getInstance();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        StringBuilder result = new StringBuilder();
        Gson gson = new Gson();
        String valorJson = "";
        String urlFinal = this.url;
        boolean typeUrl= false;
        try {

            if (this.sortData.getKeyr() != null) {
                data = dtoServices.generateDTO(this.sortData);
                StringBuilder urlS = new StringBuilder(this.url);
                urlS.append("/");
                urlS.append(this.sortData.getKeyr());
                urlS.append(".json");
                urlFinal =urlS.toString();
                typeUrl= true;

            } else {
                data = dtoServices.generateDTO(this.sortData);
                StringBuilder urlS = new StringBuilder(this.url);
                urlS.append(".json");
                urlFinal =urlS.toString();
            }

            URL url = new URL(urlFinal);
            urlConnection = (HttpURLConnection) url.openConnection();
            if(typeUrl){
                urlConnection.setRequestMethod("PATCH");
            }else {
                urlConnection.setRequestMethod("POST");
            }
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
//            urlConnection.setRequestProperty("Accept", "application/json");
            byte[] postDataBytes = data.toString().getBytes("UTF-8");
            urlConnection.getOutputStream().write(postDataBytes);
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            String server_response = readStream(urlConnection.getInputStream());
//            Log.v("CatalogClient", server_response);

            if (server_response != null) {
                this.setId(server_response);
            }

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

        } catch (IOException e) {
            Log.e(TAG, "Error sending JSON POST request: " + e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error closing BufferedReader: " + e.getMessage());
            }
        }
        return result.toString();
    }

    private static String readStream(InputStream in) {
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

    private void setId(String response) {
        if (sortData.getKeyr() == null) {
            Gson gson = new Gson();
            JSONResponse resp = gson.fromJson(response, JSONResponse.class);
            if (resp.getName() != null) {
                sortData.setKeyr(resp.getName());
                managementService.setSortData(sortData);
                managementService.updateSortData(context);
                Log.e(TAG, "save the id into sortData from JSON POST: ");
//
            }
        }
    }

}
