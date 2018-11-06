package com.example.maria.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        final News currentNews = getItem(position);

        TextView newsTitleView = (TextView) listItemView.findViewById(R.id.news_title);
        String newsTitle = currentNews.getNewsTitle();
        newsTitleView.setText(newsTitle);


        String author = currentNews.getAuthor();
        TextView authorView = (TextView) listItemView.findViewById(R.id.author);
        authorView.setText(author);

        String sectionName = currentNews.getSectionName();
        TextView sectionView = (TextView) listItemView.findViewById(R.id.section_name);
        sectionView.setText(sectionName);

        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        String formattedDate = formatDate(currentNews.getPublishedDate());
        dateView.setText(formattedDate);

        ImageView imageView=(ImageView) listItemView.findViewById(R.id.image);
        if(currentNews.getImage()!=null){
            imageView.setImageDrawable(currentNews.getImage());
            imageView.setVisibility(View.VISIBLE);
        }
        else {
            imageView.setVisibility(View.GONE);
        }

        return listItemView;
    }

    private String formatDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String s1= dateString.substring(0, dateString.indexOf("T"));
        s1.trim();
        String s2= dateString.substring(dateString.indexOf("T")+1);
        s2.trim();
        s2= s2.substring(0, s2.indexOf("Z"));
        s2.trim();
        dateString=s1+" "+s2;
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dateFormat = new SimpleDateFormat("hh:mm:ss, dd LLLL yyyy");
        return dateFormat.format(convertedDate);
    }

}
