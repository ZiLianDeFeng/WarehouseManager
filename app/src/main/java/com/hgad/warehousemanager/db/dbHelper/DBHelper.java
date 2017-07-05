package com.hgad.warehousemanager.db.dbHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.hgad.warehousemanager.bean.UserInfo;
import com.hgad.warehousemanager.util.DatabaseUtil;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/28.
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {
    private static final String TABLE_NAME = "ware-house.db";

    private Map<String, Dao> daos = new HashMap<String, Dao>();

    private DBHelper(Context context) {
        super(context, TABLE_NAME, null, 9);
    }

    @Override
    public void onCreate(SQLiteDatabase database,
                         ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, UserInfo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,
                          ConnectionSource connectionSource, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            DatabaseUtil.upgradeTable(database, connectionSource, UserInfo.class, DatabaseUtil.OPERATION_TYPE.ADD);
            onCreate(database, connectionSource);
        }
    }

    private static DBHelper instance;

    /**
     * 单例获取该Helper
     *
     * @param context
     * @return
     */
    public static synchronized DBHelper getHelper(Context context) {
        context = context.getApplicationContext();
        if (instance == null) {
            synchronized (DBHelper.class) {
                if (instance == null)
                    instance = new DBHelper(context);
            }
        }

        return instance;
    }

    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();

        if (daos.containsKey(className)) {
            dao = daos.get(className);
        }
        if (dao == null) {
            dao = super.getDao(clazz);
            daos.put(className, dao);
        }
        return dao;
    }

    /**
     * 释放资源
     */
    @Override
    public void close() {
        super.close();

        for (String key : daos.keySet()) {
            Dao dao = daos.get(key);
            dao = null;
        }
    }

}
