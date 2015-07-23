package cn.daixiaodong.myapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.io.InputStream;
import java.sql.SQLException;

import cn.daixiaodong.myapp.bean.PushMessage;

/**
 * Created by daixiaodong on 15/7/21.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {


    public DatabaseHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, TABLE_NAME, null, 1);
    }

    public DatabaseHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion, int configFileId) {
        super(context, databaseName, factory, databaseVersion, configFileId);
    }

    public DatabaseHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion, File configFile) {
        super(context, databaseName, factory, databaseVersion, configFile);
    }

    public DatabaseHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion, InputStream stream) {
        super(context, databaseName, factory, databaseVersion, stream);
    }


    private static final String TABLE_NAME = "push_message";

    private Dao<PushMessage, Integer> dao;


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, PushMessage.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.dropTable(connectionSource, PushMessage.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null) {
                    instance = new DatabaseHelper(context, null, null, 1);
                }
            }
        }
        return instance;
    }

    public Dao<PushMessage, Integer> getPushMessageDao() throws SQLException {
        if (dao == null) {
            dao = getDao(PushMessage.class);
        }

        return dao;
    }


    @Override
    public void close() {
        super.close();
        dao = null;
    }
}
