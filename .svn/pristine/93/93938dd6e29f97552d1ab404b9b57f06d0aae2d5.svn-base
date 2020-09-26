package com.dnc.loc.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.CompletableTransformer;
import io.reactivex.FlowableTransformer;
import io.reactivex.MaybeTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by tcg on 2016/4/5.
 */
public class RxUtils {

    public static ExecutorService executor = Executors.newFixedThreadPool(3);

    public static <T> SingleTransformer<T, T> singleAsync(LifecycleTransformer transformer){
        return obs -> obs.subscribeOn(Schedulers.from(executor))
                .observeOn(AndroidSchedulers.mainThread())
                .compose(transformer);
    }


    public static <T> FlowableTransformer<T, T> flowableAsync(LifecycleTransformer transformer){
        return obs -> obs.subscribeOn(Schedulers.from(executor))
                .observeOn(AndroidSchedulers.mainThread());
    }
    public static <T> ObservableTransformer<T, T> observableAsync(LifecycleTransformer transformer){
        return obs -> obs.subscribeOn(Schedulers.from(executor))
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> SingleTransformer<T, T> singleAsync(){
        return obs -> obs.subscribeOn(Schedulers.from(executor))
                .observeOn(AndroidSchedulers.mainThread());
    }
    public static <T> FlowableTransformer<T, T> flowableAsync(){
        return obs -> obs.subscribeOn(Schedulers.from(executor))
                .observeOn(AndroidSchedulers.mainThread());
    }
    public static <T> ObservableTransformer<T, T> observableAsync(){
        return obs -> obs.subscribeOn(Schedulers.from(executor))
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> MaybeTransformer<T, T> maybeAsync(){
        return obs -> obs.subscribeOn(Schedulers.from(executor))
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static CompletableTransformer completableAsync(){
        return obs -> obs.subscribeOn(Schedulers.from(executor))
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<Bitmap> loadBitmap(Picasso picasso, String imageUrl) {
        return Single.create(subscriber -> {
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    subscriber.onSuccess(bitmap);
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    subscriber.onError(e);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            };
            subscriber.setCancellable(() -> picasso.cancelRequest(target));
            subscriber.setDisposable(new Disposable() {
                private boolean unSubscribed;
                @Override
                public void dispose() {
                    picasso.cancelRequest(target);
                    unSubscribed = true;
                }

                @Override
                public boolean isDisposed() {
                    return unSubscribed;
                }
            });

            try {
                picasso.load(imageUrl).into(target);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        });
    }
}