package com.example.testandroid.manager;

import android.os.Looper;
import android.util.Log;

import com.example.testandroid.MainApplication;
import com.example.testandroid.bean.User;
import com.example.testandroid.dao.UserDao;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UserManager {

    private static final class ClassLoader {
        private static final UserManager instance = new UserManager();
    }

    public static UserManager getInstance(){
        return ClassLoader.instance;
    }

    private UserDao userDao;
    private List<User> userList;
    private List<UserListener> userListenerList;

    private UserManager(){
        userListenerList = new ArrayList<>();
    }

    private UserDao getUserDao(){
        if (userDao == null) {
            userDao = MainApplication.getInstance().getAppDataBase().userDao();
        }
        return userDao;
    }

    public void addListener(UserListener userListener){
        if (userListener == null) {
            return;
        }
        userListenerList.add(userListener);
    }

    public void removeListener(UserListener userListener){
        if (userListener == null) {
            return;
        }
        userListenerList.remove(userListener);
    }

    public void updateUser(final User user){
        Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(ObservableEmitter<User> emitter) {
               try {
                   emitter.onNext(user);
                   emitter.onComplete();
               }catch (Exception e){
                   e.printStackTrace();
                   emitter.onError(e);
               }
            }
        }).subscribeOn(Schedulers.single())
        .subscribe(new Observer<User>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(UserManager.class.getSimpleName(),"updateUser onSubscribe MainThread="+(Thread.currentThread() == Looper.getMainLooper().getThread()));
            }

            @Override
            public void onNext(User user) {
                Log.d(UserManager.class.getSimpleName(),"updateUser onNext MainThread="+(Thread.currentThread() == Looper.getMainLooper().getThread()));
                User saveUser = getUserDao().getUserById(user.getId());
                if (saveUser == null) {
                    getUserDao().insertUser(user);
                }else {
                    getUserDao().updateUser(user);
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.d(UserManager.class.getSimpleName(),"updateUser onError MainThread="+(Thread.currentThread() == Looper.getMainLooper().getThread()));

            }

            @Override
            public void onComplete() {
                Log.d(UserManager.class.getSimpleName(),"updateUser onComplete MainThread="+(Thread.currentThread() == Looper.getMainLooper().getThread()));
                notifyUpdateCompleteUser(user);
            }
        });
    }


    public void getAllUser(){
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) {
                try {
                    emitter.onNext(true);
                    emitter.onComplete();
                }catch (Exception e){
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
        .subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(UserManager.class.getSimpleName(),"getAllUser onSubscribe MainThread="+(Thread.currentThread() == Looper.getMainLooper().getThread()));

            }

            @Override
            public void onNext(Boolean aVoid) {
                Log.d(UserManager.class.getSimpleName(),"getAllUser onNext MainThread="+(Thread.currentThread() == Looper.getMainLooper().getThread()));
                userList = getUserDao().getAllUser();
            }

            @Override
            public void onError(Throwable e) {
                Log.d(UserManager.class.getSimpleName(),"getAllUser onError MainThread="+(Thread.currentThread() == Looper.getMainLooper().getThread()));
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                Log.d(UserManager.class.getSimpleName(),"getAllUser onComplete MainThread="+(Thread.currentThread() == Looper.getMainLooper().getThread()));
                notifyQueryCompleteUserList(userList);
            }
        });
    }


    private void notifyQueryCompleteUserList(List<User> userList){
        for (UserListener userListener : userListenerList){
            userListener.onQueryCompleteUserList(userList);
        }
    }

    private void notifyUpdateCompleteUser(User user){
        for (UserListener userListener : userListenerList){
            userListener.onUpdateCompleteUser(user);
        }
    }

    public interface UserListener{
        void onQueryCompleteUserList(List<User> userList);
        void onUpdateCompleteUser(User user);
    }
}
