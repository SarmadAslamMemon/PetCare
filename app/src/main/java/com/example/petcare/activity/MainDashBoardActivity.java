package com.example.petcare.activity;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.petcare.R;
import com.example.petcare.fragments.ConsultationFragment;
import com.example.petcare.fragments.DashBoardFragment;
import com.example.petcare.fragments.PersonProfileFragment;
import com.example.petcare.fragments.consultion.CommunityFragment;
import com.example.petcare.navhostfragment;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainDashBoardActivity extends AppCompatActivity {

    private SmoothBottomBar bottomBar;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dash_board);

        fragmentManager= getSupportFragmentManager();
        loadFragment(new navhostfragment()); // Load default fragment
    }

    private void loadFragment(Fragment fragment) {

        fragmentManager.beginTransaction()
                .replace(R.id.frameLayoutMainDashBoard, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {

        Fragment currentFragment = fragmentManager.findFragmentById(R.id.frameLayoutMainDashBoard);
        if (!(currentFragment instanceof DashBoardFragment)) {
            fragmentManager.beginTransaction()
                    .replace(R.id.frameLayoutMainDashBoard, new DashBoardFragment())
                    .addToBackStack(null)
                    .commit();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Exit Application")
                    .setMessage("Do you really want to quit?")
                    .setPositiveButton("Yes", (dialog, which) -> finish())
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        }
    }
}
