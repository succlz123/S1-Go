package org.succlz123.s1go.app.support.swingindicator;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ParamsCreator {
	private Context context;
    private int screenWidth;//屏幕宽度
    private int screenHeight;//屏幕高度
    private int densityDpi;//像素密度
    public ParamsCreator(Context context){
    	this.context = context;
    	WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    	screenWidth = wm.getDefaultDisplay().getWidth();
    	screenHeight = wm.getDefaultDisplay().getHeight();
    	DisplayMetrics metric = new DisplayMetrics();
    	wm.getDefaultDisplay().getMetrics(metric);
    	densityDpi = metric.densityDpi;
    }
    
	/**
	 * 获得默认圆的半径
	 */
	public int getDefaultCircleRadius(){
    	if(screenWidth >= 1400){//1440
    		return 30;
    	}
    	if(screenWidth >= 1000){//1080
    		if(densityDpi >=480)
        		return 30;
        	if(densityDpi >= 320)
        		return 30;
        	return 30;
    	}
    	if(screenWidth >= 700){//720
        	if(densityDpi >= 320)
        		return 18;
        	if(densityDpi >= 240)
        		return 18;
        	if(densityDpi >= 160)
        		return 18;
        	return 18;
    	}
    	if(screenWidth >= 500){//540
        	if(densityDpi >= 320)
        		return 15;
        	if(densityDpi >= 240)
        		return 15;
        	if(densityDpi >= 160)
        		return 15;
        	return 15;
    	}
    	return 15;
	}
	/**
	 * 获得默认摆动半径
	 */
	public int getDefaultSwingRadius(){
    	if(screenWidth >= 1400){//1440
    		return 140;
    	}
    	if(screenWidth >= 1000){//1080
    		if(densityDpi >=480)
        		return 140;
        	if(densityDpi >= 320)
        		return 140;
        	return 140;
    	}
    	if(screenWidth >= 700){//720
        	if(densityDpi >= 320)
        		return 90;
        	if(densityDpi >= 240)
        		return 90;
        	if(densityDpi >= 160)
        		return 90;
        	return 90;
    	}
    	if(screenWidth >= 500){//540
        	if(densityDpi >= 320)
        		return 70;
        	if(densityDpi >= 240)
        		return 70;
        	if(densityDpi >= 160)
        		return 70;
        	return 70;
    	}
    	return 70;
	}
}
