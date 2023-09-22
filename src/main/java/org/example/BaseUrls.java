package org.example;

public enum BaseUrls {
    BASE_POSTCODE_VALIDATION_URL("api.postcodes.io/postcodes/%s/validate"),
    BASE_LIVE_DATA_ARRIVAL_URL("https://api.tfl.gov.uk/StopPoint/%s/Arrivals"),
    BASE_STOPPOINT_URL("https://api.tfl.gov.uk/StopPoint/?lat=%s&lon=%s&stopTypes=NaptanOnstreetBusCoachStopPair&radius=50"),
    BASE_POSTCODE_URL("https://api.postcodes.io/postcodes/%s");


    private final String baseUrl;

    private BaseUrls(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }
}