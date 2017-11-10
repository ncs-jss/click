package com.hackncs.click;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import java.lang.reflect.Field;
import android.view.View;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentNotice.OnFragmentInteractionListener,
        FragmentPlacement.OnFragmentInteractionListener {

    /*private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };*/

    private ShareActionProvider mShareActionProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Iconify.with(new FontAwesomeModule());
//        setSupportActionBar(toolbar);
//        verifyStoragePermissions(this);
        if (savedInstanceState == null) {
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = FragmentNotice.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;

        View headerView =  navigationView.getHeaderView(0);
        TextView name = (TextView)headerView.findViewById(R.id.tvFirstName);
        name.setText("Welcome, "+PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("com.hackncs.click.FIRST_NAME","User"));

        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        MenuItem nav_notices = menu.findItem(R.id.nav_notices);
        nav_notices.setIcon( new IconDrawable(this, FontAwesomeIcons.fa_thumb_tack)
                .colorRes(R.color.icon_color)
                .actionBarSize());
        MenuItem nav_administration = menu.findItem(R.id.nav_administration);
        nav_administration.setIcon( new IconDrawable(this, FontAwesomeIcons.fa_university)
                .colorRes(R.color.icon_color)
                .actionBarSize());
        MenuItem nav_academics = menu.findItem(R.id.nav_academics);
        nav_academics.setIcon( new IconDrawable(this, FontAwesomeIcons.fa_pencil_square_o)
                .colorRes(R.color.icon_color)
                .actionBarSize());
        MenuItem nav_placements = menu.findItem(R.id.nav_placement);
        nav_placements.setIcon( new IconDrawable(this, FontAwesomeIcons.fa_calendar)
                .colorRes(R.color.icon_color)
                .actionBarSize());
        MenuItem nav_events = menu.findItem(R.id.nav_events);
        nav_events.setIcon( new IconDrawable(this, FontAwesomeIcons.fa_graduation_cap)
                .colorRes(R.color.icon_color)
                .actionBarSize());
        MenuItem nav_starred = menu.findItem(R.id.nav_starred);
        nav_starred.setIcon( new IconDrawable(this, FontAwesomeIcons.fa_star)
                .colorRes(R.color.icon_color)
                .actionBarSize());

        MenuItem nav_download = menu.findItem(R.id.nav_download);
        nav_download.setIcon( new IconDrawable(this, FontAwesomeIcons.fa_download)
                .colorRes(R.color.icon_color)
                .actionBarSize());

        MenuItem nav_create = menu.findItem(R.id.nav_create);
        nav_create.setIcon( new IconDrawable(this, FontAwesomeIcons.fa_plus)
                .colorRes(R.color.icon_color)
                .actionBarSize());

        MenuItem nav_myprofile = menu.findItem(R.id.nav_myprofile);
        nav_myprofile.setIcon( new IconDrawable(this, FontAwesomeIcons.fa_user)
                .colorRes(R.color.icon_color)
                .actionBarSize());
        MenuItem nav_logout = menu.findItem(R.id.nav_logout);
        nav_logout.setIcon( new IconDrawable(this, FontAwesomeIcons.fa_inbox)
                .colorRes(R.color.icon_color)
                .actionBarSize());
        
    }

    //to remove

    private void startShareIntent(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        // sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //to remove share lines
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == R.id.menu_item_share) {
            startShareIntent();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = FragmentNotice.class;
        /*try {
            Field field = fragmentClass.getDeclaredField("Category");
            field.setAccessible(true);
            switch(id){
                case R.id.nav_notices: field.set(fragmentClass,"misc");
                break;
                case R.id.nav_events: field.set(fragmentClass,"events");
                break;
                case R.id.nav_download: field.set(fragmentClass,"downloads");
                break;
                case R.id.nav_placement: field.set(fragmentClass,"tnp");
                break;
                case R.id.nav_administration: field.set(fragmentClass,"administration");
                break;
                case R.id.nav_academics: field.set(fragmentClass,"academics");
                break;

            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }*/

        if (id == R.id.nav_notices) {
            fragmentClass = FragmentNotice.class;
        } else if (id == R.id.nav_placement) {
            fragmentClass = FragmentPlacement.class;
        } else if (id == R.id.nav_administration) {
            fragmentClass = FragmentAdministration.class;
        } else if (id == R.id.nav_academics) {
            fragmentClass = FragmentAcademics.class;
        } else if (id == R.id.nav_events) {
            fragmentClass = FragmentEvents.class;
        } else if (id == R.id.nav_download) {
            fragmentClass = Downloads.class;
        } else if (id == R.id.nav_starred) {
            fragmentClass = FragmentStarredNotices.class;
        } else if (id == R.id.nav_myprofile) {
            if (PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("com.hackncs.click.GROUP","").equals("student"))
                fragmentClass = FragmentStudentProfile.class;
            else if (PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("com.hackncs.click.GROUP","").equals("faculty"))
                fragmentClass = FragmentFacultyProfile.class;
        }else if (id == R.id.nav_logout) {
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().apply();
            new OfflineDatabaseHandler(this).flush();
            Intent intent = new Intent(MainActivity.this, Splash.class);
            startActivity(intent);
            fragmentClass = FragmentNotice.class;
        } else if (id == R.id.nav_create)
            fragmentClass = CreateNotice.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
           // Field field = fragment.
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param //activity
     */
    /*public static void verifyStoragePermissions(Activity activity) {
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
*/
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
