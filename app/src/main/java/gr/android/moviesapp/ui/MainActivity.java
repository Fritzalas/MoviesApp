package gr.android.moviesapp.ui;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import dagger.hilt.android.AndroidEntryPoint;
import gr.android.moviesapp.common.ConnectivityReceiver;
import gr.android.moviesapp.R;
import gr.android.moviesapp.databinding.ActivityMainBinding;


@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private final ConnectivityReceiver connectivityReceiver = new ConnectivityReceiver();
    private boolean isConnected = true; // Track the previous state

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gr.android.moviesapp.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up the NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        if (navHostFragment != null) {
            navHostFragment.getNavController();
        }

        // Register the connectivity receiver listener
        ConnectivityReceiver.connectivityReceiverListener = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (this.isConnected != isConnected) { // Only handle state change
            this.isConnected = isConnected;
            if (!isConnected) {
                Toast.makeText(this, "Internet connection lost", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Internet connection restored", Toast.LENGTH_LONG).show();
                reloadAllFragments();
            }
        }
    }
    private void reloadAllFragments() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            int startDestinationId = navController.getGraph().getStartDestinationId();

            navController.popBackStack(startDestinationId, false); // Navigate back to the root of the navigation graph
            navController.navigate(startDestinationId); // Re-navigate to the starting destination
        }
    }
}