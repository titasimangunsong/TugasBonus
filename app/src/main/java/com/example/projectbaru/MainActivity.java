package com.example.projectbaru;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.widget.Toast;

import com.example.projectbaru.model.Dosen;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    DataDosenService dataDosenService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataDosenService = RetrofitClient.getRetrofiyInstance().create(DataDosenService.class);
        getAllDataDosen();
        insertDosen();
        insertDosenWithPhoto();
    }
    private boolean CheckPermission()
    {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                return result == PackageManager.PERMISSION_GRANTED;
    }


    private void getAllDataDosen() {
        Call<List<Dosen>> call = dataDosenService.getDosenAll("72170154");
        call.enqueue(new Callback<List<Dosen>>() {
            @Override
            public void onResponse(Call<List<Dosen>> call, Response<List<Dosen>> response) {
                for (Dosen dosen : response.body()) {
                    System.out.println("Nama : " + dosen.getNama());
                    System.out.println("NIDN : " + dosen.getNidn());
                }

            }

            @Override
            public void onFailure(Call<List<Dosen>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something wrong dude...",
                        Toast.LENGTH_LONG).show();

            }
        });
    }

    private void insertDosen() {
        final Call<DefaultResult> call = dataDosenService.insertDosen("aaa", "11111111",
                "aws", "aaa@si.ukdw.ac.id", "S Kom", "aaa.jpg", "72170154");
        call.enqueue(new Callback<DefaultResult>() {
            @Override
            public void onResponse(Call<DefaultResult> call, Response<DefaultResult> response) {
                //System.out.println(response.body().getStatus());
            }

            @Override
            public void onFailure(Call<DefaultResult> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong dude...",
                        Toast.LENGTH_LONG).show();

            }
        });
    }

    private void insertDosenWithPhoto() {
        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard,"/Download/images.jpg");
        String imageToSend = null;


        if (file.exists()) {
            if(!CheckPermission()){
                ActivityCompat.requestPermissions(this, new String[]
                        {Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }

            Bitmap imageBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] bytes = baos.toByteArray();
            imageToSend = Base64.encodeToString(bytes, Base64.DEFAULT);

        }

        final Call<DefaultResult> call = dataDosenService.insertDosen("aaa", "11111111",
                 "aws", "aaa@si.ukdw.ac.id", "S Kom", imageToSend, "72170154");
        call.enqueue(new Callback<DefaultResult>() {
            @Override
            public void onResponse(Call<DefaultResult> call, Response<DefaultResult> response) {
                //System.out.println(response.body().getStatus());
            }

            @Override
            public void onFailure(Call<DefaultResult> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong dude...",
                        Toast.LENGTH_LONG).show();

            }
        });
    }
}
