package space.hamsters.clockdemo;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by hamster on 16/7/12.
 * <p/>
 * Countdown timer.
 */
public class TimerFragment extends PageFragment {
    private View mRootView;
    private EditText mHourEditText;
    private EditText mMinuteEditText;
    private EditText mSecondEditText;
    private Button mStartButton;
    private Button mPauseResumeButton;
    private Button mResetButton;
    private int mRemainingSeconds;

    private enum TimerState {
        TIMER_STOPPED,
        TIMER_RUNNING,
        TIMER_PAUSED
    }

    private TimerState mState;

    private Runnable decreaseOneSecond = new Runnable() {
        @Override
        public void run() {
            if (mState == TimerState.TIMER_RUNNING) {
                mRemainingSeconds--;
                updateRemainingTime();
                if (mRemainingSeconds > 0) {
                    mRootView.postDelayed(this, 1000);
                } else {
                    mState = TimerState.TIMER_STOPPED;
                    updateButtonVisibility();
                    AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext());
                    builder.setTitle("Countdown Timer");
                    builder.setMessage("Time up!");
                    builder.setNegativeButton("Dismiss", null);
                    builder.show();
                }
            }
        }
    };

    private Button.OnClickListener pauseResumeButtonListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (mState) {
                case TIMER_RUNNING:
                    /* RUNNING -> PAUSED */
                    mState = TimerState.TIMER_PAUSED;
                    updateButtonVisibility();
                    break;
                case TIMER_PAUSED:
                    /* PAUSED -> RUNNING */
                    mState = TimerState.TIMER_RUNNING;
                    updateButtonVisibility();
                    mRootView.postDelayed(decreaseOneSecond, 1000);
                    break;
            }
        }
    };

    private Button.OnClickListener resetListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            mState = TimerState.TIMER_STOPPED;
            updateButtonVisibility();
            mRemainingSeconds = 0;
            updateRemainingTime();
        }
    };

    private Button.OnClickListener startButtonOnClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            mRemainingSeconds = Integer.parseInt(mHourEditText.getText().toString()) * 3600
                    + Integer.parseInt(mMinuteEditText.getText().toString()) * 60
                    + Integer.parseInt(mSecondEditText.getText().toString());
            if (mRemainingSeconds > 0) {
                mState = TimerState.TIMER_RUNNING;
                updateButtonVisibility();
                mRootView.postDelayed(decreaseOneSecond, 1000);
            }
        }
    };

    private EditText.OnKeyListener textChangedListener = new EditText.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                try {
                    mRemainingSeconds = Integer.parseInt(mHourEditText.getText().toString()) * 3600
                            + Integer.parseInt(mMinuteEditText.getText().toString()) * 60
                            + Integer.parseInt(mSecondEditText.getText().toString());
                    if (mRemainingSeconds > 0)
                        mStartButton.setEnabled(true);
                    else
                        mStartButton.setEnabled(false);
                } catch (NumberFormatException e) {
                    // Not an integer in EditText
                    mStartButton.setEnabled(false);
                }
            }
            return false;
        }
    };

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_timer, container, false);
        mHourEditText = (EditText) mRootView.findViewById(R.id.timer_hour_input);
        mMinuteEditText = (EditText) mRootView.findViewById(R.id.timer_minute_input);
        mSecondEditText = (EditText) mRootView.findViewById(R.id.timer_second_input);

        mHourEditText.setOnKeyListener(textChangedListener);
        mMinuteEditText.setOnKeyListener(textChangedListener);
        mSecondEditText.setOnKeyListener(textChangedListener);

        mStartButton = (Button) mRootView.findViewById(R.id.start_timer_button);
        mPauseResumeButton = (Button) mRootView.findViewById(R.id.pause_resume_timer_button);
        mResetButton = (Button) mRootView.findViewById(R.id.reset_timer_button);

        mStartButton.setOnClickListener(startButtonOnClickListener);
        mPauseResumeButton.setOnClickListener(pauseResumeButtonListener);
        mResetButton.setOnClickListener(resetListener);

        mState = TimerState.TIMER_STOPPED;
        updateButtonVisibility();

        return mRootView;
    }

    @Override
    public String getTitle() {
        return "Timer";
    }

    /**
     * Show mRemainingSeconds in EditTexts
     */
    private void updateRemainingTime() {
        int hour = mRemainingSeconds / 3600;
        int minute = (mRemainingSeconds - hour * 3600) / 60;
        int second = mRemainingSeconds - hour * 3600 - minute * 60;

        mHourEditText.setText(String.valueOf(hour));
        mMinuteEditText.setText(String.valueOf(minute));
        mSecondEditText.setText(String.valueOf(second));
    }

    /**
     * Update button visibility and enable state by timer state
     */
    private void updateButtonVisibility() {
        switch (mState) {
            case TIMER_STOPPED:
                mPauseResumeButton.setVisibility(View.GONE);
                mResetButton.setVisibility(View.GONE);
                mStartButton.setVisibility(View.VISIBLE);
                mStartButton.setEnabled(false);
                mHourEditText.setEnabled(true);
                mMinuteEditText.setEnabled(true);
                mSecondEditText.setEnabled(true);
                break;
            case TIMER_RUNNING:
                mPauseResumeButton.setVisibility(View.VISIBLE);
                mPauseResumeButton.setText("Pause");
                mResetButton.setVisibility(View.VISIBLE);
                mStartButton.setVisibility(View.GONE);
                mHourEditText.setEnabled(false);
                mMinuteEditText.setEnabled(false);
                mSecondEditText.setEnabled(false);
                break;
            case TIMER_PAUSED:
                /*
                * We can only transfer to PAUSED from RUNNING, so only button text
                * needs updating
                */
                mPauseResumeButton.setText("Resume");
                break;
        }
    }
}
