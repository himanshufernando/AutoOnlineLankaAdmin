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
import tkhub.project.autoonlineadmin.Adapter.ModelAdapter;
import tkhub.project.autoonlineadmin.Adapter.ModelItem;
import tkhub.project.autoonlineadmin.R;
import tkhub.project.autoonlineadmin.Servies.NetworkAvailable;

/**
 * Created by Himanshu on 11/21/2016.
 */

public class Model extends Activity {


    RecyclerView recyclerView;
    ProgressDialog dialogProgress;
    ArrayList<ModelItem> modelItems = new ArrayList<ModelItem>();
    ModelAdapter modelAdapter;

    NetworkAvailable connection;
    Button btnAdd;
    EditText edtModel;
    int btnsattus =0,updateIdmodel;
    String updateModelname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_model);

        dialogProgress = new ProgressDialog(Model.this);
        recyclerView = (RecyclerView) findViewById(R.id.list_model);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        btnAdd = (Button) findViewById(R.id.button2);
        edtModel =(EditText)findViewById(R.id.editText);


        modelAdapter = new ModelAdapter(this, modelItems);
        connection = new NetworkAvailable(Model.this);
        if (connection.CheckNetworkAvailable() == false) {
            new SweetAlertDialog(Model.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Connection Error")
                    .setContentText("No Internet Connection!")
                    .show();
        } else {
            new getModelFromSever().execute();

        }


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connection = new NetworkAvailable(Model.this);
                if (connection.CheckNetworkAvailable() == false) {
                    edtModel.setError(null);
                    new SweetAlertDialog(Model.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Connection Error")
                            .setContentText("No Internet Connection!")
                            .show();
                } else if(edtModel.getText().toString().isEmpty()||edtModel.getText().toString().equals("")) {
                    edtModel.setError("Empty");
                }else {
                    if(btnsattus==0){
                        edtModel.setError(null);
                        new setAddModel(edtModel.getText().toString().trim()).execute();
                    }else {
                        new SweetAlertDialog(Model.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Warning")
                                .setContentText("Do you want to update this ?")
                                .setConfirmText("Yes!")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();

                                        edtModel.setError(null);
                                       new updateMake(edtModel.getText().toString().trim(),2,updateIdmodel).execute();

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

    private class getModelFromSever extends AsyncTask<Void, Void, Void> {

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

            modelItems.clear();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                OkHttpClient client = new OkHttpClient();
                String url = "http://himanshufernando.com/App/Autolanka/" + "model.php";
                Request request = new Request.Builder().url(url).build();
                Response responses = null;

                responses = client.newCall(request).execute();

                String jsonData = responses.body().string();
                Jobject = new JSONObject(jsonData);
                Jarray = Jobject.getJSONArray("modelJosnArray");
                for (int i = 0; i < Jarray.length(); i++) {
                    object = Jarray.getJSONObject(i);
                    modelItems.add(new ModelItem(Integer.parseInt(object.getString("autolankaModelID")),
                            object.getString("autolankaModelName")));

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
                new SweetAlertDialog(Model.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("Server is busy please try after some time!")
                        .show();
            } else if (errorType == 3) {
                new SweetAlertDialog(Model.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Sorry")
                        .setContentText("You don't have any history!")
                        .show();
            } else {
                recyclerView.setAdapter(modelAdapter);

            }


        }

    }
    private class setAddModel extends AsyncTask<Void, Void, Void> {

        JSONObject Jobject;
        int errorType;
        String model;

        public setAddModel(String model) {
            this.model = model;
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
                String url = "http://himanshufernando.com/App/Autolanka/" + "modelInsert.php";
                RequestBody formBody = new FormBody.Builder()
                        .add("Model", model.trim()).build();
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
                new SweetAlertDialog(Model.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("Server is busy please try after some time!")
                        .show();
            } else {
                try {
                    int severresult =Integer.parseInt(Jobject.getString("Result"));
                    if(severresult==0){
                        new SweetAlertDialog(Model.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error")
                                .setContentText("Server is busy please try after some time!")
                                .show();
                    }else if(severresult==3){
                        new SweetAlertDialog(Model.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error")
                                .setContentText("Server is busy please try after some time!")
                                .show();
                    }else if(severresult==2){
                        new SweetAlertDialog(Model.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error")
                                .setContentText("This Make already exist!")
                                .show();
                    }else if(severresult==1){
                        new SweetAlertDialog(Model.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success")
                                .setContentText("Make Added successfully!")
                                .show();
                        edtModel.setText("");
                        edtModel.setError(null);
                        new getModelFromSever().execute();
                    }else {
                        new SweetAlertDialog(Model.this, SweetAlertDialog.ERROR_TYPE)
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
    public void deleteModelFromAdapter(final int idmake, final String make) {
        connection = new NetworkAvailable(Model.this);
        if (connection.CheckNetworkAvailable() == false) {
            new SweetAlertDialog(Model.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Connection Error")
                    .setContentText("No Internet Connection!")
                    .show();
        } else {
            new SweetAlertDialog(Model.this, SweetAlertDialog.WARNING_TYPE)
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
    public void udateModelFromAdapter(final int idmodel, final String model) {

        updateIdmodel =idmodel;
        updateModelname =model;
        connection = new NetworkAvailable(Model.this);
        if (connection.CheckNetworkAvailable() == false) {
            new SweetAlertDialog(Model.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Connection Error")
                    .setContentText("No Internet Connection!")
                    .show();
        } else {

            btnsattus=1;
            btnAdd.setText("Update");
            edtModel.setText(model);
        }

    }
    private class updateMake extends AsyncTask<Void, Void, Void> {

        JSONObject Jobject;
        int errorType;
        String model;
        int status,modelid;

        public updateMake(String model, int status, int modelid) {
            this.model = model;
            this.status = status;
            this.modelid = modelid;
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
                String url = "http://himanshufernando.com/App/Autolanka/" + "modelUpdate.php";
                RequestBody formBody = new FormBody.Builder()
                        .add("Status", String.valueOf(status))
                        .add("ModelID", String.valueOf(modelid))
                        .add("Model", model.trim()).build();
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
                new SweetAlertDialog(Model.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("Server is busy please try after some time!")
                        .show();
            } else {
                try {
                    int severresult =Integer.parseInt(Jobject.getString("Result"));
                    if(severresult==0){
                        new SweetAlertDialog(Model.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error")
                                .setContentText("Server is busy please try after some time!")
                                .show();
                    }else if(severresult==3){
                        new SweetAlertDialog(Model.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error")
                                .setContentText("Server is busy please try after some time!")
                                .show();
                    }else if(severresult==1){
                        new SweetAlertDialog(Model.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success")
                                .setContentText("Make update successfully,Please Login!")
                                .show();
                        btnsattus=0;
                        btnAdd.setText("Add");
                        edtModel.setText("");
                        new getModelFromSever().execute();
                    }else {
                        new SweetAlertDialog(Model.this, SweetAlertDialog.ERROR_TYPE)
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
    @Override
    public void onBackPressed() {

        Intent i = new Intent(Model.this, Home.class);
        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation, R.anim.animation2).toBundle();
        finish();
        startActivity(i, bndlanimation);

    }
}
