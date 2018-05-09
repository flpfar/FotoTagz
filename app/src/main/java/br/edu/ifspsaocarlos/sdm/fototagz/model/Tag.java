package br.edu.ifspsaocarlos.sdm.fototagz.model;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;

public class Tag extends RealmObject{
    private String title;
    private String description;
    private Point point;

    @LinkingObjects("tags")
    public final RealmResults<TaggedImage> taggedImage = null;

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

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }
}
