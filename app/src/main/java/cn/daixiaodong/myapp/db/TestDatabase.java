package cn.daixiaodong.myapp.db;

import android.content.Context;
import android.util.Log;

import java.sql.SQLException;
import java.util.List;

import cn.daixiaodong.myapp.bean.PushMessage;

/**
 * Created by daixiaodong on 15/7/21.
 */
public class TestDatabase {

    public static void testAdd(Context context) {
        PushMessage pushMessage = new PushMessage("xxxxxxx");
        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        try {
            helper.getPushMessageDao().create(pushMessage);
            List<PushMessage> pushMessages = helper.getPushMessageDao().queryForAll();
            for (PushMessage message : pushMessages) {
                Log.i("message", message.getMessage());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
