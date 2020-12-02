package com.suffix.fieldforce.akg.util;

import android.app.Activity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.suffix.fieldforce.akg.model.AkgLoginResponse;
import com.suffix.fieldforce.akg.model.CustomerData;
import com.suffix.fieldforce.akg.model.GlobalSettings;
import com.suffix.fieldforce.akg.model.product.CategoryModel;

import java.util.Arrays;
import java.util.Locale;

import io.realm.RealmResults;

public class AkgPrintingService {
  private static final String TAG = "PrintUtils";
  private WebView mWebView;
  private final Activity mActivity;

  public AkgPrintingService(Activity activity) {
    mActivity = activity;
  }

  public void print(CustomerData customerData, long currentTimeMillis, AkgLoginResponse loginResponse,
                    RealmResults<CategoryModel> products) {
    try {
      String time = android.text.format.DateFormat.format("dd/MM/yyyy HH:mm", new java.util.Date()).toString();

      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("[L]").append("Distributor Name").append("\n");
      stringBuilder.append("[L]").append(customerData.getMobileNo()).append(", ").append(time).append("\n");
      stringBuilder.append("[L]Memo: ").append(currentTimeMillis).append("\n");
      stringBuilder.append("[L]SR: ").append("HR Name").append("\n");
      stringBuilder.append("[L]").append(customerData.getCustomerName()).append("\n");
      stringBuilder.append("[L]\n");
      stringBuilder.append("[L]<b>Brand</b>[C]<b>Q.</b>[R]<b>Tk</b>\n");
      stringBuilder.append("[L]--------------------------------\n");

      double totalAmount = 0;
      for (CategoryModel product : products) {
        int brandLen = product.getProductCode().length();
        int quantityLen = String.valueOf(product.getOrderQuantity()).length();
        double amount = product.getSellingRate() * Double.parseDouble(product.getOrderQuantity());
        String tk = String.format(Locale.getDefault(), "%.2f", amount);
        totalAmount += amount;
        int tkLen = tk.length();
        int totalLen = brandLen + quantityLen + tkLen;
        int dotLen = 28 - totalLen;

        int dotNum = 0;
        if (dotLen > 1) {
          dotNum = dotLen / 2;
        }

        char[] dots = new char[dotNum];
        Arrays.fill(dots, '-');

        stringBuilder.append("[L]").append(product.getProductCode()).append(new String(dots))
            .append("[C]").append(product.getOrderQuantity()).append(new String(dots))
            .append("[R]").append(tk).append("\n");
      }

      stringBuilder.append("[L]--------------------------------\n");

      String totalAmountString = String.format(Locale.getDefault(), "%.2f", totalAmount);
      int totalAmountLen = totalAmountString.length();
      int dotLen = 30 - totalAmountLen - 5;

      char[] dots = new char[dotLen];
      Arrays.fill(dots, '-');

      stringBuilder.append("[L]TOTAL").append(new String(dots)).append("[R]")
          .append(totalAmountString).append("\n");
      stringBuilder.append("[L]\n");

      String pricePerPack = "";
      for (GlobalSettings globalSettings : loginResponse.getData().getGlobalSettingList()) {
        if (globalSettings.getAttributeName().equalsIgnoreCase("RATE_PER_PACK")) {
          pricePerPack = globalSettings.getAttributeValue();
        }
      }

      stringBuilder.append("[L]Price per pack:\n");
      stringBuilder.append("[L]").append(pricePerPack).append("\n");
      stringBuilder.append("[L]\n");
      stringBuilder.append("[C]Thanks for your purchase!\n");

      Log.d(TAG, "print: stringBuilder = " + stringBuilder.toString());

      /*EscPosPrinter printer = new EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(),
          203, 48f, 32);
      printer.printFormattedText(stringBuilder.toString());*/
    } catch (Exception e) {
      Toast.makeText(mActivity, "Printing failed!", Toast.LENGTH_SHORT).show();
    }
  }
}
