package com.hgad.warehousemanager.db.dao;

import android.content.Context;

/**
 * Created by Administrator on 2017/6/28.
 */
public class UserDao{
//    private Context context;
//    private Dao<UserInfo, Integer> userDaoOpe;
//    private DBHelper helper;

    public UserDao(Context context) {
//        super(context);
//        this.context = context;
//        try {
//            helper = DBHelper.getHelper(context);
//            userDaoOpe = helper.getDao(UserInfo.class);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }


//    private Map<Class<T>,Dao<T,Integer>> mDaoMap=new HashMap<Class<T>,Dao<T,Integer>>();
//    //缓存泛型Dao
//    public UserDao(Context context,Class<T> clazz) {
//        super(context);
//        this.clazz=clazz;
//    }
//
//    @Override
//    public Dao<T, Integer> getDao() throws SQLException {
//        Dao<T,Integer> dao=mDaoMap.get(clazz);
//        if (null==dao){
//            dao=mDatabaseHelper.getDao(clazz);
//            mDaoMap.put(clazz,dao);
//        }
//        return dao;
//    }


//    /**
//     * 增加一个用户
//     *
//     * @param user
//     */
//    public void addOrUpdate(UserInfo user) {
//        try {
//            userDaoOpe.createOrUpdate(user);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public void UpdateUser(UserInfo user) {
//        try {
//            userDaoOpe.update(user);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public List<UserInfo> findAll() {
//        List<UserInfo> userInfos = null;
//        try {
//            userInfos = userDaoOpe.queryForAll();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return userInfos;
//    }
//
//    public void deleteUser(UserInfo user) {
//        try {
//            userDaoOpe.delete(user);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public List<UserInfo> findByParam(String param, Object value) {
//        List<UserInfo> userInfos = null;
//        try {
//            userInfos = userDaoOpe.queryBuilder().where().eq(param, value).query();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return userInfos;
//    }

}
