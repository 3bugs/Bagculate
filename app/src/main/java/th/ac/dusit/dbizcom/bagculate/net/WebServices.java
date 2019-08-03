package th.ac.dusit.dbizcom.bagculate.net;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import th.ac.dusit.dbizcom.bagculate.model.Bag;
import th.ac.dusit.dbizcom.bagculate.model.Object;

public interface WebServices {

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> login(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("register")
    Call<RegisterResponse> register(
            @Field("username") String username,
            @Field("password") String password,
            @Field("name") String name
    );

    @GET("get_bag")
    Call<GetBagResponse> getBag(
    );

    @GET("get_object")
    Call<GetObjectResponse> getObject(
    );

    @FormUrlEncoded
    @POST("get_history")
    Call<GetHistoryResponse> getHistory(
            @Field("user_id") int userId
    );

    @FormUrlEncoded
    @POST("add_history")
    Call<AddHistoryResponse> addHistory(
            @Field("user_id") int userId,
            @Field("bag_id") int bagId,
            @Field("object_list") String objectListText
    );
}