package com.suffix.fieldforce.akg.database.model;

import com.suffix.fieldforce.akg.model.CustomerData;

import io.realm.RealmList;
import io.realm.RealmObject;

public class RealMCustomer extends RealmObject {

  RealmList<CustomerData> customerData;

  public RealmList<CustomerData> getCustomerData() {
    return customerData;
  }

  public void setCustomerData(RealmList<CustomerData> customerData) {
    this.customerData = customerData;
  }
}
