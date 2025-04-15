package com.example.petcare;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class navhostfragment extends Fragment {

    public navhostfragment() {
        super(R.layout.fragment_navhostfragment); // binds your XML directly
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SmoothBottomBar bottomBar = view.findViewById(R.id.bottomBar);

        NavHostFragment navHostFragment =
                (NavHostFragment) getChildFragmentManager().findFragmentById(R.id.dashboard_nav_host);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();

            navController.navigate(R.id.dashBoardFragment);
            // Listen for bottomBar tab changes and navigate to destinations in the navGraph
            bottomBar.setOnItemSelectedListener((OnItemSelectedListener) pos -> {
                if (pos == 0) {
                    navController.navigate(R.id.dashBoardFragment);
                } else if (pos == 1) {
                    navController.navigate(R.id.consultationFragment);
                } else if (pos == 2) {
                    navController.navigate(R.id.communityFragment);
                } else if (pos == 3) {
                    navController.navigate(R.id.personProfileFragment);
                }
                return true;
            });
        }
    }
}
