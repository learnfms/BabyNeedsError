package com.famees.babyneeds.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.famees.babyneeds.model.Item;
import com.famees.babyneeds.util.Constants;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private final Context context;

    public DatabaseHandler(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BABY_TABLE = "CREATE TABLE "+ Constants.TABLE_NAME + "("
                +Constants.KEY_ID+" INTEGER PRIMARY KEY,"
                +Constants.KEY_BABY_ITEM+" INTEGER,"
                +Constants.KEY_COLOR+" TEXT,"
                +Constants.KEY_QTY_NUMBER+" INTEGER, "
                +Constants.KEY_ITEM_SIZE+" INETEGER,"
                +Constants.KEY_DATE_NAME+" LONG);";

        db.execSQL(CREATE_BABY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        db.execSQL("DROP TABLE IF EXISTS "+ Constants.TABLE_NAME);

        onCreate(db);
    }
    //CRUD Operations
    public void addItem(Item item){
        SQLiteDatabase db =this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_BABY_ITEM, item.getItemName());
        values.put(Constants.KEY_COLOR,item.getItemColor());
        values.put(Constants.KEY_ITEM_SIZE, item.getItemSize());
        values.put(Constants.KEY_QTY_NUMBER,item.getItemQuantity());
        values.put(Constants.KEY_DATE_NAME,java.lang.System.currentTimeMillis());  //Timestamp of the system


        //Insert a row
        db.insert(Constants.TABLE_NAME,null,values);

        Log.d("DBHandler", "addItem: ");

    }

    //Get an Item
    public Item getItem(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME,
                new String[]{Constants.KEY_ID,
                Constants.KEY_BABY_ITEM,
                Constants.KEY_QTY_NUMBER,
                Constants.KEY_ITEM_SIZE,
                Constants.KEY_DATE_NAME},
                Constants.KEY_ID+ "=?",
                new String[]{
                        String.valueOf(id)},null,null,null,null);

        if(cursor != null)
            cursor.moveToFirst();

            Item item = new Item();
            if(cursor != null){
            item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
            item.setItemName(cursor.getString(cursor.getColumnIndex(Constants.KEY_BABY_ITEM)));
            item.setItemQuantity(cursor.getInt(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)));
            item.setItemSize(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ITEM_SIZE)));


            //convert timestamp to readable
                DateFormat dateFormat = DateFormat.getDateInstance();
                String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME)))
                .getTime());  //Feb 23, 2019

                item.setDataItemAdded(formattedDate);



        }
        return item;


    }
    //Get All the Items
    public List<Item> getAllItems(){
        SQLiteDatabase db = this.getReadableDatabase();

        List<Item> itemList = new ArrayList<>();

        Cursor cursor = db.query(Constants.TABLE_NAME,
                new String[]{Constants.KEY_ID,
                        Constants.KEY_BABY_ITEM,
                        Constants.KEY_QTY_NUMBER,
                        Constants.KEY_ITEM_SIZE,
                        Constants.KEY_DATE_NAME},
                null,null,null,null,
                Constants.KEY_DATE_NAME+" DESC");

        if(cursor.moveToFirst()){
            do{
                Item item = new Item();
                item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                item.setItemName(cursor.getString(cursor.getColumnIndex(Constants.KEY_BABY_ITEM)));
                item.setItemQuantity(cursor.getInt(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)));
                item.setItemSize(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ITEM_SIZE)));

                //convert timestamp to readable
                DateFormat dateFormat = DateFormat.getDateInstance();
                String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME)))
                        .getTime());  //Feb 23, 2019
                item.setDataItemAdded(formattedDate);

                //Add to ArrayList
                itemList.add(item);

            }while(cursor.moveToNext());
        }
        return itemList;

    }

    //Todo : Add update item

    public int updateItem(Item item){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_BABY_ITEM, item.getItemName());
        values.put(Constants.KEY_COLOR,item.getItemColor());
        values.put(Constants.KEY_ITEM_SIZE, item.getItemSize());
        values.put(Constants.KEY_QTY_NUMBER,item.getItemQuantity());
        values.put(Constants.KEY_DATE_NAME,java.lang.System.currentTimeMillis());  //Timestamp of the system

        //upade row

        return db.update(Constants.TABLE_NAME,values,
                Constants.KEY_ID+"=?",
                new String[]{String.valueOf(item.getId())});
    }

    //Todo : Add Delete item

    public void deleteItem(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME,
                Constants.KEY_ID+"=?",
                new String[]{String.valueOf(id)});

        db.close();


    }

    //Todo : getItemCount
    public int getItemsCount(){
        String countQuery = "SELECT * FROM "+Constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery,null);

        return cursor.getCount();
    }

}
