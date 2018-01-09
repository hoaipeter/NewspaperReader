package com.example.peterhoai.newspaperreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Peter Hoai on 8/7/2016.
 */
public class NewsAdapter extends ArrayAdapter<News> {
    Context context;
    public NewsAdapter(Context context){
        super(context, 0);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = convertView;
        if(rootView == null){
            rootView = LayoutInflater.from(context).inflate(R.layout.row_news, null);
        }

        News news = getItem(position);
        if(news != null){
            ((TextView)rootView.findViewById(R.id.text_view_title)).setText(news.getTitle());
        }

        return rootView;
    }
}
