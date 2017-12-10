package com.shane.baking.network;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.shane.baking.data.Recipe;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by Shane on 9/29/2017.
 */

public interface RecipeApi {
    @GET("topher/2017/May/59121517_baking/baking.json")
    Flowable<List<Recipe>> getRecipes();

    class Factory {
        public static RecipeApi create() {
            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://d17h27t6h515a5.cloudfront.net/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            return retrofit.create(RecipeApi.class);
        }
    }
}
