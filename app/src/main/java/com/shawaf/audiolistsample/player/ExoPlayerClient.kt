package com.shawaf.audiolistsample.player

import android.app.Activity
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioRendererEventListener
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.shawaf.audiolistsample.db.Audio
import com.shawaf.audiolistsample.utils.StringUtils
import com.shawaf.audiolistsample.view.AudioListActivity
import com.google.android.exoplayer2.ExoPlayer



/**
 * Created by mohamedelshawaf on 11/12/17.
 */
class ExoPlayerClient(val activity: AudioListActivity) : Player.EventListener {
    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayerError(error: ExoPlaybackException?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoadingChanged(isLoading: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPositionDiscontinuity() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRepeatModeChanged(repeatMode: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var exoPlayer: SimpleExoPlayer
    private lateinit var eventLogger: EventLogger
    private lateinit var audio: Audio
    private val baseFilePath: String = Environment.getExternalStorageDirectory().toString() + "/MOI/AudioFile";
    private lateinit var filePath: String;
    private var isInitialized: Boolean = false

    fun initializeExoplayer(audio: Audio) {
        this.audio = audio
        filePath = baseFilePath + audio.id + ".mp3"

        resetPlayer()

        val renderersFactory = DefaultRenderersFactory(activity,
                null, // drmSessionManager: DrmSessionManager
                DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF)

        val trackSelector = DefaultTrackSelector()
        exoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector)

        eventLogger = EventLogger(trackSelector)
        exoPlayer.addListener(this)
        exoPlayer.setAudioDebugListener(eventLogger)
        exoPlayer.setMetadataOutput(eventLogger)

        val userAgent = Util.getUserAgent(activity, "ExoPlayerIntro")
        val mediaSource = ExtractorMediaSource(
                Uri.parse(filePath),
                DefaultDataSourceFactory(activity, userAgent),
                DefaultExtractorsFactory(),
                null, // eventHandler: Handler
                null) // eventListener: ExtractorMediaSource.EventListener

        exoPlayer.prepare(mediaSource)
        isInitialized = true


    }

    fun resetPlayer() {
        if (isInitialized) {
            Log.e("Player", "Player Initiated Before")
            stop()
        }
    }

    fun releaseExoplayer() {
        exoPlayer.release()
    }

    fun play() {
        exoPlayer.playWhenReady = true
        activity.mViewModel.updateAudioStatus(audio.id, StringUtils.status_playing)
    }

    fun resume(audio: Audio) {
        initializeExoplayer(audio)
        exoPlayer.seekTo(audio.played)
        exoPlayer.playWhenReady = true
        activity.mViewModel.updateAudioStatus(audio.id, StringUtils.status_playing)
        //activity.mViewModel.updateAudioDuration(audio.id, exoPlayer.duration)

    }

    fun pause() {
        exoPlayer.playWhenReady = false
        activity.mViewModel.updateAudioPlayedCount(audio.id, exoPlayer.currentPosition)
        activity.mViewModel.updateAudioStatus(audio.id, StringUtils.status_paused)
    }

    fun stop() {
        if (isInitialized) {
            exoPlayer.playWhenReady = false
            exoPlayer.stop()
            if (exoPlayer.playbackState == Player.STATE_ENDED)
                activity.mViewModel.updateAudioPlayedCount(audio.id, 0)
            else
                activity.mViewModel.updateAudioPlayedCount(audio.id, exoPlayer.currentPosition)
            activity.mViewModel.updateAudioStatus(audio.id, StringUtils.status_stoped)
            releaseExoplayer()
        }
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, state: Int) {

        if (state == Player.STATE_ENDED) {
            Log.e("Player", "Player State : END")
        }
    }

}