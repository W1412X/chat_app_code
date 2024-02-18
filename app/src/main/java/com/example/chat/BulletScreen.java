package com.example.chat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.animation.ObjectAnimator;

public class BulletScreen extends androidx.appcompat.widget.LinearLayoutCompat {
    private Context context;
    public BulletScreen(Context context) {
        super(context);
        BulletScreen.this.context=context;
    }
    private void init() {
    }
    public void addBulletScreenMsg(String msg) {
        // 创建 BulletScreenMsg 实例
        BulletScreenMsg msgView = new BulletScreenMsg(context,msg);
        addView(msgView);

        // 获取屏幕宽度和子视图宽度
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int childWidth = msgView.getWidth();

        // 创建属性动画，设置移动效果
        ObjectAnimator animator = ObjectAnimator.ofFloat(msgView, "translationX", screenWidth, -childWidth);
        animator.setDuration(5000);  // 动画持续5秒

        // 设置动画监听器，在动画结束时从父容器中移除子视图
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                removeView(msgView);
            }
        });

        // 启动动画
        animator.start();
    }


    //bullet screen use button
    public class BulletScreenMsg extends androidx.appcompat.widget.AppCompatButton {
        private String position_name;
        private Context context;
        public BulletScreenMsg(Context context,  String msg) {
            super(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, // 宽度
                    LinearLayout.LayoutParams.WRAP_CONTENT  // 高度
            );
            setLayoutParams(layoutParams);
            setBackground(getResources().getDrawable(R.drawable.bulletscreen_button));
            this.context=context;
            this.position_name = msg;
            setText(position_name);
            setTextSize(25);
            setTextColor(0xffffff);
        }
    }

}
