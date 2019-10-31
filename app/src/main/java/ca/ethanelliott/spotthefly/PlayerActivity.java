package ca.ethanelliott.spotthefly;

import android.os.Bundle;
import android.transition.Slide;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class PlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    public void back(View view) {
        finish();
        overridePendingTransition( R.anim.slide_out_down, R.anim.slide_in_down);
    }
}
