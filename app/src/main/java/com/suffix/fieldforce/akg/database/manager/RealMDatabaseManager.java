package com.suffix.fieldforce.akg.database.manager;

import com.suffix.fieldforce.akg.database.model.RealMProductCategory;
import com.suffix.fieldforce.akg.model.CustomerData;
import com.suffix.fieldforce.akg.model.InvoiceRequest;
import com.suffix.fieldforce.akg.model.product.ProductCategory;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealMDatabaseManager {

  private Realm realm;

  public RealMDatabaseManager() {
    realm = Realm.getDefaultInstance();
  }

  public List<CustomerData> prepareCustomerData() {
    final RealmResults<CustomerData> customerDataRealmResults = realm.where(CustomerData.class).findAll();
    return realm.copyFromRealm(customerDataRealmResults);
  }

  public ProductCategory prepareCategoryData() {
    ProductCategory productCategory = new ProductCategory();
    final RealmResults<RealMProductCategory> customerDataRealmResults = realm.where(RealMProductCategory.class).findAll();
    List<RealMProductCategory> realMProductCategory = realm.copyFromRealm(customerDataRealmResults);
    if (realMProductCategory.size() > 0) {
      productCategory.setCigrettee(realMProductCategory.get(0).getCigrettee());
      productCategory.setBidi(realMProductCategory.get(0).getBidi());
      productCategory.setMatch(realMProductCategory.get(0).getMatch());
    }
    return productCategory;
  }

  public List<InvoiceRequest> prepareInvoiceRequest() {
    final RealmResults<InvoiceRequest> invoiceRequests = realm.where(InvoiceRequest.class).findAll();
    return realm.copyFromRealm(invoiceRequests);
  }

  public void deleteAllInvoice() {
    final RealmResults<InvoiceRequest> results = realm.where(InvoiceRequest.class).findAll();

    realm.executeTransactionAsync(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        // remove single match
        //results.deleteFirstFromRealm();
        //results.deleteLastFromRealm();

        // Delete all matches
        results.deleteAllFromRealm();
      }
    });
  }

}
