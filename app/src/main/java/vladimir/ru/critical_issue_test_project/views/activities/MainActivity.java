package vladimir.ru.critical_issue_test_project.views.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

import vladimir.ru.critical_issue_test_project.R;
import vladimir.ru.critical_issue_test_project.helpers.LocaleHelper;
import vladimir.ru.critical_issue_test_project.model.events.AddElementEvent;
import vladimir.ru.critical_issue_test_project.model.events.ItemListChangedEvent;
import vladimir.ru.critical_issue_test_project.utils.BusProvider;
import vladimir.ru.critical_issue_test_project.views.fragments.ListFragment;
import vladimir.ru.critical_issue_test_project.views.fragments.MapFragment;
import vladimir.ru.critical_issue_test_project.views.fragments.ScalingFragment;
import vladimir.ru.critical_issue_test_project.views.fragments.ServiceFragment;
import vladimir.ru.critical_issue_test_project.views.fragments.dialogs.AboutDialog;

public class MainActivity extends AppCompatActivity {
    private final static String CURRENT_PAGE_KEY = "current_page_key";
    private final int PERMISSIONS_REQUEST_INT = 1234;
    private final long LIST_DRAWER_ITEM_ID = 1L;
    private final long SCALING_DRAWER_ITEM_ID = 2L;
    private final long SERVICE_DRAWER_ITEM_ID = 3L;
    private final long MAP_DRAWER_ITEM_ID = 4L;

    PrimaryDrawerItem listDrawerItem = new PrimaryDrawerItem().withIdentifier(LIST_DRAWER_ITEM_ID).withName(R.string.list);
    PrimaryDrawerItem scalingDrawerItem = new PrimaryDrawerItem().withIdentifier(SCALING_DRAWER_ITEM_ID).withName(R.string.scaling);
    PrimaryDrawerItem serviceDrawerItem = new PrimaryDrawerItem().withIdentifier(SERVICE_DRAWER_ITEM_ID).withName(R.string.service);
    PrimaryDrawerItem mapDrawerItem = new PrimaryDrawerItem().withIdentifier(MAP_DRAWER_ITEM_ID).withName(R.string.map);

    Toolbar toolbar;
    Drawer drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initDrawer();
        setToolbarMenu();
        setStatusBarColor();
        setInitialToolbarText();
        checkPermissions();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setCurrentPageFromSettings();
    }

    private void initViews() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
    }

    private void initDrawer() {
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        listDrawerItem,
                        scalingDrawerItem,
                        serviceDrawerItem,
                        mapDrawerItem
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.getIdentifier() == LIST_DRAWER_ITEM_ID) {
                            toolbar.setTitle(getString(R.string.list));
                            toolbar.getMenu().findItem(R.id.add_item).setVisible(true);
                            openFragment(new ListFragment());
                        } else if(drawerItem.getIdentifier() == SCALING_DRAWER_ITEM_ID) {
                            toolbar.setTitle(getString(R.string.scaling));
                            toolbar.getMenu().findItem(R.id.add_item).setVisible(false);
                            openFragment(new ScalingFragment());
                        } else if(drawerItem.getIdentifier() == SERVICE_DRAWER_ITEM_ID) {
                            toolbar.setTitle(getString(R.string.service));
                            toolbar.getMenu().findItem(R.id.add_item).setVisible(false);
                            openFragment(new ServiceFragment());
                        } else if(drawerItem.getIdentifier() == MAP_DRAWER_ITEM_ID) {
                            toolbar.setTitle(getString(R.string.map));
                            toolbar.getMenu().findItem(R.id.add_item).setVisible(false);
                            openFragment(new MapFragment());
                        }

                        setCurrentPageToSettings(drawerItem.getIdentifier());
                        return false;
                    }
                }).build();
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    private void setCurrentPageToSettings(long pageId) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putLong(CURRENT_PAGE_KEY, pageId);
        editor.apply();
    }

    private void setCurrentPageFromSettings() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final long id = preferences.getLong(CURRENT_PAGE_KEY, LIST_DRAWER_ITEM_ID);
        drawer.setSelection(id);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(id == LIST_DRAWER_ITEM_ID) {
                    toolbar.setTitle(getString(R.string.list));
                } else if(id == SCALING_DRAWER_ITEM_ID) {
                    toolbar.setTitle(getString(R.string.scaling));
                } else if(id == SERVICE_DRAWER_ITEM_ID) {
                    toolbar.setTitle(getString(R.string.service));
                } else if(id == MAP_DRAWER_ITEM_ID) {
                    toolbar.setTitle(getString(R.string.map));
                }
            }
        }, 100);

    }

    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
        }
    }


    private void setInitialToolbarText() {
        toolbar.setTitle(getString(R.string.list));
    }

    private void setToolbarMenu() {
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onMenuPressed(item);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return onMenuPressed(item);
    }

    private boolean onMenuPressed(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.english_menu_item: {
                LocaleHelper.setLocale(MainActivity.this, "en");
                recreate();
                return true;
            }
            case R.id.russian_menu_item: {
                LocaleHelper.setLocale(MainActivity.this, "ru");
                recreate();
                return true;
            }
            case R.id.add_item: {
                BusProvider.getUIBusInstance().post(new AddElementEvent(this));
                return true;
            }
            case R.id.about: {
                AboutDialog.showDialog(getFragmentManager());
                return true;
            }
            default: {
                return false;
            }
        }
    }

    private void checkPermissions() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, getString(R.string.not_all_permissions_granted), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ListFragment.ADD_ELEMENT_TO_LIST_LAUNCH_CODE && resultCode == RESULT_OK && data != null) {
            final int elementPosition = data.getIntExtra(AddEditElementActivity.ELEMENT_POSITION_KEY, 0);
            final String text = data.getStringExtra(AddEditElementActivity.TEXT_RESULT_KEY);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    BusProvider.getUIBusInstance().post(new ItemListChangedEvent(elementPosition, text));
                }
            }, 100);
        }
    }
}
