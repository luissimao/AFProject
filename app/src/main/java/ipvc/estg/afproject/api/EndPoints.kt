package ipvc.estg.afproject.api;

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

    @GET("myslim/api/occurrences")
    fun getAllOccurrences(): Call<List<Occurrences>>

    @GET("myslim/api/occurrences/{id}")
    fun getOccurrenceById(@Path("id") id: Int): Call<Occurrences>

    @GET("myslim/api/occurrences/acidentes")
    fun getAcidentes(): Call<List<Occurrences>>

    @GET("myslim/api/occurrences/buracos")
    fun getBuracos(): Call<List<Occurrences>>

    @GET("myslim/api/occurrences/outros")
    fun getOutros(): Call<List<Occurrences>>

    @FormUrlEncoded
    @POST("myslim/api/posts")
    fun postTest(@Field("email") first: String?, @Field("password") second: String?): Call<List<Users>>

    @FormUrlEncoded
    @POST("myslim/api/occurrences/insert")
    fun insert(@Field("titulo") first: String?, @Field("descricao") second: String?, @Field("imagem") third: String?, @Field("latitude") fourth: Double?, @Field("longitude") fifth: Double?, @Field("user_id") sixth: Int?): Call<List<Occurrences>>

    @FormUrlEncoded
    @POST("myslim/api/occurrences/update/{id}")
    fun update(@Field("id") zero: Int?, @Field("titulo") first: String?, @Field("descricao") second: String?, @Field("imagem") third: String?, @Field("latitude") fourth: Double?, @Field("longitude") fifth: Double?, @Field("user_id") sixth: Int?): Call<List<Occurrences>>

    @FormUrlEncoded
    @POST("myslim/api/occurrences/delete/{id}")
    fun delete(@Path("id") id: Int): Call<Occurrences>

}