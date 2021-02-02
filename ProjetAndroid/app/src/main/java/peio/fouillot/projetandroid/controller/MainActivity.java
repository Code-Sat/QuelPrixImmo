package peio.fouillot.projetandroid.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import peio.fouillot.projetandroid.R;
import peio.fouillot.projetandroid.model.UrlValueHolder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

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

    //LOCATION VAR
    private LocationManager locationManager;

    //CONNECTION URL
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        mRequestQueue = Volley.newRequestQueue(this);
        verifyStoragePermissions(MainActivity.this);

        this.configureToolbar();
        this.addListenerOnButton();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.checkPreferences();
        Log.i("TEST", " Configuration done.");
    }

    //FOR READ // WRITE FILE

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    //FOR LOCATION -------------------------------------------------

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }
    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        //Change the output of long and lat and store in a obj
        DecimalFormat inputFormat = new DecimalFormat("#.##");
        valueHolder.setLatitude(Float.parseFloat(inputFormat.format(location.getLatitude())));
        valueHolder.setLongitude(Float.parseFloat(inputFormat.format(location.getLongitude()) ) );
    }


    //END LOCATION METHOD --------------------------------------------
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.activity_main_drawer_graph:
                Log.i("TEST", " activity_main_drawer_graph ! ");
                break;
            case R.id.activity_main_drawer_settings:
                Log.i("TEST", " activity_main_drawer_settings ! ");
                launchSettingsActivity();
                break;
            case R.id.activity_main_drawer_layout:
                Log.i("TEST", " activity_main_drawer_layout ! ");
                break;
            default:
                break;
        }
        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    //Close the Drawer nav if back button is pressed

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void addListenerOnButton() {

        this.searchBtn = (Button) findViewById(R.id.activity_main_button_search);
        this.radioGrp = (RadioGroup) findViewById(R.id.activity_main_radioBtn_grp);
        this.sliderDistance = (Slider) findViewById(R.id.activity_main_slider_distance);
        this.rangeSliderRoom = (RangeSlider) findViewById(R.id.activity_main_slider_room_number);

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

        valueHolder = new UrlValueHolder();
        valueHolder.setValues(rangeSliderRoom.getValues());
        Log.i("TEST", "Value of values : " + rangeSliderRoom.getValues());

        //Button rechercher onClick event
        searchBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                int idRadioBtn = radioGrp.getCheckedRadioButtonId();
                radioBtnSelected = (RadioButton) findViewById(idRadioBtn);

                //Set UrlValueHolder values for URL
                valueHolder.setType_local(radioBtnSelected.getText().toString());

                String url = valueHolder.getFinalUrl(); /*"http://api.cquest.org/dvf?lat=44.441&lon=0.322&dist=600;*/

                Log.i("INFO", "URL : " +  valueHolder.getFinalUrl());

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    JSONArray jsonArray = response.getJSONArray("features");
                                    //JSONObject jsonObject = response.getJSONObject("features");
                                    //valueHolder.setJsonHolder(jsonArray.toString());

                                    //Create Json File from the Url
                                    Writer output = null;
                                    String path = MainActivity.this.getFilesDir().getAbsolutePath();
                                    Log.i("SHOW", "PATH :  " + path);
                                    //File file = new File(path + "/dataFromApi.json");
                                    File file = new File(path + "/dataFromApi.json");
                                    output = new BufferedWriter(new FileWriter(file));
                                    output.write(jsonArray.toString());
                                    output.close();
                                    valueHolder.setJsonDataPath(path);
                                    Toast.makeText(getApplicationContext(), "Data saved", Toast.LENGTH_LONG).show();

                                    //Send valueHolder to the next activity
                                    Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                                    intent.putExtra("result", valueHolder);
                                    startActivity(intent);

                                } catch (JSONException | IOException e) {
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

        radioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkedBtn = (RadioButton) radioGroup.findViewById(i);
                boolean isChecked = checkedBtn.isChecked();
                for (int index = 0; index < radioGroup.getChildCount(); index ++) {
                    RadioButton aux;
                    aux = (RadioButton) radioGroup.getChildAt(index);
                    aux.setTypeface(null,Typeface.NORMAL);
                }
                if(isChecked) checkedBtn.setTypeface(null,Typeface.BOLD);
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
    }

    private void configureNavigationView() {
        this.navigationView = (NavigationView) findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void launchSettingsActivity() {
        Log.i("INFO", "Starting settings activity.");
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        this.startActivity(intent);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    private void checkPreferences() {
        Slider sliderDist = findViewById(R.id.activity_main_slider_distance);
        sliderDist.setValue(500);
    }
}