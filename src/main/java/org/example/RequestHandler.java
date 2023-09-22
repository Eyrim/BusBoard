package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestHandler {
    private static Logger log = LogManager.getLogger();

    public static <T> T sendGetRequest(String href, Class<T> returnType) {
        try {
            URL url = new URL(href);
            HttpURLConnection con;

            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            return parseJson(readRequestBody(con.getInputStream()), returnType);
        } catch (IOException e) {
            log.fatal("Request to: " + href + " Failed", e);
            // Rethrow
            throw new RuntimeException(e);
        }
    }

    private static <T> T parseJson(String json, Class<T> type) {
        Gson gson = new GsonBuilder().create();

        // Hopefully fine?
        return gson.fromJson(json, type);
    }

    private static String readRequestBody(InputStream in) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = reader.readLine()) != null) {
                content.append(inputLine);
            }

            return content.toString();
        } catch (IOException e) {
            log.fatal("Failed to read request body", e);
            // Rethrow after logging
            throw new RuntimeException(e);
        }
    }
}
