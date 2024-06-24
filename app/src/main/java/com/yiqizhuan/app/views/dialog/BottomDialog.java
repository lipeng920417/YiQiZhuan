package com.yiqizhuan.app.views.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

public abstract class BottomDialog extends BaseDialog {
  public BottomDialog(Context context) {
    super(context);
  }

  public BottomDialog(Context context, int themeResId) {
    super(context, themeResId);
  }

  @Override
  protected void fillGravity() {
    Window windowManager = getWindow();
    windowManager.getDecorView().setPadding(0, 0, 0, 0);
    WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
    layoutParams.gravity = Gravity.BOTTOM;
    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
    layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
    this.onWindowAttributesChanged(layoutParams);
  }
}
