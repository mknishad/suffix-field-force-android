package com.suffix.fieldforce.activity.task;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.model.AssignedTask;
import com.suffix.fieldforce.model.DistrictData;
import com.suffix.fieldforce.retrofitapi.APIClient;
import com.suffix.fieldforce.retrofitapi.APIInterface;
import com.suffix.fieldforce.util.Constants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTaskActivity extends AppCompatActivity {

    @BindView(R.id.imgMap)
    ImageView imgMap;
    @BindView(R.id.imgDrawer)
    ImageView imgDrawer;
    @BindView(R.id.toolBarTitle)
    TextView toolBarTitle;
    @BindView(R.id.ticketTitle)
    TextInputEditText ticketTitle;
    @BindView(R.id.ticketDetails)
    TextInputEditText ticketDetails;
    @BindView(R.id.priority)
    TextInputEditText priority;
    @BindView(R.id.address)
    TextInputEditText address;
    @BindView(R.id.thanaId)
    TextInputEditText thanaId;
    @BindView(R.id.districtId)
    TextInputEditText districtId;
    @BindView(R.id.startDate)
    TextInputEditText startDate;
    @BindView(R.id.endDate)
    TextInputEditText endDate;
    @BindView(R.id.imgAttach)
    ImageView imgAttach;
    @BindView(R.id.btnAttachImage)
    Button btnAttachImage;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.consumerName)
    TextInputEditText consumerName;
    @BindView(R.id.consumerMobileNumber)
    TextInputEditText consumerMobileNumber;

    private Bitmap bitmap;
    private Intent intent;

    private List<DistrictData> districtData;

    private APIInterface apiInterface = APIClient.getApiClient().create(APIInterface.class);

    final Calendar myCalendar = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        ButterKnife.bind(this);

        setActionBar();
        verifyStoragePermissions(this);

        initDistrictThana();
    }

    private void initDistrictThana() {
        Call<List<DistrictData>> getDistrictThanaInfo = apiInterface.getDistrictThanaInfo();
        getDistrictThanaInfo.enqueue(new Callback<List<DistrictData>>() {
            @Override
            public void onResponse(Call<List<DistrictData>> call, Response<List<DistrictData>> response) {
                districtData = response.body();
            }

            @Override
            public void onFailure(Call<List<DistrictData>> call, Throwable t) {

            }
        });
    }

    private void setActionBar() {
        imgDrawer.setImageResource(R.drawable.ic_arrow_back);
    }

    DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            startDate.setText(simpleDateFormat.format(myCalendar.getTime()));
        }

    };

    DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            endDate.setText(simpleDateFormat.format(myCalendar.getTime()));
        }

    };

    @OnClick({R.id.imgDrawer, R.id.startDate, R.id.endDate, R.id.btnAttachImage, R.id.btnSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgDrawer:
                super.onBackPressed();
                break;
            case R.id.startDate:
                new DatePickerDialog(this, startDateListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.endDate:
                new DatePickerDialog(this, endDateListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.btnAttachImage:
                selectImage();
                break;
            case R.id.btnSubmit:
                SmartLocation.with(CreateTaskActivity.this).location()
                        .oneFix()
                        .start(new OnLocationUpdatedListener() {
                            @Override
                            public void onLocationUpdated(Location location) {
                                uploadData(location);
                            }
                        });
                break;
        }
    }

    private void uploadData(Location location) {
        APIInterface apiInterface = APIClient.getApiClient().create(APIInterface.class);
        Call<List<AssignedTask>> taskEntry = apiInterface.taskEntry(
                Constants.INSTANCE.getKEY(),
                Constants.INSTANCE.getUSER_ID(),
                String.valueOf(location.getLatitude()),
                String.valueOf(location.getLongitude()),
                "4",
                ticketTitle.getText().toString(),
                priority.getText().toString(),
                imagetTOString(),
                ticketDetails.getText().toString(),
                startDate.getText().toString(),
                endDate.getText().toString(),
                "12",
                "12",
                address.getText().toString(),
                consumerName.getText().toString(),
                consumerMobileNumber.getText().toString()
        );

        taskEntry.enqueue(new Callback<List<AssignedTask>>() {
            @Override
            public void onResponse(Call<List<AssignedTask>> call, Response<List<AssignedTask>> response) {
                Toast.makeText(CreateTaskActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<AssignedTask>> call, Throwable t) {

            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("iamge/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 777);

    }

    private String imagetTOString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByte, Base64.DEFAULT);
    }

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 777 && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                imgAttach.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SmartLocation.with(CreateTaskActivity.this).location().stop();
    }
}
