package com.sapphire.activities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.adapters.FilesAdapter;
import com.sapphire.api.FileAddAction;
import com.sapphire.api.FileDeleteAction;
import com.sapphire.api.FilesAction;
import com.sapphire.api.GetFileAction;
import com.sapphire.api.UploadFileAction;
import com.sapphire.api.UpdateAction;
import com.sapphire.db.DBHelper;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.models.FileData;
import com.sapphire.logic.UserInfo;
import com.sapphire.ui.OpenFileDialog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FilesActivity extends BaseActivity implements FilesAdapter.OnRootClickListener,
                                                           FilesAdapter.OnDownloadClickListener,
                                                           FilesAdapter.OnDeleteClickListener,
                                                           FilesAction.RequestFiles,
                                                           FileDeleteAction.RequestFileDelete,
                                                           GetFileAction.RequestFile,
                                                           UploadFileAction.RequestUploadFile,
                                                           FileAddAction.RequestFileAdd,
                                                           UpdateAction.RequestUpdate{
    private ArrayList<FileData> fileDatas;
    private FilesAdapter adapter;
    private ProgressDialog pd;
    private RecyclerView fileslist;
    private Dialog dialog_confirm;
    private TextView tittle_message;
    private Button button_cancel_save;
    private Button button_send_save;
    private int currentPosition = 0;
    private String id = "";
    private int typeDialog = 0;
    private String file = "";
    private OpenFileDialog fileDialogBuilder;
    private Dialog fileDialog;
    private View photo_group;
    private View mobile_group;
    private View file_group;
    private View cancel_group;
    private Dialog dialog;
    private final int CAMERA_CAPTURE = 1;
    private final int PIC_CROP = 2;
    private static final int GALLERY_REQUEST = 3;
    private Uri picUri;
    private boolean isOpenGalery = false;
    private String url = "";
    private String nameField = "";
    private View text_no;
    private BroadcastReceiver br;
    private View nointernet_group;
    private ViewGroup.LayoutParams par_nointernet_group;
    private TextView text_nointernet;
    private TextView text_setinternet;
    private boolean setUpdateAll = false;
    private String newFile = "";
    private FileData fileData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_files);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        if (id == null) {
            id = "";
        }
        if (!id.equals("")) {
            TextView text_header = (TextView) findViewById(R.id.text_header);
            text_header.setText(intent.getStringExtra("name") + " " + getResources().getString(R.string.text_files));

            url = intent.getStringExtra("url");
            nameField = intent.getStringExtra("nameField");
        }

        AlertDialog.Builder adb_save = new AlertDialog.Builder(this);
        adb_save.setCancelable(true);
        LinearLayout view_save = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.dialog_save, null);
        adb_save.setView(view_save);
        tittle_message = (TextView) view_save.findViewById(R.id.tittle);
        button_cancel_save = (Button) view_save.findViewById(R.id.button_cancel);
        button_send_save = (Button) view_save.findViewById(R.id.button_send);
        dialog_confirm = adb_save.create();

        button_cancel_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_confirm.dismiss();
            }
        });

        button_send_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_confirm.dismiss();

                if (typeDialog == 1) {
                    pd.show();
                    if (fileDatas.get(currentPosition).getFileId().equals("")) {
                        DBHelper.getInstance(Sapphire.getInstance()).deleteWorkplaceInspectionItemFile(fileDatas.get(currentPosition).getId());
                        if (fileDatas.get(currentPosition).getFile().indexOf(getApplicationContext().getFilesDir().getAbsolutePath() + "/temp/") == 0) {
                            File file = new File(fileDatas.get(currentPosition).getFile());
                            if (file.exists()) {
                                file.delete();
                            }
                        }
                        new FilesAction(FilesActivity.this).execute();
                    } else {
                        new FileDeleteAction(FilesActivity.this, fileDatas.get(currentPosition).getFileId()).execute();
                    }
                } else if (typeDialog == 2) {
                    Uri uri = Uri.fromFile(new File(file.toLowerCase()));
                    //Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    String extension = MimeTypeMap.getFileExtensionFromUrl(String.valueOf(uri));
                    String mType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                    intent.setDataAndType(uri, mType);
                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.text_error_open),
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        View back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        View add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!url.equals(Environment.PerformanceEvaluationsFilesURL)
                    && !url.equals(Environment.DisciplinesFilesURL)
                    && !url.equals(Environment.DocumentsFilesURL))
                    || fileDatas.size() == 0) {
                    dialog.show();
                } else {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.text_error_add_files),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        pd = new ProgressDialog(this);
        pd.setTitle(getResources().getString(R.string.text_loading));
        pd.setMessage(getResources().getString(R.string.text_please_wait));
        //диалог который нельзя закрыть пользователем
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        fileslist = (RecyclerView) findViewById(R.id.fileslist);
        fileslist.setNestedScrollingEnabled(false);
        fileslist.setLayoutManager(new LinearLayoutManager(FilesActivity.this));

        adapter = new FilesAdapter(this);
        fileslist.setAdapter(adapter);

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        //adb.setTitle(getResources().getString(R.string.text_change_photo));
        adb.setCancelable(true);
        LinearLayout view = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.dialog_photo, null);
        adb.setView(view);
        photo_group = view.findViewById(R.id.photo_group);
        mobile_group = view.findViewById(R.id.mobile_group);
        file_group = view.findViewById(R.id.file_group);
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

        file_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiseFile();

                dialog.dismiss();
            }
        });

        cancel_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        text_no = findViewById(R.id.text_no);

        // создаем BroadcastReceiver
        br = new BroadcastReceiver() {
            // действия при получении сообщений
            public void onReceive(Context context, Intent intent) {
                final String putreqwest = intent.getStringExtra(Environment.PARAM_TASK);

                if (putreqwest.equals("updatebottom")) {
                    UpdateBottom();
                }
            }
        };
        // создаем фильтр для BroadcastReceiver
        IntentFilter intFilt = new IntentFilter(Environment.BROADCAST_ACTION);
        // регистрируем (включаем) BroadcastReceiver
        registerReceiver(br, intFilt);

        nointernet_group = findViewById(R.id.nointernet_group);
        par_nointernet_group = nointernet_group.getLayoutParams();
        text_nointernet = (TextView) findViewById(R.id.text_nointernet);
        text_setinternet = (TextView) findViewById(R.id.text_setinternet);
        nointernet_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setUpdateAll) {
                    pd.show();

                    //new UpdateAction(FilesActivity.this, null, true, 0, "").execute();
                    new UpdateAction(FilesActivity.this);
                } else {
                    Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    startActivity(intent);
                }
            }
        });

        UpdateBottom();
    }

    private void UpdateBottom() {
        if (url.equals(Environment.WorkplaceInspectionsItemsFilesURL)) {
            text_nointernet.setText(getResources().getString(R.string.text_need_internet));
            text_setinternet.setText(getResources().getString(R.string.text_setinternet));
            setUpdateAll = false;
            if (NetRequests.getNetRequests().isOnline(false)) {
                if (Sapphire.getInstance().getNeedUpdate()) {
                    setUpdateAll = true;
                    text_nointernet.setText(getResources().getString(R.string.text_exits_nosynchronize));
                    text_setinternet.setText(getResources().getString(R.string.text_synchronize));
                    par_nointernet_group.height = GetPixelFromDips(56);
                } else {
                    par_nointernet_group.height = 0;
                }
            } else {
                par_nointernet_group.height = GetPixelFromDips(56);
            }
            nointernet_group.setLayoutParams(par_nointernet_group);
            nointernet_group.requestLayout();
        } else {
            if (Sapphire.getInstance().getNeedUpdate()) {
                par_nointernet_group.height = GetPixelFromDips(56);
            } else {
                par_nointernet_group.height = 0;
            }
            nointernet_group.setLayoutParams(par_nointernet_group);
            nointernet_group.requestLayout();
        }
    }

    public void updateVisibility() {
        if (fileDatas == null || fileDatas.size() == 0) {
            text_no.setVisibility(View.VISIBLE);
            fileslist.setVisibility(View.GONE);
        } else {
            fileslist.setVisibility(View.VISIBLE);
            text_no.setVisibility(View.GONE);
        }
    }

    private void choiseFile() {
        fileDialogBuilder = new OpenFileDialog(this, 1, null)
                //.setFilter(null)
                .setAccessDeniedMessage(getResources().getString(R.string.text_access_denied))
                //.setFolderIcon(ContextCompat.getDrawable(this, R.drawable.folder))
                .setFileIcon(ContextCompat.getDrawable(this, R.drawable.file))
                .setOpenDialogListener(new OpenFileDialog.OpenDialogListener() {
                    @Override
                    public void OnSelectedFile(String file) {
                        fileDialog.dismiss();
                        pd.show();
                        newFile = file;
                        new UploadFileAction(FilesActivity.this, newFile).execute();
                    }
                });
        fileDialogBuilder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    //dialog.cancel();
                    fileDialogBuilder.onBackPressed();
                    return true;
                }
                return false;
            }
        });
        fileDialog = fileDialogBuilder.create();
        fileDialogBuilder.setDialog(fileDialog);
        fileDialog.show();
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

    private File createTempFile() throws IOException {
        //File storageDir = Environment.getExternalStoragePublicDirectory(
        //        Environment.DIRECTORY_PICTURES);
        File storageDir = getApplicationContext().getFilesDir();

        String filesDirstr = storageDir.getAbsolutePath() + "/temp";
        // добавляем свой каталог к пути
        File sdPath = new File(filesDirstr);
        if(!sdPath.exists()){
            sdPath.delete();
            // создаем каталог
            sdPath.mkdirs();
        }

        return sdPath;
    }

    private void createImageFile(Bitmap bitmap) {
        File photoFile = null;
        try {
            photoFile = createTempFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
            ex.printStackTrace();
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy HHmmss");
            Date thisDate = new Date();
            String date = format.format(thisDate);

            File file = new File(photoFile.getAbsolutePath() + "/" + date + ".png");
            try {
                FileOutputStream outStream = new FileOutputStream(file);
                int crat = 1;
                //if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR1) {
                //    crat = bitmap.getByteCount()/131072;
                //    if (crat < 1) {
                //        crat = 1;
                //    }
                //}
                //bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90/crat, outStream);
                outStream.flush();
                outStream.close();

                pd.show();
                newFile = file.getAbsolutePath();
                new UploadFileAction(FilesActivity.this, newFile).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Ответ от камеры
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            isOpenGalery = true;
            // Вернулись от приложения Камера
            if (requestCode == CAMERA_CAPTURE) {
                // Получим Uri снимка
                picUri = data.getData();

                if (picUri == null) {
                    Bitmap thumbnailBitmap = (Bitmap) data.getExtras().get("data");
                    createImageFile(thumbnailBitmap);
                } else {
                    // кадрируем его
                    try {
                        performCrop();
                    } catch (ActivityNotFoundException anfe) {
                        Bitmap thumbnailBitmap = (Bitmap) data.getExtras().get("data");
                        createImageFile(thumbnailBitmap);
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
                createImageFile(thePic);
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

                    createImageFile(bitmap);
                }
            }
        } else {
            isOpenGalery = false;
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

    @Override
    public void onRootClick(int position) {
        //Intent intent = new Intent(PoliciesActivity.this, PdfActivity.class);
        //startActivity(intent);
    }

    @Override
    public void onDownloadClick(final int position) {
        fileDialogBuilder = new OpenFileDialog(this, 2, ".qqq\\.qqq")
                //.setFilter(".qqq\\.qqq")
                .setAccessDeniedMessage(getResources().getString(R.string.text_access_denied))
                //.setFolderIcon(ContextCompat.getDrawable(this, R.drawable.folder))
                //.setFileIcon(ContextCompat.getDrawable(this, R.drawable.file))
                .setOpenDialogListener(new OpenFileDialog.OpenDialogListener() {
                    @Override
                    public void OnSelectedFile(String folder) {
                        pd.show();
                        new GetFileAction(FilesActivity.this, fileDatas.get(position).getFileId(), fileDatas.get(position).getName(), folder).execute();
                    }
                });
        fileDialogBuilder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    //dialog.cancel();
                    fileDialogBuilder.onBackPressed();
                    return true;
                }
                return false;
            }
        });
        fileDialog = fileDialogBuilder.create();
        fileDialogBuilder.setDialog(fileDialog);
        fileDialog.show();
    }

    @Override
    public void onDeleteClick(int position) {
        currentPosition = position;

        typeDialog = 1;
        tittle_message.setText(getResources().getString(R.string.text_confirm_delete));
        button_cancel_save.setText(getResources().getString(R.string.text_cancel));
        button_send_save.setText(getResources().getString(R.string.text_delete));
        dialog_confirm.show();
    }

    @Override
    public void onRequestFiles(String result, ArrayList<FileData> fileDatas) {
        if (!result.equals("OK") && (!result.equals(getResources().getString(R.string.text_need_internet)) || !url.equals(Environment.WorkplaceInspectionsItemsFilesURL))) {
            updateVisibility();

            pd.hide();
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
            return;
        }

        ArrayList<FileData> allDatas = new ArrayList<FileData>();
        ArrayList<FileData> datas = DBHelper.getInstance(Sapphire.getInstance()).getWorkplaceInspectionItemFiles(id);

        boolean isExist = false;
        for (FileData item: fileDatas) {
            isExist = false;
            for (FileData item2: datas) {
                if (item.getParentId().equals(item2.getParentId())) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                allDatas.add(item);
            }
        }
        for (FileData item: datas) {
            allDatas.add(item);
        }

        this.fileDatas = allDatas;
        adapter.setData(this.fileDatas);

        updateVisibility();

        pd.hide();
    }

    @Override
    public void onRequestFileDelete(String result) {
        if (!result.equals("OK")) {
            pd.hide();
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
        } else {
            new FilesAction(FilesActivity.this).execute();
        }
    }

    @Override
    public void onRequestFile(String result, String file) {
        pd.hide();
        if (!result.equals("OK")) {
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
        } else {
            this.file = file;
            typeDialog = 2;
            tittle_message.setText(getResources().getString(R.string.text_open_file));
            button_cancel_save.setText(getResources().getString(R.string.text_no));
            button_send_save.setText(getResources().getString(R.string.text_open));
            dialog_confirm.show();
        }
    }

    @Override
    public void onRequestUploadFile(String result, FileData fileData) {
        if (!result.equals("OK")) {
            isOpenGalery = false;
            if (url.equals(Environment.WorkplaceInspectionsItemsFilesURL) && result.equals(getResources().getString(R.string.text_need_internet))) {
                FileData newFileData = new FileData();
                newFileData.setIsFolder(false);
                newFileData.setParentId(id);
                newFileData.setFile(newFile);
                File file = new File(newFile);
                newFileData.setName(file.getName());
                newFileData.setSize((int) file.length());

                DBHelper.getInstance(Sapphire.getInstance()).addWorkplaceInspectionItemFile(newFileData);
                new FilesAction(FilesActivity.this).execute();
            } else {
                pd.hide();
                Toast.makeText(getBaseContext(), result,
                        Toast.LENGTH_LONG).show();
            }
        } else {
            this.fileData = fileData;
            UserInfo.getUserInfo().getFileDatas().add(this.fileData);
            //new FilesAction(FilesActivity.this).execute();
            new FileAddAction(FilesActivity.this, fileData.getFileId(), id, url, nameField).execute();
        }
    }

    @Override
    public void onRequestFileAdd(String result) {
        isOpenGalery = false;
        if (!result.equals("OK")) {
            if (url.equals(Environment.WorkplaceInspectionsItemsFilesURL) && result.equals(getResources().getString(R.string.text_need_internet))) {
                fileData.setIsFolder(false);
                fileData.setParentId(id);
                fileData.setFile(newFile);

                DBHelper.getInstance(Sapphire.getInstance()).addWorkplaceInspectionItemFile(fileData);
                new FilesAction(FilesActivity.this).execute();
            } else {
                pd.hide();
                Toast.makeText(getBaseContext(), result,
                        Toast.LENGTH_LONG).show();
            }
        } else {
            new FilesAction(FilesActivity.this).execute();
        }
    }

    @Override
    public void onRequestUpdate(String result) {
        if (!result.equals("OK")) {
            pd.hide();
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
        } else {
            Sapphire.getInstance().setNeedUpdate(NetRequests.getNetRequests().isOnline(false));
            UpdateBottom();
            pd.hide();
        }
    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isOpenGalery) {
            pd.show();
            new FilesAction(FilesActivity.this).execute();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }
}