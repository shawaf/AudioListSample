package com.shawaf.audiolistsample.db

import android.os.AsyncTask
import android.util.Log
import com.shawaf.audiolistsample.utils.StringUtils
import java.util.*

/**
 * Created by mohamedelshawaf on 11/11/17.
 */
class AudioDataBaseInitializer {


    companion object {
        val audioList = arrayOf(
                "http://static1.grsites.com/archive/sounds/cartoon/cartoon001.mp3",
                "http://static1.grsites.com/archive/sounds/cartoon/cartoon002.mp3",
                "http://static1.grsites.com/archive/sounds/cartoon/cartoon003.mp3",
                "http://static1.grsites.com/archive/sounds/cartoon/cartoon004.mp3",
                "http://static1.grsites.com/archive/sounds/cartoon/cartoon005.mp3",
                "http://static1.grsites.com/archive/sounds/cartoon/cartoon006.mp3",
                "http://static1.grsites.com/archive/sounds/cartoon/cartoon007.mp3",
                "http://static1.grsites.com/archive/sounds/cartoon/cartoon008.mp3",
                "http://static1.grsites.com/archive/sounds/cartoon/cartoon009.mp3",
                "http://static1.grsites.com/archive/sounds/cartoon/cartoon010.mp3"
        )
        // Simulate a blocking operation delaying each Loan insertion with a delay:
        private val DELAY_MILLIS = 500

        fun populateAsync(db: AudioDataBase) {

            val task = PopulateDbAsync(db)
            task.execute()
        }
        private fun addAudioFilde(db: AudioDataBase, audiUrl: String, played: Long, status: String): Audio {
            val audio = Audio()
            audio.audioUrl = audiUrl
            audio.played = played
            audio.status = status
            db.audioModelDao().insetAudio(audio)
            return audio
        }

        private fun populateWithTestData(db: AudioDataBase) {
            db.audioModelDao().deleteAll()

            try {
                for (i in audioList.indices) {
                    addAudioFilde(db, audioList[i], 0, StringUtils.status_not_downloaded)
                   // Thread.sleep(DELAY_MILLIS.toLong())
                    Log.d("DB", "Added Audio : $i")
                }

            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        }
    }



     class PopulateDbAsync internal constructor(val mDb: AudioDataBase) : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void): Void? {
            AudioDataBaseInitializer.populateWithTestData(mDb)
            return null
        }

    }

}