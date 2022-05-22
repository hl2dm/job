package dm.school.suck.job;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBOpenHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "Purchasing";
	private static final int DB_VERSION = 1;
	private static final String TABLE_NAME_df = "User_info";
	private static final String COL_id = "id";
    private static final String COL_Time= "time";
    private static final String COL_Price= "price";
    private static final String COL_Qty = "qty";
    private static final String COL_Statys = "status";


    String whereClause;
	private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME_df
			+ " ( " + COL_id + " TEXT NOT NULL, "
            + COL_Time+ " TEXT NOT NULL, "
            + COL_Price+ " TEXT NOT NULL, "
            + COL_Qty+ " TEXT NOT NULL, "
            + COL_Statys+ " TEXT NOT NULL, "
			+ " BLOB, PRIMARY KEY (id)); ";


	public DBOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_df);
		onCreate(db);
	}



	public void insertDummyDf(List<DB_df> sites_dummy) {
        SQLiteDatabase db = getWritableDatabase();
       // db.delete(TABLE_NAME_df, null, null);
        ArrayList<ContentValues> values = new ArrayList<ContentValues>();
        for (DB_df site : sites_dummy) {
            ContentValues row = new ContentValues();
            row.put(COL_id, site.getId());
            row.put(COL_Time, site.getTime());
            row.put(COL_Price, site.getPrice());
            row.put(COL_Qty, site.getQty());
            row.put(COL_Statys, site.getStatus());
            values.add(row);
        }

        for (ContentValues row : values) {
//                db.insert(TABLE_NAME_df, null, row);
            db.insertWithOnConflict(TABLE_NAME_df, null, row, SQLiteDatabase.CONFLICT_REPLACE);
        }
        db.close();
    }


    public void updateDummyData(List<DB_df> sites_dummy )
    {
        SQLiteDatabase db = getWritableDatabase();
//		db.delete(TABLE_NAME_df, null, null);
        ArrayList<ContentValues> values = new ArrayList<ContentValues>();
        for (DB_df site : sites_dummy) {
            ContentValues row = new ContentValues();
            row.put(COL_id, site.getId());
            row.put(COL_Time, site.getTime());
            row.put(COL_Price, site.getPrice());
            row.put(COL_Qty, site.getQty());
            row.put(COL_Statys, site.getStatus());
            whereClause = COL_id + "='" + site.getId() + "'";
            values.add(row);
        }

        for (ContentValues row : values) {
            db.update(TABLE_NAME_df, row,whereClause, null);
        }
        db.close();

    }





	public ArrayList<DB_df> getAllSites_df() {
		SQLiteDatabase db = getReadableDatabase();
		String[] columns = { COL_id,COL_Time,COL_Price,COL_Qty,COL_Statys};
		Cursor cursor = db.query(TABLE_NAME_df, columns, null, null, null, null,null,null);
		ArrayList<DB_df> sites = new ArrayList<DB_df>();
		while (cursor.moveToNext()) {
			String id = cursor.getString(0);
            String time= cursor.getString(1);
            String price= cursor.getString(2);
            String qty= cursor.getString(3);
            String status= cursor.getString(4);
            DB_df site = new DB_df(id,time,price,qty,status);
			sites.add(site);

		}
		cursor.close();
		db.close();
		return sites;
	}



    public ArrayList<DB_df> searchDB(String key){
        String selection;
        String[] columns = { COL_id,COL_Time,COL_Price,COL_Qty,COL_Statys};String[] agr={key};
        selection=columns[0]+"=?";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_df, columns,selection, agr, null, null, null);
        ArrayList<DB_df> sites = new ArrayList<DB_df>();
        while (cursor.moveToNext()) {
            // 讀取包裝一筆資料的物件
            String id = cursor.getString(0);
            String time= cursor.getString(1);
            String price= cursor.getString(2);
            String qty= cursor.getString(3);
            String status= cursor.getString(4);
            DB_df site = new DB_df(id,time,price,qty,status);
            sites.add(site);
        }
        cursor.close();
        db.close();
        return sites;
    }


    public void deleteDB(String id){
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = COL_id + "='" + id + "'";
        db.delete(TABLE_NAME_df, whereClause, null);
        db.close();
    }

}
