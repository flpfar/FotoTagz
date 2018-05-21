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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.edu.ifspsaocarlos.sdm.fototagz.model.Tag;
import br.edu.ifspsaocarlos.sdm.fototagz.model.TaggedImage;
import br.edu.ifspsaocarlos.sdm.fototagz.util.Constant;
import io.realm.Realm;

public class ImageEditActivity extends Activity {

    static final int CAMERA_REQUEST = 1;
    static final int GALLERY_REQUEST = 2;

    static final int TAG_IMAGE_SIZE = 20;

    private ImageView ivImage;
    private RelativeLayout rl;
    private TaggedImage taggedImage = null;

    private String mCurrentPhotoPath;
    private String imageUri;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_edit);

        ivImage = (ImageView) findViewById(R.id.iv_image);
        rl = (RelativeLayout) findViewById(R.id.rl_imagetag);

        realm = Realm.getDefaultInstance();

        if(getIntent().hasExtra(Constant.CAME_FROM)){
            int imageFrom = getIntent().getIntExtra(Constant.CAME_FROM, 0);

            switch (imageFrom){
                case Constant.IMAGE_FROM_CAMERA:
                    //intent to open camera to get the image
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    //if that checks if there is some camera app in the device
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        // create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // error occurred while creating the File
                            Toast.makeText(this, "Could not load photo", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(this,
                                    "br.edu.ifspsaocarlos.sdm.fototagz.fileprovider",
                                    photoFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                        }
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
        switch (resultCode){
            case RESULT_OK:

                //*********RESPONSE FROM GALLERY OR CAMERA*********
                if (requestCode == CAMERA_REQUEST || requestCode == GALLERY_REQUEST) {

                    imageUri = mCurrentPhotoPath;

                    if(requestCode == GALLERY_REQUEST){
                        //TODO: create a copy of the image to store in app's memory case its deleted from gallery
                        if(data != null) {
                            imageUri = data.getData().toString();
                        }
                    }

                    //creates the taggedImage object
                    taggedImage = new TaggedImage(imageUri);

                    try {
                        //show chosen image using glide library
                        Glide.with(ImageEditActivity.this)
                                .load(imageUri)
                                .into(ivImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //when user clicks somewhere in imageview, creates a new tag where was touched.
                    ivImage.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(final View v, MotionEvent event) {
                            createNewImageTag(v, (int)event.getX(), (int)event.getY());
                            return false;
                        }
                    });

                //************* RESPONSE FROM NEWTAGACTIVITY *******************
                } else if(requestCode == Constant.NEW_TAG){
                    //response came from NewTagActivity
                    Tag newTag = (Tag) data.getParcelableExtra(Constant.CREATED_TAG);
                    Toast.makeText(this, newTag.getTitle(), Toast.LENGTH_SHORT).show();

                    //TODO: Insert created tag in TaggedImaged tags list
                    taggedImage.addTag(newTag);

                } else {
                    //closes activity
                    finish();
                }
                break;

            case RESULT_CANCELED:
                if(requestCode == Constant.NEW_TAG) {
                    //result came from NewTagActivity -> user pressed "Cancel"
                    int id = data.getIntExtra(Constant.TAG_ID, 0);
                    ImageView ivTag = (ImageView) findViewById(id);
                    ((ViewGroup) ivTag.getParent()).removeView(ivTag);
                } else {
                    finish();
                }
                break;

            default:
                finish();
                break;
        }
    }

    //creates a new "tag" imageview and show it
    private void createNewImageTag(View v, int x, int y){
        ImageView iv = new ImageView(v.getContext());
        iv.setImageResource(R.drawable.ic_adjust_black_24dp);
        iv.setLayoutParams(new android.view.ViewGroup.LayoutParams(TAG_IMAGE_SIZE * 2,TAG_IMAGE_SIZE * 2));
        iv.setMaxHeight(TAG_IMAGE_SIZE * 2);
        iv.setMaxWidth(TAG_IMAGE_SIZE * 2);
        iv.setX(x-TAG_IMAGE_SIZE);
        iv.setY(y-TAG_IMAGE_SIZE);
        iv.setId(View.generateViewId());
        iv.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                //Toast.makeText(v.getContext(), "TESTE"+tagId, Toast.LENGTH_SHORT).show();
            }
        });
        rl.addView(iv);
        showConfirmation(iv);
    }

    //confirmation if user really wants to create a tag where he touched
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
                        newTagActivity.putExtra(Constant.COORDX, iv.getX() + TAG_IMAGE_SIZE);
                        newTagActivity.putExtra(Constant.COORDY, iv.getY()+TAG_IMAGE_SIZE);
                        newTagActivity.putExtra(Constant.TAG_ID, iv.getId());
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


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    //    private void copyFile(File sourceFile, File destFile) throws IOException {
//        if (!sourceFile.exists()) {
//            return;
//        }
//
//        FileChannel source = null;
//        FileChannel destination = null;
//        source = new FileInputStream(sourceFile).getChannel();
//        destination = new FileOutputStream(destFile).getChannel();
//        if (destination != null && source != null) {
//            destination.transferFrom(source, 0, source.size());
//        }
//        if (source != null) {
//            source.close();
//        }
//        if (destination != null) {
//            destination.close();
//        }
//    }
}
