package com.suffix.fieldforce.akg.database.manager;

import com.suffix.fieldforce.akg.model.CustomerData;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealMDatabaseManager {

  private Realm realm;

  public RealMDatabaseManager() {
    realm = Realm.getDefaultInstance();
  }

  public List<CustomerData> prepareCustomerData(){
    final RealmResults<CustomerData> customerDataRealmResults = realm.where(CustomerData.class).findAll();
    return realm.copyFromRealm(customerDataRealmResults);
  }

}
