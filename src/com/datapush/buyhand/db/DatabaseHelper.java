package com.datapush.buyhand.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.datapush.buyhand.common.Constants;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * 数据库处理类
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public DatabaseHelper(Context context) {
		super(context, Constants.DB_NAME, null, 1);
	}

	/**
	 * 初始化操作：建表
	 * 
	 * @see com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase,
	 *      com.j256.ormlite.support.ConnectionSource)
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {

		createTables(db, connectionSource);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		if (oldVersion == 1) {


		}

	}

	private void createTables(SQLiteDatabase db, ConnectionSource connectionSource) {
	}

}