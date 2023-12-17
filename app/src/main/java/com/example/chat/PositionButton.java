package com.example.chat;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

public class PositionButton extends androidx.appcompat.widget.AppCompatButton {
    private String position_name;
    private Context context;
    public PositionButton(Context context,  String position) {
        super(context);
        this.context=context;
        this.position_name = position;
        setText(position_name);
        setBackground(getResources().getDrawable(R.drawable.position_button));
        setTextSize(25);
        setTextColor(0xffffff);
    }
    public String getPosition() {
        return position_name;
    }
    public void setPosition(String position) {
        this.position_name = position;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
        Toast.makeText(this.context,position_name,Toast.LENGTH_SHORT).show();
    }
}
