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
import br.edu.ifspsaocarlos.sdm.fototagz.util.Constant;

public class MainActivity extends Activity {

    private CardView cvFototag;
    private CardView cvGallery;
    private CardView cvCamera;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cvFototag = (CardView) findViewById(R.id.cv_fototag);
        cvGallery = (CardView) findViewById(R.id.cv_gallery);
        cvCamera = (CardView) findViewById(R.id.cv_camera);

        cvFototag.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        cvFototag.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(v.getContext(), GalleryActivity.class);
                startActivity(galleryIntent);
            }
        });

        cvGallery.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                //call imageEditActivity with gallery argument
                Intent imageEditIntent = new Intent(v.getContext(), ImageEditActivity.class);
                imageEditIntent.putExtra(Constant.CAME_FROM, Constant.IMAGE_FROM_GALLERY);
                startActivity(imageEditIntent);
            }
        });

        cvCamera.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                //call imageEditActivity with camera argument
                Intent imageEditIntent = new Intent(v.getContext(), ImageEditActivity.class);
                imageEditIntent.putExtra(Constant.CAME_FROM, Constant.IMAGE_FROM_CAMERA);
                startActivity(imageEditIntent);

            }
        });


    }
}
