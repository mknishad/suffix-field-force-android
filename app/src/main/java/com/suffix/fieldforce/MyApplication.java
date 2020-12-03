package com.suffix.fieldforce;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    FirebaseApp.initializeApp(getApplicationContext());
    FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    Fresco.initialize(this);

    Realm.init(this);
    RealmConfiguration config =
        new RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .allowWritesOnUiThread(true)
            .build();

    Realm.setDefaultConfiguration(config);
  }
}
