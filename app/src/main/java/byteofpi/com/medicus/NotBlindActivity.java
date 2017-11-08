package byteofpi.com.medicus;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class NotBlindActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    CardView card1;
    CardView card2;
    CardView card3;
    private RecyclerView recyclerView;
    private List<earthquakes> earthquakesList;
    private earthquakeAdapter adapter;
    String string;
    String to_display;
    JSONArray earthquakesArray;
    JSONObject singleEarthquake;
    TextView textView;
    private ImageView drawerlayout_image;
    private FirebaseAuth mAuth;
    private static final String TAG="NOTBLINDACTIVITY";
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private boolean HAS_CALAMITIES=false;
    private TextView navigationlayoutname;
    private CoordinatorLayout FULL_SCREEN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_blind);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        FULL_SCREEN=(CoordinatorLayout)findViewById(R.id.FULL_SCREEN);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            public void onDrawerClosed(View view) {
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                FULL_SCREEN.setTranslationX(slideOffset * drawerView.getWidth());
                drawer.bringChildToFront(drawerView);
                drawer.requestLayout();
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        earthquakesList = new ArrayList<>();
        adapter = new earthquakeAdapter(this, earthquakesList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_not_blind);
        drawerlayout_image=(ImageView) headerView.findViewById(R.id.imageViewwwww);
        navigationlayoutname=(TextView)headerView.findViewById(R.id.textViewwww);
        mAuth = FirebaseAuth.getInstance();


        navigationView.setNavigationItemSelectedListener(this);
        if(mAuth.getCurrentUser().getPhotoUrl().toString()!=null) {
            Log.d(TAG, mAuth.getCurrentUser().getPhotoUrl().toString());
            Resources Resou=getResources();
            //drawerlayout_image.setImageDrawable(Resou.getDrawable(R.drawable.blank_image));
            new DownloadImageFromInternet(drawerlayout_image).execute(mAuth.getCurrentUser().getPhotoUrl().toString());
        }
        else{
            drawerlayout_image.setImageDrawable(getDrawable(R.drawable.blank_image));
            Log.d(TAG,"NULLL EXCEPTION OR NO IMAGE");
        }

        card1=(CardView)findViewById(R.id.card_view);
        card2=(CardView)findViewById(R.id.card_view2);
        card3=(CardView)findViewById(R.id.card_view3);
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                card1.setVisibility(View.GONE);
                card2.setVisibility(View.GONE);
                textView = (TextView) findViewById(R.id.info_text3);
                if(HAS_CALAMITIES==false){
                getcalamities gola=new getcalamities();
                gola.execute();}
                else{
                    Toast.makeText(getApplicationContext(),"THE CALAMITIES ARE DISPLAYED",Toast.LENGTH_SHORT).show();
                    recyclerView.setVisibility(View.VISIBLE);
                }

                //textView.setText(string);


            }
        });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent jii=new Intent(Intent.ACTION_DIAL);
                String posted_by = "+919903941294";
                String uri = "tel:" + posted_by.trim() ;
                jii.setData(Uri.parse(uri));
                startActivity(jii);
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(checkLocationPermission()){
                        //Intent ij=new Intent(NotBlindActivity.this,MapsActivity.class);
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?daddr=12.9248,79.1354"));
                        startActivity(intent);
                        //startActivity(ij);
                    }

                }
                else{
                    Toast.makeText(getApplicationContext(),"You are being redirected to the nearest hospital",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
            Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...", Toast.LENGTH_SHORT).show();
        }
        protected void onPreExecute(){
            findViewById(R.id.avi_layout).setVisibility(View.VISIBLE);
            navigationlayoutname.setText(mAuth.getCurrentUser().getDisplayName());
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Log.d(TAG,imageURL);
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
            findViewById(R.id.avi_layout).setVisibility(View.GONE);
        }
    }




    public class getcalamities extends AsyncTask<Void, Void, Void> {
        ProgressDialog mProgress;
        @Override
        protected void onPreExecute(){
            findViewById(R.id.avi_layout).setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            JSoup hello=new JSoup();
            try {
                string= hello.main();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            earthquakes one_earthquake=new earthquakes("hello","ge","geg","geg","geg");
            recyclerView.setVisibility(View.VISIBLE);
            try {
                earthquakesArray=new JSONArray(string);
                Log.d("HELLO",string);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for(int i=0;i<earthquakesArray.length();i++){
                try {
                    singleEarthquake=earthquakesArray.getJSONObject(i);
                    to_display=to_display+"Earthquake id is:  "+singleEarthquake.getInt("earthquakeid")+"\n";
                    one_earthquake=new earthquakes(singleEarthquake.getString("region"),
                            singleEarthquake.getString("datetime"),
                            singleEarthquake.getString("magnitude"),
                            singleEarthquake.getString("latitude"),
                            singleEarthquake.getString("longitude"));
                    earthquakesList.add(one_earthquake);
                    adapter.notifyDataSetChanged();
                    //region,datetime,magnitude,latitude,longitude
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //earthquakesList.clear();
            //textView.setText(to_display);
            //to_display="";
            findViewById(R.id.avi_layout).setVisibility(View.GONE);
            HAS_CALAMITIES=true;

        }
    }
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(check_visbility()){
            card1.setVisibility(View.VISIBLE);
            card2.setVisibility(View.VISIBLE);
            card3.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        else{
            super.onBackPressed();
        }
    }
    public boolean check_visbility(){
        if(card1.getVisibility()==View.VISIBLE && card2.getVisibility()==View.VISIBLE && card3.getVisibility()==View.VISIBLE && recyclerView.getVisibility()==View.GONE){
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.not_blind, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            card1.setVisibility(View.GONE);
            card2.setVisibility(View.VISIBLE);
            card3.setVisibility(View.GONE);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            card1.setVisibility(View.VISIBLE);
            card2.setVisibility(View.VISIBLE);
            card3.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
