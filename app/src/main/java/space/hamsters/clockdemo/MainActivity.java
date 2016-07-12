package space.hamsters.clockdemo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<PageFragment> fragments = new ArrayList<>(4);
        fragments.add(new ClockFragment());
        fragments.add(new AlarmFragment());
        fragments.add(new TimerFragment());
        fragments.add(new StopwatchFragment());

        AlarmClockFragmentPagerAdapter pagerAdapter =
                new AlarmClockFragmentPagerAdapter(
                        getSupportFragmentManager(),
                        fragments);
        ViewPager pager = (ViewPager) findViewById(R.id.viewpager);

        pager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(pager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }
}
