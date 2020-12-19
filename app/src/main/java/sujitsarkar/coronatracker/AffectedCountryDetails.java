package sujitsarkar.coronatracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AffectedCountryDetails extends AppCompatActivity {

    TextView detailsCountryName,tvTodayCases,tvTodayDeath,tvTodayRecovered,tvCritical,tvTotalCases,tvTotalRecovered,tvTotalDeath,tvActiveCases,tvTotalTest,
            todayDate;
    ImageView detailsCountryImage;

    private int countryPosition;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.affected_country_details_activity);

        countryPosition = getIntent().getIntExtra("position",0);

        //Change ActionBar Title and add back button...
        getSupportActionBar().setTitle("Details of "+ AffectedCountries.countryModelList.get(countryPosition).getCountry());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        detailsCountryName = findViewById(R.id.detailsCountryName);
        tvTodayCases = findViewById(R.id.tvTodayCases);
        tvTodayDeath = findViewById(R.id.tvTodayDeath);
        tvTodayRecovered = findViewById(R.id.tvTodayRecovered);
        tvCritical = findViewById(R.id.tvCritical);
        tvTotalCases = findViewById(R.id.tvTotalCases);
        tvTotalRecovered = findViewById(R.id.tvTotalRecovered);
        tvTotalDeath = findViewById(R.id.tvTotalDeath);
        tvActiveCases = findViewById(R.id.tvActiveCases);
        tvTotalTest = findViewById(R.id.tvTotalTest);
        todayDate = findViewById(R.id.todayDate);
        detailsCountryImage = findViewById(R.id.detailsCountryImage);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        setDataToFields();

    }

    //If User press back button then goes to previous activity...
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void setDataToFields() {
        final String date;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        date = currentDate.format(calendar.getTime());
        todayDate.setText(date);

        detailsCountryName.setText(AffectedCountries.countryModelList.get(countryPosition).getCountry());
        Glide.with(this).load(AffectedCountries.countryModelList.get(countryPosition).getFlag()).into(detailsCountryImage);

        detailsCountryName.setText(AffectedCountries.countryModelList.get(countryPosition).getCountry());
        tvTodayCases.setText(AffectedCountries.countryModelList.get(countryPosition).getTodayCases());
        tvTodayDeath.setText(AffectedCountries.countryModelList.get(countryPosition).getTodayDeaths());
        tvTodayRecovered.setText(AffectedCountries.countryModelList.get(countryPosition).getTodayRecovered());
        tvCritical.setText(AffectedCountries.countryModelList.get(countryPosition).getCritical());
        tvTotalCases.setText(AffectedCountries.countryModelList.get(countryPosition).getCases());
        tvTotalRecovered.setText(AffectedCountries.countryModelList.get(countryPosition).getRecovered());
        tvTotalDeath.setText(AffectedCountries.countryModelList.get(countryPosition).getDeaths());
        tvActiveCases.setText(AffectedCountries.countryModelList.get(countryPosition).getActive());
        tvTotalTest.setText(AffectedCountries.countryModelList.get(countryPosition).getTests());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

}