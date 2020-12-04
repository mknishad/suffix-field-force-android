package com.suffix.fieldforce.akg.database;

public interface RealmDatabseManagerInterface {
  public interface Customer{
    public void onCustomerDelete(boolean OnSuccess);
  }
  public interface Cart{
    public void onCartDelete(boolean OnSuccess);
  }
  public interface Sync{
    public void onSuccess();
  }
}
