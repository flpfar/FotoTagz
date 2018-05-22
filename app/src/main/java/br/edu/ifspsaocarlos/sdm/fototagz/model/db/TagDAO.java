package br.edu.ifspsaocarlos.sdm.fototagz.model.db;

import android.support.annotation.NonNull;

import java.util.List;

import br.edu.ifspsaocarlos.sdm.fototagz.model.Tag;
import br.edu.ifspsaocarlos.sdm.fototagz.model.TaggedImage;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class TagDAO {
    private Realm mRealm;

    public TagDAO(@NonNull Realm realm) {
        mRealm = realm;
    }

    public void addTagByTaggedImageId(final Tag tag, final String imageUri) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TaggedImage taggedImage = realm.where(TaggedImage.class).equalTo("imageUri", imageUri).findFirst();
                taggedImage.addTag(tag);
                realm.copyToRealmOrUpdate(tag);
            }
        });
    }

    public void save(final List<Tag> tag) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(tag);
            }
        });
    }

    public RealmResults<Tag> loadAllTagsFromTaggedImage(String imageUri) {
        return mRealm.where(Tag.class).equalTo("taggedimages.imageuri", imageUri).findAll();
    }

    public RealmObject loadById(String id) {
        return mRealm.where(Tag.class).equalTo("id", id).findFirst();
    }
}
