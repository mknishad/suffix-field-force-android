package com.suffix.fieldforce;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class MyApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    FirebaseApp.initializeApp(getApplicationContext());
    FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    Fresco.initialize(this);
  }
}
