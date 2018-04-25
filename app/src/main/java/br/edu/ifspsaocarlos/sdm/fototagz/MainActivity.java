package br.edu.ifspsaocarlos.sdm.fototagz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private CardView cvFototag;
    private CardView cvGaleria;
    private CardView cvCamera;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cvFototag = (CardView) findViewById(R.id.cv_fototag);
        cvGaleria = (CardView) findViewById(R.id.cv_galeria);
        cvCamera = (CardView) findViewById(R.id.cv_camera);
        mImageView = (ImageView) findViewById(R.id.imageViewTeste);


        cvFototag.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        cvGaleria.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        cvCamera.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
        }
    }


}
