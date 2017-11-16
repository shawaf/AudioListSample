package com.shawaf.audiolistsample.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.ABORT
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import android.arch.persistence.room.RoomMasterTable.TABLE_NAME



/**
 * Created by mohamedelshawaf on 11/11/17.
 */


@Dao
public interface AudioDao {


    @Query("SELECT COUNT(*) FROM Audio")
    fun count(): Int


    @Query("Select * From Audio")
    fun findAllAudios(): LiveData<List<Audio>>


    @Insert(onConflict = REPLACE)
    fun insetAudio(audio: Audio)

    @Insert(onConflict = REPLACE)
    fun insetAudio(audio: List<Audio>)

    @Update()
    fun updateAudioStatus(audio: Audio)

    @Query("UPDATE Audio SET status = :arg1  WHERE id = :arg0")
    fun updateAudioStatus(id: Int, status:String)

    @Query("UPDATE Audio SET played = :arg1  WHERE id = :arg0")
    fun updateAudioPlayedCount(id: Int, played:Long)

    @Query("UPDATE Audio SET duration = :arg1  WHERE id = :arg0")
    fun updateAudioDuration(id: Int, duration:Long)

    @Query("DELETE FROM Audio")
    fun deleteAll()


}