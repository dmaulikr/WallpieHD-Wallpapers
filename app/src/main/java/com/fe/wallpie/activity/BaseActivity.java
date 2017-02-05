package com.fe.wallpie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.fe.wallpie.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import butterknife.BindView;

/**
 * Created by Farmaan-PC on 03-02-2017.
 */

public class BaseActivity extends AppCompatActivity implements
        MaterialSearchView.SearchViewListener,
        MaterialSearchView.OnQueryTextListener {


    @BindView(R.id.search_view)
    MaterialSearchView mSearchView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }


    @Override
    public void onSearchViewShown() {

    }

    @Override
    public void onSearchViewClosed() {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mSearchView.closeSearch();
        Log.d(BaseActivity.class.getName(), "onQueryTextSubmit: ");
        Intent intent = SearchActivity.createIntent(this, query);
        startActivity(intent);
        return true;

    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(item);
        return true;
    }

    protected void showSnackbar(int id) {
        Snackbar.make(mCoordinatorLayout, getString(id), Snackbar.LENGTH_SHORT).show();
    }

    protected void showSnakcBar(String msg) {
        Snackbar.make(mCoordinatorLayout, msg, Snackbar.LENGTH_SHORT).show();
    }

    protected void setUpToolbar() {
        setSupportActionBar(mToolbar);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


}
