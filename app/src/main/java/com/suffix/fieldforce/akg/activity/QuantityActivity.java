package com.suffix.fieldforce.akg.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.suffix.fieldforce.R;
import com.suffix.fieldforce.akg.model.ButtonKeyValueModel;

import java.util.ArrayList;
import java.util.List;

public class QuantityActivity extends AppCompatActivity {

  private Toolbar toolbar;
  private LinearLayout layoutOk;
  private LinearLayout layoutClear;
  private EditText txtResult;
  private Button btnRow00, btnRow01, btnRow10, btnRow11, btnRow20, btnRow21, btnRow30, btnRow31, btnRow40, btnRow41;

  private List<Button> buttons = new ArrayList<>();
  private List<ButtonKeyValueModel> buttonKeyValue = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_quantity);
    bindViews();
    bindListWithValue();
  }

  private void prepareKeyValues() {
    buttonKeyValue.add(new ButtonKeyValueModel("+1000","1000"));
    buttonKeyValue.add(new ButtonKeyValueModel("-1000","1000"));
    buttonKeyValue.add(new ButtonKeyValueModel("+500","500"));
    buttonKeyValue.add(new ButtonKeyValueModel("-500","500"));
    buttonKeyValue.add(new ButtonKeyValueModel("+200","200"));
    buttonKeyValue.add(new ButtonKeyValueModel("-200","200"));
    buttonKeyValue.add(new ButtonKeyValueModel("+100","100"));
    buttonKeyValue.add(new ButtonKeyValueModel("-100","100"));
    buttonKeyValue.add(new ButtonKeyValueModel("+20","20"));
    buttonKeyValue.add(new ButtonKeyValueModel("-20","20"));
  }

  private void prepareKeyButtons() {
    buttons.add(btnRow00);
    buttons.add(btnRow01);
    buttons.add(btnRow10);
    buttons.add(btnRow11);
    buttons.add(btnRow20);
    buttons.add(btnRow21);
    buttons.add(btnRow30);
    buttons.add(btnRow31);
    buttons.add(btnRow40);
    buttons.add(btnRow41);
  }

  private void bindListWithValue() {
    prepareKeyValues();
    prepareKeyButtons();

    int index = 0;

    for (ButtonKeyValueModel keyValue : buttonKeyValue) {
      Button button = buttons.get(index);
      button.setText(keyValue.getKey());
      button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (button.getText().toString().contains("+")){
            txtResult.setText(String.valueOf((Integer.parseInt(txtResult.getText().toString()) + Integer.parseInt(keyValue.getValue()))));
          }else{
            if(Integer.parseInt(keyValue.getValue()) <= Integer.parseInt(txtResult.getText().toString())){
              txtResult.setText(String.valueOf((Integer.parseInt(txtResult.getText().toString()) - Integer.parseInt(keyValue.getValue()))));
            }else{
              Toast.makeText(QuantityActivity.this, "Negative result is not accepted", Toast.LENGTH_SHORT).show();
            }
          }
        }
      });
      index++;
    }

  }

  private void bindViews(){
    toolbar =  findViewById(R.id.toolbar);
    layoutOk =  findViewById(R.id.layoutOk);
    layoutClear =  findViewById(R.id.layoutClear);
    txtResult =  findViewById(R.id.txtResult);
    btnRow00 =  findViewById(R.id.btnRow00);
    btnRow01 =  findViewById(R.id.btnRow01);
    btnRow10 =  findViewById(R.id.btnRow10);
    btnRow11 =  findViewById(R.id.btnRow11);
    btnRow20 =  findViewById(R.id.btnRow20);
    btnRow21 =  findViewById(R.id.btnRow21);
    btnRow30 =  findViewById(R.id.btnRow30);
    btnRow31 =  findViewById(R.id.btnRow31);
    btnRow40 =  findViewById(R.id.btnRow40);
    btnRow41 =  findViewById(R.id.btnRow41);

    layoutClear.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        txtResult.setText("0");
      }
    });
  }
}