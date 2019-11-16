package com.suffix.fieldforce.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.ConnectivityManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.InverseMethod
import com.google.android.material.snackbar.Snackbar
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun showSnackbar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    fun hasInternetConnection(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }

    @JvmStatic
    fun getFormattedDate(inputDate: String?): String? {
        if (inputDate == null) {
            return ""
        }
        val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        //SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy, hh:mm aa");
        val output = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        return try {
            output.format(input.parse(inputDate))
        } catch (e: ParseException) {
            e.printStackTrace()
            inputDate
        }
    }

    @InverseMethod("getFormattedDate")
    @JvmStatic
    fun getUtcDate(inputDate: String?): String? {
        if (inputDate == null) {
            return ""
        }
        val input = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        val output = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        return try {
            output.format(input.parse(inputDate))
        } catch (e: ParseException) {
            e.printStackTrace()
            inputDate
        }
    }

    fun convertToUtc(date: Date): String? {
        val format = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            Locale.getDefault()
        )

        //format.timeZone = TimeZone.getTimeZone("UTC")
        return format.format(date)
    }

    fun createDate(year: Int, month: Int, day: Int): Date {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month)
        cal.set(Calendar.DAY_OF_MONTH, day)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        return cal.time
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun getResizedBitmap(bitmap: Bitmap): Bitmap {
        val newWidth = bitmap.width / 2
        val newHeight = bitmap.height / 2
        val matrix = Matrix()
        matrix.postScale(newWidth.toFloat(), newHeight.toFloat())
        val resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, newWidth, newHeight, matrix,
            false)
        return resizedBitmap
    }
}
