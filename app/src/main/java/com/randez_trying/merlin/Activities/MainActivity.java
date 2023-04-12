package com.randez_trying.merlin.Activities;

import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.randez_trying.merlin.Adapters.BooksAdapter;
import com.randez_trying.merlin.R;
import com.randez_trying.merlin.Util.Book;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    private Display display;
    private RecyclerView recyclerView;
    private List<Book> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = getWindowManager().getDefaultDisplay();
        books = new ArrayList<>();

        ImageView select = findViewById(R.id.select);
        recyclerView = findViewById(R.id.rec_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(new BooksAdapter(getApplicationContext(), books));

//        String selection = "_data LIKE '%.fb2'";
//        try (Cursor cursor = getApplicationContext().getContentResolver().query(MediaStore.Files.getContentUri("external"), null, selection, null, "_id DESC")) {
//            if (cursor== null || cursor.getCount() <= 0 || !cursor.moveToFirst()) return;
//            do {
//                @SuppressLint("Range")
//                String data = cursor.getString(cursor.getColumnIndex("data"));
//
//                try (BufferedWriter writer = new BufferedWriter(new FileWriter("book.fb2"))) {
//                    writer.write(data);
//
//                } catch (IOException ignored) {}
//                Book fictionBook = new Book(new File("book.fb2"));
//                System.out.println(fictionBook.getBookTitle());
//                books.add(fictionBook);
//                recyclerView.setAdapter(new BooksAdapter(getApplicationContext(), books));
//            } while (cursor.moveToNext());
//        } catch (ParserConfigurationException | IOException | SAXException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            searchDir(Environment.getExternalStorageDirectory());
//        } catch (ParserConfigurationException | IOException | SAXException e) {
//            e.printStackTrace();
//        }

        select.setOnClickListener(view -> getContent.launch("application/*"));
    }

//    public void searchDir(File dir) throws ParserConfigurationException, IOException, SAXException {
//        String fbPattern = ".fb2";
//        File[] fileList = dir.listFiles();
//
//        if (fileList != null) {
//            for (File file : fileList) {
//                if (file.isDirectory()) {
//                    searchDir(file);
//                } else {
//                    if (file.getName().endsWith(fbPattern)) {
//                        Book fictionBook = new Book(file, display.getWidth(), display.getHeight());
//                        books.add(fictionBook);
//                        recyclerView.setAdapter(new BooksAdapter(getApplicationContext(), books));
//                    }
//                }
//            }
//            System.out.println(books.size());
//        }
//    }

    ActivityResultLauncher<String> getContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uri);
                        File file = new File(getCacheDir(), "cacheBook.srl");
                        copyInputStreamToFile(inputStream, file);
                        Book fictionBook = new Book(file, display.getWidth(), display.getHeight());
                        books.add(fictionBook);
                        recyclerView.setAdapter(new BooksAdapter(getApplicationContext(), books));
                    } catch (IOException | ParserConfigurationException | SAXException e) {
                        e.printStackTrace();
                    }
                }
            });

    private void copyInputStreamToFile(InputStream in, File file) {
        OutputStream out = null;

        try {
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (out != null) out.close();
                in.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}