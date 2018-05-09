package br.edu.ifspsaocarlos.sdm.fototagz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

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

                DisplayMetrics displaymetrics = new DisplayMetrics();
                ImageEditActivity.this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                final int screenWidth = displaymetrics.widthPixels;
                final int screenHeight = displaymetrics.heightPixels;

                //set image using glide library, creates a bitmap to draw the dots on screen;
                Glide.with(ImageEditActivity.this)
                        .asBitmap()
                        .load(imageUri)
                        .into(new SimpleTarget<Bitmap>(screenWidth, screenWidth) { //two width arguments bc it makes a square
                            @Override
                            public void onResourceReady(final Bitmap bitmap,
                                                        Transition<? super Bitmap> transition) {
                                ivImage.setImageBitmap(bitmap);

                                final Canvas canvas = new Canvas(bitmap);
                                final Paint paint = new Paint();

                                //when user touches the image, creates a dot where was touched
                                ivImage.setOnTouchListener(new View.OnTouchListener()
                                {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event)
                                    {
                                        //dot coordinates
                                        final float dotX = event.getX();
                                        final float dotY = event.getY();

                                        //shows confirmation display
                                        new AlertDialog.Builder(ImageEditActivity.this)
                                                .setTitle("Title")
                                                .setMessage(getResources().getString(R.string.tag_confirmation))
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int whichButton) {
                                                        //intent to open activity to set information to the point tagged

                                                        //create the dot
                                                        paint.setColor(Color.RED);
                                                        canvas.drawCircle(dotX, dotY, 15, paint);
                                                        paint.setColor(Color.YELLOW);
                                                        canvas.drawCircle(dotX, dotY, 10, paint);

                                                        //refresh the image, inserting the dot
                                                        ivImage.setImageBitmap(bitmap);
                                                    }})
                                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int whichButton) {

                                                    }}).show();
                                        return false;
                                    }
                                });
                            }
                        });
            }
        } else{
            //closes activity
            finish();
        }
    }
}
