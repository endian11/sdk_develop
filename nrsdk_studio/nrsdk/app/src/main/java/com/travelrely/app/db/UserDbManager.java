package com.travelrely.app.db;

import java.util.concurrent.atomic.AtomicInteger;

import com.travelrely.core.nrs.Engine;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDbManager
{
    private AtomicInteger mOpenCounter = new AtomicInteger();

    private static UserDbManager instance;
    private static SQLiteOpenHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;

    public static synchronized void init(SQLiteOpenHelper helper)
    {
        if (instance == null)
        {
            instance = new UserDbManager();
            mDatabaseHelper = helper;
        }
    }

    public static synchronized UserDbManager getInstance()
    {
        if (instance == null)
        {
            instance = new UserDbManager();
            mDatabaseHelper = new UserDBOpenHelper(Engine.getInstance()
                    .getContext(), Engine.getInstance().getUserName());
        }

        return instance;
    }

    public synchronized SQLiteDatabase openDb()
    {
        if (mOpenCounter.incrementAndGet() == 1)
        {
            // Opening new database
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    public synchronized void closeDb()
    {
        if (mOpenCounter.decrementAndGet() == 0)
        {
            // Closing database
            mDatabase.close();
        }
    }
}
