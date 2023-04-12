package com.randez_trying.merlin.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.randez_trying.merlin.R;
import com.randez_trying.merlin.StaticHelper;
import com.randez_trying.merlin.Util.Book;

import java.util.Objects;

public class BookPreviewActivity extends AppCompatActivity {

    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_preview);

        if (StaticHelper.book != null) book = StaticHelper.book;

        RecyclerView recyclerView = findViewById(R.id.rec_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new PreviewAdapter(getApplicationContext()));
    }

    private class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.ViewHolder> {

        private final Context context;

        private PreviewAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public PreviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == 0) return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_image, parent, false));
            else return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_preview_main, parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull PreviewAdapter.ViewHolder holder, int position) {
            if (position == 0)
                Glide.with(context).load(Base64.decode(Objects.requireNonNull(book.getBinaries().get("cover.jpg")).getBinary().getBytes(), Base64.DEFAULT)).into(holder.img);
            else {
                holder.title.setText(book.getBookTitle());
                holder.author.setText(book.getFirstName() + " " + book.getLastName());

                holder.lang.setText(book.getLang());
                holder.annotation.setText(book.getBookAnnotation());

                holder.read.setOnClickListener(view -> {
                    Intent intent = new Intent(context, ReadActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                });
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ImageView img;
            public TextView title, author, tag, lang, annotation;
            public Button read;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                img = itemView.findViewById(R.id.img);
                title = itemView.findViewById(R.id.title);
                author = itemView.findViewById(R.id.author);
                tag = itemView.findViewById(R.id.tag);
                lang = itemView.findViewById(R.id.lang);
                annotation = itemView.findViewById(R.id.annotation);
                read = itemView.findViewById(R.id.read);
            }
        }
    }
}