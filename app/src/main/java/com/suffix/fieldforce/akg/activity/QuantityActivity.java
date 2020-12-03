package com.suffix.fieldforce.akg.activity;

import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.suffix.fieldforce.R;
import com.suffix.fieldforce.akg.model.ButtonKeyValueModel;
import com.suffix.fieldforce.akg.model.product.CartModel;
import com.suffix.fieldforce.akg.model.product.CategoryModel;
import com.suffix.fieldforce.akg.util.AkgConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class QuantityActivity extends AppCompatActivity {

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.txtBase)
  TextView txtBase;

  @BindView(R.id.txtSOQ)
  TextView txtSOQ;

  private LinearLayout layoutOk;
  private LinearLayout layoutClear;
  private EditText txtResult;
  private Button btnRow00, btnRow01, btnRow10, btnRow11, btnRow20, btnRow21, btnRow30, btnRow31, btnRow40, btnRow41;

  private List<Button> buttons = new ArrayList<>();
  private List<ButtonKeyValueModel> buttonKeyValue = new ArrayList<>();
  private CategoryModel categoryModel;

  private Realm realm;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_quantity);
    ButterKnife.bind(this);

    setupToolbar();

    bindViews();
    bindListWithValue();

    realm = Realm.getDefaultInstance();

    //CATEGORY_MODEL
    if (getIntent().hasExtra("CATEGORY_MODEL")) {
      categoryModel = getIntent().getParcelableExtra("CATEGORY_MODEL");
    }

    txtBase.setText(categoryModel.getInHandQty().toString());
    txtSOQ.setText(categoryModel.getInHandQty().toString());

  }

  private void setupToolbar() {
    setSupportActionBar(toolbar);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      toolbar.setTitleTextColor(getResources().getColor(android.R.color.white, null));
    } else {
      toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayShowTitleEnabled(true);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      toolbar.getNavigationIcon().setColorFilter(new BlendModeColorFilter(Color.WHITE,
          BlendMode.SRC_ATOP));
    } else {
      toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
    }
  }

  private void prepareKeyValues(String type) {
    switch (type) {
      case AkgConstants.CIGARETTE:
        buttonKeyValue.add(new ButtonKeyValueModel("+1000", "1000"));
        buttonKeyValue.add(new ButtonKeyValueModel("-1000", "1000"));
        buttonKeyValue.add(new ButtonKeyValueModel("+500", "500"));
        buttonKeyValue.add(new ButtonKeyValueModel("-500", "500"));
        buttonKeyValue.add(new ButtonKeyValueModel("+200", "200"));
        buttonKeyValue.add(new ButtonKeyValueModel("-200", "200"));
        buttonKeyValue.add(new ButtonKeyValueModel("+100", "100"));
        buttonKeyValue.add(new ButtonKeyValueModel("-100", "100"));
        buttonKeyValue.add(new ButtonKeyValueModel("+20", "20"));
        buttonKeyValue.add(new ButtonKeyValueModel("-20", "20"));
        break;
      case AkgConstants.BIRI:
        buttonKeyValue.add(new ButtonKeyValueModel("+500", "500"));
        buttonKeyValue.add(new ButtonKeyValueModel("-500", "500"));
        buttonKeyValue.add(new ButtonKeyValueModel("+250", "250"));
        buttonKeyValue.add(new ButtonKeyValueModel("-250", "250"));
        buttonKeyValue.add(new ButtonKeyValueModel("+100", "100"));
        buttonKeyValue.add(new ButtonKeyValueModel("-100", "100"));
        buttonKeyValue.add(new ButtonKeyValueModel("+50", "50"));
        buttonKeyValue.add(new ButtonKeyValueModel("-50", "50"));
        buttonKeyValue.add(new ButtonKeyValueModel("+25", "25"));
        buttonKeyValue.add(new ButtonKeyValueModel("-25", "25"));
        break;
      case AkgConstants.MATCH:
        buttonKeyValue.add(new ButtonKeyValueModel("+24", "24"));
        buttonKeyValue.add(new ButtonKeyValueModel("-24", "24"));
        buttonKeyValue.add(new ButtonKeyValueModel("+12", "12"));
        buttonKeyValue.add(new ButtonKeyValueModel("-12", "12"));
        buttonKeyValue.add(new ButtonKeyValueModel("+6", "6"));
        buttonKeyValue.add(new ButtonKeyValueModel("-6", "6"));
        buttonKeyValue.add(new ButtonKeyValueModel("+2", "2"));
        buttonKeyValue.add(new ButtonKeyValueModel("-2", "2"));
        buttonKeyValue.add(new ButtonKeyValueModel("+1", "1"));
        buttonKeyValue.add(new ButtonKeyValueModel("-1", "1"));
        break;
    }
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
    prepareKeyValues(AkgConstants.BIRI);
    prepareKeyButtons();

    int index = 0;

    for (ButtonKeyValueModel keyValue : buttonKeyValue) {
      Button button = buttons.get(index);
      button.setText(keyValue.getKey());
      button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (button.getText().toString().contains("+")) {
            txtResult.setText(String.valueOf((Integer.parseInt(txtResult.getText().toString()) + Integer.parseInt(keyValue.getValue()))));
          } else {
            if (Integer.parseInt(keyValue.getValue()) <= Integer.parseInt(txtResult.getText().toString())) {
              txtResult.setText(String.valueOf((Integer.parseInt(txtResult.getText().toString()) - Integer.parseInt(keyValue.getValue()))));
            } else {
              Toast.makeText(QuantityActivity.this, "Negative result is not accepted", Toast.LENGTH_SHORT).show();
            }
          }
        }
      });
      index++;
    }

  }

  private void bindViews() {
    layoutOk = findViewById(R.id.layoutOk);
    layoutClear = findViewById(R.id.layoutClear);
    txtResult = findViewById(R.id.txtResult);
    btnRow00 = findViewById(R.id.btnRow00);
    btnRow01 = findViewById(R.id.btnRow01);
    btnRow10 = findViewById(R.id.btnRow10);
    btnRow11 = findViewById(R.id.btnRow11);
    btnRow20 = findViewById(R.id.btnRow20);
    btnRow21 = findViewById(R.id.btnRow21);
    btnRow30 = findViewById(R.id.btnRow30);
    btnRow31 = findViewById(R.id.btnRow31);
    btnRow40 = findViewById(R.id.btnRow40);
    btnRow41 = findViewById(R.id.btnRow41);

    layoutClear.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        txtResult.setText("0");
      }
    });

    layoutOk.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!txtResult.getText().toString().trim().equals("0")) {

          realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
              CartModel realmCategory = new CartModel();
              realmCategory.setOrderQuantity(txtResult.getText().toString());
              realm.insertOrUpdate(realmCategory);
            }
          }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
              Toast.makeText(QuantityActivity.this, "Successful", Toast.LENGTH_SHORT).show();
              onBackPressed();
            }
          });

//          realm.beginTransaction();
//          CartModel realmCategory = realm.createObject(CartModel.class);
//          realmCategory.setOrderQuantity(txtResult.getText().toString());
//          realm.commitTransaction();
//
//          Toast.makeText(QuantityActivity.this, "Success", Toast.LENGTH_SHORT).show();
//          onBackPressed();

        }
      }
    });
  }
}