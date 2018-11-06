package connection;

import java.util.List;

import basic.Receita;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RecipeService {

    @FormUrlEncoded
    @POST("api.php")
    Call<List<Receita>> searchRecipe(@Field("receita") String rec, @Field("id") int id);

    @FormUrlEncoded
    @POST("api.php")
    Call<Receita> getRecipe(@Field("receita") String r, @Field("id") int i);
}