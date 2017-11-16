package com.shawaf.audiolistsample.view

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.shawaf.audiolistsample.R
import com.shawaf.audiolistsample.db.Audio
import com.shawaf.audiolistsample.utils.CommonMethods
import com.shawaf.audiolistsample.view.adapter.AudioListAdapter
import com.shawaf.audiolistsample.view.viewmodel.AudioListViewModel
import com.shawaf.audiolistsample.view.viewmodel.Injection
import com.shawaf.audiolistsample.view.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class AudioListActivity : AppCompatActivity() {

    var isPermissioned: Boolean = false
    var audioList : MutableList<Audio> = ArrayList()
    lateinit var audioListAdapter: AudioListAdapter

    private lateinit var viewModelFactory: ViewModelFactory
    lateinit var mViewModel : AudioListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Get a reference to the ViewModel for this screen.
        viewModelFactory = Injection.provideViewModelFactory(this.application)
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(AudioListViewModel::class.java)
        subscribForAudioListDataChange()
        initAudioList()
    }

    fun initAudioList() {
        audio_list_rv.layoutManager = LinearLayoutManager(this)
        audioListAdapter= AudioListAdapter(this, audioList)
        audio_list_rv.adapter = audioListAdapter
    }

    fun subscribForAudioListDataChange(){
        mViewModel.getAudiosList()!!.observe(this, object : Observer<List<Audio>> {
            override fun onChanged(audios: List<Audio>?) {
                audioList= audios as MutableList<Audio>
                audioListAdapter.updateList(audioList)
            }
        })
    }




    fun checkDownloadPermissions(): Boolean {
        isPermissioned = true
        if (CommonMethods.shouldAskPermission()) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                val permissions = arrayOf("android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE")
                ActivityCompat.requestPermissions(this, permissions, 200)
                isPermissioned = false;
            } else {
                isPermissioned = true;
            }
        }
        return isPermissioned;
    }

    override fun onRequestPermissionsResult(permsRequestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        when (permsRequestCode) {
            200 -> {
                val writeAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (writeAccepted == true) {
                    isPermissioned = true;
                }
            }
        }

    }
}
