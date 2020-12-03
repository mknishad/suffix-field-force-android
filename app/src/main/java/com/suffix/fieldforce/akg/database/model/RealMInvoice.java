package com.suffix.fieldforce.akg.database.model;

import com.suffix.fieldforce.akg.model.InvoiceRequest;

import io.realm.RealmList;
import io.realm.RealmObject;

public class RealMInvoice extends RealmObject {

  RealmList<InvoiceRequest> requestRealmList;

  public RealmList<InvoiceRequest> getRequestRealmList() {
    return requestRealmList;
  }

  public void setRequestRealmList(RealmList<InvoiceRequest> requestRealmList) {
    this.requestRealmList = requestRealmList;
  }
}
