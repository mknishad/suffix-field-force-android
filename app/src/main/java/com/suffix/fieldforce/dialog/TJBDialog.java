package com.suffix.fieldforce.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

  @BindView(R.id.txtLocation)
  TextInputEditText txtLocation;
  @BindView(R.id.txtACoreId)
  TextInputEditText txtACoreId;
  @BindView(R.id.txtACore)
  TextInputEditText txtACore;
  @BindView(R.id.txtBCoreId)
  TextInputEditText txtBCoreId;
  @BindView(R.id.txtBCore)
  TextInputEditText txtBCore;
  @BindView(R.id.btnCancel)
  TextView btnCancel;
  @BindView(R.id.btnSubmit)
  TextView btnSubmit;
  @BindView(R.id.layoutButton)
  LinearLayout layoutButton;

  private TJBDialogListener tjbDialogListener;

  public void setTjbDialogListener(TJBDialogListener tjbDialogListener) {
    this.tjbDialogListener = tjbDialogListener;
  }

  @OnClick(R.id.btnCancel)
  public void cancel(){
    dismiss();
  }

  @OnClick(R.id.btnSubmit)
  public void onViewClicked() {

    String locationName = txtLocation.getText().toString();
    String fromCoreId = txtACoreId.getText().toString();
    String fromCore = txtACore.getText().toString();
    String toCoreId = txtBCoreId.getText().toString();
    String toCore = txtBCore.getText().toString();

    if(tjbDialogListener != null){

      tjbDialogListener.onSubmit(new TJBInfoData(locationName, "", "", fromCoreId, fromCore, toCoreId, toCore));
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
