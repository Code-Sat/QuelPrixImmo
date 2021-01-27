package peio.fouillot.projetandroid.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import peio.fouillot.projetandroid.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.configureToolbar();
        this.configureButtonSearch();
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

    private void configureToolbar(){
        // Get the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //Set the Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Rechercher un bien");
       // getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    private void configureButtonSearch(){
        //Serialise Button
        this.searchBtn = (Button) findViewById(R.id.activity_main_button_search);
        //Set OnClick Listener on Button
        searchBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                launchStatisticActivity();
            }
        });
    }

    private void launchStatisticActivity(){
        Intent intent = new Intent(MainActivity.this, StatisticActivity.class);
        this.startActivity(intent);
    }
}