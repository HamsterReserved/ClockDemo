package space.hamsters.clockdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hamster on 16/7/12.
 *
 * Show current time.
 */
public class ClockFragment extends PageFragment {
    private TextView mTimeText;
    private View mRootView;
    private Runnable updateTimeText = new Runnable() {
        @Override
        public void run() {
            Date date = new Date(System.currentTimeMillis());
            mTimeText.setText(new SimpleDateFormat("HH:mm:ss").format(date));
            mRootView.postDelayed(this, 500);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_clock, container, false);
        mTimeText = (TextView)v.findViewById(R.id.text_current_time);
        mRootView = v;
        v.post(updateTimeText);
        return v;
    }

    @Override
    public String getTitle() {
        return "Clock";
    }
}
