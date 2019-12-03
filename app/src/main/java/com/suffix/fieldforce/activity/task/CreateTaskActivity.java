package com.suffix.fieldforce.activity.task;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.developer.kalert.KAlertDialog;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.model.DistrictData;
import com.suffix.fieldforce.model.DistrictInfo;
import com.suffix.fieldforce.model.TaskEntry;
import com.suffix.fieldforce.model.ThanaInfo;
import com.suffix.fieldforce.retrofitapi.APIClient;
import com.suffix.fieldforce.retrofitapi.APIInterface;
import com.suffix.fieldforce.util.Constants;
import com.suffix.fieldforce.util.Utils;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
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
    AutoCompleteTextView priority;
    @BindView(R.id.address)
    TextInputEditText address;
    @BindView(R.id.thanaId)
    AutoCompleteTextView thanaId;
    @BindView(R.id.districtId)
    AutoCompleteTextView districtId;
    @BindView(R.id.startDate)
    TextInputEditText startDate;
    @BindView(R.id.endDate)
    TextInputEditText endDate;
    @BindView(R.id.imgAttach)
    ImageView imgAttach;
    @BindView(R.id.btnAttachImage)
    FloatingActionButton btnAttachImage;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.consumerName)
    TextInputEditText consumerName;
    @BindView(R.id.consumerMobileNumber)
    TextInputEditText consumerMobileNumber;

    private Bitmap bitmap;

    private String strPriority;
    private String strDistrictId;
    private String strThanaId;

    private String[] priorityList = new String[]{"Low", "Medium", "High"};
    private String[] districtList;
    private String[] thanaList;
    private ArrayAdapter<String> adapter;
    private List<DistrictData> districtData;
    private List<DistrictInfo> districtInfos;
    private APIInterface apiInterface = APIClient.getApiClient().create(APIInterface.class);
    private final Calendar myCalendar = Calendar.getInstance();
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd hh:mm:ss a");
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    SpotsDialog waitingDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        ButterKnife.bind(this);
        setActionBar();
        initDistrictThana();
        verifyStoragePermissions(this);

        waitingDialog = new SpotsDialog(CreateTaskActivity.this, R.style.Custom);

        priority.setKeyListener(null);
        districtId.setKeyListener(null);
        thanaId.setKeyListener(null);

        priority.setAdapter(new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.dropdown_menu_popup_item,
                priorityList));
        priority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priority.showDropDown();
            }
        });

        priority.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              strPriority = String.valueOf(i);
            }
        });

        districtId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                districtId.showDropDown();
            }
        });
        districtId.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                strDistrictId = String.valueOf(i);
                DistrictInfo districtInfo = districtInfos.get(i);
                List<ThanaInfo> thanaInfos = districtInfo.getThanaInfo();
                thanaList = new String[thanaInfos.size()];
                for(int thanaIndex = 0; thanaIndex<thanaInfos.size(); thanaIndex++){
                    thanaList [thanaIndex] = thanaInfos.get(thanaIndex).getThanaName();
                }

                thanaId.setText("");

                thanaId.setAdapter(new ArrayAdapter<>(
                        getApplicationContext(),
                        R.layout.dropdown_menu_popup_item,
                        thanaList));
                thanaId.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        thanaId.showDropDown();
                    }
                });
            }
        });

        thanaId.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                strThanaId = String.valueOf(i);
            }
        });


    }

    private void initDistrictThana() {
        Call<List<DistrictData>> getDistrictThanaInfo = apiInterface.getDistrictThanaInfo();
        getDistrictThanaInfo.enqueue(new Callback<List<DistrictData>>() {
            @Override
            public void onResponse(Call<List<DistrictData>> call, Response<List<DistrictData>> response) {
                districtData = response.body();
                districtInfos = districtData.get(0).getResponseData();
                districtList = new String[districtInfos.size()];
                for (int i = 0; i < districtInfos.size(); i++) {
                    districtList[i] = districtInfos.get(i).getDistrictName();
                }
                districtId.setAdapter(new ArrayAdapter<>(
                        getApplicationContext(),
                        R.layout.dropdown_menu_popup_item,
                        districtList));
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
            String currentDateTime = simpleDateFormat.format(myCalendar.getTime());
            if(currentDateTime.contains("AM")){
                currentDateTime.replace("AM","am");
            }else{
                currentDateTime.replace("PM","pm");
            }
            startDate.setText(currentDateTime);
        }

    };

    DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String currentDateTime = simpleDateFormat.format(myCalendar.getTime());
            if(currentDateTime.contains("AM")){
                currentDateTime.replace("AM","am");
            }else{
                currentDateTime.replace("PM","pm");
            }
            endDate.setText(currentDateTime);
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
                                waitingDialog.show();
                                uploadData(location);
                            }
                        });
                break;
        }
    }

    private void uploadData(Location location) {
        APIInterface apiInterface = APIClient.getApiClient().create(APIInterface.class);
        Call<TaskEntry> taskEntry = apiInterface.taskEntry(
                Constants.INSTANCE.getKEY(),
                Constants.INSTANCE.getUSER_ID(),
                String.valueOf(location.getLatitude()),
                String.valueOf(location.getLongitude()),
                "1",
                ticketTitle.getText().toString(),
                "1",
                encodeToBase64(bitmap),
                ticketDetails.getText().toString(),
                startDate.getText().toString(),
                endDate.getText().toString(),
                "41",
                "524",
                address.getText().toString(),
                consumerName.getText().toString(),
                consumerMobileNumber.getText().toString()
        );

        taskEntry.enqueue(new Callback<TaskEntry>() {
            @Override
            public void onResponse(Call<TaskEntry> call, Response<TaskEntry> response) {
                TaskEntry taskResponse = response.body();

                if(waitingDialog.isShowing()){
                    waitingDialog.dismiss();
                    new KAlertDialog(CreateTaskActivity.this, KAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Good job!")
                            .setContentText("Task Created Successfully")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog kAlertDialog) {
                                    kAlertDialog.dismissWithAnimation();
                                    CreateTaskActivity.super.onBackPressed();
                                }
                            })
                            .show();
                }

            }

            @Override
            public void onFailure(Call<TaskEntry> call, Throwable t) {
                Toast.makeText(CreateTaskActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                if(waitingDialog.isShowing()){
                    waitingDialog.dismiss();
                }
            }
        });
    }

    private void selectImage() {
        ImagePicker.create(this)
                .start();

    }

    public String encodeToBase64(Bitmap image) {
        Bitmap bitmap = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] b = baos.toByteArray();
        //String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return Utils.INSTANCE.encodeToBase64(b);
    }

    public Bitmap getResizedBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bitmap, 0, 0, width, height, matrix, false);
        bitmap.recycle();
        return resizedBitmap;
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
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            List<Image> images = ImagePicker.getImages(data);
            Image image = ImagePicker.getFirstImageOrNull(data);
            bitmap = getResizedBitmap(BitmapFactory.decodeFile(image.getPath()), 512, 512);
            imgAttach.setImageBitmap(bitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SmartLocation.with(CreateTaskActivity.this).location().stop();
    }
}
