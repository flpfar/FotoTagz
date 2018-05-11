package br.edu.ifspsaocarlos.sdm.fototagz;

/*
* Activity: ImageEditActivity
* Displays an image selected by the user, from camera or gallery, and creates tags on the image where user touched.
* When touching somewhere in image, alert dialog is displayed and if user really wants to create a tag there, he will be
* redirected to the NewTagActivity.
* Opened from: MainActivity
* Opens: NewTagActivity
* */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import br.edu.ifspsaocarlos.sdm.fototagz.util.Constant;

public class ImageEditActivity extends Activity {

    static final int CAMERA_REQUEST = 1;
    static final int GALLERY_REQUEST = 2;
    private int tagId = 0;

    private ImageView ivImage;
    private RelativeLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_edit);
        ivImage = (ImageView) findViewById(R.id.iv_image);
        rl = (RelativeLayout) findViewById(R.id.rl_imagetag);

        if(getIntent().hasExtra(Constant.CAME_FROM)){
            int imageFrom = getIntent().getIntExtra(Constant.CAME_FROM, 0);

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

                //show chosen image using glide library
                Glide.with(ImageEditActivity.this)
                        .asBitmap()
                        .load(imageUri)
                        .into(ivImage);

                //when user clicks somewhere in imageview, creates a new tag where was touched.
                ivImage.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(final View v, MotionEvent event) {
                        createNewImageTag(v, (int)event.getX(), (int)event.getY());
                        return false;
                    }
                });
            } else if(requestCode == Constant.NEW_TAG){
                //response came from NewTagActivity

            } else {
                //closes activity
                finish();
            }
        }
    }

    //creates a new "tag" imageview and show it
    private void createNewImageTag(View v, int x, int y){
        ImageView iv = new ImageView(v.getContext());
        iv.setImageResource(R.drawable.ic_adjust_black_24dp);
        iv.setLayoutParams(new android.view.ViewGroup.LayoutParams(40,40));
        iv.setMaxHeight(40);
        iv.setMaxWidth(40);
        iv.setX(x-20);
        iv.setY(y-20);
        final int a = View.generateViewId();
        iv.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(v.getContext(), "TESTE"+a, Toast.LENGTH_SHORT).show();
            }
        });
        rl.addView(iv);
        showConfirmation(iv);
    }

    private void showConfirmation(final ImageView iv){
        new AlertDialog
                .Builder(ImageEditActivity.this)
                .setTitle("Title")
                .setMessage(getResources().getString(R.string.tag_confirmation))
                .setIcon(android.R.drawable.ic_dialog_alert)

                //user chooses 'yes', must show an activity to put details about the point touched
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //intent to open activity to set information to the point tagged
                        Intent newTagActivity = new Intent(getBaseContext(), NewTagActivity.class);
                        startActivityForResult(newTagActivity, Constant.NEW_TAG);
                    }})

                //user chooses 'no'
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //removes tag imageview from image
//                        ImageView namebar = (ImageView) findViewById(tagId);
                        ((ViewGroup) iv.getParent()).removeView(iv);

                    }}).show();
    }
}
