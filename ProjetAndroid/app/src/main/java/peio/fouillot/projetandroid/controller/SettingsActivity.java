package peio.fouillot.projetandroid.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import peio.fouillot.projetandroid.R;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    Slider sliderDistance;
    RangeSlider rangeSliderRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.configureToolbar();
        this.addListener();
    }

    private void addListener() {
        rangeSliderRoom = findViewById(R.id.activity_settings_slider_room_number);
        sliderDistance = findViewById(R.id.activity_settings_slider_distance);

        //TO CUSTOMIZE THE LAST LABEL OF THE RANGESLIDER
        rangeSliderRoom.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                //It is just an example
                if (value == 8.0f)
                    return "Illimité";
                return String.format(Locale.FRENCH, "%.0f", value);
            }
        });

        rangeSliderRoom.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                //TODO Save on SharePreferences data
                Log.i("TEST", rangeSliderRoom.getValues().toString());
            }
        });

        Log.i("TEST", "Value of values : " + rangeSliderRoom.getValues());

        sliderDistance.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                //TODO Save on SharePreferences data
                Log.i("TEST", String.valueOf(sliderDistance.getValue()));
            }
        });
    }

    private void configureToolbar(){
        //Get the toolbar (Serialise)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //Set the toolbar
        setSupportActionBar(toolbar);
        //Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Paramètres de l'application");
    }

}