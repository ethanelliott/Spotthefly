package ca.ethanelliott.spotthefly;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DownloadActivity extends AppCompatActivity {

    private static final String TAG = "DownloadActivity";
    private ArrayList<DownloadSong> mSongs = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        initList();
        initRecyclerView();
    }

    public void initList() {
        SongDB songDB = new SongDB(this);
        ArrayList<String> downloadedSongNames = songDB.getAllSongsNames();

        for (int i = 1; i < 17; i++) {
            String songName = String.format(Locale.CANADA, "Generic Song %d", i);
            if (!downloadedSongNames.contains(songName)) {
                DownloadSong song = new DownloadSong(
                        songName,
                        String.format(Locale.CANADA, "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-%d.mp3", i),
                        false
                );
                this.mSongs.add(song);
            }
        }
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recycler view");
        RecyclerView recyclerView = findViewById(R.id.downloadRecyleView);
        DownloadRecyclerViewAdapter adapter = new DownloadRecyclerViewAdapter(this, this, mSongs);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void gotoHome(View view) {
        finish();
    }

    public void gotoPlayer(View view) {
        Intent intent = new Intent(DownloadActivity.this, PlayerActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }
}
