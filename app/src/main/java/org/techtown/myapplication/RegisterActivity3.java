package org.techtown.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import java.io.File;

public class RegisterActivity3 extends AppCompatActivity implements AutoPermissionsListener {
    Button nextbutton3;
    ImageView imageView;
    File file;
    Button picturebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);

        nextbutton3=findViewById(R.id.nextbutton3);
        nextbutton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterActivity3.this, RegisterActivity4.class);
                startActivity(intent);
            }
        });

        imageView=findViewById(R.id.imageView);

        picturebutton=findViewById(R.id.picturebutton);
        picturebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        AutoPermissions.Companion.loadAllPermissions(this,101);

    }
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode==101 && resultCode==RESULT_OK) {
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inSampleSize=8;
            Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath(),options);

            imageView.setImageBitmap(bitmap);
        }
    }

    public void takePicture() {
        if (file==null) {
            file=createFile();
        }

        Uri fileUri= FileProvider.getUriForFile(this,"org.techtown.myapplication.fileprovider",file);
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
        startActivityForResult(intent, 101);
    }

    private File createFile() {
        String filename="capture.jpg";
        File storageDir = Environment.getExternalStorageDirectory();
        File outFile = new File(storageDir,filename);

        return outFile;
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        AutoPermissions.Companion.parsePermissions(this, requestCode,permissions,this);
    }

    public void onDenied(int requestCode, String[] permissions) {
        Toast.makeText(this,"permissions denied : "+permissions.length,
                Toast.LENGTH_LONG).show();
    }

    public void onGranted(int requestCode, String[] permissions) {
        Toast.makeText(this,"permissions granted : "+permissions.length,Toast.LENGTH_LONG).show();
    }


}