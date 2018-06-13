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
    private Intent returnIntent;
    private Tag editTag;

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
            if(tagAlreadyInDB){
                tagId = intent.getIntExtra(Constant.TAG_ID, -1);
                imageUri = intent.getStringExtra(Constant.IMG_URI);
                viewId = intent.getIntExtra(Constant.TAG_VIEWID, -1);

                editTag = (Tag) RealmManager.createTagDAO().loadById(tagId);

                etTitle.setText(editTag.getTitle());
                etDescription.setText(editTag.getDescription());

                btSave.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        //update tag in BD
                        RealmManager.createTagDAO().setTagTitleAndDescription(editTag, etTitle.getText().toString(), etDescription.getText().toString());
                        finish();
                    }
                });
            }
        }

        if(getIntent().hasExtra(Constant.TAG_VIEWID)){
            x = getIntent().getIntExtra(Constant.COORDX, 0);
            y = getIntent().getIntExtra(Constant.COORDY, 0);
            viewId = getIntent().getIntExtra(Constant.TAG_VIEWID, 0);
            imageUri = getIntent().getStringExtra(Constant.IMG_URI);

            //as the view is being removed once the user clicks any button in dialog from imageeditactivity, its not necessary anymore
            //already sets the return intent (case user press back button)
//            returnIntent = new Intent();
//            returnIntent.putExtra(Constant.TAG_VIEWID, viewId);
//            setResult(RESULT_CANCELED, returnIntent);

            btSave.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v){
                    //creates the new tag
                    Tag newTag = new Tag(x, y, viewId);
                    newTag.setTitle(etTitle.getText().toString());
                    newTag.setDescription(etDescription.getText().toString());

                    //TODO: save to BD
                    RealmManager.createTagDAO().addTagByTaggedImageId(newTag, imageUri);

                    //return to ImageEditActivity
                    returnIntent = new Intent();
                    returnIntent.putExtra(Constant.CREATED_TAG, newTag);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            });
        } else if(getIntent().hasExtra(Constant.TAG_ID)) {

        } else {
            finish();
        }

        //when user chooses to cancel
        btCancel.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
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
                returnIntent = new Intent();
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
