package org.succlz123.s1go.app.support.widget.swingindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import org.succlz123.s1go.app.R;

public class SwingIndicator extends View{
	private ParamsCreator paramsCreator = new ParamsCreator(this.getContext());
	private CircleWrapper leftWrapper;
	private CircleWrapper rightWrapper;
	private Paint paint = new Paint();
	private RectF oval=new RectF();
	//属性
	private int circleRadius;//圆半径
	private int swingRadius;//摆动半径
	private int circleColor;//圆的颜色
	private int increment = 4;//增量

	public SwingIndicator(Context context) {
		super(context);
	}
	public SwingIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.swingindicator);
		circleRadius = (int)a.getDimension(R.styleable.swingindicator_circleRadius, paramsCreator.getDefaultCircleRadius());
		swingRadius = (int)a.getDimension(R.styleable.swingindicator_swingRadius, paramsCreator.getDefaultSwingRadius());

		circleColor = (int)a.getColor(R.styleable.swingindicator_circleColor, 0);
		if(circleColor == 0)
			circleColor = a.getResourceId(R.styleable.swingindicator_circleColor, 0);
		if(circleColor == 0)
			circleColor = 0xFF7A97EA;

		int cycle = a.getInt(R.styleable.swingindicator_cycle, 400);//周期，默认为2秒
		cycle = cycle/2;
		int number = (int)(cycle*1.0/1000 * 83);
		this.increment = (int)(this.swingRadius / number);
		this.increment = this.increment<=0?1:this.increment;
	}
	/**
	 * 测绘
	 */
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }
	/**
     * 计算组件宽度
     */
    private int measureWidth(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
        	result = getDefaultWidth();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
    /**
     * 计算组件高度
     */
    private int measureHeight(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = getDefaultHeight();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
    /**
     * 计算默认宽度
     */
    private int getDefaultWidth(){
    	int diameter = this.circleRadius * 2;
    	int defaultWidth = diameter * 7 + this.swingRadius * 2;
    	return defaultWidth;
    }
    /**
     * 计算默认宽度
     */
    private int getDefaultHeight(){
    	return this.swingRadius + this.circleRadius * 2;
    }
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//canvas.drawColor(0xFF00FF33);
		if(this.leftWrapper == null)
			createWrappers();
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(this.circleColor);
		drawStatic(canvas);
		drawLeftDynamicCircle(canvas);
		drawRightDynamicCircle(canvas);
		this.invalidate();
	}
	/**
	 * 创建wrappers
	 */
	private void createWrappers(){
		int diameter = this.circleRadius * 2;//直径
		int totalWidth = diameter * 7 + this.swingRadius * 2;
		int totalHeight = this.swingRadius + diameter;
		//左边的摆圆
		CircleWrapper wrapper = new CircleWrapper();
		wrapper.diameter = diameter;
		wrapper.maxTop = this.getHeight()/2 + this.swingRadius/2;
		wrapper.minTop = wrapper.maxTop - this.swingRadius;
		wrapper.dynamicTop = wrapper.minTop;
		wrapper.minLeft = this.getWidth()/2 - diameter * 3 - this.swingRadius;
		wrapper.maxLeft = wrapper.minLeft + this.swingRadius;
		wrapper.orientation = 1;
		wrapper.active = true;
		this.leftWrapper = wrapper;
		//右边的摆圆
		wrapper = new CircleWrapper();
		wrapper.diameter = diameter;
		wrapper.maxTop = this.getHeight()/2 + this.swingRadius/2;
		wrapper.minTop = wrapper.maxTop - this.swingRadius;
		wrapper.dynamicTop = wrapper.maxTop;
		wrapper.minLeft = this.getWidth()/2 + this.circleRadius * 6;
		wrapper.maxLeft = wrapper.minLeft + this.swingRadius;
		wrapper.orientation = -1;
		wrapper.active = false;
		this.rightWrapper = wrapper;
	}
	/**
	 * 画静态的圆
	 */
	private void drawStatic(Canvas canvas){
		int diameter = this.circleRadius * 2;//直径
		int left = this.getWidth()/2 - this.circleRadius * 5;
		int top = this.getHeight()/2 + this.swingRadius/2 - this.circleRadius;
		for(int i = 0; i<5; i++){
			oval.left = left;
			oval.top = top;
			oval.right = oval.left + diameter;
			oval.bottom = oval.top + diameter;
			canvas.drawArc(oval, 0, 360, false, paint);
			left += diameter;
		}
	}
	/**
	 * 画左边的动态源
	 */
	private void drawLeftDynamicCircle(Canvas canvas){
		CircleWrapper wrapper = this.leftWrapper;
		int diameter = this.circleRadius * 2;//直径
		int y = wrapper.maxTop-wrapper.dynamicTop;
		int x = (int)Math.sqrt(Math.pow(this.swingRadius, 2) - Math.pow(y-this.swingRadius, 2)) - this.swingRadius;
		oval.left = wrapper.minLeft + Math.abs(x) - this.circleRadius;
		oval.top = wrapper.dynamicTop - this.circleRadius;
		oval.right = oval.left + diameter;
		oval.bottom = oval.top + diameter;
		canvas.drawArc(oval, 0, 360, false, paint);
		if(!wrapper.active)
			return ;
		wrapper.dynamicTop += this.increment*wrapper.orientation;
		if(wrapper.dynamicTop > wrapper.maxTop)
			wrapper.dynamicTop = wrapper.maxTop;
		if(wrapper.dynamicTop < wrapper.minTop)
			wrapper.dynamicTop = wrapper.minTop;
		if(wrapper.dynamicTop == wrapper.maxTop){
			wrapper.active = false;
			this.rightWrapper.active = true;
			wrapper.orientation = -1;
		}
		if(wrapper.dynamicTop == wrapper.minTop){
			wrapper.orientation *= -1;
		}
	}
	/**
	 * 画右边的动态源
	 */
	private void drawRightDynamicCircle(Canvas canvas){
		CircleWrapper wrapper = this.rightWrapper;
		int diameter = this.circleRadius * 2;//直径
		int y = wrapper.maxTop-wrapper.dynamicTop;
		int x = (int)Math.sqrt(Math.pow(this.swingRadius, 2) - Math.pow(y-this.swingRadius, 2)) - this.swingRadius;
//		System.out.println("xxx="+x);
		oval.left = wrapper.minLeft - this.circleRadius + (this.swingRadius - Math.abs(x));
		oval.top = wrapper.dynamicTop - this.circleRadius;
		oval.right = oval.left + diameter;
		oval.bottom = oval.top + diameter;
		canvas.drawArc(oval, 0, 360, false, paint);
		if(!wrapper.active)
			return ;
		wrapper.dynamicTop += this.increment*wrapper.orientation;
		if(wrapper.dynamicTop > wrapper.maxTop)
			wrapper.dynamicTop = wrapper.maxTop;
		if(wrapper.dynamicTop < wrapper.minTop)
			wrapper.dynamicTop = wrapper.minTop;
		if(wrapper.dynamicTop == wrapper.maxTop){
			wrapper.active = false;
			this.leftWrapper.active = true;
			wrapper.orientation = -1;
		}
		if(wrapper.dynamicTop == wrapper.minTop){
			wrapper.orientation *= -1;
		}
	}
	/**
     * 内部类
     */
    private class CircleWrapper{
    	private int diameter;//圆的直径
    	private int minTop;//圆心的最小top值
    	private int maxTop;//圆心的最大top值
    	private int dynamicTop;//动态直径
    	private int minLeft;
    	private int maxLeft;
    	private int orientation;//方向，即增加还是减少 1:增加 -1为减少
    	private boolean active;//是否活动的
    }
}
