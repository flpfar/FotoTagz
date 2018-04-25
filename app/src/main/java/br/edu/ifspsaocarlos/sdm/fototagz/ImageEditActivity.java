package br.edu.ifspsaocarlos.sdm.fototagz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.FileNotFoundException;
import java.io.InputStream;

import br.edu.ifspsaocarlos.sdm.fototagz.util.Constant;

public class ImageEditActivity extends Activity {

    static final int CAMERA_REQUEST = 1;
    static final int GALLERY_REQUEST = 2;

    private ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_edit);
        ivImage = (ImageView) findViewById(R.id.iv_image);

        if(getIntent().hasExtra(Constant.IMAGE_FROM)){
            int imageFrom = getIntent().getIntExtra(Constant.IMAGE_FROM, 0);

            switch (imageFrom){
                case Constant.IMAGE_FROM_CAMERA:
                    //intent to open camera to get the image
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                    }
                    break;

                case Constant.IMAGE_FROM_GALLERY:
                    //intent to open gallery to get the image
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
                    break;

                default:
                    //closes activity
                    finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST || requestCode == GALLERY_REQUEST) {
                final Uri imageUri = data.getData();
                Glide
                        .with(ImageEditActivity.this)
                        .load(imageUri)
                        .into(ivImage);
            }
        } else{
            //closes activity
            finish();
        }
    }
}
