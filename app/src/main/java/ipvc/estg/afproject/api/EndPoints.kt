package ipvc.estg.afproject.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

interface EndPoints {

    @GET("/users")
    fun getUsers(): Call<List<Users>>

    @GET("/users/{id}")
    fun getUserById(@Path("id") id: Int): Call<Users>

//    @FormUrlEncoded
//    @POST("/posts")
//    fun postTest(@Field("title") first: String?): Call<OutputPost>

}