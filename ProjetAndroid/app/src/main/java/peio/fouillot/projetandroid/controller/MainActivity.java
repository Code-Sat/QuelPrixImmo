package peio.fouillot.projetandroid.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import peio.fouillot.projetandroid.R;
import peio.fouillot.projetandroid.model.UrlValueHolder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button searchBtn;
    private RadioGroup radioGrp;
    private RadioButton radioBtnSelected;
    private Slider sliderDistance;
    private RangeSlider rangeSliderRoom;
    private UrlValueHolder valueHolder;

    //test
    private TextView mTextView;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.text_view_result);
        mRequestQueue = Volley.newRequestQueue(this);

        this.configureToolbar();
        this.addListenerOnButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        //3 - Handle actions on menu items
        switch (item.getItemId()) {
            case R.id.menu_activity_main_params:
                Toast.makeText(this, "Il n'y a rien à voir ici, passez votre chemin...", Toast.LENGTH_SHORT).show();
                Log.i("INFO", "Item more settings");
                return true;
            case R.id.menu_activity_main_settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                this.startActivity(intent);
                Log.i("INFO", "Starting settings activity");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void configureToolbar() {
        // Get the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //Set the Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Rechercher un bien");
        // getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    //Add listener search and radio Button too
    private void addListenerOnButton() {
        //Serialise Button
        this.searchBtn = (Button) findViewById(R.id.activity_main_button_search);
        this.radioGrp = (RadioGroup) findViewById(R.id.activity_main_radioBtn_grp);
        this.sliderDistance = (Slider) findViewById(R.id.activity_main_slider_distance);
        this.rangeSliderRoom = (RangeSlider) findViewById(R.id.activity_main_slider_room_number);

        valueHolder = new UrlValueHolder();
        valueHolder.setValues(rangeSliderRoom.getValues());
        Log.i("TEST", "Value of values : " + rangeSliderRoom.getValues());

        //Set OnClick Listener on Button
        searchBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Get the selected radio button
                int idRadioBtn = radioGrp.getCheckedRadioButtonId();
                radioBtnSelected = (RadioButton) findViewById(idRadioBtn);

                //Set UrlValueHolder values for URL
                valueHolder.setType_local(radioBtnSelected.getText().toString());
                valueHolder.setLatitude(22);
                valueHolder.setLongitude(45);

                final String url2 = "http://api.cquest.org/dvf?lat=44.441&lon=0.322&dist=600";
                final String url = "http://api.cquest.org/dvf?lat=43.3040732&lon=-0.3570529&dist=50";

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    JSONArray jsonArray = response.getJSONArray("features");
                                    valueHolder.setJsonHolder(jsonArray.toString());

                                    //TODO get the first slider values about how many room(s)

                                    Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                                    intent.putExtra("result", valueHolder);
                                    startActivity(intent);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                mRequestQueue.add(request);
            }
        });

        sliderDistance.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                valueHolder.setDistance((int) sliderDistance.getValue());
                Log.i("TEST", String.valueOf(valueHolder.getDistance()));
            }
        });

        rangeSliderRoom.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
               valueHolder.setValues(rangeSliderRoom.getValues());
                Log.i("TEST", rangeSliderRoom.getValues().toString());
            }
        });
    }
}