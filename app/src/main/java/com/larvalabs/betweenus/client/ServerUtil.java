package com.larvalabs.betweenus.client;

import retrofit.RestAdapter;

/**
 *
 */
public class ServerUtil {

    private static final String SERVER_DEV = "http://192.168.8.167:9000";
    private static final String SERVER_PROD = "https://betweenusserver.herokuapp.com/";

    private static BetweenUsService service;

    public static BetweenUsService getService() {
        if (service == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(SERVER_PROD)
                    .build();

            service = restAdapter.create(BetweenUsService.class);
        }
        return service;
    }

}