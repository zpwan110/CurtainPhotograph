package base.util;

import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtil {


	public static void showToast(String str, Context context) {
		Toast toast = Toast.makeText(context, str, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
		toast.show();
	}

	protected static void showToastResources(int id, Context context) {
		showToast(context.getResources().getString(id), context);
	}

	protected static void showToastFailPic(Application mAppContext,String message){
		Toast mSuccessToast = null;
		if(mSuccessToast==null){
			mSuccessToast = Toast.makeText(mAppContext, message,
					Toast.LENGTH_SHORT);
			mSuccessToast.setGravity(Gravity.CENTER, 0, -0);
		}else{
			mSuccessToast.setText(message);
			mSuccessToast.setDuration(Toast.LENGTH_SHORT);
		}
		mSuccessToast.show();
	}
	protected static void showToastFailPic(Application mAppContext,int message){
		Toast mSuccessToast=null;
		if(mSuccessToast==null){
			mSuccessToast = Toast.makeText(mAppContext, message,
					Toast.LENGTH_SHORT);
			mSuccessToast.setGravity(Gravity.CENTER, 0, -0);
		}else{
			mSuccessToast.setText(message);
			mSuccessToast.setDuration(Toast.LENGTH_SHORT);
		}
		mSuccessToast.show();
	}

	protected static void showToastOkPic(Application mAppContext,String message){
		Toast mFaileToast=null;
		if(mFaileToast == null) {
			mFaileToast = Toast.makeText(mAppContext, message, Toast.LENGTH_SHORT);
			mFaileToast.setGravity(Gravity.CENTER, 0, 0);
		} else {
			mFaileToast.setText(message);
			mFaileToast.setDuration(Toast.LENGTH_SHORT);
		}
		mFaileToast.show();
	}
}
