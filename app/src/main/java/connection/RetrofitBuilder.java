package connection;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitBuilder {

    private static Retrofit retrofit = null;

    public RetrofitBuilder() {

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl("https://easyntaste.000webhostapp.com/")
                    .addConverterFactory(JacksonConverterFactory.create())
                    .client(okHttpClient)
                    .build();

        }
    }

    public RecipeService recipeService(){
        return this.retrofit.create(RecipeService.class);
    }
}
