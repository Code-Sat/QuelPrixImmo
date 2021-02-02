package peio.fouillot.projetandroid.controller;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import peio.fouillot.projetandroid.R;
import peio.fouillot.projetandroid.model.StringHolder;
import peio.fouillot.projetandroid.model.UrlValueHolder;
import peio.fouillot.projetandroid.model.ResultAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    ResultAdapter mViewAdapter;
    ListView listViewResult;
    TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Log.i("INFO", " Activity Result created.");

        listViewResult = findViewById(R.id.activity_result_list_view);
        titleTextView = findViewById(R.id.activity_result_title);

        this.configureToolbar();
        /*try {
            this.setListView();
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        try {
            testData();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void configureToolbar(){
        //Get the toolbar (Serialize)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //Set the toolbar
        setSupportActionBar(toolbar);
        //Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Résultat de recherche");
    }

    /*private void getDataStored() {

        Intent intent = getIntent();
        UrlValueHolder valueHolder = (UrlValueHolder) intent.getSerializableExtra("result");

        Writer output = null;
        String path = MainActivity.this.getFilesDir().getAbsolutePath();
        Log.i("SHOW", "PATH :  " + path);
        //File file = new File(path + "/dataFromApi.json");
        File file = new File("/storage/emulated/0/Android/data/peio.fouillot.projetandroid/");
        output = new BufferedWriter(new FileWriter(file));
        output.write(jsonArray.toString());
        output.close();

        Log.i("DATA", "Value holder " + valueHolder);
    }*/
    private void testData() throws IOException, JSONException {

        Intent intent = getIntent();
        UrlValueHolder valueHolder = (UrlValueHolder) intent.getSerializableExtra("result");

        FileInputStream fis = ResultActivity.this.openFileInput("dataFromApi.json");
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader bufferedReader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
        valueHolder.setJsonHolder(String.valueOf(sb));

        setListView(valueHolder);
        //Log.i("DATA", "Value holder " + valueHolder.getJsonHolder());
    }

    private void setListView(UrlValueHolder valueHolder) throws JSONException {

        //Get UrlValueHolder obj from main
        /*Intent intent = getIntent();
        UrlValueHolder valueHolder = (UrlValueHolder) intent.getSerializableExtra("result");*/
        String strJsonRes = valueHolder.getJsonHolder();
        JSONArray jsonArray = new JSONArray(strJsonRes);

        ArrayList<StringHolder> resultArray = new ArrayList<StringHolder>();

        Log.i("TEST", "Latitude : " + valueHolder.getLatitude());
        Log.i("TEST", "Longitude: " + valueHolder.getLongitude());
        Log.i("TEST", "Value Holder json : " + valueHolder.getJsonHolder());
        Log.i("TEST", "Value typelocal : " + valueHolder.getType_local());

        //Json to ArrayList with some filter
        for (int i = 0; i < jsonArray.length(); i++) {

            StringHolder stringHolder = new StringHolder();
            JSONObject jsonObject = jsonArray.getJSONObject(i).getJSONObject("properties");

            //1:Condition to get only selected type_local (house/apartment)
            if( valueHolder.getType_local().equals(jsonObject.optString("type_local"))) {

                float roomNumber = Float.valueOf(jsonObject.getString("nombre_pieces_principales"));

                //Filter with the numbers of room number selected between [1,7]
                if(roomNumber >= valueHolder.getValues().get(0) && roomNumber < valueHolder.getValues().get(1) ) {
                    stringHolder.setResultList0(jsonObject.optString("numero_voie") + " " + jsonObject.optString("type_voie") + " " + jsonObject.getString("voie") + ", " + jsonObject.getString("code_postal") + " " + jsonObject.getString("commune"));
                    stringHolder.setResultList1(jsonObject.optString("type_local") + " " + jsonObject.getInt("nombre_pieces_principales") + " pièces de " + jsonObject.getInt("surface_relle_bati") + "m²");
                    stringHolder.setResultList2("Vendu à " + jsonObject.getInt("valeur_fonciere") + " - " + jsonObject.getInt("valeur_fonciere") / jsonObject.getInt("surface_relle_bati") + "€/m²");
                    resultArray.add(stringHolder);

                }else if(valueHolder.getValues().get(1) == 8.0) {
                    stringHolder.setResultList0(jsonObject.optString("numero_voie") + " " + jsonObject.optString("type_voie") + " " + jsonObject.getString("voie") + ", " + jsonObject.getString("code_postal") + " " + jsonObject.getString("commune"));
                    stringHolder.setResultList1(jsonObject.optString("type_local") + " " + jsonObject.getInt("nombre_pieces_principales") + " pièces de " + jsonObject.getInt("surface_relle_bati") + "m²");
                    stringHolder.setResultList2("Vendu à " + jsonObject.getInt("valeur_fonciere") + " - " + jsonObject.getInt("valeur_fonciere") / jsonObject.getInt("surface_relle_bati") + "€/m²");
                    resultArray.add(stringHolder);
                }
            }
        }

        if(resultArray.size() < 2) titleTextView.setText(resultArray.size() + " bien dans un rayon de " + valueHolder.getDistance() + " mètres.");
        else titleTextView.setText(resultArray.size() + " biens dans un rayon de " + valueHolder.getDistance() + " mètres.");
        Log.i("INFO", "ArrayList initialize done.");

        for (int i = 0; i < resultArray.size(); i++) {
            Log.i("DEBUG", "ArrayList content : " + resultArray.get(i).getResultList0() + "\n" + resultArray.get(i).getResultList1() + "\n" + resultArray.get(i).getResultList2() + "\n");
        }

        if( mViewAdapter == null ) {
            mViewAdapter = new ResultAdapter( this, R.layout.result_row, resultArray);
            listViewResult.setAdapter(mViewAdapter);
        }
        else {
            mViewAdapter.clear();
            mViewAdapter.addAll(resultArray);
            mViewAdapter.notifyDataSetChanged();
        }
    }
}