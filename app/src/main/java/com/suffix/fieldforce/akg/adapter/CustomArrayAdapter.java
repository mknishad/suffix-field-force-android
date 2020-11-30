package com.suffix.fieldforce.akg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.suffix.fieldforce.R;
import com.suffix.fieldforce.akg.model.CustomerData;

import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<CustomerData> {

  private List<CustomerData> storeModels;
  private Context context;

  public CustomArrayAdapter(Context context, int resourceId,
                            List<CustomerData> storeModels) {
    super(context, resourceId, storeModels);
    this.storeModels = storeModels;
    this.context = context;
  }

  @Override
  public View getDropDownView(int position, View convertView,
                              ViewGroup parent) {
    return getCustomView(position, convertView, parent);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    return getCustomView(position, convertView, parent);
  }

  public View getCustomView(int position, View convertView, ViewGroup parent) {

    final CustomerData storeModel = storeModels.get(position);

    LayoutInflater inflater=(LayoutInflater) context.getSystemService(  Context.LAYOUT_INFLATER_SERVICE );
    View row=inflater.inflate(R.layout.spinner_item, parent, false);

    TextView name = row.findViewById(R.id.txtName);
    TextView address = row.findViewById(R.id.txtAddress);

    name.setText(storeModel.getCustomerName());
    address.setText(storeModel.getConsumerCode());

    /*if (position == 0) {//Special style for dropdown header
      //label.setTextColor(context.getResources().getColor(R.color.text_hint_color));
      name.setTextColor(context.getResources().getColor(R.color.black_overlay));
      name.setText("SELECT USER");
      address.setVisibility(View.GONE);
    }*/

    return row;
  }

}