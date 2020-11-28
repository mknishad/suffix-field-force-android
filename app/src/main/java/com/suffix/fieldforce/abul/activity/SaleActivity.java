package com.suffix.fieldforce.abul.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.suffix.fieldforce.R;
import com.suffix.fieldforce.abul.adapter.CustomArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class SaleActivity extends AppCompatActivity {

  private Spinner spinner;
  private ArrayAdapter<String> spinnerAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sale);

    initWidget();

    spinnerAdapter = new CustomArrayAdapter(this, R.layout.spinner_item, getList());
    spinner.setAdapter(spinnerAdapter);

  }

  private void initWidget() {
    spinner = findViewById(R.id.spinnerUsers);
  }

  private List<String> getList() {
    String[] years = {"1996", "1997", "1998", "1998"};
    List<String> myList = new ArrayList<>();
    for (String value : years) {
      myList.add(value);
    }
    return myList;
  }
}