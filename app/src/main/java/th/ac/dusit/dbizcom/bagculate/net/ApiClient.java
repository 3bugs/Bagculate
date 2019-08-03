package th.ac.dusit.dbizcom.bagculate.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import th.ac.dusit.dbizcom.bagculate.BuildConfig;

public class ApiClient {

    private static final String BASE_URL = "http://5911011802057.msci.dusit.ac.th/bagculate/api/api.php/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(new OkHttpProfilerInterceptor());
        }
        OkHttpClient okHttpClient = builder.build();

        /*final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();*/

        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    //.addConverterFactory(ScalarsConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }
}