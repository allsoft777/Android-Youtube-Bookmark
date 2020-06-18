package com.owllife.youtubebookmark.presentation.player;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.owllife.youtubebookmark.R;

import org.jetbrains.annotations.NotNull;

import kr.co.prnd.YouTubePlayerView;

/**
 * @author owllife.dev
 * @since 20. 6. 12
 */
public class test extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);

        YouTubePlayerView you = findViewById(R.id.you_tube_player_view);
        you.play("kT7AIbBcXk0", new YouTubePlayerView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(@NotNull YouTubePlayer.Provider provider,
                                                @NotNull YouTubePlayer youTubePlayer,
                                                boolean b) {
            }

            @Override
            public void onInitializationFailure(@NotNull YouTubePlayer.Provider provider,
                                                @NotNull YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }
}
