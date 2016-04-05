package com.example.admin.appmarket.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.example.admin.appmarket.R;
import com.example.admin.appmarket.util.UIUtils;

/**
 * 图片适配自定义控件
 */
public class RatioLayout extends FrameLayout {

	private float ratio;

	public RatioLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		//获取attrs.xml文件中定义的属性
		TypedArray array = UIUtils.getContext().obtainStyledAttributes(attrs, R.styleable.RatioLayout);
		ratio = array.getFloat(R.styleable.RatioLayout_ratio, 0.0f);
	}
	//1,控件内部嵌套的imageView宽度为定值
	//2,内部imageView宽高比例在当前控件的自定义属性中指定为1:2.43  高宽比
	//3,通过宽高比得到imageView控件高度
	//4,拿到imageView高度后,加上当前控件和imageView顶端和底端的内边距就是现有控件的高度

	//1,onMeasure 2,onLayout 3,onDraw()
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//widthMeasureSpec参数包含两部分内容(1,宽度模式2,宽度大小)
		//heightMeasureSpec参数包含两部分内容(1,高度模式2,高度大小)
		//32位2机制的值(前两位代表模式(1,精确  2,至多  3,未定义),后30位代表大小)

		//1,获取当前自定义控件宽度的模式
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		//2,图片控件的精确宽度
		int imageWidthSize = widthSize-getPaddingLeft() - getPaddingRight();
		
		//3,获取自定义控件的高度的模式,当前获取的是非精确的,后期要将其高度计算出来,然后修改成精确
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		
		if(widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY && ratio!=0.0f){
			int heightSize = (int) (imageWidthSize/ratio+getPaddingBottom()+getPaddingTop());
			//重新构建当前控件高度的32位数的模式和精确的数值
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
		}
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
