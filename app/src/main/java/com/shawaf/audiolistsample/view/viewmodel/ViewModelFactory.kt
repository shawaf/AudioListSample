package com.shawaf.audiolistsample.view.viewmodel

/**
 * Created by mohamedelshawaf on 11/12/17.
 */
import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

/**
 * Factory for ViewModels
 */
class ViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AudioListViewModel::class.java)) {
            return AudioListViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}