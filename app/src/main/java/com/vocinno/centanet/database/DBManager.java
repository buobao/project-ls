package com.vocinno.centanet.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vocinno.centanet.model.ChoosePeople;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/17.
 */
public class DBManager extends SQLiteOpenHelper{
    private static final String DB_NAME = "choosepeople.db"; //数据库名称
    private static final int version = 1; //数据库版本
    private DBManager(Context context) {
        super(context, DB_NAME, null, version);
    }
    public static DBManager getDBManager(Context context){
        return  new DBManager(context);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table choosepeople (_id INTEGER PRIMARY KEY AUTOINCREMENT ,userId TEXT, realName TEXT, orgName TEXT, orgId TEXT, jobCode TEXT, jobName TEXT,updatetime TimeStamp NOT NULL DEFAULT (datetime('now','localtime')))");
        /*"jobCode": "JFHJL",
            "jobName": "分行经理",
            "orgId": "8a8493d553ad875901544cc01fb40df9",
            "orgName": "普陀1组",
            "realName": "周欢",
            "userId": "169F8C84F44544D39C3D3B3175E386E4"*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    /*"jobCode": "JFHJL",
                "jobName": "分行经理",
                "orgId": "8a8493d553ad875901544cc01fb40df9",
                "orgName": "普陀1组",
                "realName": "周欢",
                "userId": "169F8C84F44544D39C3D3B3175E386E4"
                userId,realName,orgName,orgId,jobCode,jobName
                */
    public void addPeople(ChoosePeople people){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("insert into choosepeople(userId,realName,orgName,orgId,jobCode,jobName) values (?,?,?,?,?,?)",
          new Object[]{people.getUserId(),people.getRealName(),people.getOrgName(),people.getOrgId(),people.getJobCode(),people.getJobName()});
        db.close();
    }
    public void updatePeople(ChoosePeople people){
        SQLiteDatabase db = this.getWritableDatabase();
        String userId=people.getUserId();
        db.execSQL("update choosepeople set updatetime=datetime('now','localtime') where userId=?",new String[]{userId});
        db.close();
    }
    public void deletePeople(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from choosepeople where _id=(select _id from choosepeople order by updatetime Limit 1)",null);
        db.close();
    }
    public int queryPeople(ChoosePeople people){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) as num from choosepeople where userId = ?", new String[]{people.getUserId()});
        int count=0;
        while(cursor.moveToNext()){
            count=cursor.getInt(cursor.getColumnIndex("num"));
        }
        cursor.close();
        db.close();
        return count;
    }
    public int queryCount(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) as num from choosepeople ", null);
        int count=0;
        while(cursor.moveToNext()){
            count=cursor.getInt(cursor.getColumnIndex("num"));
        }
        cursor.close();
        db.close();
        return count;
    }
    public List<ChoosePeople> queryAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from choosepeople order by updatetime desc Limit 5", null);
        List<ChoosePeople>peopleList=new ArrayList<ChoosePeople>();
        while(cursor.moveToNext()){
            ChoosePeople people=new ChoosePeople();
            //userId,realName,orgName,orgId,jobCode,jobName
            people.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
            people.setRealName(cursor.getString(cursor.getColumnIndex("realName")));
            people.setOrgName(cursor.getString(cursor.getColumnIndex("orgName")));
            people.setOrgId(cursor.getString(cursor.getColumnIndex("orgId")));
            people.setJobCode(cursor.getString(cursor.getColumnIndex("jobCode")));
            people.setJobName(cursor.getString(cursor.getColumnIndex("jobName")));
            peopleList.add(people);
        }
        cursor.close();
        db.close();
        return peopleList;
    }
}
