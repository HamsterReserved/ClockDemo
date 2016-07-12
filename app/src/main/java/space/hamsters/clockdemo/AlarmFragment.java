package space.hamsters.clockdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hamster on 16/7/12.
 *
 * UI for managing alarms. Press to add, long-press to remove.
 */
public class AlarmFragment extends PageFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_alarm, container, false);
        return v;
    }

    @Override
    public String getTitle() {
        return "Alarms";
    }
}
