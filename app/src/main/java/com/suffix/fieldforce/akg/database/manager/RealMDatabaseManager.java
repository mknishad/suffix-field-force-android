package com.suffix.fieldforce.akg.database.manager;

import com.suffix.fieldforce.akg.database.RealmDatabseManagerInterface;
import com.suffix.fieldforce.akg.database.model.RealMProductCategory;
import com.suffix.fieldforce.akg.model.CustomerData;
import com.suffix.fieldforce.akg.model.InvoiceRequest;
import com.suffix.fieldforce.akg.model.product.CartModel;
import com.suffix.fieldforce.akg.model.product.CategoryModel;
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

    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        results.deleteAllFromRealm();
      }
    });
  }

  public void deleteAllCart() {
    final RealmResults<CartModel> results = realm.where(CartModel.class).findAll();

    if (results.size() > 0) {
      realm.executeTransaction(new Realm.Transaction() {
        @Override
        public void execute(Realm realm) {
          results.deleteAllFromRealm();
        }
      });
    }
  }

  public void setSalesQuantity(long id, int value) {
    final RealmResults<CategoryModel> results = realm.where(CategoryModel.class).findAll();
  }

  public void deleteAllCustomer(RealmDatabseManagerInterface.Customer customerInterface) {
    final RealmResults<CustomerData> results = realm.where(CustomerData.class).findAll();

    realm.executeTransactionAsync(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        results.deleteAllFromRealm();
      }
    }, new Realm.Transaction.OnSuccess() {
      @Override
      public void onSuccess() {
        if(customerInterface != null){
          customerInterface.onCustomerDelete(true);
        }
      }
    });
  }
}
