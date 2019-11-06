package ca.ethanelliott.spotthefly;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DownloadRecyclerViewAdapter extends RecyclerView.Adapter<DownloadRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "DownloadRecyclerView";

    private ArrayList<DownloadSong> mSong;
    private Context mContext;
    private DownloadActivity mainActivity;

    DownloadRecyclerViewAdapter(Context mContext, DownloadActivity mainActivity, ArrayList<DownloadSong> mSong) {
        this.mSong = mSong;
        this.mContext = mContext;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.download_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");
        holder.textView.setText(mSong.get(position).getName());
        holder.imageView.setVisibility((mSong.get(position).isDownloading() ? View.INVISIBLE : View.VISIBLE));
        holder.progressBar.setVisibility((mSong.get(position).isDownloading() ? View.VISIBLE : View.INVISIBLE));
        holder.parentLayout.setOnClickListener(v -> {
            Log.d(TAG, "onClick: clicked on: " + mSong.get(position));
            // Start download
            new Thread(() -> {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                InputStream is = null;
                try {
                    mainActivity.runOnUiThread(() -> Toast.makeText(mContext, "Starting Download...", Toast.LENGTH_SHORT).show());
                    URL url = new URL(mSong.get(position).getUrl());
                    is = url.openStream();
                    byte[] byteChunk = new byte[2048];
                    int n;
                    while ((n = is.read(byteChunk)) > 0) {
                        baos.write(byteChunk, 0, n);
                    }
                    is.close();
                    byte[] songData = baos.toByteArray();
                    Song s = new Song(
                            -1,
                            UUID.randomUUID().toString(),
                            mSong.get(position).getName(),
                            songData
                    );
                    SongDB songDB = new SongDB(mainActivity);
                    songDB.addSong(s);
                    mainActivity.runOnUiThread(() -> Toast.makeText(mContext, "Download Complete!", Toast.LENGTH_SHORT).show());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            mSong.get(position).setDownloading(true);
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return mSong.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        ProgressBar progressBar;
        LinearLayout parentLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.songName);
            imageView = itemView.findViewById(R.id.downloadBtn);
            progressBar = itemView.findViewById(R.id.progressBar);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }
    }
}
