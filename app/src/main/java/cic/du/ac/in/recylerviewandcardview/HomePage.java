package cic.du.ac.in.recylerviewandcardview;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cic.du.ac.in.recylerviewandcardview.Utils.Book;
import cic.du.ac.in.recylerviewandcardview.Utils.QueryUtils;

public class HomePage extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>>{

    List<Book> lstBook ;
    ProgressBar progressBar;
    TextView txt;
    RecyclerView myrv;
    RecyclerViewAdapter myAdapter;

    private static final int Book_loader_id = 1;

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }

    private static final String Base_URL = "https://www.googleapis.com/books/v1/volumes?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("BookStore");

        myrv = (RecyclerView) findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

        txt = findViewById(R.id.empty_view);
        lstBook = new ArrayList<>();
        lstBook.add(new Book("The Vegitarian","Description book","http://books.google.com/books/content?id=zyTCAlFPjgYC&printsec=frontcover&img=1&zoom=1&imgtk=AFLRE73vg7-PVWYmvxGgkhRvHYT5p306Cmxy2SeH1pwXjSCOjW27CnvjRvUw5DUAzfOid4LvKBQIJygtt2_XPVI53qz9GRstLqLg43ujScRSXmmWnVs0OL_PM_JUODtbWbbGc9iF_xvP&source=gbs_api"));

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(Book_loader_id, null, this);
//        new BooksAsyncTask().execute(USGS_REQUEST_URL);
    }

    private void updateUi(List<Book> books) {
        progressBar.setVisibility(View.INVISIBLE);

        myAdapter = new RecyclerViewAdapter(this,books);
        if (myAdapter.getItemCount()==0) {
            myrv.setVisibility(View.GONE);
            txt.setVisibility(View.VISIBLE);
        }
        else {
            myrv.setLayoutManager(new GridLayoutManager(this,calculateNoOfColumns(this)));
            myrv.setAdapter(myAdapter);
            txt.setVisibility(View.GONE);
        }

    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        return new BooksLoader(this, Base_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        updateUi(books);
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        myAdapter = null;
    }
}
