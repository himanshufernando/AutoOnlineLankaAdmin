package tkhub.project.autoonlineadmin.Layout;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tkhub.project.autoonlineadmin.Adapter.NavigationDrawer;
import tkhub.project.autoonlineadmin.Adapter.NavigationDrawerItem;
import tkhub.project.autoonlineadmin.Adapter.OrderAdapter;
import tkhub.project.autoonlineadmin.Adapter.OrderImageAdapter;
import tkhub.project.autoonlineadmin.Adapter.OrderImageItem;
import tkhub.project.autoonlineadmin.Adapter.OrderItem;
import tkhub.project.autoonlineadmin.R;
import tkhub.project.autoonlineadmin.Servies.NetworkAvailable;

/**
 * Created by Himanshu on 11/19/2016.
 */

public class Home extends Activity {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 123;
    DrawerLayout dLayout;
    ListView navigationList;
    RelativeLayout layMenuView;

    SharedPreferences pref;
    ProgressDialog dialogProgress;

    NetworkAvailable connection;
    int userID;
    RecyclerView recyclerView;
    int oderStatus = 1;
    String phoneNumber;
    int udateStatus = 0;

    Dialog dialogBoxImages, dialogBoxFeedback;
    ArrayList<OrderImageItem> imageItems = new ArrayList<OrderImageItem>();
    OrderImageAdapter imageAdapter;

    ArrayList<OrderItem> orderItems = new ArrayList<OrderItem>();
    OrderAdapter orderAdapter;

    TextView feedbackDialogfeedback;

    ImageView imone, imtwo, imthree, imfore, imefive;


    WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home);

        dialogProgress = new ProgressDialog(Home.this);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        userID = pref.getInt("key_id", 1);
        imageAdapter = new OrderImageAdapter(this, imageItems);
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        layMenuView = (RelativeLayout) findViewById(R.id.relativeLayoutMenuView);

        recyclerView = (RecyclerView) findViewById(R.id.list_news);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);


        setNavigation();
        orderAdapter = new OrderAdapter(this, orderItems);
        connection = new NetworkAvailable(Home.this);
        if (connection.CheckNetworkAvailable() == false) {
            new SweetAlertDialog(Home.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Connection Error")
                    .setContentText("No Internet Connection!")
                    .show();
        } else {
            new getOrderHistor().execute();

        }

        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (connection.CheckNetworkAvailable() == false) {
                } else {
                    new getOrderHistor().execute();

                }
            }
        });


        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinner.setItems("Pending", "On Progress", "Delivered", "Rejected", "All");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if (item.equals("Pending")) {
                    oderStatus = 1;
                } else if (item.equals("On Progress")) {
                    oderStatus = 2;
                } else if (item.equals("Delivered")) {
                    oderStatus = 3;
                } else if (item.equals("Rejected")) {
                    oderStatus = 4;
                } else if (item.equals("All")) {
                    oderStatus = 5;
                }
                new getOrderHistor().execute();
            }
        });
    }

    public void setNavigation() {
        ArrayList<NavigationDrawerItem> mNavItems = new ArrayList<NavigationDrawerItem>();

        mNavItems.add(new NavigationDrawerItem("Home", R.string.icon_navigation_home));
        mNavItems.add(new NavigationDrawerItem("Make", R.string.icon_navigation_hotline));
        mNavItems.add(new NavigationDrawerItem("Model", R.string.icon_navigation_email));
        mNavItems.add(new NavigationDrawerItem("Users", R.string.icon_navigation_sms));
        mNavItems.add(new NavigationDrawerItem("Password Reset", R.string.icon_passwordreset));


        NavigationDrawer adapter = new NavigationDrawer(this, mNavItems);
        navigationList = (ListView) findViewById(R.id.listView_navigation);
        navigationList.setAdapter(adapter);

        navigationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                }
                if (position == 1) {
                    dLayout.closeDrawer(Gravity.LEFT);
                    Intent i = new Intent(Home.this, Make.class);
                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation, R.anim.animation2).toBundle();
                    finish();
                    startActivity(i, bndlanimation);

                }
                if (position == 2) {
                    dLayout.closeDrawer(Gravity.LEFT);
                    Intent i = new Intent(Home.this, Model.class);
                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation, R.anim.animation2).toBundle();
                    finish();
                    startActivity(i, bndlanimation);

                }
                if (position == 3) {
                    dLayout.closeDrawer(Gravity.LEFT);
                    Intent i = new Intent(Home.this, Users.class);
                    i.putExtra("UserSerachSatatus", 2);
                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation, R.anim.animation2).toBundle();
                    finish();
                    startActivity(i, bndlanimation);

                }
                if (position == 4) {
                    //showPasswordReset();
                }


            }
        });

        layMenuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dLayout.openDrawer(Gravity.LEFT);
            }
        });

    }


    private class getOrderHistor extends AsyncTask<Void, Void, Void> {

        JSONObject Jobjectnew;
        int errorType;
        JSONArray Jarray;
        JSONObject object;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogProgress.setCancelable(false);
            dialogProgress.setMessage("Data fetching ...");
            dialogProgress.show();

            orderItems.clear();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                String url = "http://himanshufernando.com/App/Autolanka/AllOrederDetailsAdmin.php";
                RequestBody formBody = new FormBody.Builder()
                        .add("UserID", String.valueOf(userID)).build();
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).post(formBody).build();
                Response responses = null;

                responses = client.newCall(request).execute();
                String jsonData = responses.body().string();
                Jobjectnew = new JSONObject(jsonData);
                if (Jobjectnew.isNull("partsDetailsAdmin") || Jobjectnew == null) {
                    errorType = 3;
                } else {
                    Jarray = Jobjectnew.getJSONArray("partsDetailsAdmin");
                    for (int i = 0; i < Jarray.length(); i++) {
                        object = Jarray.getJSONObject(i);

                        int statu = Integer.parseInt(object.getString("partSatues"));
                        String sattus = null;

                        if (statu == 1) {
                            sattus = "Pending";
                        } else if (statu == 2) {
                            sattus = "On Progress";
                        } else if (statu == 3) {
                            sattus = "Delivered";
                        } else if (statu == 4) {
                            sattus = "Reject";

                        }
                        String name;
                        if (object.getString("partUsername").equals("Non Registore")) {
                            name = object.getString("regName");
                        } else {
                            name = object.getString("partUsername");
                        }


                        if (statu == oderStatus) {
                            orderItems.add(new OrderItem(Integer.parseInt(object.getString("partid")),
                                    Integer.parseInt(object.getString("partUserid")),
                                    name,
                                    Integer.parseInt(object.getString("partNumber")),
                                    object.getString("makeName"),
                                    object.getString("modelName"),
                                    object.getString("partChassi"),
                                    Integer.parseInt(object.getString("partYear")),
                                    object.getString("partCity"),
                                    object.getString("partDiscription"),
                                    object.getString("partDate"),
                                    sattus,
                                    Integer.parseInt(object.getString("partUsertype")),
                                    Integer.parseInt(object.getString("partApprovedID")),
                                    object.getString("partApproved"),
                                    object.getString("regUserNumber"),
                                    Integer.parseInt(object.getString("partImageid"))
                            ));
                        } else if (oderStatus == 5) {
                            orderItems.add(new OrderItem(Integer.parseInt(object.getString("partid")),
                                    Integer.parseInt(object.getString("partUserid")),
                                    name,
                                    Integer.parseInt(object.getString("partNumber")),
                                    object.getString("makeName"),
                                    object.getString("modelName"),
                                    object.getString("partChassi"),
                                    Integer.parseInt(object.getString("partYear")),
                                    object.getString("partCity"),
                                    object.getString("partDiscription"),
                                    object.getString("partDate"),
                                    sattus,
                                    Integer.parseInt(object.getString("partUsertype")),
                                    Integer.parseInt(object.getString("partApprovedID")),
                                    object.getString("partApproved"),
                                    object.getString("regUserNumber"),
                                    Integer.parseInt(object.getString("partImageid"))
                            ));
                        } else {

                        }


                    }


                }


            } catch (IOException e) {
                e.printStackTrace();
                errorType = 1;
            } catch (JSONException e) {
                e.printStackTrace();
                errorType = 1;
            } catch (Exception cr) {
                errorType = 2;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            dialogProgress.dismiss();
            if (errorType == 1 || errorType == 2) {
                new SweetAlertDialog(Home.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("Server is busy please try after some time!")
                        .show();
            } else if (errorType == 3) {
                new SweetAlertDialog(Home.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Sorry")
                        .setContentText("You don't have any history!")
                        .show();
            } else {
                recyclerView.setAdapter(orderAdapter);

            }
            mWaveSwipeRefreshLayout.setRefreshing(false);

        }

    }

    public void showOrderImages(int imageid, int oid) {

        dialogBoxImages = new Dialog(Home.this);
        dialogBoxImages.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogBoxImages.setContentView(R.layout.dialog_images);
        dialogBoxImages.setCancelable(true);


        recyclerView = (RecyclerView) dialogBoxImages.findViewById(R.id.list_image);


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        new getImages(imageid, oid).execute();


    }


    public void showOrderStstus(final int oid, final int uid, String phone, final String ststus) {

        final Dialog dialogBox;
        dialogBox = new Dialog(Home.this);
        dialogBox.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogBox.setContentView(R.layout.dialog_orderupades);
        dialogBox.setCancelable(true);
        dialogBox.show();


        phoneNumber = "tel:" + "0" + phone;
        RelativeLayout layoutProgress = (RelativeLayout) dialogBox.findViewById(R.id.relativeLayout4);
        RelativeLayout layoutDeliverd = (RelativeLayout) dialogBox.findViewById(R.id.relativeLayout3);
        RelativeLayout layoutReject = (RelativeLayout) dialogBox.findViewById(R.id.relativeLayout10);
        RelativeLayout layoutUser = (RelativeLayout) dialogBox.findViewById(R.id.relativeLayout20);
        RelativeLayout layoutCall = (RelativeLayout) dialogBox.findViewById(R.id.relativeLayout30);

        if (ststus.equals("On Progress")) {
            layoutProgress.setBackgroundColor(getResources().getColor(R.color.colorLightBlack));
        } else if (ststus.equals("Delivered")) {
            layoutDeliverd.setBackgroundColor(getResources().getColor(R.color.colorLightBlack));
        } else if (ststus.equals("Reject")) {
            layoutReject.setBackgroundColor(getResources().getColor(R.color.colorLightBlack));
        }

        layoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uid == 0) {
                    Toast.makeText(Home.this, "This user is not a registered user", Toast.LENGTH_SHORT).show();
                    dialogBox.dismiss();

                } else {
                    Intent i = new Intent(Home.this, Users.class);
                    i.putExtra("UserSerachSatatus", 1);
                    i.putExtra("UserID", uid);
                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation, R.anim.animation2).toBundle();
                    finish();
                    startActivity(i, bndlanimation);
                }
            }
        });

        layoutCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBox.dismiss();
                if (!checkPermission()) {
                    requestPermission();
                } else {
                    try {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse(phoneNumber));
                        startActivity(callIntent);
                    } catch (Exception ex) {
                        Toast.makeText(Home.this, "Your device isn't compatible this feature", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });


        layoutProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ststus.equals("On Progress")) {
                    Toast.makeText(Home.this, "This Order already in On Progress ", Toast.LENGTH_LONG).show();
                } else {
                    udateStatus = 2;
                    dialogBox.dismiss();
                    connection = new NetworkAvailable(Home.this);
                    if (connection.CheckNetworkAvailable() == false) {
                        new SweetAlertDialog(Home.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Connection Error")
                                .setContentText("No Internet Connection!")
                                .show();
                    } else {
                        new setUpdateOrder(String.valueOf(oid), String.valueOf(udateStatus)).execute();

                    }

                }

            }
        });


        layoutDeliverd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ststus.equals("Delivered")) {
                    Toast.makeText(Home.this, "This Order already Delivered ", Toast.LENGTH_LONG).show();
                } else {
                    udateStatus = 3;
                    dialogBox.dismiss();
                    connection = new NetworkAvailable(Home.this);
                    if (connection.CheckNetworkAvailable() == false) {
                        new SweetAlertDialog(Home.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Connection Error")
                                .setContentText("No Internet Connection!")
                                .show();
                    } else {
                        new setUpdateOrder(String.valueOf(oid), String.valueOf(udateStatus)).execute();

                    }

                }

            }
        });

        layoutReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ststus.equals("Reject")) {
                    Toast.makeText(Home.this, "This Order already Rejected ", Toast.LENGTH_LONG).show();
                } else {
                    udateStatus = 4;
                    dialogBox.dismiss();
                    connection = new NetworkAvailable(Home.this);
                    if (connection.CheckNetworkAvailable() == false) {
                        new SweetAlertDialog(Home.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Connection Error")
                                .setContentText("No Internet Connection!")
                                .show();
                    } else {
                        new setUpdateOrder(String.valueOf(oid), String.valueOf(udateStatus)).execute();

                    }

                }

            }
        });


    }


    public void showFeedback(int oid) {

        dialogBoxFeedback = new Dialog(Home.this);
        dialogBoxFeedback.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogBoxFeedback.setContentView(R.layout.dialog_feedback);
        dialogBoxFeedback.setCancelable(true);

        feedbackDialogfeedback = (TextView) dialogBoxFeedback.findViewById(R.id.textView30);
        imone = (ImageView) dialogBoxFeedback.findViewById(R.id.imageView8);
        imtwo = (ImageView) dialogBoxFeedback.findViewById(R.id.imageView7);
        imthree = (ImageView) dialogBoxFeedback.findViewById(R.id.imageView4);
        imfore = (ImageView) dialogBoxFeedback.findViewById(R.id.imageView5);
        imefive = (ImageView) dialogBoxFeedback.findViewById(R.id.imageView6);

        new getFeedback(oid).execute();


    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE:
                if (grantResults.length > 0) {
                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (locationAccepted) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse(phoneNumber));
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        startActivity(callIntent);

                    } else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }

                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Home.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private class setUpdateOrder extends AsyncTask<Void, Void, Void> {

        JSONObject Jobject;
        int errorType;
        String oID, uStatus;

        public setUpdateOrder(String oID, String uStatus) {
            this.oID = oID;
            this.uStatus = uStatus;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogProgress.setCancelable(false);
            dialogProgress.setMessage("User detail validation ...");
            dialogProgress.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                String url = "http://himanshufernando.com/App/Autolanka/" + "orderUpdate.php";
                RequestBody formBody = new FormBody.Builder()
                        .add("AdminID", String.valueOf(userID))
                        .add("OderID", oID)
                        .add("Status", uStatus)
                        .build();
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).post(formBody).build();
                Response responses = null;

                responses = client.newCall(request).execute();

                String jsonData = responses.body().string();
                Jobject = new JSONObject(jsonData);


            } catch (IOException e) {
                e.printStackTrace();
                errorType = 1;
            } catch (JSONException e) {
                e.printStackTrace();
                errorType = 1;
            } catch (Exception cr) {
                errorType = 2;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            dialogProgress.dismiss();

            if (errorType == 1 || errorType == 2) {
                new SweetAlertDialog(Home.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("Server is busy please try after some time!")
                        .show();
            } else {
                try {
                    int severresult = Integer.parseInt(Jobject.getString("Result"));
                    if (severresult == 0) {
                        new SweetAlertDialog(Home.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error")
                                .setContentText("Server is busy please try after some time!")
                                .show();
                    } else if (severresult == 3) {
                        new SweetAlertDialog(Home.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error")
                                .setContentText("Server is busy please try after some time!")
                                .show();
                    } else if (severresult == 1) {

                        new SweetAlertDialog(Home.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success")
                                .setContentText("Status has been updated!")
                                .show();
                        new getOrderHistor().execute();
                    } else {
                        new SweetAlertDialog(Home.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error")
                                .setContentText("Server is busy please try after some time!")
                                .show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception ex) {

                }

            }


        }

    }


    private class setPasswordReset extends AsyncTask<Void, Void, Void> {

        JSONObject Jobject;
        int errorType, userID;

        String currentPassword, newPassword;

        public setPasswordReset(int userID, String currentPassword, String newPassword) {
            this.userID = userID;
            this.currentPassword = currentPassword;
            this.newPassword = newPassword;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogProgress.setCancelable(false);
            dialogProgress.setMessage("User detail validation ...");
            dialogProgress.show();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                String url = "http://himanshufernando.com/App/Autolanka/" + "adminPasswordReset.php";
                RequestBody formBody = new FormBody.Builder()
                        .add("UserID", String.valueOf(userID))
                        .add("CurrentPassword", currentPassword.trim())
                        .add("NewPassword", newPassword.trim())
                        .build();

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).post(formBody).build();
                Response responses = null;

                responses = client.newCall(request).execute();

                String jsonData = responses.body().string();
                Jobject = new JSONObject(jsonData);


            } catch (IOException e) {
                e.printStackTrace();
                errorType = 1;
            } catch (JSONException e) {
                e.printStackTrace();
                errorType = 1;
            } catch (Exception cr) {
                errorType = 2;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            dialogProgress.dismiss();

            if (errorType == 1 || errorType == 2) {
                new SweetAlertDialog(Home.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("Server is busy please try after some time!")
                        .show();
            } else {
                try {
                    int severresult = Integer.parseInt(Jobject.getString("Result"));
                    if (severresult == 0) {
                        new SweetAlertDialog(Home.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error")
                                .setContentText("Server is busy please try after some time!")
                                .show();
                    } else if (severresult == 3) {
                        new SweetAlertDialog(Home.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error")
                                .setContentText("Server is busy please try after some time!")
                                .show();
                    } else if (severresult == 2) {
                        new SweetAlertDialog(Home.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error")
                                .setContentText("Password incorrect try after!")
                                .show();
                    } else if (severresult == 1) {
                        new SweetAlertDialog(Home.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Password Reset Success")
                                .setContentText("Password Reset Success!")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        Intent i = new Intent(Home.this, Login.class);
                                        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation, R.anim.animation2).toBundle();
                                        finish();
                                        startActivity(i, bndlanimation);
                                    }
                                })
                                .show();
                    } else {
                        new SweetAlertDialog(Home.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error")
                                .setContentText("Server is busy please try after some time!")
                                .show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception ex) {

                }

            }


        }

    }


    private class getFeedback extends AsyncTask<Void, Void, Void> {

        JSONObject Jobject;
        JSONArray Jarray;
        JSONObject object;
        int errorType;
        int orderID;


        public getFeedback(int oid) {

            this.orderID = oid;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                String url = "http://himanshufernando.com/App/Autolanka/" + "getFeedback.php";
                RequestBody formBody = new FormBody.Builder()
                        .add("OrderID", String.valueOf(orderID)).build();
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).post(formBody).build();
                Response responses = null;

                responses = client.newCall(request).execute();

                String jsonData = responses.body().string();
                Jobject = new JSONObject(jsonData);
                Jarray = Jobject.getJSONArray("feedbackJosnArray");




            } catch (IOException e) {
                e.printStackTrace();
                errorType = 1;
            } catch (JSONException e) {
                e.printStackTrace();
                errorType = 1;
            } catch (Exception cr) {
                errorType = 2;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            dialogProgress.dismiss();

            System.out.println(" errorType = "+errorType);

            if (errorType == 1 || errorType == 2) {
                new SweetAlertDialog(Home.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Warring")
                        .setContentText("No feedback submitted yet!")
                        .show();
            } else {
                try {
                    for (int i = 0; i < Jarray.length(); i++) {
                        object = Jarray.getJSONObject(i);
                        feedbackDialogfeedback.setText(object.getString("autolanka_feedback_comment"));
                        int level = Integer.parseInt(object.getString("autolanka_feedback_lavel"));
                        switch (level) {
                            case 20:
                                imtwo.setImageResource(R.drawable.six);
                                imthree.setImageResource(R.drawable.six);
                                imfore.setImageResource(R.drawable.six);
                                imefive.setImageResource(R.drawable.six);
                                break;
                            case 40:
                                imtwo.setImageResource(R.drawable.two);
                                imthree.setImageResource(R.drawable.six);
                                imfore.setImageResource(R.drawable.six);
                                imefive.setImageResource(R.drawable.six);
                                break;
                            case 60:
                                imtwo.setImageResource(R.drawable.two);
                                imthree.setImageResource(R.drawable.three);
                                imfore.setImageResource(R.drawable.six);
                                imefive.setImageResource(R.drawable.six);
                                break;
                            case 80:
                                imtwo.setImageResource(R.drawable.two);
                                imthree.setImageResource(R.drawable.three);
                                imfore.setImageResource(R.drawable.forv);
                                imefive.setImageResource(R.drawable.six);
                                break;
                            case 100:
                                imtwo.setImageResource(R.drawable.two);
                                imthree.setImageResource(R.drawable.three);
                                imfore.setImageResource(R.drawable.forv);
                                imefive.setImageResource(R.drawable.five);
                                break;
                            default:
                                imtwo.setImageResource(R.drawable.two);
                                imthree.setImageResource(R.drawable.three);
                                imfore.setImageResource(R.drawable.forv);
                                imefive.setImageResource(R.drawable.five);
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                dialogBoxFeedback.show();

            }

        }

    }


    private class getImages extends AsyncTask<Void, Void, Void> {

        JSONObject Jobject;
        JSONArray Jarray;
        JSONObject object;
        int errorType;
        int imageid, orderID;


        public getImages(int imageid, int oid) {
            this.imageid = imageid;
            this.orderID = oid;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            imageItems.clear();
            dialogProgress.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                String url = "http://himanshufernando.com/App/Autolanka/" + "getImage.php";
                RequestBody formBody = new FormBody.Builder()
                        .add("ImageID", String.valueOf(imageid)).build();
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).post(formBody).build();
                Response responses = null;

                responses = client.newCall(request).execute();

                String jsonData = responses.body().string();
                Jobject = new JSONObject(jsonData);
                Jarray = Jobject.getJSONArray("imageDetails");
                for (int i = 0; i < Jarray.length(); i++) {
                    object = Jarray.getJSONObject(i);
                    imageItems.add(new OrderImageItem(object.getString("imageURL")));


                }


            } catch (IOException e) {
                e.printStackTrace();
                errorType = 1;
            } catch (JSONException e) {
                e.printStackTrace();
                errorType = 1;
            } catch (Exception cr) {
                errorType = 2;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            dialogProgress.dismiss();
            recyclerView.setAdapter(imageAdapter);
            dialogBoxImages.show();

            if (errorType == 1 || errorType == 2) {
                new SweetAlertDialog(Home.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Error")
                        .setContentText("No Images!")
                        .show();
            } else {
                for (int i = 0; i < Jarray.length(); i++) {
                    try {
                        new DownloadingImages(object.getString("imageURL"), orderID).execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

    }

    private class DownloadingImages extends AsyncTask<Void, Void, Void> {

        String urlImage = "";
        int orededID;

        public DownloadingImages(String url, int oid) {
            this.urlImage = url;
            this.orededID = oid;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                String root = Environment.getExternalStorageDirectory().toString();
                URL url = new URL(urlImage);
                InputStream is = url.openStream();
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/Auto Onlin");
                dir.mkdirs();
                Random generator = new Random();
                int n = 10000;
                n = generator.nextInt(n);
                String fname = "order_" + orededID + "_" + n + ".jpg";
                File file = new File(dir, fname);
                FileOutputStream out = new FileOutputStream(file);
                byte[] b = new byte[2048];
                int length;
                while ((length = is.read(b)) != -1) {
                    out.write(b, 0, length);
                }

                is.close();
                out.close();

            } catch (IOException e) {

            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


        }

    }


    @Override
    public void onBackPressed() {

        Intent i = new Intent(Home.this, Login.class);
        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation, R.anim.animation2).toBundle();
        finish();
        startActivity(i, bndlanimation);

    }

}
