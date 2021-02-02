package peio.fouillot.projetandroid.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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
import com.google.android.material.navigation.NavigationView;
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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Button searchBtn;
    private RadioGroup radioGrp;
    private RadioButton radioBtnSelected;
    private Slider sliderDistance;
    private RangeSlider rangeSliderRoom;
    private UrlValueHolder valueHolder;

    //FOR DRAWER DESIGN
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

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
        this.configureDrawerLayout();
        this.configureNavigationView();
        Log.i("TEST", " Configuration done.");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.activity_main_drawer_graph :
                Log.i("TEST", " activity_main_drawer_graph ! ");
                break;
            case R.id.activity_main_drawer_settings :
                Log.i("TEST", " activity_main_drawer_settings ! ");
                launchSettingsActivity();
                break;
            case R.id.activity_main_drawer_layout :
                Log.i("TEST", " activity_main_drawer_layout ! ");
                break;
            default:
                break;
        }
        this.drawerLayout.closeDrawer(GravityCompat.START);
        Log.i("TEST", " onNavigationItemSelected ! ");

        return true;
    }

    //Close the Drawer nav if back button is pressed
    @Override
    public void onBackPressed() {
        if(this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void configureToolbar() {
        // Get the toolbar view inside the activity layout
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        //Set the Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Rechercher un bien");
        // getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void configureDrawerLayout() {
        this.drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        Log.i("TEST", " syncState ! ");
    }
    private void configureNavigationView() {
        this.navigationView = (NavigationView) findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

    private void launchGraphActivity() {

    }
    private void launchSettingsActivity() {
        Log.i("INFO", "Starting settings activity.");
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        this.startActivity(intent);
    }
}