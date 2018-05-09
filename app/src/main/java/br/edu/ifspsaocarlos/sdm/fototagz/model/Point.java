package br.edu.ifspsaocarlos.sdm.fototagz.model;

import io.realm.RealmModel;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.RealmClass;

@RealmClass
public class Point implements RealmModel{
    private float coordX;
    private float coordY;

    public float getCoordX() {
        return coordX;
    }

    public void setCoordX(float coordX) {
        this.coordX = coordX;
    }

    public float getCoordY() {
        return coordY;
    }

    public void setCoordY(float coordY) {
        this.coordY = coordY;
    }
}
