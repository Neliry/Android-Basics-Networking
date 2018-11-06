
package com.example.maria.booklisting;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BooksSearchActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {

    public static final String LOG_TAG =BooksSearchActivity.class.getName();
    private static String USGS_REQUEST_URL = "";
    private static final int BOOK_LOADER_ID = 1;
    private BookAdapter mAdapter;
    private TextView mEmptyStateTextView;
    private static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.books_search_activity);

        BooksSearchActivity.context = getApplicationContext();
        final ListView bookListView = (ListView) findViewById(R.id.list);

        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        bookListView.setAdapter(mAdapter);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Book currentBook = mAdapter.getItem(position);
                Uri bookUri = Uri.parse(currentBook.getInfoLink());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);
                startActivity(websiteIntent);
            }
        });
        final EditText edittext = (EditText) findViewById(R.id.searchEditText);

        edittext.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    USGS_REQUEST_URL="https://www.googleapis.com/books/v1/volumes?q="+edittext.getText()+"&maxResults=40";
                    View loadingIndicator = findViewById(R.id.loading_indicator);
                    loadingIndicator.setVisibility(View.VISIBLE);
                    mEmptyStateTextView.setText("");
                    getLoaderManager().restartLoader(BOOK_LOADER_ID, null, BooksSearchActivity.this);

                    return true;
                }
                return false;
            }
        });
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).

            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {

        // Create a new loader for the given URL
        return new BookLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_books);

        mAdapter.clear();

        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {

        mAdapter.clear();
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        String someString = USGS_REQUEST_URL;
        savedInstanceState.putString(USGS_REQUEST_URL, someString);
        super.onSaveInstanceState(savedInstanceState);
    }

    public static Context getAppContext() {
        return BooksSearchActivity.context;
    }

}