package com.ziaetaiba.ziaehajjandumrah.dao;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper {

//	private Context mycontext;
	private static AssetManager res;
	private static String pack;
	private String DB_PATH;
	
	public static final String DB_NAME = "hajj-backup.sqlite";

	public DBHelper(Context context) throws IOException {
		super(context, DB_NAME, null, 1);
		
		context = context.getApplicationContext();
//		this.mycontext = context;
		res = context.getAssets();
		System.out.println("getInstance :: " + res);
		pack = context.getPackageName();
		DB_PATH = Environment.getDataDirectory() + "/data/" + pack
				+ "/databases/";
		//this.mName= DB_PATH + DB_NAME;
		boolean dbexist = checkdatabase();
		if (dbexist) {
			System.out.println("Database exists");
			opendatabase();
		} else {
			System.out.println("Database doesn't exist");
			createdatabase();
			opendatabase();

		}
		
	}

	public void createdatabase() throws IOException {
		boolean dbexist = checkdatabase();
		if (dbexist) {
			 System.out.println(" Database exists.");
		} else {
			try {
				copydatabase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	private boolean checkdatabase() {
		boolean checkdb = false;
		try {
			String myPath = DB_PATH + DB_NAME;

			File dbfile = new File(myPath);

			checkdb = dbfile.exists();
		} catch (SQLiteException e) {
			System.out.println("Database doesn't exist");
		}
		return checkdb;
	}

	private void copydatabase() throws IOException {
		// Open your local db as the input stream
		InputStream myinput = res.open(DB_NAME);
		Log.d("SQLite Copy: ", "Input Stream Created");
		// Path to the just created empty db
		String outfilename = DB_PATH + DB_NAME ;
		Log.d("SQLite Copy File Path: ", outfilename);
		// Open the empty db as the output stream
		File dir = new File(DB_PATH);
		if (dir.exists() == false) {
			dir.mkdirs();
			Log.d("SQLite Copy: ", "Directory Made");
		}
		File file = new File(DB_PATH, DB_NAME);
		try {
			if ((file.exists())) {
				file.delete();
				Log.d("SQLite Copy: ", "File Created");
			} else {
				file.createNewFile();
				Log.d("SQLite Copy: ", "File Doesn't Exist!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		OutputStream myoutput = new FileOutputStream(file);
		Log.d("SQLite Copy: ", "Output Stream Reached");
		// transfer byte to inputfile to outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myinput.read(buffer)) > 0) {
			myoutput.write(buffer, 0, length);
			Log.d("SQLite Copy: ", "File Write");
		}

		// Close the streams
		myoutput.flush();
		myoutput.close();
		myinput.close();
		
	}

	public void opendatabase() throws SQLException {
		// Open the database

		String myPath = DB_PATH + DB_NAME;

		File file = new File(myPath);
		Log.d("HELPER", file.getPath() + " Exists?" + file.exists());
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
}
