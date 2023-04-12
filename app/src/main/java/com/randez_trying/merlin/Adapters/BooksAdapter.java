package com.randez_trying.merlin.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.randez_trying.merlin.Activities.BookPreviewActivity;
import com.randez_trying.merlin.R;
import com.randez_trying.merlin.StaticHelper;
import com.randez_trying.merlin.Util.Book;

import java.util.List;
import java.util.Objects;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {

    private final Context context;
    private final List<Book> books;

    public BooksAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    @NonNull
    @Override
    public BooksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_book, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BooksAdapter.ViewHolder holder, int position) {
        Book book = books.get(position);

        Glide.with(context).load(Base64.decode(Objects.requireNonNull(book.getBinaries().get("cover.jpg")).getBinary().getBytes(), Base64.DEFAULT)).into(holder.img);
        holder.name.setText(book.getBookTitle());
        holder.author.setText(book.getFirstName() + " " + book.getLastName());

        holder.itemView.setOnClickListener(view -> {
            StaticHelper.book = book;
            Intent intent = new Intent(context, BookPreviewActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView img;
        public TextView name, author;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.book_img);
            name = itemView.findViewById(R.id.book_name);
            author = itemView.findViewById(R.id.book_author);
        }
    }
}
