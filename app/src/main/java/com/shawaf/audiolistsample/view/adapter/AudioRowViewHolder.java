package com.shawaf.audiolistsample.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shawaf.audiolistsample.R;

/**
 * Created by mohamedelshawaf on 11/7/17.
 */

public class AudioRowViewHolder extends RecyclerView.ViewHolder {

    ImageView playIconIV;
    TextView audioNameTV;
    TextView audioPlayedCountTV;
    ProgressBar downloadProgress;

    public AudioRowViewHolder(View itemView) {
        super(itemView);

        playIconIV = (ImageView) itemView.findViewById(R.id.audio_play_icon_iv);
        audioNameTV = (TextView) itemView.findViewById(R.id.audio_name_tv);
        audioPlayedCountTV = (TextView) itemView.findViewById(R.id.audio_played_count_tv);
        downloadProgress=(ProgressBar) itemView.findViewById(R.id.audio_play_progbar);
    }
}
