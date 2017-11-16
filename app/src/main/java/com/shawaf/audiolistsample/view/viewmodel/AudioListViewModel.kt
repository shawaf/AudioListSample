package com.shawaf.audiolistsample.view.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import com.shawaf.audiolistsample.db.Audio
import com.shawaf.audiolistsample.db.AudioDataBase
import com.shawaf.audiolistsample.db.AudioDataBaseInitializer
import java.time.Duration

/**
 * Created by mohamedelshawaf on 11/11/17.
 */
class AudioListViewModel(application: Application) : AndroidViewModel(application) {

    var audios: LiveData<List<Audio>>

    var mDb: AudioDataBase

    init {
        mDb = AudioDataBase.getInstance(application)
        createDb()

        // Audio is a LiveData object so updates are observed.
        audios = mDb.audioModelDao().findAllAudios()

    }

    fun createDb() {
        // Populate it with initial data
        InitDBTask(mDb).execute()
    }

    fun getAudiosList(): LiveData<List<Audio>>? {
        return audios
    }

    fun updateAudioStatus(id: Int, status: String) {
        UpdateAudioStatusTask(mDb, id, status).execute()
    }

    fun updateAudioDuration(id: Int, duration: Long) {
        UpdateAudioDurationTask(mDb, id, duration).execute()
    }

    fun updateAudioPlayedCount(id: Int, played: Long) {
        UpdateAudioPlayedTask(mDb, id, played).execute()
    }


    class UpdateAudioStatusTask internal constructor(val mDb: AudioDataBase, val id: Int, val status: String) : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void): Void? {
            mDb.audioModelDao().updateAudioStatus(id, status)
            return null
        }

    }

    class UpdateAudioPlayedTask internal constructor(val mDb: AudioDataBase, val id: Int, val played: Long) : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void): Void? {
            mDb.audioModelDao().updateAudioPlayedCount(id, played)
            return null
        }

    }
    class UpdateAudioDurationTask internal constructor(val mDb: AudioDataBase, val id: Int, val duration: Long) : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void): Void? {
            mDb.audioModelDao().updateAudioDuration(id, duration)
            return null
        }

    }

    class InitDBTask internal constructor(val mDb: AudioDataBase) : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void): Void? {
            if (mDb.audioModelDao().count() == 0) {
                AudioDataBaseInitializer.populateAsync(mDb)
            }
            return null
        }

    }
}