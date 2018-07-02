package com.hackncs.click;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

    FloatingActionButton fab;
    private ShareActionProvider mShareActionProvider;
   static Menu menu;
    Menu nav_menu;
    String group;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Iconify.with(new FontAwesomeModule());
        fab = (FloatingActionButton) findViewById(R.id.fab);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;

        View headerView =  navigationView.getHeaderView(0);
        TextView name = (TextView)headerView.findViewById(R.id.tvFirstName);
        name.setText("Welcome, "+PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("com.hackncs.click.FIRST_NAME","User"));

        navigationView.setNavigationItemSelectedListener(this);

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
            MenuItem item=navigationView.getMenu().getItem(0);
            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
        }




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                try {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.flContent,CreateNotice.class.newInstance(),"create_notice").addToBackStack(null).commit();
                    MenuItem item=navigationView.getMenu().getItem(7);
                    item.setChecked(true);
                    showMenu(true);
                    getSupportActionBar().setTitle(item.getTitle());

                    fab.hide();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }


            }
        });

         nav_menu = navigationView.getMenu();
        MenuItem nav_notices = nav_menu.findItem(R.id.nav_notices);
        nav_notices.setIcon( new IconDrawable(this, FontAwesomeIcons.fa_thumb_tack)
                .colorRes(R.color.icon_color)
                .actionBarSize());
        MenuItem nav_administration = nav_menu.findItem(R.id.nav_administration);
        nav_administration.setIcon( new IconDrawable(this, FontAwesomeIcons.fa_university)
                .colorRes(R.color.icon_color)
                .actionBarSize());
        MenuItem nav_academics = nav_menu.findItem(R.id.nav_academics);
        nav_academics.setIcon( new IconDrawable(this, FontAwesomeIcons.fa_pencil_square_o)
                .colorRes(R.color.icon_color)
                .actionBarSize());
        MenuItem nav_placements = nav_menu.findItem(R.id.nav_placement);
        nav_placements.setIcon( new IconDrawable(this, FontAwesomeIcons.fa_calendar)
                .colorRes(R.color.icon_color)
                .actionBarSize());
        MenuItem nav_events = nav_menu.findItem(R.id.nav_events);
        nav_events.setIcon( new IconDrawable(this, FontAwesomeIcons.fa_graduation_cap)
                .colorRes(R.color.icon_color)
                .actionBarSize());
        MenuItem nav_starred = nav_menu.findItem(R.id.nav_starred);
        nav_starred.setIcon( new IconDrawable(this, FontAwesomeIcons.fa_star)
                .colorRes(R.color.icon_color)
                .actionBarSize());

        MenuItem nav_download = nav_menu.findItem(R.id.nav_download);
        nav_download.setIcon( new IconDrawable(this, FontAwesomeIcons.fa_download)
                .colorRes(R.color.icon_color)
                .actionBarSize());

        MenuItem nav_create = nav_menu.findItem(R.id.nav_create);
        nav_create.setIcon( new IconDrawable(this, FontAwesomeIcons.fa_plus)
                .colorRes(R.color.icon_color)
                .actionBarSize());

        MenuItem nav_myprofile = nav_menu.findItem(R.id.nav_myprofile);
        nav_myprofile.setIcon( new IconDrawable(this, FontAwesomeIcons.fa_user)
                .colorRes(R.color.icon_color)
                .actionBarSize());
        MenuItem nav_logout = nav_menu.findItem(R.id.nav_logout);
        nav_logout.setIcon( new IconDrawable(this, FontAwesomeIcons.fa_inbox)
                .colorRes(R.color.icon_color)
                .actionBarSize());
        group=PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("com.hackncs.click.GROUP","student");
        checkGroup(group);
    }

    //to remove

    private void startShareIntent(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        // sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            int size=getSupportFragmentManager().getFragments().size();

            String fragmentTag=getSupportFragmentManager().getFragments().get(size-1).getTag();
            Log.i("Fragment",fragmentTag);


            if(fragmentTag.equals("notices")){

                finishAffinity();
            }
            else{
                Fragment fragment = null;
                Class fragmentClass = null;
                fragmentClass = FragmentNotice.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();
                MenuItem item=navigationView.getMenu().getItem(0);
                item.setChecked(true);
                fab.show();
                showMenu(false);
                checkGroup(group);
                getSupportActionBar().setTitle(item.getTitle());
            }
        }
    }

    //to remove share lines
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        this.menu=menu;
        this.menu.getItem(0).setVisible(false);
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
        ///item.setChecked(true);
        String tag="";
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = FragmentNotice.class;

        if (id == R.id.nav_notices) {
            fragmentClass = FragmentNotice.class;
            showMenu(false);
            fab.show();
            checkGroup(group);
            tag="notices";

        } else if (id == R.id.nav_placement) {
            fragmentClass = FragmentPlacement.class;
            fab.show();
            showMenu(false);
            checkGroup(group);
            tag="placements";
        } else if (id == R.id.nav_administration) {
            fragmentClass = FragmentAdministration.class;
            fab.show();
            showMenu(false);
            checkGroup(group);
            tag="administration";
        } else if (id == R.id.nav_academics) {
            fragmentClass = FragmentAcademics.class;
            fab.show();
            showMenu(false);
            checkGroup(group);
            tag="academics";
        } else if (id == R.id.nav_events) {
            fragmentClass = FragmentEvents.class;
            fab.show();
            showMenu(false);
            checkGroup(group);
            tag="events";
        } else if (id == R.id.nav_download) {
            fragmentClass = Downloads.class;
            fab.show();
            showMenu(false);
            checkGroup(group);
            tag="download";
        } else if (id == R.id.nav_starred) {
            fragmentClass = FragmentStarredNotices.class;
            fab.show();
            showMenu(false);
            checkGroup(group);
            tag="starred";
        } else if (id == R.id.nav_myprofile) {
            String group=PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("com.hackncs.click.GROUP","");
            if (group.equals("student"))
                fragmentClass = FragmentStudentProfile.class;
            else
                fragmentClass = FragmentFacultyProfile.class;

            fab.show();
            showMenu(true);
            checkGroup(group);
            tag="profile";
        }else if (id == R.id.nav_logout) {
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().apply();
            new OfflineDatabaseHandler(this).flush();
            Intent intent = new Intent(MainActivity.this, Splash.class);
            startActivity(intent);
            finishAffinity();
        } else if (id == R.id.nav_create) {
            fragmentClass = CreateNotice.class;
            fab.hide();
            showMenu(true);
            checkGroup(group);
            tag="create_notice";
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment,tag).addToBackStack(null).commit();
        item.setChecked(true);
        getSupportActionBar().setTitle(item.getTitle());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onPause() {
        super.onPause();
       

    }


    private void showMenu(boolean show)
    {

        menu.getItem(0).setVisible(show);
    }
    private void checkGroup(String group)
    {

        if(group.equals("student"))
        {
            fab.hide();

             nav_menu.getItem(7).setVisible(false);
        }
    }


}
