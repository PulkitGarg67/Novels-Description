package cic.du.ac.in.recylerviewandcardview;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import cic.du.ac.in.recylerviewandcardview.Utils.Book;
import cic.du.ac.in.recylerviewandcardview.Utils.QueryUtils;

/**
 * Created by Scorpion on 11/1/2018.
 */

public class BooksLoader extends AsyncTaskLoader<List<Book>>{

    String murl;

    public BooksLoader(Context context,String url){
        super(context);
        murl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Book> loadInBackground() {
        if(murl.isEmpty()){
            return  null;
        }
            ArrayList<Book> result = QueryUtils.extractBooks(murl);
            return result;
    }
}
