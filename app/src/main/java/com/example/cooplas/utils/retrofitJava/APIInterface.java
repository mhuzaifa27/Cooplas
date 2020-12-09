package com.example.cooplas.utils.retrofitJava;


import com.example.cooplas.models.Food.Callbacks.CallbackGetCart;
import com.example.cooplas.models.Food.Callbacks.CallbackGetFoodCategories;
import com.example.cooplas.models.Food.Callbacks.CallbackGetFoodForYou;
import com.example.cooplas.models.Food.Callbacks.CallbackGetFood;
import com.example.cooplas.models.Food.Callbacks.CallbackGetPopularRestaurants;
import com.example.cooplas.models.Food.Callbacks.CallbackGetNearbyRestaurants;
import com.example.cooplas.models.Food.Callbacks.CallbackGetRestaurantDetail;
import com.example.cooplas.models.GeneralRes;
import com.example.cooplas.models.home.commentModels.CommentMainModel;
import com.example.cooplas.models.home.createPost.CreatePostModel;
import com.example.cooplas.models.home.homeFragmentModel.HomeModel;
import com.example.cooplas.models.home.homeLikesModels.LikeMainModel;
import com.example.cooplas.models.home.searchUser.SearchUserModel;
import com.example.cooplas.models.home.singlePost.SinglePostMainModel;
import com.example.cooplas.models.profile.Followers.FollowersModel;
import com.example.cooplas.models.profile.Following.FollowingModel;
import com.example.cooplas.models.profile.ProfileModel;
import com.example.cooplas.models.videos.VideosFeedModel;
import com.google.gson.JsonObject;
import com.example.cooplas.models.GoogleDistanceApi.CallbackGetDistanceTime;
import com.example.cooplas.models.Travel.Customer.CallbackAcceptRide.CallbackAcceptRide;
import com.example.cooplas.models.Travel.Customer.CallbackDriverReached.CallbackDriverReached;
import com.example.cooplas.models.Travel.Customer.CallbackSearchForVehicle.CallbackSearchForVehicle;
import com.example.cooplas.models.Travel.Customer.CallbackUpdateVehicleType.CallbackUpdateVehicleType;
import com.example.cooplas.models.Travel.Customer.Callbacks.CallbackCreateRide;
import com.example.cooplas.models.Travel.Customer.Callbacks.CallbackGetRecentPlaces;


import okhttp3.RequestBody;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    @FormUrlEncoded
    @POST("api/change/password")
    Call<GeneralRes> changeForgetPassword(@Header("Authorization") String token, @Field("password") String offset);

    @GET("api/user/wall")
    Call<ProfileModel> getUserWall(@Header("Authorization") String token, @Query("posts_offset") String offset);

    @GET("api/user/wall")
    Call<ProfileModel> getOtherUserWall(@Header("Authorization") String token, @Query("user_id") String user_id, @Query("posts_offset") String offset);

    @GET("api/home")
    Call<HomeModel> getHomeFeed(@Header("Authorization") String token, @Query("posts_offset") String offset);

    @GET("api/post/likes")
    Call<LikeMainModel> getPostLikes(@Header("Authorization") String token, @Query("post_id") String offset);


    @POST("api/create/post")
    Call<CreatePostModel> createHomePost(@Header("Authorization") String token, @Body() RequestBody file);


    @GET("api/like/post/{id}")
    Call<JsonObject> postLikeHome(@Header("Authorization") String token, @Path("id") String id);


    @GET("api/dislike/post/{id}")
    Call<JsonObject> poskUnLikeHome(@Header("Authorization") String token, @Path("id") String id);


    @GET("api/follow/user/{id}")
    Call<JsonObject> followUserHome(@Header("Authorization") String token, @Path("id") String id);

    @GET("api/unfollow/user/{id}")
    Call<JsonObject> UnFollowUserHome(@Header("Authorization") String token, @Path("id") String id);

    @DELETE("api/delete/post/{id}")
    Call<JsonObject> deletePost(@Header("Authorization") String token, @Path("id") String id);


    @GET("api/single-post")
    Call<SinglePostMainModel> getSinglePost(@Header("Authorization") String token, @Query("id") String id);

    @FormUrlEncoded
    @POST("api/add/comment")
    Call<CommentMainModel> postComment(@Header("Authorization") String token, @Field("post_id") String id, @Field("body") String body);


    @DELETE("api/delete/comment/{id}")
    Call<JsonObject> deleteComment(@Header("Authorization") String token, @Path("id") String id);


    @FormUrlEncoded
    @POST("api/update/comment/{id}")
    Call<JsonObject> updateComment(@Header("Authorization") String token, @Path("id") String id, @Field("body") String body);


    @POST("api/update/profile")
    Call<JsonObject> updateProfile(@Header("Authorization") String token, @Body() RequestBody file);

    @GET("api/resend/email-verification-code")
    Call<JsonObject> emailVerifyMain(@Header("Authorization") String token);

    @GET("api/verify/email")
    Call<JsonObject> verifyEmailInApp(@Header("Authorization") String token, @Query("code") String code);


    @POST("api/update/post/{id}")
    Call<CreatePostModel> updatePost(@Header("Authorization") String token, @Path("id") String id, @Body() RequestBody file);


    @POST("api/add/story")
    Call<JsonObject> createStoryPost(@Header("Authorization") String token, @Body() RequestBody file);


    @GET("api/videos")
    Call<VideosFeedModel> getVideosFeed(@Header("Authorization") String token, @Query("type") String Type, @Query("offset") String offset);

    @GET("api/unfollow/user/{id}")
    Call<JsonObject> followUser(@Header("Authorization") String token, @Path("id") String id);

    @GET("api/follow/user/{id}")
    Call<JsonObject> unFollowUser(@Header("Authorization") String token, @Path("id") String id);


    @POST("api/report/post/{id}")
    Call<JsonObject> repostPost(@Header("Authorization") String token, @Path("id") String id);

    @GET("api/user/followers")
    Call<FollowersModel> userFollowers(@Header("Authorization") String token, @Query("offset") String offset);

    @GET("api/user/following")
    Call<FollowingModel> userFollowing(@Header("Authorization") String token, @Query("offset") String offset);


    @GET("api/search/users")
    Call<SearchUserModel> searchUsers(@Header("Authorization") String token, @Query("q") String searchKeyword, @Query("offset") String offset);


    /**************************APIS IMPLEMENTED BY M.HUZAIFA 11/04/2020********************************/

    /************FOOD API's**************/
    @GET("api/food")
    Call<CallbackGetFood> getFood(@Header("Authorization") String token);

    @GET("api/popular/restaurants")
    Call<CallbackGetPopularRestaurants> getPopularRestaurants(@Header("Authorization") String token);

    @GET("api/food/categories")
    Call<CallbackGetFoodCategories> getFoodCategories(@Header("Authorization") String token);

    @GET("api/for/you")
    Call<CallbackGetFoodForYou> getFoodForYou(@Header("Authorization") String token);

    @GET("api/nearby/restaurants")
    Call<CallbackGetNearbyRestaurants> getRestaurantsNearYou(@Header("Authorization") String token);

    @GET("api/restaurant")
    Call<CallbackGetRestaurantDetail> getRestaurantDetail(@Header("Authorization") String token,
                                                          @Query("id") String id);

    @FormUrlEncoded
    @POST("api/add/cart-item")
    Call<ResponseBody> addToCart(@Header("Authorization") String token,
                                 @FieldMap Map<String, String> params);

    @GET("api/cart")
    Call<CallbackGetCart> getCart(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("api/update/cart-item/{id}")
    Call<ResponseBody> updateCartItem(@Header("Authorization") String token,
                                      @FieldMap Map<String, String> param,
                                      @Path("id") String id);

    @FormUrlEncoded
    @POST("api/add/to-favourites")
    Call<ResponseBody> makeFoodFavourite(@Header("Authorization") String token,
                                         @Field("item_id") String item_id);

    @FormUrlEncoded
    @POST("api/remove/from-favourites")
    Call<ResponseBody> makeFoodUnFavourite(@Header("Authorization") String token,
                                           @Field("item_id") String item_id);

    @DELETE("api/remove/cart-item/{id}")
    Call<ResponseBody> removeCartItem(@Header("Authorization") String token,
                                      @Path("id") String id);
    /************FOOD API's**************/

    /************TRAVEL API's**************/

    @GET("api/recent/locations")
    Call<CallbackGetRecentPlaces> getRecentPlaces(@Header("Authorization") String token);

    @GET("api/directions/json")
    Call<CallbackGetDistanceTime> getDistanceFromGoogleApi(
            @Query("origin") String origin,
            @Query("destination") String destination,
            @Query("mode") String mode,
            @Query("key") String key
    );

    @FormUrlEncoded
    @POST("api/set/ride-locations")
    Call<CallbackCreateRide> createRide(@Header("Authorization") String token,
                                        @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("api/update/ride-vehicle-type")
    Call<CallbackUpdateVehicleType> updateRideVehicleType(@Header("Authorization") String token,
                                                          @FieldMap Map<String, String> params);

    @GET("api/search/vehicle")
    Call<CallbackSearchForVehicle> searchForVehicle(@Header("Authorization") String token,
                                                    @Query("ride_id") String ride_id);

    @FormUrlEncoded
    @POST("api/accept/ride")
    Call<CallbackAcceptRide> acceptRide(@Header("Authorization") String token,
                                        @Field("ride_id") String ride_id);

    @FormUrlEncoded
    @POST("api/driver/arrived")
    Call<CallbackDriverReached> driverReached(@Header("Authorization") String token,
                                              @Field("ride_id") String ride_id);

    @FormUrlEncoded
    @POST("api/rate/driver")
    Call<ResponseBody> rateDriver(@Header("Authorization") String token,
                                  @FieldMap Map<String, String> parmas);

    /************TRAVEL API's**************/
    /**********************************************************/
}
