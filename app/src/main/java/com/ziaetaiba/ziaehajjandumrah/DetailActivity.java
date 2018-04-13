package com.ziaetaiba.ziaehajjandumrah;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ziaetaiba.hajjandumrah.R;
import com.ziaetaiba.ziaehajjandumrah.dao.DBHelper;
import com.ziaetaiba.ziaehajjandumrah.dao.QueryHandler;
import com.ziaetaiba.ziaehajjandumrah.models.Detail;

import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends Activity {

    private SQLiteDatabase db = null;

    private ListView detailListView = null;
    private DetailAdapter adptDetail = null;

    private TextView headingText;
    private Typeface headingFont = null;
    private Typeface arabicFont = null;

    private LinearLayout shareLayout = null;

    private ScaleGestureDetector scaleGestureDetector;
    private SocialAuthAdapter adapterSocialAuth;
    private List<Detail> detailList;
    String subCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        shareLayout = (LinearLayout) findViewById(R.id.LinearLayoutShare);
        shareLayout.setVisibility(View.GONE);

        headingText = (TextView) findViewById(R.id.heading);
        headingFont = Typeface.createFromAsset(this.getAssets(), "Aslam.ttf");
        headingText.setTypeface(headingFont);


        arabicFont = Typeface.createFromAsset(this.getAssets(), "Trad_Arabic_Bold.ttf");

        setDetailList();

        try {
            db = new DBHelper(this).getWritableDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int subCategoryId = extras.getInt("subCategoryId");

            subCategory = extras.getString("subCategory");
            List<Detail>  detailList = QueryHandler.getSubCategoryDetail(this.db, subCategoryId);
            Log.e("DetailListSize",String.valueOf(detailList.size()));
            //For Books
            if(detailList.size()>0){
                if (detailList.get(0).getType() == 2) {
                    Intent i = new Intent(DetailActivity.this,BookActivity.class);
                    i.putExtra("detailCategoryID",detailList.get(0).getId());
                    i.putExtra("BookName",subCategory);
                    i.putExtra("Description",detailList.get(0).getDescription());
                    startActivity(i);
                    finish();

                } else {

                    loadDetailPage(subCategoryId, subCategory);

                }
            }else{
                //do nothing...
            }

        }


        scaleGestureDetector = new ScaleGestureDetector(this,
                new simpleOnScaleGestureListener());

        adapterSocialAuth = new SocialAuthAdapter(new ResponseListener());
//		adapterSocialAuth.addProvider(Provider.FACEBOOK, R.drawable.facebook);
//		adapterSocialAuth.addProvider(Provider.TWITTER, R.drawable.twitter);
//		adapterSocialAuth.addProvider(Provider.LINKEDIN, R.drawable.linkedin);
//		adapterSocialAuth.addProvider(Provider.EMAIL, R.drawable.email);


        ActionBar actionBar = getActionBar();
        //for color
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(MainActivity.TOP_BAR_COLOR)));
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        getMenuInflater().inflate(R.menu.share, menu);


        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d("OPTION_CLICKED", "Top app icon clicked to go back :::::: " + item.getItemId());
        int id = item.getItemId();
//		View actionView = item.getActionView().findViewById(R.id.imgbtnShare);
//		switch (item.getItemId()) {
        if (id == android.R.id.home) {
            Log.d("OPTION_CLICKED", "Top app icon clicked to go back");
            DetailActivity.this.finish();

        } else if (id == R.id.share_setting) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("",clipboard.getText().toString());
            clipboard.setPrimaryClip(clip);
            if(clipboard.hasPrimaryClip()){
                String description = clipboard.getText().toString();
                String sub = subCategory;
                if(!(description.equals(""))){
                    i.putExtra(Intent.EXTRA_SUBJECT, sub);
                    i.putExtra(Intent.EXTRA_TEXT, description);
                    startActivity(Intent.createChooser(i, "Share Using"));
                }else{
                    Toast.makeText(this,"select some data",Toast.LENGTH_SHORT).show();
                }
                ClipboardManager clipb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipp = ClipData.newPlainText("","");
                clipboard.setPrimaryClip(clipp);

            }else{
                Toast.makeText(this,"select some data",Toast.LENGTH_SHORT).show();
            }
        }
//				return true;
//			default:
        return super.onOptionsItemSelected(item);
        //	}
    }

    @Override
    public void onBackPressed() {

        Log.d("Back", "Phone back pressed.");
        super.onBackPressed();
        DetailActivity.this.finish();
    }


    private void loadDetailPage(int subCategoryId, String subCategory) {

      List<Detail>  detailList = QueryHandler.getSubCategoryDetail(this.db, subCategoryId);
        Log.e("DetailListSize",String.valueOf(detailList.size()));

//	   	setContentView(R.layout.sub_category_item);

        headingText.setText(subCategory);

        adptDetail.setItemList(detailList);



        Log.d("Detail List", "Detail List Size: " + detailList.size());

    }

    private void setDetailList() {

//    	setContentView(R.layout.detail_item_list);

        detailListView = (ListView) findViewById(R.id.detailList);
        adptDetail = new DetailAdapter(new ArrayList<Detail>(), this);
        Log.d("adptDetail", "adptDetail :::::::::::::::::::::: " + adptDetail + " : " + detailListView);
        detailListView.setAdapter(adptDetail);


        detailListView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub

                int pointers = MotionEventCompat.getPointerCount(event);
                Log.d("TOUCH", "UPPER ON TOUCH CALLED:::" + pointers);
                if (pointers > 0) {
                    scaleGestureDetector.onTouchEvent(event);
                }
                return false;
            }
        });

    }

    private class simpleOnScaleGestureListener extends
            SimpleOnScaleGestureListener {
        float size;
        float product;
        float factor;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            size = ((TextView) ((LinearLayout) ((RelativeLayout) (detailListView
                    .getChildAt(0))).getChildAt(0)).getChildAt(0))
                    .getTextSize();
            Log.d("TextSizeStart", "TextSize! size start: " + String.valueOf(size));
            //
            factor = detector.getScaleFactor();
            Log.d("Factor", "TextSize! Factor" + String.valueOf(factor));
            //
            product = size * factor;
            adptDetail.setTextSize(product);
            Log.d("TextSize", String.valueOf(product * 10));

            adptDetail.notifyDataSetChanged();

            size = ((TextView) ((LinearLayout) ((RelativeLayout) (detailListView
                    .getChildAt(0))).getChildAt(0)).getChildAt(0))
                    .getTextSize();
            Log.d("TextSizeEnd", "TextSize! size end: " + String.valueOf(size));

            return true;
        }

    }

    /**
     * Listens Response from Library
     */
    private final class ResponseListener implements DialogListener {
        @Override
        public void onComplete(Bundle values) {
            // Variable to receive message status
            Log.d("Share-Menu", "Authentication Successful");

            // Get name of provider after authentication
            final String providerName = values.getString(SocialAuthAdapter.PROVIDER);
            Log.d("Share-Bar", "Provider Name = " + providerName);
            Toast.makeText(DetailActivity.this, providerName + " connected", Toast.LENGTH_SHORT).show();

            List<Detail> detailList = adptDetail.getItemList();

            StringBuilder tmpShareText = new StringBuilder("<h1>" + headingText.getText() + "</h1>");
            for (Detail item : detailList) {
                tmpShareText.append(item.getDescription());
                tmpShareText.append("<br>");
            }


            // Share via Email Intent
            if (providerName.equalsIgnoreCase("share_mail")) {
                // Share via Email Intent
                final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, headingText.getText() + ":");

                emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(tmpShareText.toString()));
                startActivity(Intent.createChooser(emailIntent, "Hajj and Umrah"));
            } else {

                shareLayout.setVisibility(View.VISIBLE);

                Button btnShare = (Button) findViewById(R.id.btnShare);
                final EditText shareText = (EditText) findViewById(R.id.shareText);

                shareText.setText(Html.fromHtml(tmpShareText.toString()));

                // Please avoid sending duplicate message. Social Media Providers
                // block duplicate messages.

                btnShare.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        shareLayout.setVisibility(View.GONE);
                        // Call updateStatus to share message via oAuth providers
                        adapterSocialAuth.updateStatus(shareText.getText().toString(), new MessageListener(), false);
                    }
                });
            }

        }

        @Override
        public void onError(SocialAuthError error) {
            error.printStackTrace();
            Log.d("Share-Menu", error.getMessage());
        }

        @Override
        public void onCancel() {
            Log.d("Share-Menu", "Authentication Cancelled");
        }

        @Override
        public void onBack() {
            Log.d("Share-Menu", "Dialog Closed by pressing Back Key");

        }
    }


    // To get status of message after authentication
    private final class MessageListener implements SocialAuthListener<Integer> {
        @Override
        public void onExecute(String provider, Integer t) {
            Integer status = t;
            if (status.intValue() == 200 || status.intValue() == 201 || status.intValue() == 204)
                Toast.makeText(DetailActivity.this, "Message posted on " + provider, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(DetailActivity.this, "Message not posted on" + provider, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(SocialAuthError e) {

        }
    }


}
