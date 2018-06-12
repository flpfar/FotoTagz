package br.edu.ifspsaocarlos.sdm.fototagz;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import br.edu.ifspsaocarlos.sdm.fototagz.model.Tag;
import br.edu.ifspsaocarlos.sdm.fototagz.model.TaggedImage;
import br.edu.ifspsaocarlos.sdm.fototagz.model.db.RealmManager;
import br.edu.ifspsaocarlos.sdm.fototagz.util.Constant;
import io.realm.RealmResults;

public class GalleryActivity extends AppCompatActivity implements GalleryAdapter.ItemClickListener {

    private RecyclerView recyclerView;
    private TextView tvEmpty;
    private GalleryAdapter adapter;
    private Toolbar galleryToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        RealmManager.open();
        galleryToolbar = findViewById(R.id.gallery_toolbar);
        recyclerView = findViewById(R.id.rv_gallery);
        tvEmpty = findViewById(R.id.tv_empty_view);
        setSupportActionBar(galleryToolbar);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        RealmResults<TaggedImage> results = RealmManager.createTaggedImageDAO().loadAllTaggedImages();

        if(results.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
        }

        adapter = new GalleryAdapter(results);
        adapter.setClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
         TaggedImage taggedImage = adapter.getItem(position);

         if(taggedImage != null){
             Intent imageEditIntent = new Intent(this, ImageEditActivity.class);
             imageEditIntent.putExtra(Constant.CAME_FROM, Constant.IMAGE_FROM_FOTOTAGZ_GALLERY);
             imageEditIntent.putExtra(Constant.TAGGED_IMAGE, taggedImage.getImageUri());
             startActivity(imageEditIntent);
         }
    }
}
