package ca.ethanelliott.spotthefly;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    ArrayList<Song> mSong;
    private Context mContext;
    private MainActivity mainActivity;

    RecyclerViewAdapter(Context mContext, MainActivity mainActivity, ArrayList<Song> mSong) {
        this.mSong = mSong;
        this.mContext = mContext;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");
        holder.textView.setText(mSong.get(position).getName());
        holder.deleteEntryBtn.setOnClickListener(view -> {
            mainActivity.makeToast(mContext, "DELETE: " + mSong.get(position).getName());
            SongDB songDB = new SongDB(mContext);
            String uuid = mSong.get(position).getUuid();
            songDB.deleteSong(uuid);
        });
        holder.parentLayout.setOnClickListener(v -> {
            Log.d(TAG, "onClick: clicked on: " + mSong.get(position));
            mainActivity.makeToast(mContext, mSong.get(position).getName());
            mainActivity.songPicked(position);
        });
    }

    @Override
    public int getItemCount() {
        return mSong.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView isPlayingIcon;
        LinearLayout parentLayout;
        ImageButton deleteEntryBtn;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.songName);
            isPlayingIcon = itemView.findViewById(R.id.isPlayingIcon);
            deleteEntryBtn = itemView.findViewById(R.id.deleteEntryBtn);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }
    }
}
