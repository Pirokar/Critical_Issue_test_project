package vladimir.ru.critical_issue_test_project.views.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import vladimir.ru.critical_issue_test_project.R;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Vladimir on 14.10.2016.
 * Presents map fragment
 */

public class MapFragment extends Fragment implements OnMapReadyCallback {
    TextView currentPositionText;
    LocationListener locationListener = null;

    LocationManager locationManager;
    GoogleMap googleMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        initViews(view);
        initLocationManager();
        ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);

        return view;
    }

    private void initViews(View view) {
        currentPositionText = (TextView)view.findViewById(R.id.current_position_text);
    }

    private void initLocationManager() {
        locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
    }

    private void setCurrentLocation(Location location) {
        currentPositionText.setText(getGeoCoordinates(location));
        LatLng target = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate camUpdate = CameraUpdateFactory.newLatLngZoom(target, 15F);
        googleMap.animateCamera(camUpdate);
    }

    @Override
    public void onResume() {

        // Invoke a parent method, at first
        super.onResume();

        // Create Location Listener object (if needed)
        if (locationListener == null) locationListener = new LocListener();

        // Setting up Location Listener
        // min time - 3 seconds
        // min distance - 1 meter
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000L, 1.0F, locationListener);
        } else {
            Toast.makeText(getContext(), getString(R.string.not_all_permissions_granted), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPause() {

        // Remove Location Listener
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && locationListener != null) {

            locationManager.removeUpdates(locationListener);
        }

        super.onPause();

    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap = map;
            googleMap.setMyLocationEnabled(true);

            //Show my location at map
            final Location location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            // If location available
            if (location != null) {
                setCurrentLocation(location);
            }
        } else {
            Toast.makeText(getContext(), getString(R.string.not_all_permissions_granted), Toast.LENGTH_LONG).show();
        }
    }

    @SuppressWarnings("all")
    private String getAddressByLoc(Location loc) {

        // Create geocoder
        final Geocoder geo = new Geocoder(getContext());

        // Try to get addresses list
        List<Address> list;
        try {
            list = geo.getFromLocation(loc.getLatitude(), loc.getLongitude(), 5);
        } catch (IOException e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }

        // If list is empty, return "No data" string
        if (list.isEmpty()) return getString(R.string.no_data);

        // Get first element from List
        Address a = list.get(0);

        // Get a Postal Code
        final int index = a.getMaxAddressLineIndex();
        String postal = null;
        if (index >= 0) {
            postal = a.getAddressLine(index);
        }

        // Make address string
        StringBuilder builder = new StringBuilder();
        final String sep = ", ";
        builder.append(postal).append(sep)
                .append(a.getCountryName()).append(sep)
                .append(a.getAdminArea()).append(sep)
                .append(a.getThoroughfare()).append(sep)
                .append(a.getSubThoroughfare());

        return builder.toString();
    }

    private String getGeoCoordinates(Location location) {
        String address = getString(R.string.address) + ": " + getAddressByLoc(location);
        String coordinates = getString(R.string.latitude) + ": " + location.getLatitude() + ", "
                + getString(R.string.longitude) + ": " + location.getLongitude();

        return address + "\n" + coordinates;
    }


    /**
     * Class that implements Location Listener interface
     * */
    private final class LocListener implements LocationListener {

        /**
         *  Called when the location has changed.
         * */
        @Override
        public void onLocationChanged(Location location) {
            setCurrentLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onProviderDisabled(String provider) {}
    }
}
