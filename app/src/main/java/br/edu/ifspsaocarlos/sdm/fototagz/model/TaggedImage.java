package br.edu.ifspsaocarlos.sdm.fototagz.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class TaggedImage extends RealmObject {

    @PrimaryKey
    @Required
    private String imageUri;

    private RealmList<Tag> tags;

    public TaggedImage() {
    }

    public TaggedImage(String imageUri) {
        this.tags = new RealmList<Tag>();
        this.imageUri = imageUri;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public void addTag(Tag newTag){
        tags.add(newTag);
    }

    public RealmList<Tag> getTags() {
        return tags;
    }
}
