package ie.antranet.clock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity  {
    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    private String[] tabTitles = new String[]{"Stopwatch", "Timer"};

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setBackgroundColor(getColor(R.color.colorAccent));

        viewPager = findViewById(R.id.pageviewer);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager.setAdapter(createCardAdapter());
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor(getResources().getString(R.color.colorPrimary)));
        tabLayout.setTabTextColors(Color.parseColor("#B0B0B0"), Color.parseColor(getResources().getString(R.color.colorPrimary)));
        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(tabTitles[position]);
                    }
                }).attach();

    }

    private ViewPagerAdapter createCardAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        return adapter;
    }
}
