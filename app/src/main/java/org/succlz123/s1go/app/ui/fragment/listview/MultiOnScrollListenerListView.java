package org.succlz123.s1go.app.ui.fragment.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by fashi on 2015/6/27.
 */
public class MultiOnScrollListenerListView extends ListView {

	private ArrayList<OnScrollListener> onScrollListeners = new ArrayList<>();

	private ListView.OnScrollListener onScrollListener = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			for (OnScrollListener onScrollListener : onScrollListeners) {
				onScrollListener.onScrollStateChanged(view, scrollState);
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			for (OnScrollListener onScrollListener : onScrollListeners) {
				onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
			}
		}
	};

	public MultiOnScrollListenerListView(Context context) {
		super(context);
		super.setOnScrollListener(onScrollListener);
	}

	public MultiOnScrollListenerListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		super.setOnScrollListener(onScrollListener);
	}

	public MultiOnScrollListenerListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		super.setOnScrollListener(onScrollListener);
	}

	public MultiOnScrollListenerListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		super.setOnScrollListener(onScrollListener);
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		this.onScrollListeners.add(l);
	}
}
