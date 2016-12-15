package tkhub.project.autoonlineadmin.Layout;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
import tkhub.project.autoonlineadmin.Adapter.MakeAdapter;
import tkhub.project.autoonlineadmin.Adapter.MakeItem;
import tkhub.project.autoonlineadmin.R;
import tkhub.project.autoonlineadmin.Servies.NetworkAvailable;

/**
 * Created by Himanshu on 11/21/2016.
 */

public class Make extends Activity {


    RecyclerView recyclerView;
    ProgressDialog dialogProgress;
    ArrayList<MakeItem> makeItems = new ArrayList<MakeItem>();
    MakeAdapter makeAdapter;

    NetworkAvailable connection;
    Button btnAdd;
    EditText edtMake;
    int btnsattus =0,updateIdmake;
    String updateMakename;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_make);
        dialogProgress = new ProgressDialog(Make.this);
        recyclerView = (RecyclerView) findViewById(R.id.list_make);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        btnAdd = (Button) findViewById(R.id.button2);
        edtMake =(EditText)findViewById(R.id.editText);


        makeAdapter = new MakeAdapter(this, makeItems);
        connection = new NetworkAvailable(Make.this);
        if (connection.CheckNetworkAvailable() == false) {
            new SweetAlertDialog(Make.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Connection Error")
                    .setContentText("No Internet Connection!")
                    .show();
        } else {
            new getMakeFromSever().execute();

        }


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connection = new NetworkAvailable(Make.this);
                if (connection.CheckNetworkAvailable() == false) {
                    edtMake.setError(null);
                    new SweetAlertDialog(Make.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Connection Error")
                            .setContentText("No Internet Connection!")
                            .show();
                } else if(edtMake.getText().toString().isEmpty()||edtMake.getText().toString().equals("")) {
                    edtMake.setError("Empty");
                }else {
                    if(btnsattus==0){
                        edtMake.setError(null);
                        new setAddMake(edtMake.getText().toString().trim()).execute();
                    }else {
                        new SweetAlertDialog(Make.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Warning")
                                .setContentText("Do you want to update this ?")
                                .setConfirmText("Yes!")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();

                                        edtMake.setError(null);
                                        new updateMake(edtMake.getText().toString().trim(),2,updateIdmake).execute();

                                    }
                                })
                                .setCancelText("No!")
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                    }
                                })
                                .show();



                    }



                }
            }
        });


    }

    private class getMakeFromSever extends AsyncTask<Void, Void, Void> {

        JSONObject Jobject;
        JSONArray Jarray;
        JSONObject object;
        int errorType;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialogProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogProgress.setCancelable(false);
            dialogProgress.setMessage("Data fetching ...");
            dialogProgress.show();

            makeItems.clear();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                OkHttpClient client = new OkHttpClient();
                String url = "http://himanshufernando.com/App/Autolanka/" + "make.php";
                Request request = new Request.Builder().url(url).build();
                Response responses = null;

                responses = client.newCall(request).execute();

                String jsonData = responses.body().string();
                Jobject = new JSONObject(jsonData);
                Jarray = Jobject.getJSONArray("makeJosnArray");
                for (int i = 0; i < Jarray.length(); i++) {
                    object = Jarray.getJSONObject(i);
                    makeItems.add(new MakeItem(Integer.parseInt(object.getString("autolankaMakeID")),
                            object.getString("autolankaMakeName")));

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
                new SweetAlertDialog(Make.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("Server is busy please try after some time!")
                        .show();
            } else if (errorType == 3) {
                new SweetAlertDialog(Make.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Sorry")
                        .setContentText("You don't have any history!")
                        .show();
            } else {
                recyclerView.setAdapter(makeAdapter);

            }


        }

    }
    private class setAddMake extends AsyncTask<Void, Void, Void> {

        JSONObject Jobject;
        int errorType;
        String make;

        public setAddMake(String make) {
            this.make = make;
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
                String url = "http://himanshufernando.com/App/Autolanka/" + "makeInsert.php";
                RequestBody formBody = new FormBody.Builder()
                        .add("Make", make.trim()).build();
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
                new SweetAlertDialog(Make.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("Server is busy please try after some time!")
                        .show();
            } else {
                try {
                    int severresult =Integer.parseInt(Jobject.getString("Result"));
                    if(severresult==0){
                        new SweetAlertDialog(Make.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error")
                                .setContentText("Server is busy please try after some time!")
                                .show();
                    }else if(severresult==3){
                        new SweetAlertDialog(Make.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error")
                                .setContentText("Server is busy please try after some time!")
                                .show();
                    }else if(severresult==2){
                        new SweetAlertDialog(Make.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error")
                                .setContentText("This Make already exist!")
                                .show();
                    }else if(severresult==1){
                        new SweetAlertDialog(Make.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success")
                                .setContentText("Make Added successfully!")
                                .show();
                        edtMake.setText("");
                        edtMake.setError(null);
                        new getMakeFromSever().execute();
                    }else {
                        new SweetAlertDialog(Make.this, SweetAlertDialog.ERROR_TYPE)
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
    private class updateMake extends AsyncTask<Void, Void, Void> {

        JSONObject Jobject;
        int errorType;
        String make;
        int status,makeid;

        public updateMake(String make, int status, int makeid) {
            this.make = make;
            this.status = status;
            this.makeid = makeid;
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
                String url = "http://himanshufernando.com/App/Autolanka/" + "makeupdate.php";
                RequestBody formBody = new FormBody.Builder()
                        .add("Status", String.valueOf(status))
                        .add("MakeID", String.valueOf(makeid))
                        .add("Make", make.trim()).build();
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
                new SweetAlertDialog(Make.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("Server is busy please try after some time!")
                        .show();
            } else {
                try {
                    int severresult =Integer.parseInt(Jobject.getString("Result"));
                    if(severresult==0){
                        new SweetAlertDialog(Make.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error")
                                .setContentText("Server is busy please try after some time!")
                                .show();
                    }else if(severresult==3){
                        new SweetAlertDialog(Make.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error")
                                .setContentText("Server is busy please try after some time!")
                                .show();
                    }else if(severresult==1){
                        new SweetAlertDialog(Make.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success")
                                .setContentText("Make update successfully,Please Login!")
                                .show();
                        btnsattus=0;
                        btnAdd.setText("Add");
                        edtMake.setText("");
                        new getMakeFromSever().execute();
                    }else {
                        new SweetAlertDialog(Make.this, SweetAlertDialog.ERROR_TYPE)
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

    public void deleteMakeFromAdapter(final int idmake, final String make) {
        connection = new NetworkAvailable(Make.this);
        if (connection.CheckNetworkAvailable() == false) {
            new SweetAlertDialog(Make.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Connection Error")
                    .setContentText("No Internet Connection!")
                    .show();
        } else {
            new SweetAlertDialog(Make.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Warning")
                    .setContentText("Do you want to delete this ?")
                    .setConfirmText("Yes!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            new updateMake(make,1,idmake).execute();
                        }
                    })
                    .setCancelText("No!")
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                        }
                    })
                    .show();


        }

    }
    public void udateMakeFromAdapter(final int idmake, final String make) {

        updateIdmake =idmake;
        updateMakename =make;
        connection = new NetworkAvailable(Make.this);
        if (connection.CheckNetworkAvailable() == false) {
            new SweetAlertDialog(Make.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Connection Error")
                    .setContentText("No Internet Connection!")
                    .show();
        } else {

            btnsattus=1;
            btnAdd.setText("Update");
            edtMake.setText(make);
        }

    }



    @Override
    public void onBackPressed() {

        Intent i = new Intent(Make.this, Home.class);
        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation, R.anim.animation2).toBundle();
        finish();
        startActivity(i, bndlanimation);

    }


}
