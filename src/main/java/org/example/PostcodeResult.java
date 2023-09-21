package org.example;

import com.google.gson.annotations.SerializedName;

public class PostcodeResult {
    @SerializedName("result")
    private PostcodeData data;

    public PostcodeData getData() {
        return data;
    }

    public class PostcodeData {
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

}
