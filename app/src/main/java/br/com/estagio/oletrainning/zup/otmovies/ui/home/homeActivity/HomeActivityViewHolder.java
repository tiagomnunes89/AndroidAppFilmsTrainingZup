package br.com.estagio.oletrainning.zup.otmovies.ui.home.homeActivity;

import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.estagio.oletrainning.zup.otmovies.R;

public class HomeActivityViewHolder {

    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    TextView textView_navView_email;
    TextView textView_navView_name;
    TextView titleToobar;
    LinearLayout logoutButton;

    HomeActivityViewHolder(View view) {

        toolbar = view.findViewById(R.id.toolbar);
        navigationView = view.findViewById(R.id.nav_view);
        drawerLayout = view.findViewById(R.id.drawer_layout);
        CoordinatorLayout homeDrawer = view.findViewById(R.id.home_drawer);
        bottomNavigationView = homeDrawer.findViewById(R.id.bottom_navigation);
        View headerView = navigationView.getHeaderView(0);
        textView_navView_email = headerView.findViewById(R.id.textview_nav_email);
        textView_navView_name = headerView.findViewById(R.id.textview_nav_name);
        logoutButton = headerView.findViewById(R.id.linearLayout_logout);
        titleToobar = homeDrawer.findViewById(R.id.textHomeTitle);
    }
}
