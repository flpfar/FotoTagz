package br.edu.ifspsaocarlos.sdm.fototagz.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.Ignore;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;

public class Tag extends RealmObject implements Parcelable{

    @PrimaryKey
    private int id;
    private String title;
    private String description;
    private int x, y;

    @Ignore
    private int viewId;

    public Tag(){

    }

    public Tag(int x, int y, int id) {
        this.x = x;
        this.y = y;
        this.viewId = id;
    }

    protected Tag(Parcel in) {
        title = in.readString();
        description = in.readString();
        x = in.readInt();
        y = in.readInt();
        viewId = in.readInt();
    }

    public static final Creator<Tag> CREATOR = new Creator<Tag>() {
        @Override
        public Tag createFromParcel(Parcel in) {
            return new Tag(in);
        }

        @Override
        public Tag[] newArray(int size) {
            return new Tag[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getViewId() {
        return viewId;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeInt(x);
        parcel.writeInt(y);
        parcel.writeInt(viewId);
    }
}
