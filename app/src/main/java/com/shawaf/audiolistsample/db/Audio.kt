package com.shawaf.audiolistsample.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.time.Duration

/**
 * Created by mohamedelshawaf on 11/7/17.
 */

@Entity
data class Audio(

        @PrimaryKey(autoGenerate = true) var id: Int = 0,
        var audioUrl: String = "",
        var played: Long = 0,
        var duration: Long = 0,
        var status: String = ""
)
