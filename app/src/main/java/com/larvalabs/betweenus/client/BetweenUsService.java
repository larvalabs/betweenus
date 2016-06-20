package com.larvalabs.betweenus.client;

import retrofit.Callback;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 *
 */
public interface BetweenUsService {

    @POST("/application/registerUser")
    void registerUser(@Query("username") String username, @Query("latitude") double latitude, @Query("longitude") double longitude, Callback<ServerResponse> callback);

    @POST("/application/setUsername")
    void setUsername(@Query("userId") Long userId, @Query("username") String username, Callback<ServerResponse> callback);

    @POST("/application/connect")
    void connect(@Query("userId1") Long userId1, @Query("userId2") Long userId2, Callback<ServerResponse> callback);

    @POST("/application/updateLocation")
    void updateLocation(@Query("userId") Long userId, @Query("latitude") double latitude, @Query("longitude") double longitude, Callback<ServerResponse> callback);

    @POST("/application/updateLocation")
    ServerResponse updateLocationSync(@Query("userId") Long userId, @Query("latitude") double latitude, @Query("longitude") double longitude);

    @POST("/application/getinfo")
    ServerResponse getInfoSync(@Query("userId") Long userId);

    @POST("/application/endConversation")
    void endConversation(@Query("userId") Long userId, Callback<ServerResponse> callback);

}
