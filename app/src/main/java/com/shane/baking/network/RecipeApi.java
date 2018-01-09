package com.shane.baking.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.shane.baking.data.Recipe;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.reactivex.Flowable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import timber.log.Timber;

/**
 * Created by Shane on 9/29/2017.
 */

public interface RecipeApi {
    @GET("topher/2017/May/59121517_baking/baking.json")
    Flowable<List<Recipe>> getRecipes();

    class Factory {
        public static RecipeApi create() {
            final OkHttpClient.Builder builder = new OkHttpClient.Builder();

            builder.addInterceptor(new LoggingInterceptor());

            final OkHttpClient client = builder.build();

            final Gson gson = new GsonBuilder().create();

            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://d17h27t6h515a5.cloudfront.net/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();

            return retrofit.create(RecipeApi.class);
        }
    }

    class LoggingInterceptor implements Interceptor {
        @Override public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            Timber.d(String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            Timber.d(String.format(Locale.getDefault(), "Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            return response;
        }
    }
}
