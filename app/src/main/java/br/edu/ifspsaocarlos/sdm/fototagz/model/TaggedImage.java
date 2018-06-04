package br.edu.ifspsaocarlos.sdm.fototagz.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class TaggedImage extends RealmObject implements Parcelable{

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

    protected TaggedImage(Parcel in) {
        imageUri = in.readString();
    }

    public static final Creator<TaggedImage> CREATOR = new Creator<TaggedImage>() {
        @Override
        public TaggedImage createFromParcel(Parcel in) {
            return new TaggedImage(in);
        }

        @Override
        public TaggedImage[] newArray(int size) {
            return new TaggedImage[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageUri);
    }
}
