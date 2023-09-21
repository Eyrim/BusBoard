package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostcodeResult {
    @SerializedName("result")
    private PostcodeData data;

    public PostcodeData getData() {
        return data;
    }

    public static class PostcodeData {
        @SerializedName("postcode")
        private String postcode;
        @SerializedName("latitude")
        private String latitude;
        @SerializedName("longitude")
        private String longitude;

        private String getPostcode() {
            return this.postcode;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }
    }

    private static final String BASE_POSTCODE_URL = "https://api.postcodes.io/postcodes/%s";

    public static PostcodeResult getPostCodeData(String postCode) {
        try {
            URL postcodeUrl = new URL(String.format(BASE_POSTCODE_URL, postCode));
            HttpURLConnection con = (HttpURLConnection) postcodeUrl.openConnection();
            Gson gson = new GsonBuilder().create();

            if (con.getResponseCode() == 200) {
                return readPostcodeData(con.getInputStream());
            } else {
                throw new RuntimeException("BAD RESPONSE CODE");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static PostcodeResult readPostcodeData(InputStream in) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            PostcodeResult postcodeData;
            Gson gson = new GsonBuilder().create();
            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = br.readLine()) != null) {
                content.append(inputLine);
            }

            postcodeData = gson.fromJson(content.toString(), PostcodeResult.class);

            return postcodeData;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
