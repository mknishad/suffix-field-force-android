package com.suffix.fieldforce.akg.util;

import android.app.Activity;
import android.content.Context;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.suffix.fieldforce.R;

public class AkgPrintService {
  private static final String TAG = "PrintUtils";
  private WebView mWebView;

  public void doWebViewPrint(Activity activity, String htmlDocument) {
    // Create a WebView object specifically for printing
    WebView webView = new WebView(activity);
    webView.setWebViewClient(new WebViewClient() {

      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
      }

      @Override
      public void onPageFinished(WebView view, String url) {
        Log.i(TAG, "page finished loading " + url);
        createWebPrintJob(activity, view);
        mWebView = null;
      }
    });

    // Generate an HTML document on the fly:
    /*String htmlDocument = "<html><body><h1>Test Content</h1><p>Testing, " +
        "testing, testing...</p></body></html>";*/
    webView.loadDataWithBaseURL(null, htmlDocument, "text/HTML", "UTF-8", null);

    // Keep a reference to WebView object until you pass the PrintDocumentAdapter
    // to the PrintManager
    mWebView = webView;
  }

  private void createWebPrintJob(Activity activity, WebView webView) {
    try {
      // Get a PrintManager instance
      PrintManager printManager = (PrintManager) activity.getSystemService(Context.PRINT_SERVICE);

      String jobName = activity.getString(R.string.app_name) + " Document";

      // Get a print adapter instance
      PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter(jobName);

      // Create a print job with name and adapter instance
      PrintJob printJob = printManager.print(jobName, printAdapter,
          new PrintAttributes.Builder().build());

      // Save the job object for later status checking
      //printJobs.add(printJob);
    } catch (Exception e) {
      Log.e(TAG, "createWebPrintJob: ", e);
      Toast.makeText(activity, "Can't print now!", Toast.LENGTH_SHORT).show();
    }
  }
}
