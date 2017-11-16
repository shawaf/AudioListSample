package com.shawaf.audiolistsample.download;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

import com.shawaf.audiolistsample.app.MainApplication;
import com.shawaf.audiolistsample.db.Audio;
import com.shawaf.audiolistsample.utils.StringUtils;
import com.shawaf.audiolistsample.view.AudioListActivity;

import java.io.File;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Response;

/**
 * Created by mohamedelshawaf on 11/9/17.
 */

public class DownloadManager {

    private AudioListActivity audioListActivity;
    private NetworkApiService networkApiService;
    private String TAG = "DownloadManager";
    private String fileName;
    private Audio audioModel;


    public DownloadManager(Activity activity) {
        this.audioListActivity = (AudioListActivity) activity;
        networkApiService = ((MainApplication) activity.getApplication()).getNetworkService();
    }


    public void downloadFile(Audio audioModel) {
        this.audioModel=audioModel;
        this.fileName = "AudioFile"+audioModel.getId();
        networkApiService.getAPI().download(audioModel.getAudioUrl())
                .flatMap(processResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handleResult());
    }


    private Function<Response<ResponseBody>, Observable<File>> processResponse() {
        return new Function<Response<ResponseBody>, Observable<File>>() {
            @Override
            public Observable<File> apply(Response<ResponseBody> responseBodyResponse) throws Exception {
                return saveToDiskRx(responseBodyResponse);
            }
        };
    }


    private Observable<File> saveToDiskRx(final Response<ResponseBody> response) {
        return Observable.create(new ObservableOnSubscribe<File>() {
            @Override
            public void subscribe(ObservableEmitter<File> subscriber) throws Exception {
                try {
                    String dirPath = Environment.getExternalStorageDirectory() + "/MOI/";

                    new File(dirPath).mkdir();
                    File destinationFile = new File(dirPath + fileName + ".mp3");

                    BufferedSink bufferedSink = Okio.buffer(Okio.sink(destinationFile));
                    bufferedSink.writeAll(response.body().source());
                    bufferedSink.close();

                    subscriber.onNext(destinationFile);
                    subscriber.onComplete();
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }


    private Observer<File> handleResult() {
        return new Observer<File>() {
            @Override
            public void onComplete() {
                Log.d(TAG, "onCompleted");
                audioListActivity.mViewModel.updateAudioStatus(audioModel.getId(), StringUtils.status_downloaded);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.d(TAG, "Error " + e.getMessage());
            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(File file) {
                Log.d(TAG, "File downloaded to " + file.getAbsolutePath());
            }
        };
    }


}
