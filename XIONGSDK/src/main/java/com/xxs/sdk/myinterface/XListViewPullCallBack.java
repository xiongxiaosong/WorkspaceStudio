package com.xxs.sdk.myinterface;

/**
 * 自定义XListView回调接口
 * 
 * @author xiongxs
 * @date 2014-10-11
 * @introduce 包括下拉刷新，上拉更多两个过掉接口
 */
public interface XListViewPullCallBack {
	/**
	 * 开始下拉刷新回调接口
	 * <p/>
	 * 在这个方法里面添加需要执行的下拉刷新操作 <br/>
	 * 注意2：如果该方法要执行UI修改操作请放到主线程里面执行
	 */
	public void onStartPullDownBack();

//	/**
//	 * 下拉刷新完成回调接口
//	 * <p/>
//	 * 这里需要执行隐藏头部和底部文件的操作通过调用resetHeadAndFoot()方法实现 <br/>
//	 * 注意2：如果该方法要执行UI修改操作请放到主线程里面执行
//	 */
//	public void onPullDownFinishBack();

	/**
	 * 开始上拉更多回调接口
	 * <p/>
	 * 在这个方法里面添加需要执行的上拉更多操作 <br/>
	 * 注意2：如果该方法要执行UI修改操作请放到主线程里面执行
	 */
	public void onStratPullUpBack();

//	/**
//	 * 上拉更多完成回调接口
//	 * <p/>
//	 * 这里需要执行隐藏头部和底部文件的操作通过调用resetHeadAndFoot()方法实现 <br/>
//	 * 注意2：如果该方法要执行UI修改操作请放到主线程里面执行
//	 */
//	public void onPullUpFinishBack();
}
