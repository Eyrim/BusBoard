package org.example;

import com.google.gson.annotations.SerializedName;

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
}
