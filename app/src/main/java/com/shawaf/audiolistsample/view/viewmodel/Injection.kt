package com.shawaf.audiolistsample.view.viewmodel


import android.app.Application
import android.content.Context
import com.shawaf.audiolistsample.db.AudioDao
import com.shawaf.audiolistsample.db.AudioDataBase

/**
 * Enables injection of data sources.
 */
object Injection {

    fun provideUserDataSource(context: Context): AudioDao {
        val database = AudioDataBase.getInstance(context)
        return database.audioModelDao()
    }

    fun provideViewModelFactory(application: Application): ViewModelFactory {
       // val dataSource = provideUserDataSource(context)
        return ViewModelFactory(application)
    }
}