package br.edu.ifspsaocarlos.sdm.fototagz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.edu.ifspsaocarlos.sdm.fototagz.model.Tag;
import br.edu.ifspsaocarlos.sdm.fototagz.model.db.RealmManager;
import br.edu.ifspsaocarlos.sdm.fototagz.util.Constant;

public class NewTagActivity extends Activity {

    private int x, y, id;
    private String imageUri;
    private EditText etTitle, etDescription;
    private Button btCancel, btSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tag);
        RealmManager.open();

        etTitle = (EditText) findViewById(R.id.et_title);
        etDescription = (EditText) findViewById(R.id.et_description);
        btCancel = (Button) findViewById(R.id.bt_cancel);
        btSave = (Button) findViewById(R.id.bt_save);

        if(getIntent().hasExtra(Constant.TAG_ID)){
            x = getIntent().getIntExtra(Constant.COORDX, 0);
            y = getIntent().getIntExtra(Constant.COORDY, 0);
            id = getIntent().getIntExtra(Constant.TAG_ID, 0);
            imageUri = getIntent().getStringExtra(Constant.IMG_URI);
        }

        //when user chooses to cancel
        btCancel.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent returnIntent = new Intent();
                returnIntent.putExtra(Constant.TAG_ID, id);
                setResult(RESULT_CANCELED, returnIntent);
                finish();
            }
        });

        btSave.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                //creates the new tag
                Tag newTag = new Tag(x, y, id);
                newTag.setTitle(etTitle.getText().toString());
                newTag.setDescription(etDescription.getText().toString());

                //TODO: save to BD
                RealmManager.createTagDAO().addTagByTaggedImageId(newTag, imageUri);

                //return to ImageEditActivity
                Intent returnIntent = new Intent();
                returnIntent.putExtra(Constant.CREATED_TAG, newTag);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RealmManager.close();
    }
}
