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

    /**
     * <pre>
     * Sends a get request to the URL found at href
     *
     * The returned data is then parsed into an object of type T
     * </pre>
     * @param href The URL to request
     * @param returnType The type the JSON should be parsed into
     * @return An object produced via <code>Gson.fromJson(String, Class&lt;T&gt;)</code>
     */
    public static <T> T sendGetRequest(String href, Class<T> returnType) {
        try {
            URL url = new URL(href);
            HttpURLConnection con;

            log.info("Sending get request to: " + href);

            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            if (con.getResponseCode() == 200) {
                log.info("Response code: 200 from: " + href);
                return parseJson(readRequestBody(con.getInputStream()), returnType);
            }

            log.error("Response code: " + con.getResponseCode() + " from: " + href);
            throw new RuntimeException("Non 200 response code from: " + href);
        } catch (IOException e) {
            log.fatal("Request to: " + href + " Failed", e);
            // Rethrow
            throw new RuntimeException(e);
        }
    }

    private static <T> T parseJson(String json, Class<T> type) {
        Gson gson = new GsonBuilder().create();

        log.info("Parsing response");
        // Hopefully fine?
        return gson.fromJson(json, type);
    }

    private static String readRequestBody(InputStream in) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            log.info("Reading request body");
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
