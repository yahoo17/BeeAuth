package com.Beeyan;

public  class  ApiRequest {

    private String baseUrl;

    private String appId;

    private String token;

    private Long timestamp;


    public ApiRequest(String baseUrl, String appId, String token, long timestamp) {
        this.baseUrl = baseUrl;
        this.appId = appId;
        this.token = token;
        this.timestamp = timestamp;
    }

    public static ApiRequest BuildFromFullUrl(String url){
        String [] reqArray = url.split("&");
        String baseUrl = reqArray[0].split("=", 2)[1];
        String appId = reqArray[1].split("=",2)[1];
        String token = reqArray[2].split("=", 2)[1];
        long timestamp = Long.parseLong(reqArray[3].split("=", 2)[1]);
        return new ApiRequest(baseUrl, appId, token, timestamp);

    }


    public String getToken()    { return this.token; }

    public String getBaseUrl()  { return this.baseUrl;}

    public String getAppId()    { return this.appId; }

    public Long getTimestamp() { return this.timestamp; }


}
