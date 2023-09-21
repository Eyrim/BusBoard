package org.example;

import com.google.gson.annotations.SerializedName;

public class StopPointData {
    @SerializedName("stopPoints")
    private StopPoint[] stopPoints;

    public StopPoint[] getStopPoints() {
        return stopPoints;
    }

    public class StopPoint {
        @SerializedName("lines")
        private Line[] lines;

        public class Line {
            @SerializedName("id")
            private String id;
            @SerializedName("name")
            private String name;
            @SerializedName("uri")
            private String uri;

            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public String getUri() {
                return uri;
            }
        }

        public Line[] getLines() {
            return lines;
        }
    }
}
