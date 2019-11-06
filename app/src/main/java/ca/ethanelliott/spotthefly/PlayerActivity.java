package ca.ethanelliott.spotthefly;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class PlayerActivity extends AppCompatActivity {
    private static final String TAG = "PlayerActivity";

    public MusicService musicService;
    private Intent playIntent;
    private boolean musicBound = false;

    SeekBar playerSeekBar;
    TextView playerCurrentTime;
    TextView playerSongDuration;
    TextView songName;
    ImageButton togglePlaybackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        playerSeekBar = findViewById(R.id.playerSeekBar);
        playerCurrentTime = findViewById(R.id.playerCurrentTime);
        playerSongDuration = findViewById(R.id.playerSongDuration);
        songName = findViewById(R.id.songName);
        togglePlaybackBtn = findViewById(R.id.togglePlaybackBtn);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }

        new Thread(() -> {
            Log.d(TAG, "onStart: thread start");
            while (!Thread.interrupted()) {
                if (musicBound) {
                    float progress = musicService.player.getCurrentPosition() / (float) musicService.player.getDuration() * 100;
                    Log.d(TAG, "onLoop: loop tick: " + progress + " @ " + musicService.player.getCurrentPosition() + " / " + musicService.player.getDuration());
                    playerSeekBar.setProgress((int) progress);
                    runOnUiThread(() -> {
                        playerCurrentTime.setText(formatmillis(musicService.player.getCurrentPosition()));
                        playerSongDuration.setText(formatmillis(musicService.player.getDuration()));
                        songName.setText(musicService.songs.get(musicService.songPosition).getName());
                        if (musicService.player.isPlaying()) {
                            togglePlaybackBtn.setImageResource(R.drawable.pause);
                        } else {
                            togglePlaybackBtn.setImageResource(R.drawable.play);
                        }
                    });
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(musicConnection);
    }

    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicService = binder.getService();
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicBound = false;
        }
    };

    public void back(View view) {
        finish();
        overridePendingTransition(R.anim.slide_out_down, R.anim.slide_in_down);
    }

    public void toggleMusic(View view) {
        if (this.musicService.player.isPlaying()) {
            this.musicService.player.pause();
        } else {
            this.musicService.player.start();
        }
    }

    public void makeToast(Context mContext, String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    public String formatmillis(int milis) {
        int minutes = (milis / 1000) / 60;
        int seconds = (milis / 1000) % 60;
        return String.format(Locale.CANADA, "%02d:%02d", minutes, seconds);
    }
}
