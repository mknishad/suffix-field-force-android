package com.suffix.fieldforce.abul;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.suffix.fieldforce.R;
import com.suffix.fieldforce.activity.abul.adapter.CustomArrayAdapter;
import com.suffix.fieldforce.activity.abul.adapter.SalesAdapter;

import java.util.ArrayList;
import java.util.List;

public class SaleActivity extends AppCompatActivity {

  private SalesAdapter salesAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sale);

    Spinner spinner = findViewById(R.id.spinnerUsers);

    ArrayAdapter<String> myAdapter = new CustomArrayAdapter(this, R.layout.spinner_item, getList());
    spinner.setAdapter(myAdapter);

  }

  private List<String> getList() {
    String[] years = {"1996","1997","1998","1998"};
    List<String> myList = new ArrayList<>();
    for(String value : years){
      myList.add(value);
    }
    return myList;
  }
}