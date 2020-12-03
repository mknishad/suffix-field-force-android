package com.suffix.fieldforce.akg.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.suffix.fieldforce.R;

public class CustomProgress {

  private Activity activity;
  private AlertDialog alertDialog;

  public CustomProgress(Activity activity) {
    this.activity = activity;
  }

  public void show(String progressText) {
    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
    LayoutInflater layoutInflater = activity.getLayoutInflater();
    View view = layoutInflater.inflate(R.layout.layout_custom_loader, null);
    TextView pTextView = view.findViewById(R.id.progress_text);
    pTextView.setText(progressText);

    builder.setView(view);
    builder.setCancelable(false);
    alertDialog = builder.create();
    alertDialog.show();
    alertDialog.getWindow().setLayout(600, 400);
  }

  public void dismiss(){
    alertDialog.dismiss();
  }


}
