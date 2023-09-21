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
    private static final String BASE_POSTCODE_URL = "https://api.postcodes.io/postcodes/%s";
                                                                                                            //  Dice rolling this as the right type
    private static final String BASE_STOPPOINT_URL = "https://api.tfl.gov.uk/StopPoint/?lat=%s&lon=%s&stopTypes=NaptanOnstreetBusCoachStopPair&radius=500";

    public static void main(String[] args) {
        try { // 490000129R
            BusData[] data;
            //URL tflUrl = new URL(String.format(BASE_TFL_URL, getValueFromUser("Enter StopPoint ID:")));
            URL tflUrl;
            StopPointData.StopPoint[] stopPoints = getStopPoints();

            for (StopPointData.StopPoint s : stopPoints) {
                tflUrl = new URL(String.format(BASE_TFL_URL, s.getLines()));
                /*
                This is where it breaks ^^^

                I do not understand the data TFL return in the stoppoint endpoint
                SO I don't know what to append to the URL to get the desired bus data
                 */

                HttpURLConnection con = (HttpURLConnection) tflUrl.openConnection();

                con.setRequestMethod("GET");

                if (con.getResponseCode() == 200) {
                    data = readBusData(con.getInputStream());
                    listBusData(data, 5);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static PostcodeResult getPostCodeData() {
        try {
            URL postcodeUrl = new URL(String.format(BASE_POSTCODE_URL, getValueFromUser("Enter Postcode:")));
            HttpURLConnection con = (HttpURLConnection) postcodeUrl.openConnection();
            Gson gson = new GsonBuilder().create();
            PostcodeResult postcodeData;

            if (con.getResponseCode() == 200) {
                postcodeData = readPostcodeData(con.getInputStream());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static StopPointData.StopPoint[] getStopPoints() {
        try {
            URL stopPointUrl;
            PostcodeResult postCodeData = getPostCodeData();
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