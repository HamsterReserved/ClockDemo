package space.hamsters.clockdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by hamster on 16/7/12.
 *
 * Basic stopwatch with start/pause resume/lap features.
 */
public class StopwatchFragment extends PageFragment {
    private static final int DISPLAY_UPDATE_INTERVAL_MS = 250;
    private Long startMillis; /* Won't survive pause/resume */
    private Long elapsedMillis;
    private View mRootView;

    private TextView mHourText;
    private TextView mMinuteText;
    private TextView mSecondText;
    private TextView mMillisText;

    private Button mStartButton;
    private Button mPauseResumeButton;
    private Button mLapResetButton;

    private RecyclerView mLapList;
    private SingleLineListAdapter mListAdapter;

    private enum StopwatchState {
        STOPWATCH_STOPPED,
        STOPWATCH_RUNNING,
        STOPWATCH_PAUSED
    }

    private StopwatchState mState;

    private Runnable displayElapsedTime = new Runnable() {
        @Override
        public void run() {
            if (mState == StopwatchState.STOPWATCH_RUNNING) {
                /* Measure delta between calls */
                long currentMillis = System.currentTimeMillis();
                elapsedMillis += currentMillis - startMillis;
                startMillis = currentMillis;
                updateTimeDisplay(elapsedMillis);
                mRootView.postDelayed(this, DISPLAY_UPDATE_INTERVAL_MS);
            }
        }
    };

    private Button.OnClickListener startListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            startTimer();
        }
    };

    private Button.OnClickListener pauseResumeListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (mState) {
                case STOPWATCH_RUNNING:
                    /* RUNNING -> PAUSED*/
                    mState = StopwatchState.STOPWATCH_PAUSED;
                    updateButtonAppearance();
                    break;
                case STOPWATCH_PAUSED:
                    /* PAUSED -> RUNNING */
                    startTimer();
                    break;
            }
        }
    };

    private Button.OnClickListener resetLapListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (mState) {
                case STOPWATCH_RUNNING:
                    /* Lap */
                    long hours = elapsedMillis / 3600000;
                    long minutes = (elapsedMillis - hours * 3600000) / 60000;
                    long seconds = (elapsedMillis - hours * 3600000 - minutes * 60000) / 1000;
                    long millis = elapsedMillis - hours * 3600000 - minutes * 60000 - seconds * 1000;
                    mListAdapter.appendItem(String.format(Locale.getDefault(),
                            "%d:%d:%d.%d", hours, minutes, seconds, millis));
                    mListAdapter.notifyItemInserted(0);
                    mLapList.scrollToPosition(0); /* Lock at top */
                    break;
                case STOPWATCH_PAUSED:
                    /* Reset */
                    mListAdapter.notifyItemRangeRemoved(0, mListAdapter.getItemCount());
                    mListAdapter.clear();
                    stopTimer();
                    break;
            }
        }
    };

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_stopwatch, container, false);

        mHourText = (TextView) mRootView.findViewById(R.id.stopwatch_hour_text);
        mMinuteText = (TextView) mRootView.findViewById(R.id.stopwatch_minute_text);
        mSecondText = (TextView) mRootView.findViewById(R.id.stopwatch_second_text);
        mMillisText = (TextView) mRootView.findViewById(R.id.stopwatch_milli_text);

        mStartButton = (Button) mRootView.findViewById(R.id.start_stopwatch_button);
        mPauseResumeButton = (Button) mRootView.findViewById(R.id.pause_resume_stopwatch_button);
        mLapResetButton = (Button) mRootView.findViewById(R.id.reset_lap_stopwatch_button);

        mStartButton.setOnClickListener(startListener);
        mPauseResumeButton.setOnClickListener(pauseResumeListener);
        mLapResetButton.setOnClickListener(resetLapListener);

        mListAdapter = new SingleLineListAdapter(mRootView.getContext());
        mLapList = (RecyclerView) mRootView.findViewById(R.id.stopwatch_lap_list);
        mLapList.setAdapter(mListAdapter);
        mLapList.setItemAnimator(new DefaultItemAnimator());
        mLapList.setLayoutManager(new LinearLayoutManager(mRootView.getContext(),
                LinearLayoutManager.VERTICAL,
                false));

        stopTimer();

        return mRootView;
    }

    @Override
    public String getTitle() {
        return "Stopwatch";
    }

    private void updateButtonAppearance() {
        switch (mState) {
            case STOPWATCH_STOPPED:
                mPauseResumeButton.setVisibility(View.GONE);
                mLapResetButton.setVisibility(View.GONE);
                mStartButton.setVisibility(View.VISIBLE);
                break;
            case STOPWATCH_RUNNING:
                mPauseResumeButton.setVisibility(View.VISIBLE);
                mPauseResumeButton.setText("Pause");
                mLapResetButton.setVisibility(View.VISIBLE);
                mLapResetButton.setText("Lap");
                mStartButton.setVisibility(View.GONE);
                break;
            case STOPWATCH_PAUSED:
                /*
                * We can only transfer to PAUSED from RUNNING, so only button text
                * needs updating
                */
                mPauseResumeButton.setText("Resume");
                mLapResetButton.setText("Reset");
                break;
        }
    }

    /**
     * Update time text boxes with millis
     * @param millis time to show
     */
    private void updateTimeDisplay(long millis) {
        long hours = millis / 3600000;
        long minutes = (millis - hours * 3600000) / 60000;
        long seconds = (millis - hours * 3600000 - minutes * 60000) / 1000;
        long milli = millis - hours * 3600000 - minutes * 60000 - seconds * 1000;
        mHourText.setText(String.valueOf(hours));
        mMinuteText.setText(String.valueOf(minutes));
        mSecondText.setText(String.valueOf(seconds));
        mMillisText.setText(String.valueOf(milli));
    }

    private void startTimer() {
        mState = StopwatchState.STOPWATCH_RUNNING;
        updateButtonAppearance();
        startMillis = System.currentTimeMillis();
        mRootView.postDelayed(displayElapsedTime, DISPLAY_UPDATE_INTERVAL_MS);
    }

    private void stopTimer() {
        elapsedMillis = 0L;
        startMillis = 0L;
        updateTimeDisplay(0);
        mState = StopwatchState.STOPWATCH_STOPPED;
        updateButtonAppearance();
    }
}
