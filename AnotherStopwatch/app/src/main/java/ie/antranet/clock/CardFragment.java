package ie.antranet.clock;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.lang.reflect.Field;

public class CardFragment extends Fragment {
    private static final String ARG_COUNT = "param1";

    private Integer counter;

    public CardFragment() {}

    public static CardFragment newInstance(Integer counter) {
        CardFragment fragment = new CardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COUNT, counter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            counter = getArguments().getInt(ARG_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        switch (counter) {
            case 1:
                return inflater.inflate(R.layout.timer_tab, container, false);
            default:
                return inflater.inflate(R.layout.stopwatch_tab, container, false);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        switch (counter) {
            case 0:
                new Stopwatch(getView());
                break;
            case 1:
                new Timer(view);
                break;
        }
    }

    /**
     * Set a number picker's divider to the specified drawable.
     *
     * @param picker A NumberPicker input field.
     * @param customDrawable A drawable to be set as the divider
     */
    private static void setDividerColor(NumberPicker picker, Drawable customDrawable) {
        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field field : pickerFields) {
            if (field.getName().equals("mSelectionDivider")) {
                field.setAccessible(true);
                try {
                    field.set(picker, customDrawable);
                } catch (IllegalArgumentException | IllegalAccessException | Resources.NotFoundException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    /**
     * Configure a NumberPicker with the provided parameters.
     *
     * @param numPicker The NumberPicker input field to configure.
     * @param options The displayed options on the NumberPicker.
     * @param view The layout view.
     * @param pickerId The ID of the NumberPicker in the layout.
     * @return A configured NumberPicker.
     */
    static NumberPicker initilizeNumberPicker(NumberPicker numPicker, String[] options, View view, int pickerId) {
        numPicker = view.findViewById(pickerId);
        numPicker.setMinValue(0);
        numPicker.setMaxValue(options.length-1);
        numPicker.setDisplayedValues(options);
        numPicker.setDividerPadding(100);
        CardFragment.setDividerColor(numPicker, ContextCompat.getDrawable(view.getContext(),R.drawable.empty));
        return numPicker;
    }
}
