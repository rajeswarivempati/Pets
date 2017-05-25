package com.example.android.pets.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.Selection;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by dhanya on 24/05/17.
 */

public class PetProvider extends ContentProvider {
    private PetDbHelper mDbHelper;
    public static final int pets=100;
    public static final int pets_id=101;
    public static final String LOG_TAG = PetProvider.class.getSimpleName();

    private static final UriMatcher sUriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
    static{

        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_PETS,pets);

        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_PETS+"/#",pets_id);




    }






    @Override
    public boolean onCreate() {
        mDbHelper=new PetDbHelper(getContext());
        return false;
    }

    @Nullable

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case pets:
                // For the PETS code, query the pets table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the pets table.
                cursor=database.query(PetContract.PetEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case pets_id:
                // For the PET_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = PetContract.PetEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(PetContract.PetEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case pets:
                return insertPet(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }



    private Uri insertPet(Uri uri, ContentValues values) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(PetContract.PetEntry.TABLE_NAME, null, values);
        if(id==-1)
        {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }


        return ContentUris.withAppendedId(uri, id);
        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it

    }


    @Override
    public int delete(Uri uri, String s, String[] strings) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case pets:
                // Delete all rows that match the selection and selection args
                return database.delete(PetContract.PetEntry.TABLE_NAME, s, strings);
            case pets_id:
                // Delete a single row given by the ID in the URI
                s = PetContract.PetEntry._ID + "=?";
                strings = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return database.delete(PetContract.PetEntry.TABLE_NAME, s, strings);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }


    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case pets:
                return updatePet(uri, contentValues, s, strings);
            case pets_id:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
              String  selection = PetContract.PetEntry._ID + "=?";
              String []  selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatePet(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }

    }
    private int updatePet(Uri uri,ContentValues contentValues,String selection,String[] strings)
    {
        SQLiteDatabase database=mDbHelper.getWritableDatabase();
        int row=database.update(PetContract.PetEntry.TABLE_NAME,contentValues,selection,strings);
        Toast.makeText(getContext(),row+"rows are affected",Toast.LENGTH_SHORT).show();

        return 0;
    }
}
