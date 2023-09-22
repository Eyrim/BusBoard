package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class StopPointData {
    @SerializedName("stopPoints")
    private StopPoint[] stopPoints;

    public StopPoint[] getStopPoints() {
        return stopPoints;
    }

    public static class StopPoint {
        @SerializedName("lineGroup")
        private LineGroup[] lineGroups;

        public LineGroup[] getLineGroups() {
            return this.lineGroups;
        }

        public static class LineGroup {
            @SerializedName("naptanIdReference")
            private String naptanId;

            public String getNaptanId() {
                return this.naptanId;
            }
        }
    }

    private static final String BASE_STOPPOINT_URL = "https://api.tfl.gov.uk/StopPoint/?lat=%s&lon=%s&stopTypes=NaptanOnstreetBusCoachStopPair&radius=50";

    public static StopPointData.StopPoint[] getStopPoints(String postCode) {
        try {
            URL stopPointUrl;
            PostcodeResult postCodeData = PostcodeResult.getPostCodeData(postCode);
            HttpURLConnection con;
            Gson gson = new GsonBuilder().create();
            StopPointData stopPointData;

            stopPointUrl = new URL(String.format(
                    BASE_STOPPOINT_URL,
                    postCodeData.getData().getLatitude(),
                    postCodeData.getData().getLongitude())
            );
            con = (HttpURLConnection) stopPointUrl.openConnection();
            con.setRequestMethod("GET");

            stopPointData = readStopPointData(con.getInputStream());

            if (stopPointData.getStopPoints().length == 0) {
                throw new RuntimeException("NO STOP POINTS IN THAT POSTCODE");
            }

            return stopPointData.getStopPoints();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static StopPointData readStopPointData(InputStream in) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            StopPointData stopPointData;
            Gson gson = new GsonBuilder().create();
            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = br.readLine()) != null) {
                content.append(inputLine);
            }

            stopPointData = gson.fromJson(content.toString(), StopPointData.class);

            return stopPointData;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
