package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button login_button;
    private EditText code_input;
    private boolean if_updated=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在应用启动时检查是否第一次运行
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        String saved_version=sharedPreferences.getString("version","0.0.0");
        String now_version=BuildConfig.VERSION_NAME.toString();
        if(!saved_version.equals(now_version)){
            SharedPreferences.Editor editor= sharedPreferences.edit();
            editor.putString("version",now_version);
            editor.apply();
            if_updated=true;
        }
        if (isFirstRun) {
            setContentView(R.layout.activity_main);
            login_button=findViewById(R.id.login_button);
            code_input=findViewById(R.id.invite_code_input);
            login_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String inputtext=String.valueOf(code_input.getText());
                    if(inputtext.equals("1412")){
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isFirstRun", false);
                        editor.apply();
                        Toast.makeText(MainActivity.this,"认证成功",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(MainActivity.this,WebActivity.class);
                        intent.putExtra("if_updated",if_updated);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(MainActivity.this,"认证失败",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Intent intent=new Intent(MainActivity.this,WebActivity.class);
            intent.putExtra("if_updated",if_updated);
            startActivity(intent);
            Toast.makeText(MainActivity.this,"免认证",Toast.LENGTH_SHORT).show();
        }

    }
}