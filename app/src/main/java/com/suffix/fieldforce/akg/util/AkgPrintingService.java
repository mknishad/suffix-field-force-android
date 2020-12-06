package com.suffix.fieldforce.akg.util;

import android.util.Log;

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.suffix.fieldforce.akg.adapter.PrintingInterface;
import com.suffix.fieldforce.akg.model.AkgLoginResponse;
import com.suffix.fieldforce.akg.model.GlobalSettings;
import com.suffix.fieldforce.akg.model.InvoiceProduct;
import com.suffix.fieldforce.akg.model.InvoiceRequest;
import com.suffix.fieldforce.akg.model.product.CategoryModel;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AkgPrintingService {
  private static final String TAG = "PrintUtils";

  public void printStock(String distributorName, String distributorMobile,
                         AkgLoginResponse loginResponse, List<CategoryModel> products,
                         PrintingInterface printingInterface) {
    try {
      String time = android.text.format.DateFormat.format("dd/MM/yyyy HH:mm", new java.util.Date()).toString();

      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("[L]").append(distributorName).append("\n");
      stringBuilder.append("[L]").append(distributorMobile).append(", ").append(time).append("\n");
      stringBuilder.append("[L]SR: ").append(loginResponse.getData().getUserName()).append("\n");
      stringBuilder.append("[L]<b>Stock:</b>\n");
      stringBuilder.append("[L]--------------------------------\n");

      int primaryQuantity = 0;
      int currentQuantity = 0;

      for (CategoryModel product : products) {
        primaryQuantity += product.getInHandQty();
        currentQuantity += product.getSalesQty();
        stringBuilder.append("[L]").append(printFirst(product.getProductCode()))
            .append(printLast(String.valueOf(product.getInHandQty()))).append("--")
            .append(printLast(String.valueOf(product.getInHandQty() - product.getSalesQty()))).append("\n");
      }

      stringBuilder.append("[L]--------------------------------\n");

//      stringBuilder.append("[L]").append(printFirst("TOTAL"))
//          .append(printLast("-")).append("--")
//          .append(printLast(String.valueOf(currentQuantity))).append("\n");

      Log.d(TAG, "printStock: stringBuilder = " + stringBuilder.toString());

      EscPosPrinter printer = new EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(),
          203, 48f, 32);
      printer.printFormattedText(stringBuilder.toString());

      printingInterface.onPrintSuccess("Done!");
    } catch (Exception e) {
      Log.e(TAG, "printStock: ", e);
      printingInterface.onPrintSuccess("Printing Failed!");
    }
  }

  public void printSale(String distributorName, String distributorMobile,
                         AkgLoginResponse loginResponse, List<CategoryModel> products,
                         PrintingInterface printingInterface) {
    try {
      String time = android.text.format.DateFormat.format("dd/MM/yyyy HH:mm", new java.util.Date()).toString();

      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("[L]").append(distributorName).append("\n");
      stringBuilder.append("[L]").append(distributorMobile).append(", ").append(time).append("\n");
      stringBuilder.append("[L]SR: ").append(loginResponse.getData().getUserName()).append("\n");
      stringBuilder.append("[L]<b>Sale:</b>\n");
      stringBuilder.append("[L]--------------------------------\n");

      int primaryQuantity = 0;
      int currentQuantity = 0;

      for (CategoryModel product : products) {
        primaryQuantity += product.getInHandQty();
        currentQuantity += product.getSalesQty();
        stringBuilder.append("[L]").append(printFirst(product.getProductCode()))
            .append(printLast(String.valueOf(product.getSalesQty()))).append("--")
            .append(printLast(String.format(Locale.getDefault(), "%.2f",
                product.getSellingRate() - product.getSalesQty()))).append("\n");
      }

      stringBuilder.append("[L]--------------------------------\n");

//      stringBuilder.append("[L]").append(printFirst("TOTAL"))
//          .append(printLast("-")).append("--")
//          .append(printLast(String.valueOf(currentQuantity))).append("\n");

      Log.d(TAG, "printStock: stringBuilder = " + stringBuilder.toString());

      EscPosPrinter printer = new EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(),
          203, 48f, 32);
      printer.printFormattedText(stringBuilder.toString());

      printingInterface.onPrintSuccess("Done!");
    } catch (Exception e) {
      Log.e(TAG, "printStock: ", e);
      printingInterface.onPrintSuccess("Printing Failed!");
    }
  }

  public void print(String distributorName, String distributorMobile,
                    AkgLoginResponse loginResponse, InvoiceRequest invoiceRequest, PrintingInterface printingInterface) {
    try {
      String time = android.text.format.DateFormat.format("dd/MM/yyyy HH:mm", invoiceRequest.getInvoiceDate()).toString();
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("[L]").append(distributorName).append("\n");
      stringBuilder.append("[L]").append(distributorMobile).append(", ").append(time).append("\n");
      stringBuilder.append("[L]Memo: ").append(invoiceRequest.getInvoiceId()).append("\n");
      stringBuilder.append("[L]SR: ").append(loginResponse.getData().getUserName()).append("\n");
      stringBuilder.append("[L]").append(invoiceRequest.getCustomerName()).append("\n");
      stringBuilder.append("[L]\n");
      stringBuilder.append("[L]<b>Brand</b>[C]<b>Q.</b>[R]<b>Tk</b>\n");
      stringBuilder.append("[L]--------------------------------\n");

      double totalAmount = 0;
      for (InvoiceProduct product : invoiceRequest.getInvoiceProducts()) {
        //int brandLen = product.getProductCode().length();
        //int quantityLen = String.valueOf(product.getProductQty()).length();
        double amount = product.getSellingRate() * product.getProductQty();
        String tk = String.format(Locale.getDefault(), "%.2f", amount);
        totalAmount += amount;
        /*totalAmount += amount;
        int tkLen = tk.length();
        int totalLen = brandLen + quantityLen + tkLen;
        int dotLen = 30 - totalLen;

        int dotNum = 0;
        if (dotLen > 1) {
          dotNum = dotLen / 2;
        }

        char[] dots = new char[dotNum];
        Arrays.fill(dots, '-');*/

        /*stringBuilder.append("[L]").append(product.getProductCode()).append(new String(dots))
            .append("[L]").append(product.getProductQty())
            .append("[R]").append(new String(dots)).append(tk).append("\n");*/

        stringBuilder.append("[L]").append(printFirst(product.getProductCode()))
            .append(printLast(String.valueOf(product.getProductQty()))).append("--")
            .append(printLast(tk)).append("\n");
      }

      stringBuilder.append("[L]--------------------------------\n");

      String totalAmountString = String.format(Locale.getDefault(), "%.2f", totalAmount);
      int totalAmountLen = totalAmountString.length();
      int dotLen = 31 - totalAmountLen - 5;

      char[] dots = new char[dotLen];
      Arrays.fill(dots, '-');

      stringBuilder.append("[L]TOTAL").append(new String(dots)).append("[R]")
          .append(totalAmountString).append("\n");
      stringBuilder.append("[L]\n");

      if (totalAmount > invoiceRequest.getRecievedAmount()) {
        stringBuilder.append("[L]").append("Due.").append("\n");
      } else {
        stringBuilder.append("[L]").append("Paid.").append("\n");
      }
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

      EscPosPrinter printer = new EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(),
          203, 48f, 32);
      printer.printFormattedText(stringBuilder.toString());

      printingInterface.onPrintSuccess("Done!");
    } catch (Exception e) {
      Log.e(TAG, "print: ", e);
      printingInterface.onPrintSuccess("Printing Failed!");
    }
  }

  public String printFirst(String t) {
    char[] line = new char[10];
    char[] ca = t.toCharArray();
    for (int i = 0; i < 10; i++) {
      if (i < t.length()) {
        line[i] = ca[i];
      } else {
        line[i] = '-';
      }
    }

    return new String(line);
  }

  public String printLast(String t) {
    char[] line = new char[10];
    char[] ca = t.toCharArray();
    int index = 0;
    for (int i = 0; i < 10; i++) {
      if (i < 10 - t.length()) {
        line[i] = '-';
      } else {
        line[i] = ca[index];
        index++;
      }
    }
    return new String(line);
  }
}
