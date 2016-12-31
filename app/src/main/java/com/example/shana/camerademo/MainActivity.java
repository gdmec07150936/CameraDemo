package com.example.shana.camerademo;


import android.hardware.Camera;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private File file;
    private Camera camera;
    private SurfaceView sv;
    private boolean mRuning;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mRuning=false;
    }

    private void initView() {
        sv= (SurfaceView) findViewById(R.id.sv);
        SurfaceHolder holder=sv.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void takePhoto(View v){

        if(mRuning){
            camera.takePicture(null,null,null,picCallback);
            mRuning=false;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        camera=Camera.open();
        Camera.Parameters p=camera.getParameters();
        p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);//自动对焦
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            camera.release();
            camera=null;
            e.printStackTrace();
        }
        camera.startPreview();
        mRuning=true;

    }
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
    Camera.PictureCallback picCallback=new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            if(bytes!=null){
                savePicture(bytes);
            }
        }
    };

    private void savePicture(byte[] bytes) {
        String id=System.currentTimeMillis()+"";
        String path= Environment.getExternalStorageDirectory().getPath()+"/";
        file=new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        path+=id+".jpeg";
        file=new File(path);
        if(!file.exists()){
            try {
                file.createNewFile();
                FileOutputStream fos=new FileOutputStream(file);
                fos.write(bytes);
                fos.close();
                Toast.makeText(MainActivity.this,"已经保存在"+path,Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
