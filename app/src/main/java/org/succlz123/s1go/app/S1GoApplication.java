package org.succlz123.s1go.app;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Handler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import org.succlz123.s1go.app.bean.login.LoginVariables;
import org.succlz123.s1go.app.dao.Helper.S1DatabaseHelper;
import org.succlz123.s1go.app.dao.Helper.S1FidImgHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fashi on 2015/4/13.
 */
public class S1GoApplication extends Application {

    private static S1GoApplication instance;
    private LoginVariables loginVariables;
    private Handler handler = new Handler();

    public static interface UserInfoListener {
        public void onInfoChanged(LoginVariables loginVariables);
    }

    private List<UserInfoListener> userInfoListenerList = new ArrayList<UserInfoListener>();

    public static S1GoApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        S1FidImgHelper.getS1FidImg();//获得论坛板块名图标

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()

                .showImageOnLoading(R.drawable.noavatar)   //默认图片
                .showImageForEmptyUri(R.drawable.noavatar)    //url爲空會显示该图片，自己放在drawable里面的
                .showImageOnFail(R.drawable.noavatar)// 加载失败显示的图片
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(1000)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .displayer(new SimpleBitmapDisplayer()) // default
                .handler(new Handler()) // default
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultOptions)  //上面的options对象，一些属性配置
                .build();

        ImageLoader.getInstance().init(config);
    }

    public void addUserInfo(final LoginVariables loginVariables) {
        this.loginVariables = loginVariables;
        S1DatabaseHelper.getInstance().insert(loginVariables);//插入用户登录数据
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (UserInfoListener userInfoListener : userInfoListenerList) {
                    userInfoListener.onInfoChanged(loginVariables);
                }
            }
        });
    }

    public void removeUserInfo() {
        S1DatabaseHelper.getInstance().delete();//删除
        this.loginVariables = null;
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (UserInfoListener userInfoListener : userInfoListenerList) {
                    userInfoListener.onInfoChanged(null);
                }
            }
        });
    }

    public void addUserInfoListener(UserInfoListener userInfoListener) {
        this.userInfoListenerList.add(userInfoListener);
    }

    public void removeUserInfoListener(UserInfoListener userInfoListener) {
        this.userInfoListenerList.remove(userInfoListener);
    }

    public LoginVariables getUserInfo() {
        if (this.loginVariables == null) {
            this.loginVariables = S1DatabaseHelper.getInstance().get();//获取数据库里的用户数据
        }
        return this.loginVariables;
    }

    public void runOnUIThread(Runnable runnable) {
        handler.post(runnable);//从子线程取出toast 放在ui线程更新显示toast
    }
}
