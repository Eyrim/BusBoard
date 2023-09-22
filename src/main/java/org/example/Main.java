package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.StopPointData.StopPoint;

import java.util.*;

import static org.example.BaseUrls.*;
import static org.example.StopPointData.StopPoint.LineGroup;

public class Main {
    private static final Logger log = LogManager.getLogger();

    public static void main(String[] args) {
        PostcodeResult postcodeData;
        StopPoint[] stopPoints;
        BusData[] busData = null;

        // 490000129R <- known working stoppoint id
        postcodeData = RequestHandler.sendGetRequest(
                String.format(
                        BASE_POSTCODE_URL.getBaseUrl(),
                        getPostcodeFromUser()
                ),
                PostcodeResult.class
        );;

        stopPoints = RequestHandler.sendGetRequest(
                String.format(
                        BASE_STOPPOINT_URL.getBaseUrl(),
                        postcodeData.getData().getLatitude(),
                        postcodeData.getData().getLongitude()
                ),
                StopPointData.class
        ).getStopPoints();

        for (StopPoint stopPoint : stopPoints) {
            for (LineGroup lineGroup : stopPoint.getLineGroups()) {
                busData = RequestHandler.sendGetRequest(
                        String.format(
                                BASE_LIVE_DATA_ARRIVAL_URL.getBaseUrl(),
                                lineGroup.getNaptanId() // NaptanID is the ID of the stop in the TFL DB
                        ),
                        BusData[].class
                );
            }
        }

        if (busData.length == 0) {
            log.error("No bus data found: Exiting");
            System.exit(-1);
        }

        busData = sortBusData(busData);
        listBusData(busData, 5);
    }

    /**
     * <pre>
     * Asks the user for a postcode
     *
     * If they enter an invalid postcode, the user will be prompted
     * until a valid postcode is entered
     * </pre>
     * @return The postcode they entered
     */
    private static String getPostcodeFromUser() {
        String input = "";

        while (input.isEmpty()) {
            input = getValueFromUser("Enter Postcode:");
            input = PostcodeResult.validatePostcode(input) ? input : "";
        }

        return input;
    }

    /**
     * <pre>
     * Lists all of the bus data
     *
     * If amount is greater than data.length, it will print all of the data
     * without throwing an exception
     *
     * This method calls <code>BusData.listArrival()</code>
     * </pre>
     * @param data
     * @param amount
     */
    private static void listBusData(BusData[] data, int amount) {
        amount = Math.min(amount, data.length);

        for (int i = 0; i < amount; i++) {
            data[i].listArrival();
        }
    }

    /**
     * <pre>
     * Lists all of the bus data
     *
     * This method calls <code>BusData.listArrival()</code>
     * </pre>
     * @param data The data to list
     */
    private static void listBusData(BusData[] data) {
        listBusData(data, data.length);
    }

    /**
     * Prompts the user for a String value and returns their response
     * @param prompt To print to the user
     * @return The user's response as a String using <code>Scanner.nextLine()</code>
     */
    private static String getValueFromUser(String prompt) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println(prompt);

            return sc.nextLine();
        }
    }

    /**
     * Sorts the bus data by how close they are to the desired stop in seconds
     * @param data The array to sort
     * @return A new array of <code>data</code> sorted
     */
    private static BusData[] sortBusData(BusData[] data) {
        return Arrays.stream(data)
                .sorted(Comparator.comparingInt(BusData::getTimeToStationSeconds))
                .toList().toArray(BusData[]::new);
    }
}