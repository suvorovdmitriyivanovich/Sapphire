package com.sapphire.activities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.adapters.AdressAdapter;
import com.sapphire.api.AddAvatarAction;
import com.sapphire.api.GetContactsAction;
import com.sapphire.api.GetProfilesAction;
import com.sapphire.api.PunchesAddAction;
import com.sapphire.api.UpdateAction;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.models.ContactData;
import com.sapphire.models.ProfileData;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ProfileActivity extends BaseActivity implements AdressAdapter.OnRootClickListener,
                                                             AdressAdapter.OnOpenClickListener,
                                                             GetProfilesAction.RequestProfiles,
                                                             GetProfilesAction.RequestProfilesData,
                                                             GetContactsAction.RequestContacts,
                                                             GetContactsAction.RequestContactsData,
                                                             AddAvatarAction.RequestAddAvatar,
                                                             UpdateAction.RequestUpdate,
                                                             PunchesAddAction.RequestPunchesAdd{

    private ProgressDialog pd;
    private TextView contact;
    private TextView additional;
    private TextView employee;
    private TextView payroll;
    private TextView work_additional;
    private TextView custom;
    private RecyclerView adresslist;
    private AdressAdapter adapter;
    private View personal_group;
    private BroadcastReceiver br;
    private View nointernet_group;
    private ViewGroup.LayoutParams par_nointernet_group;
    private Dialog dialog;
    private View photo_group;
    private View mobile_group;
    private View delete_group;
    private View cancel_group;
    private ImageView ico;
    private final int CAMERA_CAPTURE = 1;
    private final int PIC_CROP = 2;
    private final int GALLERY_REQUEST = 3;
    private Uri picUri;
    private Bitmap bitmap;
    private Bitmap bitmapold;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        View menu = findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        View exit = findViewById(R.id.delete);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        pd = new ProgressDialog(this);
        pd.setTitle(getResources().getString(R.string.text_loading));
        pd.setMessage(getResources().getString(R.string.text_please_wait));
        //диалог который нельзя закрыть пользователем
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        contact = (TextView) findViewById(R.id.contact);
        additional = (TextView) findViewById(R.id.additional);
        employee = (TextView) findViewById(R.id.employee);
        payroll = (TextView) findViewById(R.id.payroll);
        work_additional = (TextView) findViewById(R.id.work_additional);
        custom = (TextView) findViewById(R.id.custom);
        personal_group = findViewById(R.id.personal_group);

        adresslist = (RecyclerView) findViewById(R.id.adresslist);
        adapter = new AdressAdapter(this, false);
        adresslist.setAdapter(adapter);
        adresslist.setLayoutManager(new LinearLayoutManager(this));

        // создаем BroadcastReceiver
        br = new BroadcastReceiver() {
            // действия при получении сообщений
            public void onReceive(Context context, Intent intent) {
                final String putreqwest = intent.getStringExtra(Environment.PARAM_TASK);

                if (putreqwest.equals("updateleftmenu")) {
                    try {
                        Fragment fragment = new MenuFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.nav_left, fragment).commit();
                    } catch (Exception e) {
                    }
                } else if (putreqwest.equals("updaterightmenu")) {
                    try {
                        Fragment fragmentRight = new RightFragment(pd);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.nav_right, fragmentRight).commit();
                    } catch (Exception e) {}
                } else if (putreqwest.equals("updatebottom")) {
                    UpdateBottom();
                } else if (putreqwest.equals("update")) {
                    pd.show();
                    new GetProfilesAction(ProfileActivity.this).execute();
                }
            }
        };
        // создаем фильтр для BroadcastReceiver
        IntentFilter intFilt = new IntentFilter(Environment.BROADCAST_ACTION);
        // регистрируем (включаем) BroadcastReceiver
        registerReceiver(br, intFilt);

        nointernet_group = findViewById(R.id.nointernet_group);
        par_nointernet_group = nointernet_group.getLayoutParams();
        nointernet_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new UpdateAction(ProfileActivity.this);
            }
        });

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        //adb.setTitle(getResources().getString(R.string.text_change_photo));
        adb.setCancelable(true);
        LinearLayout view = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.dialog_photo_profile, null);
        adb.setView(view);
        photo_group = view.findViewById(R.id.photo_group);
        mobile_group = view.findViewById(R.id.mobile_group);
        delete_group = view.findViewById(R.id.delete_group);
        cancel_group = view.findViewById(R.id.cancel_group);
        dialog = adb.create();

        photo_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                        checkSelfPermission(Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                } else {
                    try {
                        // Намерение для запуска камеры
                        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(captureIntent, CAMERA_CAPTURE);
                    } catch (Exception e) {
                        Toast toast = Toast
                                .makeText(getApplicationContext(), getResources().getString(R.string.text_error_camera), Toast.LENGTH_LONG);
                        toast.show();
                    }
                }

                dialog.dismiss();
            }
        });

        mobile_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);

                dialog.dismiss();
            }
        });

        delete_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ico.setImageResource(R.drawable.user);

                deletePhoto();

                dialog.dismiss();
            }
        });

        cancel_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        ico = (ImageView) findViewById(R.id.ico);
        ico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        View button_change = findViewById(R.id.button_change);
        button_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        File f = new File(Sapphire.getInstance().getFilesDir().getAbsolutePath() + "/user.png");
        if (f.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), options);
            this.bitmap = bitmap;
            ico.setImageBitmap(this.bitmap);
        } else {
            this.bitmap = null;
            ico.setImageResource(R.drawable.user);
        }
        this.bitmapold = this.bitmap;

        UpdateBottom();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    // Намерение для запуска камеры
                    Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(captureIntent, CAMERA_CAPTURE);
                } catch (Exception e) {
                    Toast toast = Toast
                            .makeText(getApplicationContext(), getResources().getString(R.string.text_error_camera), Toast.LENGTH_LONG);
                    toast.show();
                }
            }
            return;
        }
    }

    //Ответ от камеры
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // Вернулись от приложения Камера
            if (requestCode == CAMERA_CAPTURE) {
                // Получим Uri снимка
                picUri = data.getData();

                if (picUri == null) {
                    Bitmap thumbnailBitmap = (Bitmap) data.getExtras().get("data");
                    //ico.setImageBitmap(thumbnailBitmap);

                    savePhoto(thumbnailBitmap);
                } else {
                    // кадрируем его
                    try {
                        performCrop();
                    } catch (ActivityNotFoundException anfe) {
                        Bitmap thumbnailBitmap = (Bitmap) data.getExtras().get("data");
                        //ico.setImageBitmap(thumbnailBitmap);

                        savePhoto(thumbnailBitmap);
                    }
                }
                // Вернулись из операции кадрирования
            } else if(requestCode == PIC_CROP){
                Bundle extras = data.getExtras();
                Bitmap thePic;
                try {
                    // Получим кадрированное изображение
                    thePic = extras.getParcelable("data");
                } catch(Exception anfe){
                    thePic = (Bitmap) extras.get("data");
                }
                // передаём его в ImageView
                //ico.setImageBitmap(thePic);

                savePhoto(thePic);
            } else if (requestCode == GALLERY_REQUEST) {
                Bitmap bitmap = null;
                // Получим Uri снимка
                picUri = data.getData();
                // кадрируем его
                try {
                    performCrop();
                } catch(ActivityNotFoundException anfe){
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //ico.setImageBitmap(bitmap);

                    savePhoto(bitmap);
                }
            }
        }
    }

    private void performCrop(){
        // Намерение для кадрирования. Не все устройства поддерживают его
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(picUri, "image/*");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        cropIntent.putExtra("return-data", true);
        startActivityForResult(cropIntent, PIC_CROP);
    }

    private void savePhoto(Bitmap bitmap) {
        this.bitmap = bitmap;

        new AddAvatarAction(ProfileActivity.this, this.bitmap).execute();
    }

    private void deletePhoto() {
        this.bitmap = null;
        if (bitmapold == null) {
            return;
        }

        new AddAvatarAction(ProfileActivity.this, this.bitmap).execute();
    }

    private void UpdateBottom() {
        if (Sapphire.getInstance().getNeedUpdate()) {
            par_nointernet_group.height = GetPixelFromDips(56);
        } else {
            par_nointernet_group.height = 0;
        }
        nointernet_group.setLayoutParams(par_nointernet_group);
        nointernet_group.requestLayout();
    }

    @Override
    public void onRequestAddAvatar(String result) {
        if (!result.equals("OK")) {
            pd.hide();
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
            if (result.equals(getResources().getString(R.string.text_unauthorized))) {
                //Intent intent = new Intent(this, LoginActivity.class);
                //startActivity(intent);
                Intent intExit = new Intent(Environment.BROADCAST_ACTION);
                try {
                    intExit.putExtra(Environment.PARAM_TASK, "unauthorized");
                    Sapphire.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        } else {
            this.bitmapold = this.bitmap;
            if (bitmap == null) {
                ico.setImageResource(R.drawable.user);

                File f = new File(Sapphire.getInstance().getFilesDir().getAbsolutePath() + "/user.png");
                if (f.exists()) {
                    try {
                        f.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                ico.setImageBitmap(bitmap);

                File f = new File(Sapphire.getInstance().getFilesDir().getAbsolutePath() + "/user.png");
                try {
                    FileOutputStream outStream = new FileOutputStream(f);
                    int crat = 1;
                    //if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR1) {
                    //    crat = bitmap.getByteCount()/131072;
                    //    if (crat < 1) {
                    //        crat = 1;
                    //    }
                    //}
                    //bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90 / crat, outStream);
                    outStream.flush();
                    outStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Intent intent = new Intent(Environment.BROADCAST_ACTION);
            try {
                intent.putExtra(Environment.PARAM_TASK, "updaterightmenu");
                Sapphire.getInstance().sendBroadcast(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestProfiles(String result) {
        pd.hide();
        if (!result.equals("OK")) {
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
            if (result.equals(getResources().getString(R.string.text_unauthorized))) {
                //Intent intent = new Intent(this, LoginActivity.class);
                //startActivity(intent);
                Intent intExit = new Intent(Environment.BROADCAST_ACTION);
                try {
                    intExit.putExtra(Environment.PARAM_TASK, "unauthorized");
                    Sapphire.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        }
    }

    @Override
    public void onRequestProfilesData(ProfileData profileData) {
        String detail = "";
        //if (!profileData.getContact().getPhone1().equals("")) {
            if (!detail.equals("")) {
                detail = detail + "<br>";
            }
            detail = detail + "<b>" + getResources().getString(R.string.text_home_phone) + "</b>: " + profileData.getContact().getPhone1();
        //}
        //if (!profileData.getContact().getPhone2().equals("")) {
            if (!detail.equals("")) {
                detail = detail + "<br>";
            }
            detail = detail + "<b>" + getResources().getString(R.string.text_cell_phone) + "</b>: " + profileData.getContact().getPhone2();
        //}
        //if (!profileData.getContact().getEmail1().equals("")) {
            if (!detail.equals("")) {
                detail = detail + "<br>";
            }
            detail = detail + "<b>" + getResources().getString(R.string.text_business_email) + "</b>: " + profileData.getContact().getEmail1();
        //}
        //if (!profileData.getContact().getEmail2().equals("")) {
            if (!detail.equals("")) {
                detail = detail + "<br>";
            }
            detail = detail + "<b>" + getResources().getString(R.string.text_personal_email) + "</b>: " + profileData.getContact().getEmail2();
        //}
        contact.setText(Html.fromHtml(detail));

        String additionalStr = "";
        additionalStr = additionalStr + "<b>" + getResources().getString(R.string.text_birthday) + "</b>: " + profileData.getBirthdayString();
        additionalStr = additionalStr + "<br><b>" + getResources().getString(R.string.text_sinnumber) + "</b>: " + profileData.getSINNumber();
        additionalStr = additionalStr + "<br><b>" + getResources().getString(R.string.text_driver_license) + "</b>: " + profileData.getDriverLicenseNumber();
        additionalStr = additionalStr + "<br><b>" + getResources().getString(R.string.text_expire_date) + "</b>: " + profileData.getDriverLicenseNumberExpireString();
        additional.setText(Html.fromHtml(additionalStr));

        String employeeStr = "";
        employeeStr = employeeStr + "<b>" + getResources().getString(R.string.text_hire_date) + "</b>: " + profileData.getHireDateString();
        employeeStr = employeeStr + "<br><b>" + getResources().getString(R.string.text_probation) + "</b>: " + profileData.getProbationEndDateString();
        employeeStr = employeeStr + "<br><b>" + getResources().getString(R.string.text_hire_type) + "</b>: " + profileData.getHireType().getName();
        employeeStr = employeeStr + "<br><b>" + getResources().getString(R.string.text_manager) + "</b>: " + "";
        employeeStr = employeeStr + "<br><b>" + getResources().getString(R.string.text_secondary_manager) + "</b>: " + "";
        employeeStr = employeeStr + "<br><b>" + getResources().getString(R.string.text_termination) + "</b>: " + profileData.getTerminationDateString();
        employee.setText(Html.fromHtml(employeeStr));

        String payrollStr = "";
        payrollStr = payrollStr + "<b>" + getResources().getString(R.string.text_number) + "</b>: " + profileData.getPayrollInformation().getEmployeeNumber();
        payrollStr = payrollStr + "<br><b>" + getResources().getString(R.string.text_punch_number) + "</b>: " + profileData.getPayrollInformation().getPunchClockNumber();
        payrollStr = payrollStr + "<br><b>" + getResources().getString(R.string.text_pay_frequency) + "</b>: " + profileData.getPayrollInformation().getPayFrequency();
        payrollStr = payrollStr + "<br><b>" + getResources().getString(R.string.text_hours_per_day) + "</b>: " + profileData.getPayrollInformation().getHoursPerDay();
        payrollStr = payrollStr + "<br><b>" + getResources().getString(R.string.text_salary) + "</b>: " + profileData.getPayrollInformation().getSalary();
        payrollStr = payrollStr + "<br><b>" + getResources().getString(R.string.text_hourly_rate) + "</b>: " + profileData.getPayrollInformation().getPayRate();
        payroll.setText(Html.fromHtml(payrollStr));

        String additionalworkStr = "";
        additionalworkStr = additionalworkStr + "<b>" + getResources().getString(R.string.text_work_permit) + "</b>: " + profileData.getWorkPermitNumber();
        additionalworkStr = additionalworkStr + "<br><b>" + getResources().getString(R.string.text_driver_license_expire) + "</b>: " + profileData.getDriverLicenseNumberExpireString();
        additionalworkStr = additionalworkStr + "<br><b>" + getResources().getString(R.string.text_tech_license) + "</b>: " + profileData.getTechLicenseNumber();
        additionalworkStr = additionalworkStr + "<br><b>" + getResources().getString(R.string.text_tech_license_expire) + "</b>: " + profileData.getTechLicenseNumberExpireString();
        additionalworkStr = additionalworkStr + "<br><b>" + getResources().getString(R.string.text_vsr_number) + "</b>: " + profileData.getVSRNumber();
        additionalworkStr = additionalworkStr + "<br><b>" + getResources().getString(R.string.text_vsr_expire) + "</b>: " + profileData.getVSRNumberExpireString();
        work_additional.setText(Html.fromHtml(additionalworkStr));

        String customStr = "";
        customStr = customStr + "<b>" + getResources().getString(R.string.text_custom1) + "</b>: " + profileData.getCustomField1();
        customStr = customStr + "<br><b>" + getResources().getString(R.string.text_custom2) + "</b>: " + profileData.getCustomField1();
        custom.setText(Html.fromHtml(customStr));

        new GetContactsAction(ProfileActivity.this, false).execute();
        //pd.hide();
    }

    @Override
    public void onRequestContacts(String result) {
        pd.hide();
        personal_group.setVisibility(View.GONE);
        if (!result.equals("OK")) {
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
            if (result.equals(getResources().getString(R.string.text_unauthorized))) {
                //Intent intent = new Intent(this, LoginActivity.class);
                //startActivity(intent);
                Intent intExit = new Intent(Environment.BROADCAST_ACTION);
                try {
                    intExit.putExtra(Environment.PARAM_TASK, "unauthorized");
                    Sapphire.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        }
    }

    @Override
    public void onRequestContactsData(ArrayList<ContactData> adressDatas) {
        if (adressDatas == null || adressDatas.size() == 0) {
            personal_group.setVisibility(View.GONE);
        } else {
            adapter.setData(adressDatas);
            personal_group.setVisibility(View.VISIBLE);
        }

        pd.hide();
    }

    @Override
    public void onRootClick(int position) {

    }

    @Override
    public void onOpenClick(int position) {

    }

    @Override
    public void onRequestUpdate(String result) {
        if (!result.equals("OK")) {
            pd.hide();
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
            if (result.equals(getResources().getString(R.string.text_unauthorized))) {
                //Intent intent = new Intent(this, LoginActivity.class);
                //startActivity(intent);
                Intent intExit = new Intent(Environment.BROADCAST_ACTION);
                try {
                    intExit.putExtra(Environment.PARAM_TASK, "unauthorized");
                    Sapphire.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        } else {
            Sapphire.getInstance().setNeedUpdate(NetRequests.getNetRequests().isOnline(false));
            UpdateBottom();
            pd.hide();
        }
    }

    @Override
    public void onRequestPunchesAdd(String result) {
        pd.hide();
        if (!result.equals("OK")) {
            Toast.makeText(Sapphire.getInstance(), result,
                    Toast.LENGTH_LONG).show();
            if (result.equals(getResources().getString(R.string.text_unauthorized))) {
                //Intent intent = new Intent(this, LoginActivity.class);
                //startActivity(intent);
                Intent intExit = new Intent(Environment.BROADCAST_ACTION);
                try {
                    intExit.putExtra(Environment.PARAM_TASK, "unauthorized");
                    Sapphire.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        } else {
            drawerLayout.closeDrawers();
        }
    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            Fragment fragment = new MenuFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.nav_left, fragment).commit();

            Fragment fragmentRight = new RightFragment(pd);
            fragmentManager.beginTransaction().replace(R.id.nav_right, fragmentRight).commit();
        } catch (Exception e) {}

        pd.show();
        new GetProfilesAction(ProfileActivity.this).execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }
}
