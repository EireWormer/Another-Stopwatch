package ie.antranet.clock;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import org.apache.commons.lang3.time.StopWatch;

import java.util.ArrayList;

class Stopwatch extends Activity {
    private ArrayAdapter<String> lapsAapter;
    private ArrayList<String> laps = new ArrayList<String>();
    private Button startButton;
    private Button stopButton;
    private Button lapButton;
    private Button resetButton;
    private Button resumeButton;
    private ConstraintLayout theLayout;
    private ConstraintLayout.LayoutParams newLayoutParams;
    private LayoutTransition layoutTransition;
    private ListView lapsListView;
    private Resources r;
    private StopWatch stopWatch;
    private TextView displayField;
    private ViewGroup.LayoutParams origDisplayParams;

    Stopwatch(View view) {
        r = view.getContext().getResources();

        lapsListView = view.findViewById(R.id.stopwatch_laps_listview);

        displayField = (TextView) view.findViewById(R.id.stopwatch_display);
        displayField.setText("00:00.00");

        startButton = view.findViewById(R.id.stopwatch_tab_start_button);
        startButton.setOnClickListener(startButtonListener);

        lapButton = view.findViewById(R.id.stopwatch_tab_lap_button);;
        lapButton.animate().alpha(0.0f);
        lapButton.setVisibility(View.GONE);
        lapButton.setOnClickListener(lapButtonListener);

        stopButton = view.findViewById(R.id.stopwatch_tab_stop_button);;
        stopButton.animate().alpha(0.0f);
        stopButton.setVisibility(View.GONE);
        stopButton.setOnClickListener(stopButtonListener);

        resumeButton = view.findViewById(R.id.stopwatch_tab_resume_button);;
        resumeButton.animate().alpha(0.0f);
        resumeButton.setVisibility(View.GONE);
        resumeButton.setOnClickListener(resumeButtonListener);

        resetButton = view.findViewById(R.id.stopwatch_tab_reset_button);;
        resetButton.animate().alpha(0.0f);
        resetButton.setVisibility(View.GONE);
        resetButton.setOnClickListener(resetButtonListener);

        stopWatch = new StopWatch();

        newLayoutParams = (ConstraintLayout.LayoutParams) displayField.getLayoutParams();
        origDisplayParams = displayField.getLayoutParams();

        //allow transitions in layout
        theLayout = view.findViewById(R.id.time_picker_constraint);
        layoutTransition = theLayout.getLayoutTransition();
        layoutTransition.setDuration(500); // Change duration
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
    }

    private String getCurrentTimeString() {
        long milliseconds = stopWatch.getTime();
        long seconds = (long) (milliseconds / 1000) % 60 ;
        long minutes = (long) ((milliseconds / (1000*60)) % 60);
        String millisString = milliseconds + "";

        switch (millisString.length()) {
            case 1:
                millisString = "0" + millisString;
            case 3:
                millisString = millisString.substring(0, 2);
                break;
            case 4:
                millisString = millisString.substring(1, 3);
                break;
            case 5:
                millisString = millisString.substring(2, 4);
                break;
        }

        String minsAsString = minutes >= 10 ? "" + minutes : "0" + minutes;
        String secsAsString = seconds >= 10 ? "" + seconds : "0" + seconds;
        String currentTime = minsAsString + ":" + secsAsString + "." + millisString;

        return currentTime;
    }

    private View.OnClickListener startButtonListener = new View.OnClickListener() {
        public void onClick(View view) {
            stopWatch.start();
            startButton.animate().alpha(0.0f).setDuration(150);
            startButton.setVisibility(View.GONE);
            lapButton.setVisibility(View.VISIBLE);
            lapButton.animate().alpha(1.0f).setDuration(150);
            stopButton.setVisibility(View.VISIBLE);
            stopButton.animate().alpha(1.0f).setDuration(150);

            // every 1 second, update display
            (new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    while (!Thread.interrupted())
                        try
                        {
                            Thread.sleep(1);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    displayField.setText(getCurrentTimeString());
                                }
                            });
                        }
                        catch (InterruptedException e)
                        {
                            // ooops
                        }
                }
            })).start();
        }
    };

    private View.OnClickListener stopButtonListener = new View.OnClickListener() {
        public void onClick(View view) {
            stopWatch.suspend();
            stopButton.animate().alpha(0.0f).setDuration(150);
            stopButton.setVisibility(View.GONE);
            lapButton.animate().alpha(0.0f).setDuration(150);
            lapButton.setVisibility(View.GONE);
            resumeButton.setVisibility(View.VISIBLE);
            resumeButton.animate().alpha(1.0f).setDuration(150);
            resetButton.setVisibility(View.VISIBLE);
            resetButton.animate().alpha(1.0f).setDuration(150);
        }
    };

    private View.OnClickListener lapButtonListener = new View.OnClickListener() {
        public void onClick(View view) {
            String newLap = (laps.size() + 1) + "                  " + getCurrentTimeString();
            newLap = laps.size() >= 10 ? newLap : "0" + newLap;
            laps.add(newLap);

            String toPrint = "\n";
            for(int x = 0; x < laps.size(); x++) {
                toPrint += laps.get(x) + "\n";
            }
            Log.e("", toPrint);

            int px = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    50,
                    r.getDisplayMetrics()
            );

            newLayoutParams.topMargin = px;
            displayField.setLayoutParams(newLayoutParams);
            displayField.requestLayout();

            lapsAapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, laps);
            lapsListView.setAdapter(lapsAapter);
        }
    };

    private View.OnClickListener resetButtonListener = new View.OnClickListener() {
        public void onClick(View view) {
            stopWatch.reset();
            laps.clear();
            resetButton.animate().alpha(0.0f).setDuration(150);
            resetButton.setVisibility(View.GONE);
            resumeButton.animate().alpha(0.0f).setDuration(150);
            resumeButton.setVisibility(View.GONE);
            startButton.setVisibility(View.VISIBLE);
            startButton.animate().alpha(1.0f).setDuration(150);

            int px = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    200,
                    r.getDisplayMetrics()
            );

            newLayoutParams.topMargin = px;
            displayField.setLayoutParams(origDisplayParams);
        }
    };

    private View.OnClickListener resumeButtonListener = new View.OnClickListener() {
        public void onClick(View view) {
            stopWatch.resume();
            resumeButton.animate().alpha(0.0f).setDuration(150);
            resumeButton.setVisibility(View.GONE);
            resetButton.animate().alpha(0.0f).setDuration(150);
            resetButton.setVisibility(View.GONE);
            stopButton.setVisibility(View.VISIBLE);
            stopButton.animate().alpha(1.0f).setDuration(150);
            lapButton.setVisibility(View.VISIBLE);
            lapButton.animate().alpha(1.0f).setDuration(150);
        }
    };
}
