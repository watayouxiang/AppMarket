package com.example.admin.appmarket.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.appmarket.util.UIUtils;

//FlowerLayout中添加的是带背景TextView,所以TextView就是FlowerLayout直接子节点
public class FlowerLayout extends ViewGroup {
	//管理行对象,有此集合就可以存储所有的行对象,后期可以循环遍历此集合用于获取每一个行对象,行对象的高度属性做叠加+行间距得到的就是所有行用的高度
	private List<Line> lineList = new ArrayList<Line>();
	private Line line;
	private int usedWidth;
	//控件和控件水平方向上的间距值大小
	private int horizontolSpacing = UIUtils.dip2px(10);
	private int verticalSpacing = UIUtils.dip2px(10);

	public FlowerLayout(Context context) {
		super(context);
	}

	//定义每一个行对应的控件所在屏幕未知,当前行所在位置
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		//1,获取左上角的内边距paddingLeft paddingTop
		int left = getPaddingLeft();
		int top = getPaddingTop();
		
		//2,循环遍历放置行对象的集合,去放置每一个行对象的所在位置
		for(int i=0;i<lineList.size();i++){
			Line line = lineList.get(i);
			line.layout(left, top);
			top += line.lineHeight+verticalSpacing;
		}
	}

	
	//1,onMeasure 2,onlayout(每一个FlowerLayout内部的textView,都需要去定义对应位置)  3,onDraw
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//widthMeasureSpec  32位数 前2位的具体值
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		
		//当前控件内部,一行可用的大小
		int widthSize = MeasureSpec.getSize(widthMeasureSpec)-getPaddingLeft() - getPaddingRight();
		//当前控件内部所有行的可用高度
		int heightSize = MeasureSpec.getSize(heightMeasureSpec)-getPaddingTop() - getPaddingBottom();
		//创建对应的一个行对象,用于比对行对象对应的宽度和控件使用的宽度
		restoreLine();
		//准备去添加每一个行的view对象
		//获取当前控件子节点的总个数
		int count = getChildCount();
		for(int i=0;i<count;i++){
			//获取索引位置的直接子节点对应的view
			View childView = getChildAt(i);
			//限定当前childView的宽高都不能超过其夫控件
			
			//自定义控件换行规则中的第3点
			
			//以下规则都需要作用到childView中
			//如果夫控件宽度模式为精确,子控件模式至多,如果夫控件为至多,子控件至多,夫控件是未定义,子控件未定义
			int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
					widthSize, widthMode == MeasureSpec.EXACTLY?MeasureSpec.AT_MOST:widthMode);
			int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
					heightSize, heightMode == MeasureSpec.EXACTLY?MeasureSpec.AT_MOST:heightMode);
			
			childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
			
			if(line==null){
				line = new Line();
			}
			//获取循环过程中控件的宽度值
			int childWidth = childView.getMeasuredWidth();
			usedWidth += childWidth;
			//当前控件加到行中去以后,小于行可用宽度
			if(usedWidth<widthSize){
				//当前的view可以加到当前行对象中
				line.addView(childView);
				//自定义控件换行的第2中情况
//				usedWidth+行中的水平方向间距>可用行宽度,下一个控件就需要去换行放置
				usedWidth += horizontolSpacing;
				if(usedWidth>widthSize){
					//换行,准备放置下面的控件
					if(!newLine()){
						//当前控件添加进去以后,就需要开启一个新的行,去放置后续的控件(下一次循环过程)
						break;
					}
				}
			}else{
				//当前view对应的宽度大于或者等于行宽度
				if(line.getLineViewCount() == 0){
					//当前行中没有view对象,不管遍历到的view对象有多长

					//等同图片中的第3中添加情形
					line.addView(childView);
					if(!newLine()){
						//当前控件添加进去以后,就需要开启一个新的行,去放置后续的控件(下一次循环过程)
						break;
					}
				}else{
					//图解中第1中情况
					//换行,放置后续的控件
					if(!newLine()){
						//当前控件添加进去以后,就需要开启一个新的行,去放置后续的控件(下一次循环过程)
						break;
					}
					line.addView(childView);
					usedWidth += childWidth+horizontolSpacing; 
				}
			}
		}
		
		//容错处理(最后的一个行对象,一定也要让其添加到行集合中)
		if(line!=null && line.getLineViewCount()>0 && !lineList.contains(line)){
			lineList.add(line);
		}
		
		//以上都是去在行对象中放置相应的view的逻辑
		
		//测量放置上行对象内部的view之后,此指定控件的宽度值
		int flowerWidth = MeasureSpec.getSize(widthMeasureSpec);
		
		
		//测量自定义控件的高度(顶端内边距+底端内边距+所有行的高度+所有行竖直间距) = 当前自定义控件高度
		int paddingTotal = getPaddingTop()+getPaddingBottom();
		int flowerHeight = 0;
		//循环变量行对象所在的集合
		for(int i=0;i<lineList.size();i++){
			flowerHeight += lineList.get(i).lineHeight;
		}
		
		int verticalSpacingTotal =(lineList.size()-1)*verticalSpacing;
		//自定义控件要去用到的高度
		flowerHeight = paddingTotal+flowerHeight+verticalSpacingTotal;
		
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//详情定义自定义控件的(过滤掉了模式)宽高做法,
		setMeasuredDimension(flowerWidth, resolveSize(flowerHeight, heightMeasureSpec));
	}
	
	private boolean newLine() {
		//1,行添加到管理行对象的集合中
		lineList.add(line);
		if(lineList.size()<100){
			//2,新起一行,创建一个行对象
			line = new Line();
			//3,新行对象使用宽度归0操作
			usedWidth = 0;
			//创建行对象成功
			return true;
		}
		//创建行对象失败
		return false;
	}

	private void restoreLine() {
		//将行对象原有的行统统清空
		lineList.clear();
		//创建一个准备去添加view的行对象
		line = new Line();
		//当前行使用宽度的临时变量
		usedWidth = 0;
	}

	class Line{
		//一行的高度
		private int lineHeight;
		//一行已用的宽度
		private int widthTotal;
		
		//创建一个存储行对象中view的集合
		private List<View> viewList = new ArrayList<View>();
		
		//返回当前行中view总个数的方法
		public int getLineViewCount(){
			return viewList.size();
		}
		
		//向当前行添加一个方格(View)方法
		public void addView(View child){
			int childViewWidth = child.getMeasuredWidth();
			int childViewHeight = child.getMeasuredHeight();
			//当前行的高度,是由行中最高的那个view决定的
			lineHeight = lineHeight>childViewHeight?lineHeight:childViewHeight;
			//当前行中所有的view的宽度做一个叠加,放置在widthTotal中
			widthTotal +=  childViewWidth;
			//将view对象添加到当前行的集合中去
			viewList.add(child);
		}
		
		//定义当前行中每一个view对象位置的方法
		public void layout(int l,int t){
			int left = l;
			int top = t;
			
			//逐一获取行集合中的每一个view对象,然后去设置其位置
			//处理当前行中竖直和水平留白区域(行的宽度-当前内部控件使用的宽度-行中控件总间隔宽度-左侧内边距-右侧内边距)
			int width = getMeasuredWidth()-getPaddingLeft() - getPaddingRight();
			int surplusWidthTotal = width - widthTotal - (viewList.size()-1)*horizontolSpacing;
			//每一个控件可以额外分配到的宽度
			int surplusWidth = (int)(surplusWidthTotal/getLineViewCount()+0.5);
			if(surplusWidth>0){
				for(int i=0;i<viewList.size();i++){
					View childView = viewList.get(i);
					
					int childWidth = childView.getMeasuredWidth();
					childWidth += surplusWidth;
					int childHeight = childView.getMeasuredHeight();
					
					//view对象测量过程
					int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
					int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
					
					childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
					
					//竖直方向上控件需要居中显示,(高的控件高度-矮的控件高度)/2 = 矮的控件要去添加的值
					int surplusHeightHalf = (int)((lineHeight - childHeight)/2+0.5);
					
					//准备水平方向的控件位置
					//准备竖直方向的控件位置
					
					childView.layout(left, top+surplusHeightHalf, left+childWidth, top+surplusHeightHalf+childHeight);
					left += childWidth+horizontolSpacing;
				}
			}else{
				View view = viewList.get(0);
				int viewheight = view.getMeasuredHeight();
				int viewWidth = view.getMeasuredWidth();
				
				view.layout(left, top, left+viewWidth, top+viewheight);
			}
		}
	}
}
