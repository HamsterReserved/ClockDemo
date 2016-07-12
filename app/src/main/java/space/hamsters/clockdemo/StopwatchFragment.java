package space.hamsters.clockdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hamster on 16/7/12.
 *
 * Basic stopwatch with start/pause resume/lap features.
 */
public class StopwatchFragment extends PageFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stopwatch, container, false);
        return v;
    }

    @Override
    public String getTitle() {
        return "Stopwatch";
    }
}
