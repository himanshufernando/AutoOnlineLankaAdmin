package tkhub.project.autoonlineadmin.Layout;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tkhub.project.autoonlineadmin.Adapter.UserRegAdapter;
import tkhub.project.autoonlineadmin.Adapter.UserRegItem;
import tkhub.project.autoonlineadmin.R;
import tkhub.project.autoonlineadmin.Servies.NetworkAvailable;

/**
 * Created by Himanshu on 11/21/2016.
 */

public class Users extends Activity {

    RecyclerView recyclerView;
    NetworkAvailable connection;
    ProgressDialog dialogProgress;

    ArrayList<UserRegItem> userItems = new ArrayList<UserRegItem>();
    UserRegAdapter userAdapter;
    int selectUserType=0,userSerachSatatus,userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_users);


        dialogProgress = new ProgressDialog(Users.this);
        recyclerView = (RecyclerView) findViewById(R.id.list_users);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);


        userSerachSatatus = getIntent().getIntExtra("UserSerachSatatus",0);
        userID= getIntent().getIntExtra("UserID",0);

        userAdapter = new UserRegAdapter(this, userItems);
        connection = new NetworkAvailable(Users.this);
        if (connection.CheckNetworkAvailable() == false) {
            new SweetAlertDialog(Users.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Connection Error")
                    .setContentText("No Internet Connection!")
                    .show();
        } else {
            if(userSerachSatatus==1){
                new getRegUsersByID(userID).execute();
            }else {
                new getRegUsers().execute();
            }



        }


        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinner.setItems("Registered", "Non Registered");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if (item.equals("Registered")) {
                    selectUserType = 1;
                    new getRegUsers().execute();
                } else if (item.equals("Non Registered")) {
                    selectUserType = 2;
                    new getNonRegUsers().execute();
                }

            }
        });
    }

    private class getRegUsersByID extends AsyncTask<Void, Void, Void> {

        JSONObject Jobjectnew;
        int errorType,userID;
        JSONArray Jarray;
        JSONObject object;


        public getRegUsersByID(int userID) {
            this.userID = userID;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogProgress.setCancelable(false);
            dialogProgress.setMessage("Data fetching ...");
            dialogProgress.show();

            userItems.clear();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                String url = "http://himanshufernando.com/App/Autolanka/regUsersByID.php";
                RequestBody formBody = new FormBody.Builder()
                        .add("UserID", String.valueOf(userID))
                        .build();
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).post(formBody).build();
                Response responses = null;
                responses = client.newCall(request).execute();

                String jsonData = responses.body().string();
                Jobjectnew = new JSONObject(jsonData);
                if (Jobjectnew.isNull("userJosnArray") || Jobjectnew == null) {
                    errorType = 3;
                } else {
                    Jarray = Jobjectnew.getJSONArray("userJosnArray");
                    for (int i = 0; i < Jarray.length(); i++) {
                        object = Jarray.getJSONObject(i);
                        userItems.add(new UserRegItem(object.getString("autolanka_user_name"),
                                object.getString("autolanka_user_email"),
                                object.getString("autolanka_user_phone"),
                                object.getString("autolanka_user_nic"),
                                object.getString("autolanka_user_sex"),
                                object.getString("autolanka_user_date")));
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
                new SweetAlertDialog(Users.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("Server is busy please try after some time!")
                        .show();
            } else if (errorType == 3) {
                new SweetAlertDialog(Users.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Sorry")
                        .setContentText("You don't have any history!")
                        .show();
            } else {
                recyclerView.setAdapter(userAdapter);

            }


        }

    }



    private class getRegUsers extends AsyncTask<Void, Void, Void> {

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

            userItems.clear();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                String url = "http://himanshufernando.com/App/Autolanka/regUsers.php";
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).build();
                Response responses = null;

                responses = client.newCall(request).execute();
                String jsonData = responses.body().string();
                Jobjectnew = new JSONObject(jsonData);
                if (Jobjectnew.isNull("userJosnArray") || Jobjectnew == null) {
                    errorType = 3;
                } else {
                    Jarray = Jobjectnew.getJSONArray("userJosnArray");
                    for (int i = 0; i < Jarray.length(); i++) {
                        object = Jarray.getJSONObject(i);
                        userItems.add(new UserRegItem(object.getString("autolanka_user_name"),
                                object.getString("autolanka_user_email"),
                                object.getString("autolanka_user_phone"),
                                object.getString("autolanka_user_nic"),
                                object.getString("autolanka_user_sex"),
                                object.getString("autolanka_user_date")));
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
                new SweetAlertDialog(Users.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("Server is busy please try after some time!")
                        .show();
            } else if (errorType == 3) {
                new SweetAlertDialog(Users.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Sorry")
                        .setContentText("You don't have any history!")
                        .show();
            } else {
                recyclerView.setAdapter(userAdapter);

            }


        }

    }

    private class getNonRegUsers extends AsyncTask<Void, Void, Void> {

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

            userItems.clear();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                String url = "http://himanshufernando.com/App/Autolanka/NonRegUser.php";
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).build();
                Response responses = null;

                responses = client.newCall(request).execute();
                String jsonData = responses.body().string();
                Jobjectnew = new JSONObject(jsonData);
                if (Jobjectnew.isNull("userNonRegJosnArray") || Jobjectnew == null) {
                    errorType = 3;
                } else {
                    Jarray = Jobjectnew.getJSONArray("userNonRegJosnArray");
                    for (int i = 0; i < Jarray.length(); i++) {
                        object = Jarray.getJSONObject(i);
                        userItems.add(new UserRegItem(object.getString("autolanka_order_number"),
                                object.getString("autolanka_order_name")
                               ));
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
                new SweetAlertDialog(Users.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("Server is busy please try after some time!")
                        .show();
            } else if (errorType == 3) {
                new SweetAlertDialog(Users.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Sorry")
                        .setContentText("You don't have any history!")
                        .show();
            } else {
                recyclerView.setAdapter(userAdapter);

            }


        }

    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(Users.this, Home.class);
        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation, R.anim.animation2).toBundle();
        finish();
        startActivity(i, bndlanimation);

    }
}
