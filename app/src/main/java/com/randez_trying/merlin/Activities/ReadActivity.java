package com.randez_trying.merlin.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.randez_trying.merlin.PageFragment;
import com.randez_trying.merlin.R;
import com.randez_trying.merlin.StaticHelper;
import com.randez_trying.merlin.Util.Book;

public class ReadActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    private ViewPager pager;
    private SeekBar progress;
    private TextView count;
    private Book parser;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        pager = findViewById(R.id.view_pager);
        progress = findViewById(R.id.progress);
        count = findViewById(R.id.count);
        parser = StaticHelper.book;

        PageAdapter adapter = new PageAdapter(getSupportFragmentManager(), parser.getPageCount());
        pager.setAdapter(adapter);
        pager.setCurrentItem(0);

        progress.setMax(parser.getPageCount());
        handleProgress();
        progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                pager.setCurrentItem(i);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @SuppressLint("SetTextI18n")
    private void handleProgress() {
        Runnable runnable = this::handleProgress;
        progress.setProgress(pager.getCurrentItem());
        count.setText((pager.getCurrentItem() + 1) + "/" + parser.getPageCount());
        handler.postDelayed(runnable, 100);
    }

    public class PageAdapter extends FragmentPagerAdapter {

        private final int pageCount;

        PageAdapter(@NonNull FragmentManager fm, int pageCount) {
            super(fm);
            this.pageCount = pageCount;
        }

        @Override
        public int getCount() {
            return pageCount;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return new PageFragment(parser.getPage(position));
        }
    }
}