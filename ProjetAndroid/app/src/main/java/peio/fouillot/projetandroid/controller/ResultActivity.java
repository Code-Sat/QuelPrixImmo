package peio.fouillot.projetandroid.controller;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import peio.fouillot.projetandroid.R;
import peio.fouillot.projetandroid.model.UrlValueHolder;
import peio.fouillot.projetandroid.model.ResultAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    ResultAdapter mViewAdapter;

    ListView listViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Log.i("INFO", " Activity Result created.");

        listViewResult = findViewById(R.id.activity_result_list_view);

        this.configureToolbar();
        try {
            this.setListView();
        } catch (JSONException e) {
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

    private void setListView() throws JSONException {

        Intent intent = getIntent();
        UrlValueHolder valueHolder = (UrlValueHolder) intent.getSerializableExtra("result");
        String strJsonRes = valueHolder.getJsonHolder();
        JSONArray jsonArray = new JSONArray(strJsonRes);

        ArrayList<UrlValueHolder> resultArray = new ArrayList<UrlValueHolder>();

        Log.i("DEBUG", "Length of json array : " + jsonArray.length());

        //Json to ArrayList with some filter
        for (int i = 0; i < jsonArray.length(); i++) {

            UrlValueHolder resultHolder = new UrlValueHolder();
            JSONObject jsonObject = jsonArray.getJSONObject(i).getJSONObject("properties");

            if(valueHolder.getType_local().equals(jsonObject.getString("type_local"))) {

                resultHolder.setResultList0(jsonObject.optString("numero_voie") + " " + jsonObject.optString("type_voie") + " " + jsonObject.getString("voie") + ", " + jsonObject.getString("code_postal") + " " + jsonObject.getString("commune"));
                resultHolder.setResultList1(jsonObject.optString("type_local") + " " + jsonObject.getInt("nombre_pieces_principales") + " pièces de " + jsonObject.getInt("surface_relle_bati") + "m²");
                resultHolder.setResultList2("Vendu à " + jsonObject.getInt("valeur_fonciere") + " - " + jsonObject.getInt("valeur_fonciere")  / jsonObject.getInt("surface_relle_bati") + "€/m²");
                resultArray.add(resultHolder);
            }
        }

        Log.i("INFO", "ArrayList initialize done.");

        for (int i = 0; i < resultArray.size(); i++) {
            Log.i("DEBUG", "ArrayList2 content : " + resultArray.get(i).getResultList0() + "\n" + resultArray.get(i).getResultList1() + "\n" + resultArray.get(i).getResultList2() + "\n");
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