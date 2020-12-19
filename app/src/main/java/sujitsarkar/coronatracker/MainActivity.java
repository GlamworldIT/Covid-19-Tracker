package sujitsarkar.coronatracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView tvCases,tvRecovered,tvActive,tvCritical,tvDeath,tvTodayCases,tvTodayDeath,tvTodayRecovered,tvTests,tvAffectedCountries,todayDate,
    active_case_TV,recovered_TV,death_TV;
    SimpleArcLoader simpleArcLoader, pieLoader;
    ScrollView scrollView;
    PieChart pieChart;
    private int activeCase,recoveredCase,deathCase,totalCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCases = findViewById(R.id.tvCases);
        tvRecovered = findViewById(R.id.tvRecovered);
        tvActive = findViewById(R.id.tvActive);
        tvCritical = findViewById(R.id.tvCritical);
        tvDeath = findViewById(R.id.tvDeath);
        tvTodayCases = findViewById(R.id.tvTodayCases);
        tvTodayDeath = findViewById(R.id.tvTodayDeath);
        tvTodayRecovered = findViewById(R.id.tvTodayRecovered);
        tvTests = findViewById(R.id.tvTests);
        tvAffectedCountries = findViewById(R.id.tvAffectedCountries);
        todayDate = findViewById(R.id.todayDate);

        active_case_TV = findViewById(R.id.active_case_TV);
        recovered_TV = findViewById(R.id.recovered_TV);
        death_TV = findViewById(R.id.death_TV);

        simpleArcLoader = findViewById(R.id.loader);
        pieLoader = findViewById(R.id.pieLoader);
        scrollView = findViewById(R.id.scrollStats);
        pieChart = findViewById(R.id.pieChart);


        fetchData();
    }


    private void fetchData() {
        final String date;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        date = currentDate.format(calendar.getTime());

        String url = "https://disease.sh/v3/covid-19/all";
        simpleArcLoader.start();
        pieLoader.start();

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            tvCases.setText(jsonObject.getString("cases"));
                            tvRecovered.setText(jsonObject.getString("recovered"));
                            tvActive.setText(jsonObject.getString("active"));
                            tvCritical.setText(jsonObject.getString("critical"));
                            tvDeath.setText(jsonObject.getString("deaths"));
                            tvTodayCases.setText(jsonObject.getString("todayCases"));
                            tvTodayRecovered.setText(jsonObject.getString("todayRecovered"));
                            tvTodayDeath.setText(jsonObject.getString("todayDeaths"));
                            tvTests.setText(jsonObject.getString("tests"));
                            tvAffectedCountries.setText(jsonObject.getString("affectedCountries"));
                            todayDate.setText(date);

                            totalCase = Integer.parseInt(tvCases.getText().toString());
                            activeCase = Integer.parseInt(tvActive.getText().toString());
                            recoveredCase = Integer.parseInt(tvRecovered.getText().toString());
                            deathCase = Integer.parseInt(tvDeath.getText().toString());

                            active_case_TV.setText("Active Cases "+(activeCase*100)/totalCase+"%");
                            recovered_TV.setText("Recovered "+(recoveredCase*100)/totalCase+"%");
                            death_TV.setText("Deaths "+(deathCase*100)/totalCase+"%");


                            pieChart.addPieSlice(new PieModel("Cases",totalCase, Color.parseColor("#FFA726")));
                            pieChart.addPieSlice(new PieModel("Active",activeCase, Color.parseColor("#29B6F6")));
                            pieChart.addPieSlice(new PieModel("Recovered",recoveredCase, Color.parseColor("#64dd17")));
                            pieChart.addPieSlice(new PieModel("Deaths",deathCase, Color.parseColor("#EF5350")));

                            pieLoader.stop();
                            pieLoader.setVisibility(View.GONE);
                            pieChart.setVisibility(View.VISIBLE);
                            pieChart.startAnimation();

                            simpleArcLoader.stop();
                            simpleArcLoader.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            pieLoader.stop();
                            pieLoader.setVisibility(View.GONE);
                            pieChart.setVisibility(View.VISIBLE);

                            simpleArcLoader.stop();
                            simpleArcLoader.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pieLoader.stop();
                        pieLoader.setVisibility(View.GONE);
                        pieChart.setVisibility(View.VISIBLE);

                        simpleArcLoader.stop();
                        simpleArcLoader.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
        requestQueue.getCache().clear();

    }

    public void goTrackCountries(View view) {
        startActivity(new Intent(MainActivity.this,AffectedCountries.class));
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Do you want to exit?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

}