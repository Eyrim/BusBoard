package org.example;

public enum BaseUrls {
    /**
     * The base URL used to validate postcodes
     */
    BASE_POSTCODE_VALIDATION_URL("api.postcodes.io/postcodes/%s/validate"),
    /**
     * The base URL used to find live data with a given TFL StopPoint
     */
    BASE_LIVE_DATA_ARRIVAL_URL("https://api.tfl.gov.uk/StopPoint/%s/Arrivals"),
    /**
     * The base URL used to find all StopPoints in a certain area, using a radius of 50
     */
    BASE_STOPPOINT_URL("https://api.tfl.gov.uk/StopPoint/?lat=%s&lon=%s&stopTypes=NaptanOnstreetBusCoachStopPair&radius=50"),
    /**
     * The base URL used to get information on a specific postcode, such as latitude and longitude
     */
    BASE_POSTCODE_URL("https://api.postcodes.io/postcodes/%s");


    private final String baseUrl;

    private BaseUrls(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }
}