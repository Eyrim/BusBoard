package org.example;

import com.google.gson.annotations.SerializedName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.example.BaseUrls.BASE_POSTCODE_VALIDATION_URL;

public class PostcodeResult {
    private static final Logger log = LogManager.getLogger();

    @SerializedName("result")
    private PostcodeData data;

    public PostcodeData getData() {
        return data;
    }

    /**
     * Validates a postcode using <code>postcodes.io/[postcode]/validate</code>
     * @param postCode The postcode to validate
     * @return True if valid postcode, false if not
     */
    public static boolean validatePostcode(String postCode) {
        log.info("Validating postcode:");

        PostcodeValidationResult result = RequestHandler.sendGetRequest(
                String.format(
                        BASE_POSTCODE_VALIDATION_URL.getBaseUrl(),
                        postCode
                ),
                PostcodeValidationResult.class
        );

        return result.isValid();
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

    private static class PostcodeValidationResult {
        @SerializedName("result")
        private boolean isValid;

        public boolean isValid() {
            return isValid;
        }
    }
}
