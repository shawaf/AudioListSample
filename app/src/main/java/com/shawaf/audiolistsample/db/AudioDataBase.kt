package com.shawaf.audiolistsample.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

/**
 * Created by mohamedelshawaf on 11/11/17.
 */

@Database(entities = arrayOf(Audio::class), version = 1)
abstract class AudioDataBase : RoomDatabase() {


    abstract fun audioModelDao(): AudioDao

    companion object {

        @Volatile private var INSTANCE: AudioDataBase? = null

        fun getInstance(context: Context): AudioDataBase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        AudioDataBase::class.java, "Audio.db")
                        .build()
    }
}
