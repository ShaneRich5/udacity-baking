package com.shane.baking.ui;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.shane.baking.R;
import com.shane.baking.adapters.IngredientAdapter;
import com.shane.baking.adapters.StepAdapter;
import com.shane.baking.data.RecipeContract.RecipeEntry;
import com.shane.baking.models.Ingredient;
import com.shane.baking.models.Recipe;
import com.shane.baking.models.Step;
import com.shane.baking.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeDetailFragment extends Fragment implements StepAdapter.OnClickHandler{

    public static final String TAG = RecipeDetailFragment.class.getName();

    @BindView(R.id.ingredient_recycler) RecyclerView ingredientRecyclerView;
    @BindView(R.id.step_recycler) RecyclerView stepRecyclerView;

    private Recipe recipe;
    private InteractionListener listener;

    private MenuItem bookmarkItem;

    interface InteractionListener {
        void onStepSelected(Step step);
    }

    public RecipeDetailFragment() {}

    public static RecipeDetailFragment newInstance(Recipe recipe) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(Constants.EXTRA_RECIPE, recipe);
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recipe = getArguments().getParcelable(Constants.EXTRA_RECIPE);

        if (recipe == null) return;

        List<Ingredient> ingredients = recipe.getIngredients();
        List<Step> steps = recipe.getSteps();

        IngredientAdapter ingredientAdapter = new IngredientAdapter(getContext(), ingredients);
        StepAdapter stepAdapter = new StepAdapter(getContext(), this, steps);

        ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        stepRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ingredientRecyclerView.setAdapter(ingredientAdapter);
        stepRecyclerView.setAdapter(stepAdapter);

        ViewCompat.setNestedScrollingEnabled(ingredientRecyclerView, false);
        ViewCompat.setNestedScrollingEnabled(stepRecyclerView, false);
    }

    @Override
    public void onClick(Step step) {
        if (recipe == null) return;
        listener.onStepSelected(step);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecipeDetailActivity)
            listener = (InteractionListener) context;
        else
            throw new IllegalStateException("Activity must implement InteractionListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_recipe_detail, menu);
        bookmarkItem = menu.findItem(R.id.action_bookmark);
        setInitialBookmarkIcon();
    }

    private void setInitialBookmarkIcon() {
        isRecipeBookmarked()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(updateBookmarkObservable);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_bookmark:
                toggleBookmark();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void toggleBookmark() {
        isRecipeBookmarked().flatMap(isBookmarked -> {
            ContentResolver contentResolver = getContext().getContentResolver();

            return Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
                if (isBookmarked) {
                    contentResolver.delete(RecipeEntry.buildRecipeUri(recipe.getId()), null, null);
                } else {
                    ContentValues recipeContentValues = new Recipe.Builder().build(recipe);
                    contentResolver.insert(RecipeEntry.CONTENT_URI, recipeContentValues);
                }

                emitter.onNext( ! isBookmarked);
            });
        })
        .concatWith(new Observable<Boolean>() {
            @Override
            protected void subscribeActual(Observer<? super Boolean> observer) {

            }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(updateBookmarkObservable);
    }

    private Observable<Boolean> isRecipeBookmarked() {
        return Observable.create(emitter -> {
            Cursor cursor = getContext().getContentResolver()
                    .query(RecipeEntry.buildRecipeUri(recipe.getId()), null, null, null, null);
            emitter.onNext(cursor != null && cursor.getCount() > 0);
            if (cursor != null) {
                DatabaseUtils.dumpCursor(cursor);
                cursor.close();
            }
        });
    }

    private void setBookmarkIcon(boolean isBookmarked) {
        int bookmarkIcon = isBookmarked
                ? R.drawable.ic_bookmark_white_24dp
                : R.drawable.ic_bookmark_border_white_24dp;

        bookmarkItem.setIcon(ResourcesCompat.getDrawable(getResources(), bookmarkIcon, null));
    }

    Observer<Boolean> updateBookmarkObservable = new Observer<Boolean>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull Boolean isBookmarked) {
            setBookmarkIcon(isBookmarked);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            Timber.e(e);
        }

        @Override
        public void onComplete() {

        }
    };
}
