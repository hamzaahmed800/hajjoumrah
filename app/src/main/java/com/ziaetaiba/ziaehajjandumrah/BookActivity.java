package com.ziaetaiba.ziaehajjandumrah;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ziaetaiba.hajjandumrah.R;
import com.ziaetaiba.ziaehajjandumrah.dao.DBHelper;
import com.ziaetaiba.ziaehajjandumrah.dao.QueryHandler;
import com.ziaetaiba.ziaehajjandumrah.models.Book;

import java.io.File;
import java.io.IOException;

public class BookActivity extends Activity {

    ImageView bookImage;
    Button download, remove;
    TextView description, headingText;
    private SQLiteDatabase db;
    private Book book;
    private Typeface headingFont = null;
    private Typeface descriptionFont = null;
    ProgressBar progressBar;
    public final static String TOP_BAR_COLOR = "#003300";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        ActionBar bar = getActionBar();
        //for color
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(MainActivity.TOP_BAR_COLOR)));

        bookImage = (ImageView) findViewById(R.id.book_image);
        remove = (Button) findViewById(R.id.remove_Button);
        description = (TextView) findViewById(R.id.book_description);
        headingText = (TextView) findViewById(R.id.heading);
        headingFont = Typeface.createFromAsset(this.getAssets(), "Aslam.ttf");
        descriptionFont = Typeface.createFromAsset(this.getAssets(), "Jameel_Noori_Nastaleeq.ttf");
        headingText.setTypeface(headingFont);
        description.setTypeface(descriptionFont);


        //  arabicFont = Typeface.createFromAsset(this.getAssets(), "Trad_Arabic_Bold.ttf");

        try {
            db = new DBHelper(this).getWritableDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int detailid = extras.getInt("detailCategoryID");
            headingText.setText(extras.getString("BookName"));
            description.setText(extras.getString("Description"));
            getImageData(detailid);
        }


    }

    private void getImageData(int detailid) {
        book = QueryHandler.getBookDetails(this.db, detailid);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        Glide.with(this).load(book.getBook_imageLink()).into(bookImage);
        progressBar.setVisibility(View.GONE);
        //checking file available or not
        checkIfFileExsists_or_Not_andDownload();


    }
    //If File exist open it otherwise download it
    private void checkIfFileExsists_or_Not_andDownload() {
        download = (Button) findViewById(R.id.download_Button);
        if (checkFile()) {
            download.setText("View");
            remove.setVisibility(View.VISIBLE);
            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    open_file();
                }
            });
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteBook();
                }
            });
        } else {
            download.setText("Download");
            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    file_download(book.getBook_downloadLink());

                }
            });
        }
    }
    //Deleting book locally
    private void deleteBook() {
        File pdfFile = new File(Environment
                .getExternalStorageDirectory() + "/Hajj and Umrah", book.getBook_name() + ".pdf");
        if (pdfFile.exists()) {
            if (pdfFile.delete()) {
                Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            } else {
                System.out.println("file not Deleted :" + book.getBook_name());
            }
        } else {
            Toast.makeText(this, "File not exist!", Toast.LENGTH_SHORT).show();
        }
    }

    private void open_file() {
        File pdfFile = new File(Environment
                .getExternalStorageDirectory() + "/Hajj and Umrah", book.getBook_name() + ".pdf");
        Log.e("File Open", "In Open File Function");
        try {
            if (pdfFile.exists()) {
                Uri path = Uri.fromFile(pdfFile);
                Intent objIntent = new Intent(Intent.ACTION_VIEW);
                objIntent.setDataAndType(path, "application/pdf");
                objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(objIntent);
            } else {
                Toast.makeText(BookActivity.this, "File NotFound", Toast.LENGTH_SHORT).show();
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(BookActivity.this, "Invalid File Format", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //File Downloading function
    public void file_download(String uRl) {
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/Hajj and Umrah");
        if (!direct.exists()) {
            direct.mkdirs();
        }
        if (checkFile()) {
            Toast.makeText(this, "Already Downloaded!", Toast.LENGTH_SHORT).show();
//            finish();
//            startActivity(getIntent());
        } else {
            DownloadManager mgr = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(uRl);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setDescription(book.getBook_name())
                    .setDestinationInExternalPublicDir("/Hajj and Umrah", book.getBook_name() + ".pdf");
            final long refid = mgr.enqueue(request);
            //After Download completion..
            IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            BroadcastReceiver receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                    if (refid == reference) {
                        Toast.makeText(BookActivity.this, "download complete", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                    }
                }
            };
            registerReceiver(receiver, filter);
        }


    }

    //Checking Existence of file
    private boolean checkFile() {
        File pdfFile = new File(Environment
                .getExternalStorageDirectory() + "/Hajj and Umrah", book.getBook_name() + ".pdf");
        if (pdfFile.exists()) {
            return true;
        }
        return false;
    }

    //Menus
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        getMenuInflater().inflate(R.menu.share, menu);
        return true;

    }

    //Menus Actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d("OPTION_CLICKED", "Top app icon clicked to go back :::::: " + item.getItemId());
        int id = item.getItemId();
//		View actionView = item.getActionView().findViewById(R.id.imgbtnShare);
//		switch (item.getItemId()) {
        if (id == android.R.id.home) {
            Log.d("OPTION_CLICKED", "Top app icon clicked to go back");
            BookActivity.this.finish();

        } else if (id == R.id.share_setting) {
            File pdfFile = new File(Environment
                    .getExternalStorageDirectory() + "/Hajj and Umrah", book.getBook_name() + ".pdf");
            if (pdfFile.exists()) {
                Uri uri = Uri.fromFile(pdfFile);
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("application/pdf");
                i.setAction(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(i);
            } else {
                Toast.makeText(this, "File not found!", Toast.LENGTH_SHORT).show();
            }

        }
//				return true;
//			default:
        return super.onOptionsItemSelected(item);
        //	}
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BookActivity.this.finish();
    }


}
