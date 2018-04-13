package com.ziaetaiba.ziaehajjandumrah;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SearchView;

import com.ziaetaiba.hajjandumrah.R;
import com.ziaetaiba.ziaehajjandumrah.dao.DBHelper;
import com.ziaetaiba.ziaehajjandumrah.dao.QueryHandler;
import com.ziaetaiba.ziaehajjandumrah.models.SubCategory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity implements OnClickListener {

    public final static int RAHNUMAI_ID = 1;
    public final static int AHRAM_ID = 2;
    public final static int UMRAH_ID = 3;
    public final static int HAJJ_ID = 4;

    public final static int ZIAE_TAIBA_ID = 5;
    public final static int MASAIL_E_HAJJ_O_UMRAH_ID = 6;
    public final static int AURAD_O_WAZAIF_ID = 7;
    public final static int SAB_SE_PHELE_ID = 8;
    public final static int FAZAIL_E_HAJJ_O_UMRAH_ID = 9;
    public final static int MUQADDAS_MAQAMAT_ID = 10;
    public final static int APKE_MASAIL_ID = 11;
    public final static int ABOUT_US_ID = 12;
    public final static int KUTUB_ID = 13;
    public final static int SHOWBAJAT_ID = 14;

    public final static int TYPE_NORMAL = 0;
    public final static int TYPE_ARABIC = 1;
    public final static int TYPE_LINK = 5;
    public final static int TYPE_EMAIL = 6;

    public final static String TOP_BAR_COLOR = "#003300";
//	private SQLiteDatabase db = null; 

//	private ListView subCategoryListView = null, detailListView;
//	private SubCategoryAdapter adptSubCategory = null;
//	private DetailAdapter adptDetail = null;

    public static Map<Integer, String> headings = null;
    public static List<SubCategory> searchList1,searchList2,newList;

    private ImageView optionAhram;
    private ImageView optionRahnumai;
    private ImageView optionHajj;
    private ImageView optionUmrah;
    private ImageView optionSubSePhele;
    private ImageView optionFazailHajjOUmrah;
    private ImageView optionMasailHajjOUmrah;
    private ImageView optionMuqaddasMaqamat;
    private ImageView optionApkeMasail;
    private ImageView optionZiaeTaiba;
    private ImageView optionAuradOWazaif;
    private ImageView optionAboutUs;
    private ImageView optionKutub;
    private ImageView optionshowbajat;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        headings = new HashMap<Integer, String>();
        headings.put(RAHNUMAI_ID, "قدم قدم رہنمائی");
        headings.put(AHRAM_ID, "ضیائے احرام");
        headings.put(UMRAH_ID, "ضیائے عمرہ");
        headings.put(HAJJ_ID, "ضیائے حج");
        headings.put(ZIAE_TAIBA_ID, "ضیائے طیبہ");
        headings.put(MASAIL_E_HAJJ_O_UMRAH_ID, "مسائل حج و عمرہ ");
        headings.put(AURAD_O_WAZAIF_ID, "اوراد و ظائف");
        headings.put(SAB_SE_PHELE_ID, "سب سے پہلے");
        headings.put(FAZAIL_E_HAJJ_O_UMRAH_ID, "فضائلِ حج و عمرہ");
        headings.put(MUQADDAS_MAQAMAT_ID, "مقدس مقامات");
        headings.put(APKE_MASAIL_ID, "آپ کے مسائل");
        headings.put(ABOUT_US_ID, "کچھ ہمارے بارے میں");
        headings.put(KUTUB_ID, "کتب");
        headings.put(SHOWBAJAT_ID, "شعبہ");

        optionAhram = (ImageView) findViewById(R.id.optionAhram);
        optionRahnumai = (ImageView) findViewById(R.id.optionRahnumai);
        optionHajj = (ImageView) findViewById(R.id.optionHajj);
        optionUmrah = (ImageView) findViewById(R.id.optionUmrah);

        optionSubSePhele = (ImageView) findViewById(R.id.optionSubSePhele);
        optionFazailHajjOUmrah = (ImageView) findViewById(R.id.optionFazailHajjOUmrah);
        optionMasailHajjOUmrah = (ImageView) findViewById(R.id.optionMasailHajjOUmrah);
        optionMuqaddasMaqamat = (ImageView) findViewById(R.id.optionMuqaddasMaqamat);
        optionApkeMasail = (ImageView) findViewById(R.id.optionApkeMasail);
        optionZiaeTaiba = (ImageView) findViewById(R.id.optionZiaeTaiba);
        optionAuradOWazaif = (ImageView) findViewById(R.id.optionAuradOWazaif);
        optionAboutUs = (ImageView) findViewById(R.id.optionAboutUs);
        optionKutub = (ImageView) findViewById(R.id.optionkutub);
        optionshowbajat = (ImageView) findViewById(R.id.optionshowbajat);

        optionAhram.setOnClickListener(this);
        optionRahnumai.setOnClickListener(this);
        optionHajj.setOnClickListener(this);
        optionUmrah.setOnClickListener(this);
        optionSubSePhele.setOnClickListener(this);
        optionFazailHajjOUmrah.setOnClickListener(this);
        optionMasailHajjOUmrah.setOnClickListener(this);
        optionMuqaddasMaqamat.setOnClickListener(this);
        optionApkeMasail.setOnClickListener(this);
        optionZiaeTaiba.setOnClickListener(this);
        optionAuradOWazaif.setOnClickListener(this);
        optionAboutUs.setOnClickListener(this);
        optionKutub.setOnClickListener(this);
        optionshowbajat.setOnClickListener(this);

//        try {
//			db = new DBHelper(this).getWritableDatabase();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}


        ActionBar bar = getActionBar();
        //for color
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(MainActivity.TOP_BAR_COLOR)));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem item = (menu).findItem(R.id.search_item);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(!s.equals("")){
                    SearchItem(s);
                }else{
                    //do nothing..
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.e("URDU STRING",s);

                return false;
            }
        });

        return true;
    }
    //Category within a category
    private void SearchItem(String s) {
        try {
            db = new DBHelper(this).getWritableDatabase(); //
        } catch (IOException e) {
            e.printStackTrace();
        }
        searchList1 = new ArrayList<SubCategory>();
        searchList2 = new ArrayList<SubCategory>();
        newList = new ArrayList<SubCategory>();
        for(int i=0;i<14;i++){
          searchList1 =   QueryHandler.getSubCategoriesByCategoryId(this.db, i);

          for(int j=0;j<searchList1.size();j++){
              if(searchList1.get(j).getSubCategory().contains(s)){
                  newList.add(searchList1.get(j));
//                  searchList2 =   QueryHandler.getChildSubCategories(this.db,searchList1.get(j).getParentSubCategoryId());
//                  for(int k =0;k<searchList2.size();k++){
//                      if(searchList2.get(j).getSubCategory().contains(s)){
//                          newList.add(searchList2.get(k));
//                      }
//                  }

              }
          }
        }
        startActivity(new Intent(MainActivity.this,SubCategoryActivity.class));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id==R.id.action_settings){
          Intent  intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=7848049246569116556"));
          startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.optionAhram:
                Log.d("Option Ahram", "Option Ahram Clicked!");
                startSubCategoryActivity(AHRAM_ID);
                break;
            case R.id.optionRahnumai:
                Log.d("Option Rahnumai", "Option Rahnumai Clicked!");
                startSubCategoryActivity(RAHNUMAI_ID);
                break;
            case R.id.optionHajj:
                Log.d("Option Hajj", "Option Hajj Clicked!");
                startSubCategoryActivity(HAJJ_ID);
                break;
            case R.id.optionUmrah:
                Log.d("Option Ahram", "Option Umrah Clicked!");
                startSubCategoryActivity(UMRAH_ID);
                break;
            case R.id.optionSubSePhele:
                Log.d("Option SubSePhele", "Option SubSePhele Clicked!");
                startSubCategoryActivity(SAB_SE_PHELE_ID);
                break;
            case R.id.optionFazailHajjOUmrah:
                Log.d("Option FazailHajjOUmrah", "Option FazailHajjOUmrah Clicked!");
                startSubCategoryActivity(FAZAIL_E_HAJJ_O_UMRAH_ID);
                break;
            case R.id.optionMasailHajjOUmrah:
                Log.d("Option MasailHajjOUmrah", "Option MasailHajjOUmrah Clicked!");
                startSubCategoryActivity(MASAIL_E_HAJJ_O_UMRAH_ID);
                break;
            case R.id.optionMuqaddasMaqamat:
                Log.d("Option MuqaddasMaqamat", "Option MuqaddasMaqamat Clicked!");
                startSubCategoryActivity(MUQADDAS_MAQAMAT_ID);
                break;
            case R.id.optionZiaeTaiba:
                Log.d("Option ZiaeTaiba", "Option ZiaeTaiba Clicked!");
                startSubCategoryActivity(ZIAE_TAIBA_ID);
                break;
            case R.id.optionAuradOWazaif:
                Log.d("Option AuradOWazaif", "Option AuradOWazaif Clicked!");
                startSubCategoryActivity(AURAD_O_WAZAIF_ID);
                break;
            case R.id.optionshowbajat://working new
                Log.d("Option Showbajat", "Option Showbajat");
                startSubCategoryActivity(SHOWBAJAT_ID);
                break;
            case R.id.optionkutub://working new
                Log.d("Option KUTUB", "Option KUTUB");
                startSubCategoryActivity(KUTUB_ID);
                break;

            case R.id.optionApkeMasail:
                Log.d("Option ApkeMasail", "Option ApkeMasail Clicked!");
                //startSubCategoryActivity(APKE_MASAIL_ID);
                startDetailActivity(221, MainActivity.headings.get(APKE_MASAIL_ID));
                break;
            case R.id.optionAboutUs:
                Log.d("Option AboutUs", "Option AboutUs Clicked!");
                //startSubCategoryActivity(ABOUT_US_ID); //220
                startDetailActivity(220, MainActivity.headings.get(ABOUT_US_ID));
                break;

            default:
                break;
        }

    }

    private void startSubCategoryActivity(int categoryId) {
        Intent intent = new Intent(this, SubCategoryActivity.class);
        intent.putExtra("categoryId", categoryId);
        startActivity(intent);
    }

    private void startDetailActivity(int subCategoryId, String subCategory) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("subCategoryId", subCategoryId);
        intent.putExtra("subCategory", subCategory);
        startActivity(intent);
    }

}
