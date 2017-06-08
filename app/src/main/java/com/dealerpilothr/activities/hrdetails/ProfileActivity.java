package com.dealerpilothr.activities.hrdetails;

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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.activities.MenuFragment;
import com.dealerpilothr.activities.RightFragment;
import com.dealerpilothr.adapters.AdressAdapter;
import com.dealerpilothr.api.AddAvatarAction;
import com.dealerpilothr.api.GetAdressesAction;
import com.dealerpilothr.api.GetContactsAction;
import com.dealerpilothr.api.GetCountriesAction;
import com.dealerpilothr.api.GetHealthAndSafetyMemberAction;
import com.dealerpilothr.api.GetProfilesAction;
import com.dealerpilothr.api.GetProfilesAdditionalInformationAction;
import com.dealerpilothr.api.GetProfilesContactInformationAction;
import com.dealerpilothr.api.GetProfilesCustomFieldsAction;
import com.dealerpilothr.api.PunchesAddAction;
import com.dealerpilothr.api.UpdateAction;
import com.dealerpilothr.logic.Environment;
import com.dealerpilothr.logic.NetRequests;
import com.dealerpilothr.models.AdressData;
import com.dealerpilothr.models.ContactData;
import com.dealerpilothr.models.CountryData;
import com.dealerpilothr.models.ProfileData;
import com.dealerpilothr.utils.GeneralOperations;
import com.dealerpilothr.R;
import com.dealerpilothr.activities.BaseActivity;
import com.dealerpilothr.api.GetProfilesEmployeeInformationAction;
import com.dealerpilothr.logic.UserInfo;

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
                                                             GetAdressesAction.RequestAdresses,
                                                             GetHealthAndSafetyMemberAction.RequestHealthAndSafetyMember,
                                                             GetProfilesEmployeeInformationAction.RequestProfilesEmployeeInformation,
                                                             GetProfilesContactInformationAction.RequestProfilesContactInformation,
                                                             GetProfilesAdditionalInformationAction.RequestProfilesAdditionalInformation,
                                                             GetProfilesCustomFieldsAction.RequestProfilesCustomFields,
                                                             GetCountriesAction.RequestCountries,
                                                             UpdateAction.RequestUpdate,
                                                             PunchesAddAction.RequestPunchesAdd {

    private ProgressDialog pd;
    private TextView primary;
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
    private CheckBox member;
    private CheckBox certified;
    private CheckBox aidcertified;
    private CheckBox safetycertified;
    private boolean viewPersonal = false;
    private boolean editPersonal = false;
    private boolean viewWork = false;
    private boolean editWork = false;
    private String primaryOld = "";
    private String secondaryOld = "";
    private String homeOld = "";
    private String cellOld = "";
    private boolean primaryEmailAllowNotificationOld = false;
    private boolean secondaryEmailAllowNotificationOld = false;
    private Long birthdayOld = 0l;
    private String sin_numberOld = "";
    private String driver_licenseOld = "";
    private Long driver_expiryOld = 0l;
    private String custom1Old = "";
    private String custom2Old = "";
    private String custom3Old = "";
    private String custom4Old = "";
    private String custom5Old = "";
    private String custom6Old = "";
    private String custom7Old = "";
    private String custom8Old = "";
    private String custom9Old = "";
    private String adressId = "";
    private String adressOld = "";
    private String countryOld = "";
    private String provinceOld = "";
    private String cityOld = "";
    private String postalOld = "";
    private String vsrNumberOld = "";
    private Long vsrExpiryOld = 0l;
    private String techLicenseOld = "";
    private Long techExpiryOld = 0l;
    private String uniformDescriptionOld = "";
    private boolean uniformAllowanceOld = false;
    private Double uniformAmountOld = 0d;
    private Long uniformRenewalOld = 0l;
    private String workNumberOld = "";
    private Long workExpiryOld = 0l;
    private Long hireDateOld = 0l;
    private Long probationEndOld = 0l;
    private Long terminationOld = 0l;

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

        primary = (TextView) findViewById(R.id.primary);
        contact = (TextView) findViewById(R.id.contact);
        additional = (TextView) findViewById(R.id.additional);
        employee = (TextView) findViewById(R.id.employee);
        payroll = (TextView) findViewById(R.id.payroll);
        work_additional = (TextView) findViewById(R.id.work_additional);
        custom = (TextView) findViewById(R.id.custom);
        personal_group = findViewById(R.id.personal_group);
        member = (CheckBox) findViewById(R.id.member);
        certified = (CheckBox) findViewById(R.id.certified);
        aidcertified = (CheckBox) findViewById(R.id.aidcertified);
        safetycertified = (CheckBox) findViewById(R.id.safetycertified);

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

        File f = new File(Dealerpilothr.getInstance().getFilesDir().getAbsolutePath() + "/user.png");
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

        View contactedit = findViewById(R.id.contactedit);
        contactedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditContactActivity.class);
                intent.putExtra("primary", primaryOld);
                intent.putExtra("secondary", secondaryOld);
                intent.putExtra("home", homeOld);
                intent.putExtra("cell", cellOld);
                intent.putExtra("primaryEmailAllowNotification", primaryEmailAllowNotificationOld);
                intent.putExtra("secondaryEmailAllowNotification", secondaryEmailAllowNotificationOld);
                startActivity(intent);
            }
        });

        View additionaledit = findViewById(R.id.additionaledit);
        additionaledit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditAdditionalActivity.class);
                intent.putExtra("birthday", birthdayOld);
                intent.putExtra("sin_number", sin_numberOld);
                intent.putExtra("driver_license", driver_licenseOld);
                intent.putExtra("driver_expiry", driver_expiryOld);
                startActivity(intent);
            }
        });

        View primary_residenceedit = findViewById(R.id.primary_residenceedit);
        primary_residenceedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditAdressActivity.class);
                intent.putExtra("adressId", adressId);
                intent.putExtra("adress", adressOld);
                intent.putExtra("country", countryOld);
                intent.putExtra("province", provinceOld);
                intent.putExtra("city", cityOld);
                intent.putExtra("postal", postalOld);
                startActivity(intent);
            }
        });

        View employeeedit = findViewById(R.id.employeeedit);
        employeeedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditEmployeeActivity.class);
                intent.putExtra("hireDate", hireDateOld);
                intent.putExtra("probationEnd", probationEndOld);
                intent.putExtra("termination", terminationOld);
                //startActivity(intent);
            }
        });

        View payrolledit = findViewById(R.id.payrolledit);
        payrolledit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        View aditional_workedit = findViewById(R.id.aditional_workedit);
        aditional_workedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditWorkAdditionalActivity.class);
                intent.putExtra("vsr_number", vsrNumberOld);
                intent.putExtra("vsr_expiry", vsrExpiryOld);
                intent.putExtra("tech_license", techLicenseOld);
                intent.putExtra("tech_expiry", techExpiryOld);
                intent.putExtra("uniform_description", uniformDescriptionOld);
                intent.putExtra("uniform_allowance", uniformAllowanceOld);
                intent.putExtra("uniform_amount", uniformAmountOld);
                intent.putExtra("uniform_renewal", uniformRenewalOld);
                intent.putExtra("work_number", workNumberOld);
                intent.putExtra("work_expiry", workExpiryOld);
                startActivity(intent);
            }
        });

        View customedit = findViewById(R.id.customedit);
        customedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditCustomActivity.class);
                intent.putExtra("custom1", custom1Old);
                intent.putExtra("custom2", custom2Old);
                intent.putExtra("custom3", custom3Old);
                intent.putExtra("custom4", custom4Old);
                intent.putExtra("custom5", custom5Old);
                intent.putExtra("custom6", custom6Old);
                intent.putExtra("custom7", custom7Old);
                intent.putExtra("custom8", custom8Old);
                intent.putExtra("custom9", custom9Old);
                startActivity(intent);
            }
        });

        View memberedit = findViewById(R.id.memberedit);
        memberedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditHealthSafetyActivity.class);
                intent.putExtra("member", member.isChecked());
                intent.putExtra("cpr", certified.isChecked());
                intent.putExtra("aid", aidcertified.isChecked());
                intent.putExtra("safety", safetycertified.isChecked());
                startActivity(intent);
            }
        });

        UpdateBottom();

        UserInfo userInfo = UserInfo.getUserInfo();
        //String securityModePersonal = userInfo.getGlobalAppRoleAppSecurities().getSecurityMode("/me/my-emergency contacts", "", "");
        String securityModePersonal = userInfo.getGlobalAppRoleAppSecurities().getSecurityMode("", "Personal Information", "c9c8db39-2b01-5bec-0975-537930843497");
        if (securityModePersonal.equals("")) {
            securityModePersonal = userInfo.getGlobalAppRoleAppSecurities().getSecurityMode("/me/my-emergency contacts", "");
        }
        if (securityModePersonal.equals("fullAccess")) {
            editPersonal = true;
        } else if (securityModePersonal.equals("viewOnly")) {
            viewPersonal = true;
        }

        //String securityModeWork = userInfo.getGlobalAppRoleAppSecurities().getSecurityMode("/me/my-family-members", "");
        //if (securityModeWork.equals("")) {
        //    securityModeWork = userInfo.getGlobalAppRoleAppSecurities().getSecurityMode("/me/my-hr-details", "");
        //}
        String securityModeWork = userInfo.getGlobalAppRoleAppSecurities().getSecurityMode("", "Work Information", "c9c8db39-2b01-5bec-0975-537930843497");
        if (securityModeWork.equals("")) {
            securityModeWork = userInfo.getGlobalAppRoleAppSecurities().getSecurityMode("/me/my-family-members", "");
            if (securityModeWork.equals("")) {
                securityModeWork = userInfo.getGlobalAppRoleAppSecurities().getSecurityMode("/me/my-hr-details", "");
            }
        }
        if (securityModeWork.equals("fullAccess")) {
            editWork = true;
        } else if (securityModeWork.equals("viewOnly")) {
            viewWork = true;
        }

        if (!editPersonal && !viewPersonal) {
            View text_personal = findViewById(R.id.text_personal);
            text_personal.setVisibility(View.GONE);

            View border = findViewById(R.id.border);
            border.setVisibility(View.GONE);

            View photo_group_group = findViewById(R.id.photo_group);
            photo_group_group.setVisibility(View.GONE);

            personal_group.setVisibility(View.GONE);

            View contact_group = findViewById(R.id.contact_group);
            contact_group.setVisibility(View.GONE);

            View additional_group = findViewById(R.id.additional_group);
            additional_group.setVisibility(View.GONE);

            View primary_group = findViewById(R.id.primary_group);
            primary_group.setVisibility(View.GONE);
        }

        if (!editWork && !viewWork) {
            View text_work = findViewById(R.id.text_work);
            text_work.setVisibility(View.GONE);

            View border2 = findViewById(R.id.border2);
            border2.setVisibility(View.GONE);

            View employee_group = findViewById(R.id.employee_group);
            employee_group.setVisibility(View.GONE);

            View payroll_group = findViewById(R.id.payroll_group);
            payroll_group.setVisibility(View.GONE);

            View work_additional_group = findViewById(R.id.work_additional_group);
            work_additional_group.setVisibility(View.GONE);

            View custom_group = findViewById(R.id.custom_group);
            custom_group.setVisibility(View.GONE);

            View member_group = findViewById(R.id.member_group);
            member_group.setVisibility(View.GONE);
        }

        if (viewPersonal && !editPersonal) {
            contactedit.setVisibility(View.GONE);
            additionaledit.setVisibility(View.GONE);
            primary_residenceedit.setVisibility(View.GONE);
        }

        if (viewWork && !editWork) {
            employeeedit.setVisibility(View.GONE);
            payrolledit.setVisibility(View.GONE);
            aditional_workedit.setVisibility(View.GONE);
            customedit.setVisibility(View.GONE);
            memberedit.setVisibility(View.GONE);
        }
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
        if (Dealerpilothr.getInstance().getNeedUpdate()) {
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
                    Dealerpilothr.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        } else {
            this.bitmapold = this.bitmap;
            if (bitmap == null) {
                ico.setImageResource(R.drawable.user);

                File f = new File(Dealerpilothr.getInstance().getFilesDir().getAbsolutePath() + "/user.png");
                if (f.exists()) {
                    try {
                        f.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                ico.setImageBitmap(bitmap);

                File f = new File(Dealerpilothr.getInstance().getFilesDir().getAbsolutePath() + "/user.png");
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
                Dealerpilothr.getInstance().sendBroadcast(intent);
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
                    Dealerpilothr.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        }
    }

    @Override
    public void onRequestProfilesData(ProfileData profileData) {
        String additionalStr = "";
        birthdayOld = profileData.getBirthday();
        sin_numberOld = profileData.getSINNumber();
        driver_licenseOld = profileData.getDriverLicenseNumber();
        driver_expiryOld = profileData.getDriverLicenseNumberExpire();
        additionalStr = additionalStr + "<b>" + getResources().getString(R.string.text_birthday) + "</b>: " + profileData.getBirthdayString();
        additionalStr = additionalStr + "<br><b>" + getResources().getString(R.string.text_sinnumber) + "</b>: " + sin_numberOld;
        additionalStr = additionalStr + "<br><b>" + getResources().getString(R.string.text_driver_license) + "</b>: " + driver_licenseOld;
        additionalStr = additionalStr + "<br><b>" + getResources().getString(R.string.text_expire_date) + "</b>: " + profileData.getDriverLicenseNumberExpireString();
        additional.setText(Html.fromHtml(additionalStr));

        String payrollStr = "";
        payrollStr = payrollStr + "<b>" + getResources().getString(R.string.text_number) + "</b>: " + profileData.getPayrollInformation().getEmployeeNumber();
        payrollStr = payrollStr + "<br><b>" + getResources().getString(R.string.text_punch_number) + "</b>: " + profileData.getPayrollInformation().getPunchClockNumber();
        payrollStr = payrollStr + "<br><b>" + getResources().getString(R.string.text_hours_per_day) + "</b>: " + profileData.getPayrollInformation().getHoursPerDayString();
        payrollStr = payrollStr + "<br><b>" + getResources().getString(R.string.text_pay_type) + "</b>: " + profileData.getPayrollInformation().getPayType();
        payrollStr = payrollStr + "<br><b>" + getResources().getString(R.string.text_pay_amount) + "</b>: " + profileData.getPayrollInformation().getSalaryString();
        payrollStr = payrollStr + "<br><b>" + getResources().getString(R.string.text_payroll_group) + "</b>: " + profileData.getPayrollInformation().getPayrollGroup();
        payroll.setText(Html.fromHtml(payrollStr));

        //String customStr = "";
        //customStr = customStr + "<b>" + getResources().getString(R.string.text_custom_field) + " 1</b>: " + profileData.getCustomField1();
        //customStr = customStr + "<br><b>" + getResources().getString(R.string.text_custom_field) + " 2</b>: " + profileData.getCustomField2();
        //custom.setText(Html.fromHtml(customStr));

        new GetContactsAction(ProfileActivity.this, false, true, true).execute();
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
                    Dealerpilothr.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        }
    }

    @Override
    public void onRequestContactsData(ArrayList<ContactData> adressDatas) {
        /*
        if (adressDatas == null || adressDatas.size() == 0) {
            personal_group.setVisibility(View.GONE);
        } else {
            adapter.setData(adressDatas);
            personal_group.setVisibility(View.VISIBLE);
        }
        */

        //pd.hide();

        new GetAdressesAction(ProfileActivity.this).execute();
    }

    @Override
    public void onRequestAdresses(String result, AdressData adressData) {
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
                    Dealerpilothr.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        } else {
            adressId = adressData.getAddressId();
            adressOld = adressData.getAddressLine1();
            countryOld = adressData.getCountryId();
            provinceOld = adressData.getRegionId();
            cityOld = adressData.getCity();
            postalOld = adressData.getPostalCode();
            String primaryInfo = "";
            //if (!profileData.getContact().getAddress().getAddressLine1().equals("")) {
            if (!primaryInfo.equals("")) {
                primaryInfo = primaryInfo + "<br>";
            }
            primaryInfo = primaryInfo + "<b>" + Dealerpilothr.getInstance().getResources().getString(R.string.text_address) + "</b>: " + adressOld;
            //}
            //if (!profileData.getContact().getAddress().getCountry().equals("")) {
            if (!primaryInfo.equals("")) {
                primaryInfo = primaryInfo + "<br>";
            }
            primaryInfo = primaryInfo + "<b>" + Dealerpilothr.getInstance().getResources().getString(R.string.text_country) + "</b>: " + adressData.getCountry();
            //}
            //if (!profileData.getContact().getAddress().getProvince().equals("")) {
            if (!primaryInfo.equals("")) {
                primaryInfo = primaryInfo + "<br>";
            }
            primaryInfo = primaryInfo + "<b>" + Dealerpilothr.getInstance().getResources().getString(R.string.text_province) + "</b>: " + adressData.getRegion();
            //}
            //if (!profileData.getContact().getAddress().getCity().equals("")) {
            if (!primaryInfo.equals("")) {
                primaryInfo = primaryInfo + "<br>";
            }
            primaryInfo = primaryInfo + "<b>" + Dealerpilothr.getInstance().getResources().getString(R.string.text_city) + "</b>: " + cityOld;
            //}
            //if (!profileData.getContact().getAddress().getPostalCode().equals("")) {
            if (!primaryInfo.equals("")) {
                primaryInfo = primaryInfo + "<br>";
            }
            primaryInfo = primaryInfo + "<b>" + Dealerpilothr.getInstance().getResources().getString(R.string.text_postal_code) + "</b>: " + postalOld;
            //}
            primary.setText(Html.fromHtml(primaryInfo));

            new GetHealthAndSafetyMemberAction(ProfileActivity.this).execute();
        }
    }

    @Override
    public void onRequestHealthAndSafetyMember(String result, ProfileData profileData) {
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
                    Dealerpilothr.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        } else {
            member.setChecked(profileData.getIsHumanAndSafetyMember());
            certified.setChecked(profileData.getIsCPRCertified());
            aidcertified.setChecked(profileData.getIsFirstAidCertified());
            safetycertified.setChecked(profileData.getIsSafetyCertified());

            new GetProfilesEmployeeInformationAction(ProfileActivity.this).execute();
        }
    }

    @Override
    public void onRequestProfilesEmployeeInformation(String result, ProfileData profileData) {
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
                    Dealerpilothr.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        } else {
            hireDateOld = profileData.getHireDate();
            probationEndOld = profileData.getProbationEndDate();
            terminationOld = profileData.getTerminationDate();
            String employeeStr = "";
            employeeStr = employeeStr + "<b>" + getResources().getString(R.string.text_hire_date) + "</b>: " + profileData.getHireDateString();
            employeeStr = employeeStr + "<br><b>" + getResources().getString(R.string.text_probation) + "</b>: " + profileData.getProbationEndDateString();
            employeeStr = employeeStr + "<br><b>" + getResources().getString(R.string.text_hire_type) + "</b>: " + profileData.getHireType().getName();
            employeeStr = employeeStr + "<br><b>" + getResources().getString(R.string.text_termination) + "</b>: " + profileData.getTerminationDateString();
            employeeStr = employeeStr + "<br><b>" + getResources().getString(R.string.text_manager) + "</b>: " + profileData.getManager();
            employeeStr = employeeStr + "<br><b>" + getResources().getString(R.string.text_secondary_manager) + "</b>: " + profileData.getSecondaryManager();
            employee.setText(Html.fromHtml(employeeStr));

            new GetProfilesContactInformationAction(ProfileActivity.this).execute();
        }
    }

    @Override
    public void onRequestProfilesContactInformation(String result, ProfileData profileData) {
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
                    Dealerpilothr.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        } else {
            primaryOld = profileData.getEmail();
            secondaryOld = profileData.getSecondaryEmail();
            homeOld = profileData.getHomePhoneNumber();
            cellOld = profileData.getCellPhoneNumber();
            primaryEmailAllowNotificationOld = profileData.getPrimaryEmailAllowNotification();
            secondaryEmailAllowNotificationOld = profileData.getSecondaryEmailAllowNotification();
            String detail = "<b>" + getResources().getString(R.string.text_primary_email) + "</b>: " + primaryOld;
            detail = detail + "<br><b>" + getResources().getString(R.string.text_secondary_email) + "</b>: " + secondaryOld;
            detail = detail + "<br><b>" + getResources().getString(R.string.text_home_phone) + "</b>: " + GeneralOperations.formatingPhone(homeOld);
            detail = detail + "<br><b>" + getResources().getString(R.string.text_cell_phone) + "</b>: " + GeneralOperations.formatingPhone(cellOld);
            contact.setText(Html.fromHtml(detail));

            new GetProfilesAdditionalInformationAction(ProfileActivity.this).execute();
        }
    }

    @Override
    public void onRequestProfilesAdditionalInformation(String result, ProfileData profileData) {
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
                    Dealerpilothr.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        } else {
            vsrNumberOld = profileData.getVSRNumber();
            vsrExpiryOld = profileData.getVSRNumberExpire();
            techLicenseOld = profileData.getTechLicenseNumber();
            techExpiryOld = profileData.getTechLicenseNumberExpire();
            uniformDescriptionOld = profileData.getUniformDescription();
            uniformAllowanceOld = profileData.getUniformAllowance();
            uniformAmountOld = profileData.getUniformAllowanceAmount();
            uniformRenewalOld = profileData.getUniformRenewalDate();
            workNumberOld = profileData.getWorkPermitNumber();
            workExpiryOld = profileData.getWorkPermitNumberExpire();
            String additionalworkStr = "";
            additionalworkStr = additionalworkStr + "<b>" + getResources().getString(R.string.text_vsr_number) + "</b>: " + vsrNumberOld;
            additionalworkStr = additionalworkStr + "<br><b>" + getResources().getString(R.string.text_vsr_expire) + "</b>: " + profileData.getVSRNumberExpireString();
            additionalworkStr = additionalworkStr + "<br><b>" + getResources().getString(R.string.text_tech_license) + "</b>: " + techLicenseOld;
            additionalworkStr = additionalworkStr + "<br><b>" + getResources().getString(R.string.text_tech_license_expire) + "</b>: " + profileData.getTechLicenseNumberExpireString();
            additionalworkStr = additionalworkStr + "<br><b>" + getResources().getString(R.string.text_uniform_description) + "</b>: " + uniformDescriptionOld;
            additionalworkStr = additionalworkStr + "<br><b>" + getResources().getString(R.string.text_uniform_allowance) + "</b>: " + profileData.getUniformAllowanceString();
            additionalworkStr = additionalworkStr + "<br><b>" + getResources().getString(R.string.text_uniform_allowance_amount) + "</b>: " + uniformAmountOld;
            additionalworkStr = additionalworkStr + "<br><b>" + getResources().getString(R.string.text_uniform_renewal_date) + "</b>: " + profileData.getUniformRenewalDateString();
            additionalworkStr = additionalworkStr + "<br><b>" + getResources().getString(R.string.text_work_permit) + "</b>: " + workNumberOld;
            additionalworkStr = additionalworkStr + "<br><b>" + getResources().getString(R.string.text_work_permit_expiry_date) + "</b>: " + profileData.getWorkPermitNumberExpireString();
            work_additional.setText(Html.fromHtml(additionalworkStr));

            new GetProfilesCustomFieldsAction(ProfileActivity.this).execute();
        }
    }

    @Override
    public void onRequestProfilesCustomFields(String result, ProfileData profileData) {
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
                    Dealerpilothr.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        } else {
            custom1Old = profileData.getCustomField1();
            custom2Old = profileData.getCustomField2();
            custom3Old = profileData.getCustomField3();
            custom4Old = profileData.getCustomField4();
            custom5Old = profileData.getCustomField5();
            custom6Old = profileData.getCustomField6();
            custom7Old = profileData.getCustomField7();
            custom8Old = profileData.getCustomField8();
            custom9Old = profileData.getCustomField9();
            String customStr = "";
            customStr = customStr + "<b>" + getResources().getString(R.string.text_custom_field) + " 1</b>: " + custom1Old;
            customStr = customStr + "<br><b>" + getResources().getString(R.string.text_custom_field) + " 2</b>: " + custom2Old;
            customStr = customStr + "<br><b>" + getResources().getString(R.string.text_custom_field) + " 3</b>: " + custom3Old;
            customStr = customStr + "<br><b>" + getResources().getString(R.string.text_custom_field) + " 4</b>: " + custom4Old;
            customStr = customStr + "<br><b>" + getResources().getString(R.string.text_custom_field) + " 5</b>: " + custom5Old;
            customStr = customStr + "<br><b>" + getResources().getString(R.string.text_custom_field) + " 6</b>: " + custom6Old;
            customStr = customStr + "<br><b>" + getResources().getString(R.string.text_custom_field) + " 7</b>: " + custom7Old;
            customStr = customStr + "<br><b>" + getResources().getString(R.string.text_custom_field) + " 8</b>: " + custom8Old;
            customStr = customStr + "<br><b>" + getResources().getString(R.string.text_custom_field) + " 9</b>: " + custom9Old;
            custom.setText(Html.fromHtml(customStr));

            new GetCountriesAction(ProfileActivity.this).execute();
        }
    }

    @Override
    public void onRequestCountries(String result, ArrayList<CountryData> countryDatas) {
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
                    Dealerpilothr.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        } else {
            UserInfo.getUserInfo().setCountryDatas(countryDatas);
        }
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
                    Dealerpilothr.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        } else {
            Dealerpilothr.getInstance().setNeedUpdate(NetRequests.getNetRequests().isOnline(false));
            UpdateBottom();
            pd.hide();
        }
    }

    @Override
    public void onRequestPunchesAdd(String result) {
        pd.hide();
        if (!result.equals("OK")) {
            Toast.makeText(Dealerpilothr.getInstance(), result,
                    Toast.LENGTH_LONG).show();
            if (result.equals(getResources().getString(R.string.text_unauthorized))) {
                //Intent intent = new Intent(this, LoginActivity.class);
                //startActivity(intent);
                Intent intExit = new Intent(Environment.BROADCAST_ACTION);
                try {
                    intExit.putExtra(Environment.PARAM_TASK, "unauthorized");
                    Dealerpilothr.getInstance().sendBroadcast(intExit);
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