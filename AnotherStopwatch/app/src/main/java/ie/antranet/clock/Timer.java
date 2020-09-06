package ie.antranet.clock;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;

class Timer {
    private static String[] hourOptions = new String[]{"00", "01", "02", "03", "04", "05", "06", "07",
            "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22",
            "23" };
    private static String[] minOptions = new String[]{"00", "01", "02", "03", "04", "05", "06", "07", "08",
            "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23",
            "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38",
            "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53",
            "54", "55", "56", "57", "58", "59" };
    private static String[] secOptions = minOptions.clone();

    private final CountDownTimer[] countDownTimer = new CountDownTimer[1];

    private ArrayList<View> displayViews;
    private ArrayList<View> inputViews;
    private MediaPlayer mediaPlayer;
    private NumberPicker hourPicker;
    private NumberPicker minPicker;
    private NumberPicker secPicker;
    private TextView countdownDisplay;

    /**
     * Configures all the elements in the layout.
     *
     * This constructor takes configures all the attributes for the elements in the layout and the
     * functionality of te start and stop buttons.
     *
     * @param view The view from the MainActivity tab layout
     */
    Timer(View view) {
        // hide countdown display
        countdownDisplay = view.findViewById(R.id.timer_contdown_display);
        countdownDisplay.setVisibility(View.GONE);

        // Define time picker number pickers
        hourPicker = CardFragment.initilizeNumberPicker(hourPicker, hourOptions, view, R.id.timer_hour_picker);
        minPicker = CardFragment.initilizeNumberPicker(minPicker, minOptions, view, R.id.timer_min_picker);
        secPicker = CardFragment.initilizeNumberPicker(secPicker, secOptions, view, R.id.timer_sec_picker);

        inputViews = new ArrayList<View>();
        inputViews.add(view.findViewById(R.id.timer_tab_start));
        inputViews.add(view.findViewById(R.id.time_picker_constraint));
        inputViews.add(view.findViewById(R.id.timer_hour_label));
        inputViews.add(view.findViewById(R.id.timer_min_label));
        inputViews.add(view.findViewById(R.id.timer_sec_label));

        displayViews = new ArrayList<View>();
        displayViews.add(view.findViewById(R.id.timer_tab_cancel));
        displayViews.add(countdownDisplay);

        // start timer button
        view.findViewById(R.id.timer_tab_start).setOnClickListener(startButtonListener);

        // cancel button
        view.findViewById(R.id.timer_tab_cancel).setVisibility(View.GONE);
        view.findViewById(R.id.timer_tab_cancel).setOnClickListener(cancelButtonListener);
    }

    private View.OnClickListener cancelButtonListener = new View.OnClickListener() {
        /** On cancel button click, stop the timer and beep alert; toggle components visibility. */
        public void onClick(View view) {
            if(mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            for (View v : inputViews) v.setVisibility(View.VISIBLE);
            for (View v : displayViews) v.setVisibility(View.GONE);
            countDownTimer[0].cancel();
        }
    };

    private View.OnClickListener startButtonListener = new View.OnClickListener() {
        /**
         * On start button click, get input time and countdown to zero.
         *
         * When the start button is clicked, get the time inputted and begin countdown. On each tick,
         * calculate how many hours/minutes/seconds remain on the timer and display it. When timer
         * hits zero, play alerting beep.
         */
        public void onClick(View view) {
            for (View v2 : inputViews) v2.setVisibility(View.GONE);
            for (View v2 : displayViews) v2.setVisibility(View.VISIBLE);

            countDownTimer[0] = new CountDownTimer(timerToMilliseconds(hourPicker.getValue(), minPicker.getValue(), secPicker.getValue()), 1000){
                public void onTick(long millisUntilFinished){
                    long remainingMillis = millisUntilFinished;
                    long hoursRemaining = remainingMillis / 3600000;
                    remainingMillis -= hoursRemaining * 3600000;
                    long minsRemaining = remainingMillis / 60000;
                    remainingMillis -= minsRemaining * 60000;
                    long secsRemaining = remainingMillis / 1000;

                    String hrsAsString = hoursRemaining >= 10 ? "" + hoursRemaining : "0" + hoursRemaining;
                    String minsAsString = minsRemaining >= 10 ? "" + minsRemaining : "0" + minsRemaining;
                    String secsAsString = secsRemaining >= 10 ? "" + secsRemaining : "0" + secsRemaining;
                    String remaingTimeToDisplay = hrsAsString + " : " + minsAsString + " : " + secsAsString;
                    countdownDisplay.setText(remaingTimeToDisplay);
                }

                public  void onFinish(){
                    countdownDisplay.setText("00 : 00 : 00");
                    mediaPlayer = MediaPlayer.create(view.getContext(), R.raw.beep);
                    mediaPlayer.setScreenOnWhilePlaying(true);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                }
            }.start();
        }
    };

    /** @return The sum of all the time units, passed as arguments, transated to milliseconds */
    private int timerToMilliseconds(int hrs, int mins, int secs) {
        return (hrs * 3600000) + (mins * 60000) + (secs * 1000) + 1000;
    }
}