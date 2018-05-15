package br.edu.ifspsaocarlos.sdm.fototagz.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TaggedImage extends RealmObject {

    @PrimaryKey
    private String imageUri;

    private RealmList<Tag> tags;

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public void addTag(Tag newTag){
        tags.add(newTag);
    }
}
