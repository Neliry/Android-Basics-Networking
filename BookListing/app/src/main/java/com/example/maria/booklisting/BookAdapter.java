package com.example.maria.booklisting;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
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


public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        final Book currentBook = getItem(position);

        TextView bookTitleView = (TextView) listItemView.findViewById(R.id.book_title);
        String bookTitle = currentBook.getBoockTitle();
        bookTitleView.setText(bookTitle);

        String publisher = currentBook.getPublisher();
        TextView publisherView = (TextView) listItemView.findViewById(R.id.book_publisher);
        publisherView.setText(publisher);

        String authors = currentBook.getAuthors();
        TextView authorsView = (TextView) listItemView.findViewById(R.id.book_authors);
        authorsView.setText(authors);

        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        if(currentBook.getPublishedDate().length()>4){
        String formattedDate = formatDate(currentBook.getPublishedDate());
        dateView.setText(formattedDate);
        }
        else{
            String date = currentBook.getPublishedDate();
            dateView.setText(date);
        }

        ImageView imageView=(ImageView) listItemView.findViewById(R.id.bookImage);
        imageView.setImageDrawable(currentBook.getBookImage());

        return listItemView;
    }

    private String formatDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dateFormat = new SimpleDateFormat("dd LLLL yyyy");
        return dateFormat.format(convertedDate);
    }

}
