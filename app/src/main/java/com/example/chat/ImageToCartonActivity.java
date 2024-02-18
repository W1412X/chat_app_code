package com.example.chat;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import android.Manifest;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
public class ImageToCartonActivity extends AppCompatActivity {
    private boolean if_origin_display_img=true;
    private ImageView image_view;
    private String model_name;
    private ImageWebSocketClient webSocketClient;
    private ProgressBar progressBar;
    private ImageButton select_img_button,save_img_button;
    private ImageButton convert_landscape_button,convert_person1_button,convert_person2_button;
    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_to_carton);

        //同步信息
        try{
            new Record(ImageToCartonActivity.this,"ImageToCarton");
        }catch(Exception e){

        }
        //初始化imageview
        image_view=findViewById(R.id.erciyuan_image_view);
        select_img_button=findViewById(R.id.choose_img);
        save_img_button=findViewById(R.id.save_img);
        image_view.setImageResource(R.drawable.carton_example);
        //初始化几个转化按钮
        convert_landscape_button=findViewById(R.id.convert_landscape_img);
        convert_person1_button=findViewById(R.id.convert_person1_img);
        convert_person2_button=findViewById(R.id.convert_person2_img);
        //设置转化按钮的对应model
        convert_landscape_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //judge the type of the img
                if(if_origin_display_img){
                    Toast.makeText(ImageToCartonActivity.this,"请选择一张你自己图片",Toast.LENGTH_SHORT).show();
                    return;
                }
                BitmapDrawable drawable = (BitmapDrawable) image_view.getDrawable();
                if (drawable != null && drawable.getBitmap() != null) {
                    // ImageView 不为空
                } else {
                    Toast.makeText(ImageToCartonActivity.this,"请选择一张图片",Toast.LENGTH_SHORT).show();
                    return;
                }
                ImageToCartonActivity.this.model_name="celeba_distill.pt";
                progressBar.setVisibility(View.VISIBLE);
                select_img_button.setEnabled(false);
                convert_landscape_button.setEnabled(false);
                convert_person1_button.setEnabled(false);
                convert_person2_button.setEnabled(false);
                save_img_button.setEnabled(false);
                //重新连接并上传图片
                try{
                    new ImageUploadTask().execute();
                }
                catch (Exception e){
                    Toast.makeText(ImageToCartonActivity.this,"上传失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
        convert_person1_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //judge the type of the img
                if(if_origin_display_img){
                    Toast.makeText(ImageToCartonActivity.this,"请选择一张你自己图片",Toast.LENGTH_SHORT).show();
                    return;
                }
                BitmapDrawable drawable = (BitmapDrawable) image_view.getDrawable();
                if (drawable != null && drawable.getBitmap() != null) {
                    // ImageView 不为空
                } else {
                    Toast.makeText(ImageToCartonActivity.this,"请选择一张图片",Toast.LENGTH_SHORT).show();
                    return;
                }
                ImageToCartonActivity.this.model_name="paprika.pt";
                progressBar.setVisibility(View.VISIBLE);
                select_img_button.setEnabled(false);
                convert_landscape_button.setEnabled(false);
                convert_person1_button.setEnabled(false);
                convert_person2_button.setEnabled(false);
                save_img_button.setEnabled(false);
                //重新连接并上传图片
                new ImageUploadTask().execute();
            }
        });
        convert_person2_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //judge the type of the img
                if(if_origin_display_img){
                    Toast.makeText(ImageToCartonActivity.this,"请选择一张你自己图片",Toast.LENGTH_SHORT).show();
                    return;
                }
                BitmapDrawable drawable = (BitmapDrawable) image_view.getDrawable();
                if (drawable != null && drawable.getBitmap() != null) {
                    // ImageView 不为空
                } else {
                    Toast.makeText(ImageToCartonActivity.this,"请选择一张图片",Toast.LENGTH_SHORT).show();
                    return;
                }
                ImageToCartonActivity.this.model_name="face_paint_512_v2.pt";
                progressBar.setVisibility(View.VISIBLE);
                select_img_button.setEnabled(false);
                convert_landscape_button.setEnabled(false);
                convert_person1_button.setEnabled(false);
                convert_person2_button.setEnabled(false);
                save_img_button.setEnabled(false);
                //重新连接并上传图片
                try{
                    new ImageUploadTask().execute();
                }catch (Exception e){

                }
            }
        });
        //初始化加载条
        progressBar=findViewById(R.id.to_carton_loading_view);

        //设置选择图片button
        select_img_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
        //set the save button listener
        save_img_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将要保存的 Bitmap 对象

                //获取写入权限
                if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                    if(ActivityCompat.shouldShowRequestPermissionRationale(ImageToCartonActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                        Toast.makeText(ImageToCartonActivity.this, "请授权！", Toast.LENGTH_LONG).show();
                        //跳转到应用设置界面
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }else{
                        ActivityCompat.requestPermissions(ImageToCartonActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
                    }
                }
                //get the process
                Bitmap bitmap = ((BitmapDrawable)  (image_view.getDrawable())).getBitmap();
                //use the save image function
                saveImageToGallery(bitmap);
            }
        });
        //显示弹窗
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(ImageToCartonActivity.this);
        alertDialog.setTitle("声明");
        alertDialog.setMessage(getResources().getString(R.string.image_to_carton_page_declaration));
        alertDialog.setPositiveButton("确认",null);
        alertDialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            image_view.setImageURI(imageUri);
            image_view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            if_origin_display_img=false;
        }
    }
    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1;
    //the save image function
    public void saveImageToGallery(Bitmap bitmap) {

        String savedImageURL = MediaStore.Images.Media.insertImage(
                getContentResolver(),
                bitmap,
                "title",
                "description"
        );

        // 当保存成功时，savedImageURL 不为 null
        if (savedImageURL != null) {
            Toast.makeText(getApplicationContext(), "已保存至相册", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "保存失败，请检查权限", Toast.LENGTH_SHORT).show();
        }
    }


    //上传图片放到子线程执行

    private class ImageUploadTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                reconnectToServer();//重新连接服务器
            } catch (Exception e) {
                //
                Toast.makeText(ImageToCartonActivity.this,"上传失败,重新链接",Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // 执行上传完成后的操作，例如显示上传成功信息等
        }
    }


    /*public void upload_img(){
        try{
            reconnectToServer();//重新连接服务器
            // 在需要发送图片数据时，将图片转换为字节数组，并通过 WebSocket 发送
            Bitmap bitmap =((BitmapDrawable)  (image_view.getDrawable())).getBitmap(); // 获取要发送的图片
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] imageData = stream.toByteArray();
            webSocketClient.send(imageData);
        }catch (Exception e){
            webSocketClient.connect();
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    upload_img();
                }
            },1000);
            Toast.makeText(ImageToCartonActivity.this,"上传失败,重新链接",Toast.LENGTH_SHORT).show();
        }
    }*/
    //the websocket class
    public class ImageWebSocketClient extends WebSocketClient {
        private ByteBuffer receivedBytes;

        public ImageWebSocketClient(String serverUri) {
            super(URI.create(serverUri));
        }
        @Override
        public void onOpen(ServerHandshake handshakedata) {
            // 连接建立后的操作，可在此处进行握手等逻辑
            send(model_name);
            System.out.println("握手成功");
        }

        @Override
        public void onMessage(String message) {
            // 接收到服务器发送的文本消息的处理逻辑
            // 在需要发送图片数据时，将图片转换为字节数组，并通过 WebSocket 发送
            Bitmap bitmap = ((BitmapDrawable) (image_view.getDrawable())).getBitmap(); // 获取要发送的图片
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] imageData = stream.toByteArray();
            webSocketClient.send(imageData);
        }

        @Override
        public void onMessage(ByteBuffer bytes) {
            // 接收到服务器发送的二进制消息（图像数据）的处理逻辑
            try {
                Handler handler = new Handler(Looper.getMainLooper());
// 在非 UI 线程中执行后台任务
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 执行耗时操作
                        ImageWebSocketClient.this.receivedBytes = bytes;
                        ByteBuffer receivedBytes=webSocketClient.receivedBytes;
                        if(receivedBytes==null){
                            Toast.makeText(ImageToCartonActivity.this,"接收数据的bug",Toast.LENGTH_SHORT).show();
                        }
                        byte[] re_imageData = new byte[receivedBytes.remaining()];
                        receivedBytes.get(re_imageData);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(re_imageData, 0, re_imageData.length);
                        // 将结果传递给 UI 线程
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                select_img_button.setEnabled(true);
                                convert_landscape_button.setEnabled(true);
                                convert_person1_button.setEnabled(true);
                                convert_person2_button.setEnabled(true);
                                save_img_button.setEnabled(true);
                                int targetWidth = image_view.getWidth();
                                int targetHeight = image_view.getHeight();
                                // 获取原始图像的宽度和高度
                                int originalWidth = bitmap.getWidth();
                                int originalHeight = bitmap.getHeight();
                                // 计算图像的缩放比例
                                float scaleFactor = Math.min((float) targetWidth / (float) originalWidth, (float) targetHeight / (float) originalHeight);
                                // 缩放图像
                                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (originalWidth * scaleFactor), (int) (originalHeight * scaleFactor), true);
                                // 将缩放后的图像设置到ImageView中
                                image_view.setImageBitmap(scaledBitmap);
                                image_view.setScaleType(ImageView.ScaleType.CENTER);
                                // 在 UI 线程中更新视图
                            }
                        });
                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            // 连接关闭后的操作
        }

        @Override
        public void onError(Exception ex) {
            // 发生错误时的处理逻辑
            Toast.makeText(ImageToCartonActivity.this,"unknown error",Toast.LENGTH_SHORT).show();
        }

        public ByteBuffer getReceivedBytes() {
            return receivedBytes;
        }
    }



    private void requestmanageexternalstorage_Permission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 先判断有没有权限
            if (Environment.isExternalStorageManager()) {
                Toast.makeText(this, "Android VERSION  R OR ABOVE，HAVE MANAGE_EXTERNAL_STORAGE GRANTED!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Android VERSION  R OR ABOVE，NO MANAGE_EXTERNAL_STORAGE GRANTED!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + this.getPackageName()));
                startActivity(intent);
            }
        }
    }


    //转换按钮类


    //重新连接服务器，传入的参数是需要发送给服务器的模型名称
    private void reconnectToServer() {
        try {
            webSocketClient=new ImageWebSocketClient("ws://39.101.160.55:8625");
            webSocketClient.connect();
            //webSocketClient.send(model); // 发送握手字符串,握手字符串是model name
        } catch (Exception e) {
            Toast.makeText(ImageToCartonActivity.this, "连接到服务器失败", Toast.LENGTH_SHORT).show();
        }
    }


}

