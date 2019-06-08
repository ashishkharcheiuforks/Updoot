package com.ducktapedapps.updoot.repository;

import android.app.Application;
import android.util.Log;

import com.ducktapedapps.updoot.api.endPoint;
import com.ducktapedapps.updoot.model.ListingData;
import com.ducktapedapps.updoot.model.thing;
import com.ducktapedapps.updoot.utils.authManager;
import com.ducktapedapps.updoot.utils.retrofitClient;
import com.ducktapedapps.updoot.utils.tokenManager;

import io.reactivex.Single;

public class submissionsRepo {
    private static final String TAG = "submissionsRepo";
    private Application context;
    private String after = null;
    private endPoint api;

    public submissionsRepo(Application application) {
        context = application;
    }

    private Single<endPoint> refreshAPI() {
        Log.i(TAG, "getting api with valid access token");
        return authManager.authenticate(context)
                .flatMap(token -> Single.just(retrofitClient.createEndPointService(token)))
                .doOnSuccess(endPoint -> api = endPoint);
    }

    private Single<thing> fetchFrontPage(String sort, String nextPage) {
        if (tokenManager.checkTokenValidity(context) && api != null) {
            return api
                    .getFrontPage(sort, nextPage)
                    .doOnError(throwable -> Log.i(TAG, "fetchFrontPage: " + throwable.getMessage()));
        } else {
            return refreshAPI()
                    .flatMap(endPoint -> endPoint.getFrontPage(sort, nextPage))
                    .doOnSuccess(thing -> {
                        if (thing.getData() instanceof ListingData) {
                            after = ((ListingData) thing.getData()).getAfter();
                        } else {
                            Log.e(TAG, "fetchFrontPage: unrecognized json response format");
                        }
                    });
        }
    }

    public Single<thing> loadNextPage(String sort) {
        return fetchFrontPage(sort,after);
    }
}
