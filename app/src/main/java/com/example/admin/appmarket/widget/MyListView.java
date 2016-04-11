package com.example.admin.appmarket.widget;

import android.content.Context;
import android.graphics.Color;
import android.widget.ListView;

public class MyListView extends ListView {

	public MyListView(Context context) {
		super(context);
		
		initListView();
	}

	private void initListView() {
		//1,去掉listview的条目间的分割线
		this.setDivider(null);
		//2,去掉listview某一个条目的选中背景
		this.setSelector(android.R.color.transparent);
		//3,去掉listview滚动过程中黑色背景
		this.setCacheColorHint(Color.TRANSPARENT);
	}

}
