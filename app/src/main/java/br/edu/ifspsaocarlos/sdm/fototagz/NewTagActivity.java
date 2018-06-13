package br.edu.ifspsaocarlos.sdm.fototagz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.edu.ifspsaocarlos.sdm.fototagz.model.Tag;
import br.edu.ifspsaocarlos.sdm.fototagz.model.db.RealmManager;
import br.edu.ifspsaocarlos.sdm.fototagz.util.Constant;
import io.realm.Realm;

public class NewTagActivity extends AppCompatActivity {

    private int x, y, viewId, tagId;
    private String imageUri;
    private EditText etTitle, etDescription;
    private Toolbar tagToolbar;
    private Button btCancel, btSave;
    private Tag editTag;

    private boolean SHOW_EDIT_BUTTON;
    private boolean SHOW_DELETE_BUTTON;

    private static final int DEFAULT_VALUE = -2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tag);
        RealmManager.open();

        etTitle = (EditText) findViewById(R.id.et_title);
        etDescription = (EditText) findViewById(R.id.et_description);

        btCancel = (Button) findViewById(R.id.bt_cancel);
        btSave = (Button) findViewById(R.id.bt_save);
        tagToolbar = (Toolbar) findViewById(R.id.tag_toolbar);

        setSupportActionBar(tagToolbar);

        Intent intent = getIntent();

        if(intent.hasExtra(Constant.EXISTING_TAG)){
            boolean tagAlreadyInDB = intent.getBooleanExtra(Constant.EXISTING_TAG, false);

            //if tagalreadyindb, will recover the tag data and show to user.
            if(tagAlreadyInDB){
                //dont need to verify extra integer, cause they have a default value in case it has no data;
                if(intent.hasExtra(Constant.IMG_URI)) {
                    tagId = intent.getIntExtra(Constant.TAG_ID, DEFAULT_VALUE);
                    imageUri = intent.getStringExtra(Constant.IMG_URI);
                    viewId = intent.getIntExtra(Constant.TAG_VIEWID, DEFAULT_VALUE);

                    //if it didnt get the default value
                    if(tagId != DEFAULT_VALUE && viewId != DEFAULT_VALUE){
                        //gets the tag from db
                        editTag = (Tag) RealmManager.createTagDAO().loadById(tagId);

                        //sets texts from tag
                        etTitle.setText(editTag.getTitle());
                        etDescription.setText(editTag.getDescription());

                        /*
                            todo: btsave and cancel should appear only if editbutton in toolbar is clicked.
                            else, show the texts as textviews not edittexts, and change them with viewswitcher
                        */

                        btSave.setOnClickListener(new Button.OnClickListener(){
                            @Override
                            public void onClick(View v){
                                //update tag in BD
                                RealmManager.createTagDAO().setTagTitleAndDescription(editTag, etTitle.getText().toString(), etDescription.getText().toString());
                                finish();
                            }
                        });
                    } else {
                        Toast.makeText(this, "ERROR! Default values loaded", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "ERROR! Imageuri is null", Toast.LENGTH_SHORT).show();
                }

            } else { //if tag is new (not in db yet), will save it in db and put it in the intent to be recovered in imageeditactivity
                if(intent.hasExtra(Constant.IMG_URI)){
                    imageUri = getIntent().getStringExtra(Constant.IMG_URI);
                    x = getIntent().getIntExtra(Constant.COORDX, DEFAULT_VALUE);
                    y = getIntent().getIntExtra(Constant.COORDY, DEFAULT_VALUE);
                    viewId = getIntent().getIntExtra(Constant.TAG_VIEWID, DEFAULT_VALUE);

                    //todo: not show edit and delete toolbar menus if it is a new tag

                    btSave.setOnClickListener(new Button.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            //creates the new tag
                            Tag newTag = new Tag(x, y, viewId);
                            newTag.setTitle(etTitle.getText().toString());
                            newTag.setDescription(etDescription.getText().toString());

                            //saves to bd
                            RealmManager.createTagDAO().addTagByTaggedImageId(newTag, imageUri);

                            //return to ImageEditActivity
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra(Constant.CREATED_TAG, newTag);
                            setResult(RESULT_OK, returnIntent);
                            finish();
                        }
                    });
                } else {
                    Toast.makeText(this, "ERROR! Imageuri is null", Toast.LENGTH_SHORT).show();
                }
            }
            //when user chooses to cancel
            btCancel.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v){
                    finish();
                }
            });

        } else { //doesnt have the EXISTING_TAG value
            finish();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tag, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedMenuItem = item.getItemId();

        switch (selectedMenuItem){
            case R.id.menu_edit:
                Toast.makeText(NewTagActivity.this, "EDIT", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_delete:
                RealmManager.createTagDAO().deleteTag(tagId);
                Intent returnIntent = new Intent();
                returnIntent.putExtra(Constant.DELETED_TAG, viewId);
                setResult(RESULT_OK, returnIntent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RealmManager.close();
    }
}
