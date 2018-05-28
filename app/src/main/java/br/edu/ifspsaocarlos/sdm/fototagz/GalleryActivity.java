package br.edu.ifspsaocarlos.sdm.fototagz;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import br.edu.ifspsaocarlos.sdm.fototagz.model.TaggedImage;
import br.edu.ifspsaocarlos.sdm.fototagz.model.db.RealmManager;
import io.realm.RealmResults;

public class GalleryActivity extends Activity {

    private RecyclerView recyclerView;
    private GalleryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        RealmManager.open();
        recyclerView = findViewById(R.id.rv_gallery);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        RealmResults<TaggedImage> results = RealmManager.createTaggedImageDAO().loadAllTaggedImages();
        adapter = new GalleryAdapter(results);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        recyclerView.setAdapter(adapter);
    }

}
