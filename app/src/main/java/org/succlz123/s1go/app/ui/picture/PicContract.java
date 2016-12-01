package org.succlz123.s1go.app.ui.picture;

import org.succlz123.s1go.app.BaseDataSource;
import org.succlz123.s1go.app.BasePresenter;
import org.succlz123.s1go.app.BaseView;

import android.graphics.Bitmap;

import rx.Observable;

/**
 * Created by succlz123 on 2015/4/14.
 */

public interface PicContract {

    interface View extends BaseView<Presenter> {

        void setBitmapSource(String file, Bitmap bitmap);

        void onSaveSuccess();

        void onSaveFailed();
    }

    interface Presenter extends BasePresenter {

        void getBitmap(String url);

        void savePic(String url);
    }

    interface DataSource extends BaseDataSource {

        Observable<Object> getBitmap(String url);

        Observable<Boolean> savePic(String url);
    }
}
