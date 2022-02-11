package com.nextradioapp.androidSDK.data.schema;

public class Tables {

    public static class activityEvents {
        public static final String UFIDIdentifier = "UFIDIdentifier";
        public static final String _id = "_id";
        public static final String album = "album";
        public static final String artist = "artist";
        public static final String description = "description";
        public static final String imageURL = "imageURL";
        public static final String imageURLHiRes = "imageURLHiRes";
        public static final String itemType = "itemType";
        public static final String primaryAction = "primaryAction";
        public static final String stationID = "stationID";
        public static final String tableName = "activityEvents";
        public static final String teID = "teID";
        public static final String timestamp = "timestamp";
        public static final String title = "title";
        public static final String trackingID = "trackingID";
    }

    public static class impressionReporting {
        public static final String _id = "_id";
        public static final String action = "action";
        public static final String cardTrackingID = "cardTrackingID";
        public static final String createTime = "createTime";
        public static final String source = "source";
        public static final String stationID = "stationID";
        public static final String tableName = "impressionReporting";
        public static final String teID = "teID";
        public static final String trackingID = "trackingID";
        @Deprecated
        public static final String ufid = "ufid";
    }

    public static class impressionVisualReporting {
        public static final String _id = "_id";
        public static final String batchID = "batchID";
        public static final String cardTrackingID = "cardTrackingID";
        public static final String createTime = "createTime";
        public static final String source = "source";
        public static final String stationID = "stationID";
        public static final String tableName = "impressionVisualReporting";
        public static final String teID = "teID";
        public static final String trackingID = "trackingID";
    }

    public static class listeningActivityAds {
        public static final String IsFavorite = "IsFavorite";
        public static final String _id = "_id";
        public static final String adType = "adType";
        public static final String eventID = "EventID";
        public static final String field0 = "field0";
        public static final String field1 = "field1";
        public static final String field2 = "field2";
        public static final String field3 = "field3";
        public static final String field4 = "field4";
        public static final String field5 = "field5";
        public static final String tableName = "listeningActivityAds";
    }

    public static class listeningHistory {
        public static final String _id = "_id";
        public static final String eventID = "eventID";
        public static final String isFavorite = "isFavorite";
        public static final String isFromNowPlaying = "isFromNowPlaying";
        public static final String lastheard = "lastheard";
        public static final String savedDate = "savedDate";
        public static final String tableName = "listeningHistory";
    }

    public static class locationTracking {
        public static final String _id = "_id";
        public static final String action = "action";
        public static final String createTime = "createTime";
        public static final String latitude = "latitude";
        public static final String longitude = "longitude";
        public static final String source = "source";
        public static final String tableName = "LocationTracking";
    }

    public static class stationReporting {
        public static final String _id = "_id";
        public static final String endTime = "endTime";
        public static final String isClosed = "isClosed";
        public static final String lastUFID = "lastUFID";
        public static final String publicStationID = "publicStationID";
        public static final String startTime = "startTime";
        public static final String tableName = "stationReporting";
    }

    public static class stations {
        public static final String IsFavorite = "IsFavorite";
        public static final String IsValid = "IsValid";
        public static final String _id = "_id";
        public static final String artistList = "artistList";
        public static final String callLetters = "callLetters";
        public static final String countryCode = "countryCode";
        public static final String endpoint = "endpoint";
        public static final String frequency = "frequency";
        public static final String frequencySubChannel = "frequencySubChannel";
        public static final String fromNoData = "fromNoData";
        public static final String genre = "genre";
        public static final String headline = "headline";
        public static final String headlineText = "headlineText";
        public static final String imageURL = "imageURL";
        public static final String imageURLHiRes = "imageURLHiRes";
        public static final String lastListened = "lastListened";
        public static final String market = "market";
        public static final String piCode = "piCode";
        public static final String publicStationID = "publicStationID";
        public static final String slogan = "slogan";
        public static final String tableName = "stations";
        public static final String trackingID = "trackingID";
    }
}
