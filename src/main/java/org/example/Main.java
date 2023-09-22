package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static org.example.BaseUrls.*;
import static org.example.StopPointData.StopPoint.LineGroup;

public class Main {
    private static final Logger log = LogManager.getLogger();

    public static void main(String[] args) {
        PostcodeResult postcodeData;
        StopPointData stopPointData;
        StopPointData.StopPoint[] stopPoints;
        BusData[] busData = null;

        // 490000129R <- known working stoppoint id
        postcodeData = RequestHandler.sendGetRequest(
                String.format(
                        BASE_POSTCODE_URL.getBaseUrl(),
                        getPostcodeFromUser()
                ),
                PostcodeResult.class
        );;

        stopPointData = RequestHandler.sendGetRequest(
                String.format(
                        BASE_STOPPOINT_URL.getBaseUrl(),
                        postcodeData.getData().getLatitude(),
                        postcodeData.getData().getLongitude()
                ),
                StopPointData.class
        );

        stopPoints = stopPointData.getStopPoints();;

        for (StopPointData.StopPoint s : stopPoints) {
            for (LineGroup l : s.getLineGroups()) {
                busData = RequestHandler.sendGetRequest(
                        String.format(
                                BASE_LIVE_DATA_ARRIVAL_URL.getBaseUrl(),
                                l.getNaptanId()
                        ),
                        BusData[].class
                );
            }
        }

        if (busData.length == 0) {
            log.error("No bus data found: Exiting");
            System.exit(0);
        }

        busData = sortBusData(busData);
        listBusData(busData, 5);
    }

    private static String getPostcodeFromUser() {
        String input = "";

        while (input.isEmpty()) {
            input = getValueFromUser("Enter Postcode:");
            input = PostcodeResult.validatePostcode(input) ? input : "";
        }

        return input;
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