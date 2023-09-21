package org.example;

import com.google.gson.annotations.SerializedName;

public class BusData {
    @SerializedName("vehicleId")
    private String vehicleId;
    @SerializedName("naptanId")
    private String naptanId;
    @SerializedName("stationName")
    private String stationName;
    @SerializedName("direction")
    private String direction;
    @SerializedName("destinationName")
    private String destinationName;
    @SerializedName("timeToStation")
    private int timeToStationSeconds;
    @SerializedName("timestamp")
    private String timeStamp;
    @SerializedName("expectedArrival")
    private String expectedArrival;

    public void listArrival() {
        System.out.printf(
                "Bus ID: %s will arrive at %s in %d minutes%n",
                this.vehicleId,
                this.stationName,
                this.getTimeToStationMinutes()
        );
    }

    public int getTimeToStationSeconds() {
        return this.timeToStationSeconds;
    }
    public int getTimeToStationMinutes() {
        return this.timeToStationSeconds / 60;
    }
}
