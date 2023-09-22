package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class Main {
    private static final String BASE_TFL_URL = "https://api.tfl.gov.uk/StopPoint/%s/Arrivals";

    public static void main(String[] args) {
        try { // 490000129R
            BusData[] data;
            //URL tflUrl = new URL(String.format(BASE_TFL_URL, getValueFromUser("Enter StopPoint ID:")));
            URL tflUrl;
            StopPointData.StopPoint[] stopPoints = StopPointData.getStopPoints(getValueFromUser("Enter Postcode:"));

            for (StopPointData.StopPoint s : stopPoints) {
                for (StopPointData.StopPoint.LineGroup l : s.getLineGroups()) {
                    tflUrl = new URL(String.format(BASE_TFL_URL, l.getNaptanId()));
                /*
                This is where it breaks ^^^

                I do not understand the data TFL return in the stoppoint endpoint
                SO I don't know what to append to the URL to get the desired bus data
                 */

                    HttpURLConnection con = (HttpURLConnection) tflUrl.openConnection();

                    con.setRequestMethod("GET");

                    if (con.getResponseCode() == 200) {
                        data = readBusData(con.getInputStream());
                        data = sortBusData(data);
                        listBusData(data, 5);
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }







    private static BusData[] readBusData(InputStream in) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
//            ArrayList<T> data = new ArrayList<>();
            BusData[] data;
            Gson gson = new GsonBuilder().create();
            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = br.readLine()) != null) {
                content.append(inputLine);
            }

            data = gson.fromJson(content.toString(), BusData[].class);

            return data;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void listBusData(BusData[] data, int amount) {
        amount = Math.min(amount, data.length);

        for (int i = 0; i < amount; i++) {
            data[i].listArrival();
        }
    }

    private static void listBusData(BusData[] data) {
        listBusData(data, data.length);
    }

    private static String getValueFromUser(String prompt) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println(prompt);

            return sc.nextLine();
        }
    }

    private static BusData[] sortBusData(BusData[] data) {
        return Arrays.stream(data)
                .sorted(Comparator.comparingInt(BusData::getTimeToStationSeconds))
                .toList().toArray(BusData[]::new);
    }
}