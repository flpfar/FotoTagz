package br.edu.ifspsaocarlos.sdm.fototagz.model.db;

import android.support.annotation.NonNull;

import java.util.List;

import br.edu.ifspsaocarlos.sdm.fototagz.model.Tag;
import br.edu.ifspsaocarlos.sdm.fototagz.model.TaggedImage;
import io.realm.Realm;
import io.realm.RealmList;
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
                Number maxId = realm.where(Tag.class).max("id");
                int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
                tag.setId(nextId);
                taggedImage.addTag(tag);
                realm.copyToRealmOrUpdate(tag);
                //realm.copyToRealmOrUpdate(taggedImage);
            }
        });
    }

    public void setTagTitleAndDescription(final Tag tag, final String title, final String description) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                tag.setTitle(title);
                tag.setDescription(description);
            }
        });
    }

//    public void save(final List<Tag> tag) {
//        mRealm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                realm.copyToRealmOrUpdate(tag);
//            }
//        });
//    }

    public RealmList<Tag> loadAllTagsFromTaggedImage(String imageUri) {
        TaggedImage taggedImage = mRealm.where(TaggedImage.class).equalTo("imageUri", imageUri).findFirst();
        return taggedImage.getTags();
    }

    public RealmObject loadById(int id) {
        return mRealm.where(Tag.class).equalTo("id", id).findFirst();
    }
}
