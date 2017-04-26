package com.ictmakers.eventmarker;

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

    public <T> T sendCommand(Command command, Class<T> responseType) throws IOException {

        URL url = new URL("http://eventmarker.ictmakers.com/api/Command/Esegui?nomeComando=" + URLEncoder.encode(command.getTypeName()));

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
            return gson.fromJson(response, responseType);
        }

        return null;
    }

    public static final CommandManager instance = new CommandManager();
}
