package com.gruppo42.app.api.models;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserApi {
    String ENDPOINT = "https://gruppo42.azurewebsites.net/api/";

    @GET("user/me")
    Call<UserDTO> getUserDetails(@Header("AUTHORIZATION") String bearerToken);
    @HTTP(method = "DELETE", path = "auth/user/me/delete", hasBody = true)
    Call<UserApiResponse> deleteUser(@Header("AUTHORIZATION") String bearerToken,
                                     @Body LoginRequest loginRequest);
    @POST("user/me")
    Call<UserApiResponse> changeProfileDetails(@Header("AUTHORIZATION") String bearerToken,
                                               @Body ProfileChangeRequest request);
    @POST("auth/newPassword")
    Call<UserApiResponse> changePassword(@Header("AUTHORIZATION") String bearerToken,
                                         @Body PasswordChangeRequest request);
    @GET("user/checkUsernameAvailability")
    Call<AvailableResponse> checkUsernameAvailable(@Query("username") String username);
    @GET("auth/resetPassword")
    Call<UserApiResponse> resetPassword(@Query("email") String email);
    @GET("user/checkEmailAvailability")
    Call<AvailableResponse> checkEmailAvailable(@Query("email") String email);
    @POST("auth/signup")
    Call<UserApiResponse> signupUser(@Body SignUpRequest signUpRequest);
    @POST("auth/signin")
    Call<LoginResponse> signinUser(@Body LoginRequest loginRequest);
    @POST("user/add_watchlist")
    Call<UserApiResponse> addToWatchlist(@Header("AUTHORIZATION") String bearerToken,
                                         @Query("movie_id") String movie_id);
    @POST("user/add_favorite")
    Call<UserApiResponse> addToFavorites(@Header("AUTHORIZATION") String bearerToken,
                                         @Query("movie_id") String movie_id);
    @DELETE("user/remove_watchlist")
    Call<UserApiResponse> removeFromWatchlist(@Header("AUTHORIZATION") String bearerToken,
                                              @Query("movie_id") String movie_id);
    @DELETE("user/remove_favorite")
    Call<UserApiResponse> removeFromFavorites(@Header("AUTHORIZATION") String bearerToken,
                                              @Query("movie_id") String movie_id);
    class Instance {

        static UserApi instance;

        public static UserApi get() {
            if (instance == null)
                instance = newUserApi();
            return instance;
        }

        public static UserApi newUserApi() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(UserApi.ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(UserApi.class);
        }
    }

}
