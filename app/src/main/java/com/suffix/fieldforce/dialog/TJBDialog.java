package com.suffix.fieldforce.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.model.TJBInfoData;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TJBDialog extends DialogFragment {

  @BindView(R.id.txtQuantity)
  TextInputEditText txtQuantity;
  @BindView(R.id.txtProductName)
  TextView txtProductName;
  @BindView(R.id.btnUpdate)
  Button btnUpdate;

  private TJBDialogListener tjbDialogListener;

  public void setTjbDialogListener(TJBDialogListener tjbDialogListener) {
    this.tjbDialogListener = tjbDialogListener;
  }

  @OnClick(R.id.btnCancel)
  public void cancel(){
    dismiss();
  }

  @OnClick(R.id.btnUpdate)
  public void onViewClicked() {

    String quantity = txtQuantity.getText().toString();

    if(tjbDialogListener != null){
      tjbDialogListener.onSubmit(quantity);
      dismiss();
    }

  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View view = inflater.inflate(R.layout.layout_tjb, container, false);
    ButterKnife.bind(this, view);

    return view;
  }
}
