package org.succlz123.s1go.app;

/**
 * Created by succlz123 on 2016/11/28.
 */

public interface BaseView<T> {

    void setPresenter(T presenter);

    boolean isActive();

}
