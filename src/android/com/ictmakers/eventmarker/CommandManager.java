package com.ictmakers.eventmarker;

import android.os.AsyncTask;
import android.util.Log;

import com.adobe.phonegap.push.PushPlugin;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.io.BufferedReader;

import javax.net.ssl.HttpsURLConnection;


public class CommandManager {

    String mAccessToken;

    public enum CoErrorLevel
    {
        Warning,
        Fatal
    }

    public class CoExecutionError{
        public CoErrorLevel Level;
        public String Message;
        public String Source;

    }
    public class CoCommandResult<T>{
        public boolean Succeed;
        public CoExecutionError[] Errors;
        public T Result;
    }

    public  void setAccessToken(String value){
        mAccessToken = value;
        Log.d("CommandManager", "accessToken: " +  mAccessToken);
    }

    public <T> CoCommandResult<T> sendCommand(Command command, Class<T> responseType) throws IOException {

        String regId = PushPlugin.getRegistrationID();

        Log.d("CommandManager", "regId: " +  regId);


        URL url = new URL("http://eventmarker.ictmakers.com/api/Command/Esegui?nomeComando=" + URLEncoder.encode(command.getTypeName()) + "&accessToken=" + URLEncoder.encode(mAccessToken));

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setUseCaches(false);
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        OutputStream stream = urlConnection.getOutputStream();
        Gson gson = new Gson();
        String commandJson = gson.toJson(command);
        stream.write(commandJson.getBytes(Charset.forName("UTF-8")));
        stream.flush();
        stream.close();

        int responseCode = urlConnection.getResponseCode();

        if (responseCode == HttpsURLConnection.HTTP_OK) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            String response = "";
            while ((line = reader.readLine()) != null)
                response += line;
            reader.close();
            return gson.fromJson(response, new CoCommandResult<T>().getClass());
        }

        return null;
    }

    public <T> AsyncTask<Void, Void, CoCommandResult<T>> sendCommandTask(final Command command, final Class<T> responseType)  {

        AsyncTask<Void, Void, CoCommandResult<T>> task = new AsyncTask<Void, Void, CoCommandResult<T>>(){

            @Override
            protected CoCommandResult<T> doInBackground(Void[] objects) {
                try {
                    return sendCommand(command, responseType);
                }
                catch (IOException ex)
                {
                    Log.e("DEBUG", "send command task failed", ex);
                }
                return null;
            }
        };

        return task;
    }

    public static final CommandManager instance = new CommandManager();
}
