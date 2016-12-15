package tkhub.project.autoonlineadmin.Layout;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.hardware.fingerprint.FingerprintManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.inject.Inject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tkhub.project.autoonlineadmin.R;
import tkhub.project.autoonlineadmin.Servies.NetworkAvailable;

/**
 * Created by Himanshu on 11/8/2016.
 */

public class Login extends Activity {

    Button login;

    Typeface tf;
    private static final String KEY_NAME = "my_key";
    EditText edtTxtUsername, editTextPassword;

    NetworkAvailable connection;
    ProgressDialog dialogProgress;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    String userName, userPassword, logUserName;
    int userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        dialogProgress = new ProgressDialog(Login.this);
        tf = Typeface.createFromAsset(Login.this.getAssets(), "Font/GOTHIC.TTF");

        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();

        login =(Button)findViewById(R.id.btnLogin);
        edtTxtUsername = (EditText) findViewById(R.id.edt_log_username);
        edtTxtUsername.setTypeface(tf);

        editTextPassword = (EditText) findViewById(R.id.editTextpass);
        editTextPassword.setTypeface(tf);




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginValidation();
             //   Intent i = new Intent(Login.this, Home.class);
              //  Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation, R.anim.animation2).toBundle();
              //  finish();
             //   startActivity(i, bndlanimation);
            }
        });



    }
    public void loginValidation() {

        connection = new NetworkAvailable(Login.this);
        userName = edtTxtUsername.getText().toString();
        userPassword = editTextPassword.getText().toString();

        if (userName.isEmpty() || userName.equals("")) {
            edtTxtUsername.setError("Empty");
        } else if (userPassword.isEmpty() || userPassword.equals("")) {
            edtTxtUsername.setError(null);
            editTextPassword.setError("Empty");
        } else if (connection.CheckNetworkAvailable() == false) {
            edtTxtUsername.setError(null);
            editTextPassword.setError(null);
            new SweetAlertDialog(Login.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Internet Connection")
                    .setContentText("No Internet Connection,try again!")
                    .show();
        } else {
            edtTxtUsername.setError(null);
            editTextPassword.setError(null);
            new getuservalidation().execute();
        }

    }


    private class getuservalidation extends AsyncTask<Void, Void, Void> {

        JSONObject Jobject;
        JSONArray Jarray ;
        JSONObject object;
        int errorType;

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
                String url = "http://himanshufernando.com/App/Autolanka/" + "userloginAdmin.php";
                RequestBody formBody = new FormBody.Builder()
                        .add("Uname",userName)
                        .add("Password",userPassword).build();
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).post(formBody).build();
                Response responses = null;

                responses = client.newCall(request).execute();

                String jsonData = responses.body().string();
                Jobject = new JSONObject(jsonData);
                if(Jobject.isNull("UserDetailsadmin")){
                    errorType=2;
                }else {
                    errorType=3;
                    Jarray = Jobject.getJSONArray("UserDetailsadmin");
                }


            } catch (IOException e) {
                e.printStackTrace();
                errorType=1;
            } catch (JSONException e) {
                e.printStackTrace();
                errorType=1;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            dialogProgress.dismiss();
            usernamePasswordValidate(errorType,Jarray);

        }

    }
    public void usernamePasswordValidate(int type, JSONArray Jarray ) {
        JSONObject object;
        try {
            if(type==3){
                for (int i = 0; i < Jarray.length(); i++) {
                    object = Jarray.getJSONObject(i);
                    logUserName =object.getString("adminName");
                    userID=Integer.parseInt(object.getString("adminId"));
                    editor.putString("key_name",logUserName); // Storing string
                    editor.putInt("key_id",userID); // Storing integer
                    editor.commit();
                    Intent intent = new Intent(Login.this, Home.class);
                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation, R.anim.animation2).toBundle();
                    finish();
                    startActivity(intent, bndlanimation);

                }
            }else if(type==2) {

                new SweetAlertDialog(Login.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Invalid login")
                        .setContentText("You have entered an invalid login,try again!")
                        .show();
            }else {
                new SweetAlertDialog(Login.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Server Busy")
                        .setContentText("This action cannot be completed because the Server is busy,try again!")
                        .show();
            }

        } catch (JSONException e) {
            e.printStackTrace();

            new SweetAlertDialog(Login.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Server Busy")
                    .setContentText("This action cannot be completed because the Server is busy,try again!")
                    .show();
        }

    }




}
