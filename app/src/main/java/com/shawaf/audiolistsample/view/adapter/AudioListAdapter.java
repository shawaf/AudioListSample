package com.shawaf.audiolistsample.view.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shawaf.audiolistsample.R;
import com.shawaf.audiolistsample.db.Audio;
import com.shawaf.audiolistsample.download.DownloadManager;
import com.shawaf.audiolistsample.player.ExoPlayerClient;
import com.shawaf.audiolistsample.utils.StringUtils;
import com.shawaf.audiolistsample.view.AudioListActivity;

import java.util.List;

/**
 * Created by mohamedelshawaf on 11/7/17.
 */

public class AudioListAdapter extends RecyclerView.Adapter<AudioRowViewHolder> {

    private AudioListActivity activity;
    private List<Audio> audioModels;
    private DownloadManager downloadManager;
    private ExoPlayerClient exoPlayerClient;
    private int currentPlayingID;

    public AudioListAdapter(Activity activity, List<Audio> audioModels) {
        this.activity = (AudioListActivity) activity;
        this.audioModels = audioModels;
        exoPlayerClient = new ExoPlayerClient(this.activity);
    }

    @Override
    public AudioRowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(activity).inflate(R.layout.item_audio_row, parent, false);
        AudioRowViewHolder rcv = new AudioRowViewHolder(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(final AudioRowViewHolder holder, final int position) {
        final String audioUrl = audioModels.get(position).getAudioUrl();
        holder.audioNameTV.setText("AudioFile NO : " + audioModels.get(position).getId());
        holder.audioPlayedCountTV.setText(audioModels.get(position).getPlayed()+" ms" );
        holder.downloadProgress.setVisibility(View.GONE);
        holder.playIconIV.setVisibility(View.VISIBLE);

        //Check FIle Status
        String fileStatus = audioModels.get(position).getStatus();
        setIconAccordingToFIleStatus(holder, fileStatus);

        holder.playIconIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCLickedFile(holder, position);
            }
        });
    }

    private void onCLickedFile(AudioRowViewHolder holder, int position) {
        String fileStatus = audioModels.get(position).getStatus();
        switch (fileStatus) {
            case StringUtils.status_downloading:
                break;
            case StringUtils.status_not_downloaded:
                downloadFile(holder, position);
                break;
            case StringUtils.status_downloaded:
                resumAudioFromPosition(position);
                break;
            case StringUtils.status_playing:
                stopAudio(position);
                break;
            case StringUtils.status_paused:
                resumAudioFromPosition(position);
                break;
            case StringUtils.status_stoped:
                resumAudioFromPosition(position);
                break;
        }

    }

    private void setIconAccordingToFIleStatus(AudioRowViewHolder holder, String fileStatus) {
        switch (fileStatus) {
            case StringUtils.status_downloading:
                holder.downloadProgress.setVisibility(View.VISIBLE);
                holder.playIconIV.setVisibility(View.GONE);
                break;
            case StringUtils.status_not_downloaded:
                holder.playIconIV.setImageResource(R.drawable.ic_file_download_black_24dp);
                break;
            case StringUtils.status_downloaded:
                holder.playIconIV.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
                break;
            case StringUtils.status_playing:
                holder.playIconIV.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);
                break;
            case StringUtils.status_paused:
                holder.playIconIV.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
                break;
            case StringUtils.status_stoped:
                holder.playIconIV.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
                break;
        }
    }

    private void downloadFile(AudioRowViewHolder holder, int position) {
        if (activity.checkDownloadPermissions()) {
            holder.downloadProgress.setVisibility(View.VISIBLE);
            holder.playIconIV.setVisibility(View.GONE);
            downloadManager = new DownloadManager(activity);
            downloadManager.downloadFile(audioModels.get(position));
        }
    }

    private void playAudioFromBegining(int position) {
        exoPlayerClient.initializeExoplayer(audioModels.get(position));
        exoPlayerClient.play();
    }

    private void pauseAudio(int position) {
        exoPlayerClient.pause();
    }

    private void stopAudio(int position) {
        exoPlayerClient.stop();
    }

    private void resumAudioFromPosition(int position) {
        exoPlayerClient.stop();
        exoPlayerClient.resume(audioModels.get(position));
    }

    @Override
    public int getItemCount() {
        return audioModels.size();
    }

    public void updateList(List<Audio> audioModels) {
        Log.e("Adapter", "Data Chaged ");

        this.audioModels = audioModels;
        notifyDataSetChanged();
    }
}
