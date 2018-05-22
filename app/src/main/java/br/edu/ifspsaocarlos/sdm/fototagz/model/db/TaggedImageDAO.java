package br.edu.ifspsaocarlos.sdm.fototagz.model.db;

import android.support.annotation.NonNull;

import java.util.List;

import br.edu.ifspsaocarlos.sdm.fototagz.model.TaggedImage;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class TaggedImageDAO {

    private Realm mRealm;

    public TaggedImageDAO(@NonNull Realm realm) {
        mRealm = realm;
    }

    public void saveTaggedImage(final TaggedImage taggedImage) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(taggedImage);
            }
        });
    }

    public void saveTaggedImageList(final List<TaggedImage> taggedImageList) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(taggedImageList);
            }
        });
    }

    public RealmResults<TaggedImage> loadAllTaggedImages() {
        return mRealm.where(TaggedImage.class).findAll();
    }

    public RealmObject loadTaggedImageById(String imageUri) {
        return mRealm.where(TaggedImage.class).equalTo("imageUri", imageUri).findFirst();
    }

}
