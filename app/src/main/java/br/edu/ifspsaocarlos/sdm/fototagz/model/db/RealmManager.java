package br.edu.ifspsaocarlos.sdm.fototagz.model.db;

import android.util.Log;

import br.edu.ifspsaocarlos.sdm.fototagz.model.TaggedImage;
import io.realm.Realm;

public class RealmManager {
    private static Realm mRealm;

    public static Realm open() {
        mRealm = Realm.getDefaultInstance();
        return mRealm;
    }

    public static void close() {
        if (mRealm != null) {
            mRealm.close();
        }
    }

    public static TaggedImageDAO createTaggedImageDAO(){
        checkForOpenRealm();
        return new TaggedImageDAO(mRealm);
    }

    public static TagDAO createTagDAO(){
        checkForOpenRealm();
        return new TagDAO(mRealm);
    }

    private static void checkForOpenRealm() {
        if (mRealm == null || mRealm.isClosed()) {
            throw new IllegalStateException("RealmManager: Realm is closed, call open() method first");
        }
    }
}
