/*
 * Copyright @ 鹿文权
 */

package com.lwq.otk_lu.privatefood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.tu.loadingdialog.LoadingDialog;
import com.bumptech.glide.Glide;
import com.lwq.otk_lu.privatefood.util.SetFullTransparent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private ImageView picView;
    private Button loginButton;
    private EditText usernameEdit;
    private EditText passwdEdit;
    private CheckBox checkBox;
    private LoadingDialog.Builder loadBuilder = null;
    private LoadingDialog dialog = null;
    private String server = "https://hbe.ink:8443/PrivateFood/api?";//flag=login&username=admin&password=admin123

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SetFullTransparent set = new SetFullTransparent(getWindow());
        picView = findViewById(R.id.loginPicView);
        loginButton = findViewById(R.id.loginButton);
        usernameEdit = findViewById(R.id.loginUsernameEdit);
        passwdEdit = findViewById(R.id.loginPasswdEdit);
        checkBox = findViewById(R.id.login_checkbox);
        SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        String user = sp.getString("username", "");
        String pass = sp.getString("passwd", "");
        boolean isChecked = false;
        isChecked = sp.getBoolean("isChecked", isChecked);
        checkBox.setChecked(isChecked);
        usernameEdit.setText(user);
        passwdEdit.setText(pass);
    }

    public void buttonLoginClicked(View view) {
        final String username = usernameEdit.getText().toString();
        final String passwd = passwdEdit.getText().toString();
        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

        if (username.length() == 0 || passwd.length() == 0) {
            Toast.makeText(this, "有空项", Toast.LENGTH_SHORT).show();
        } else {
            OkHttpUtils.post().url(server)
                    .addParams("flag", "login")
                    .addParams("username", username)
                    .addParams("password", passwd).build()
                    .connTimeOut(1000)
                    .readTimeOut(1000)
                    .writeTimeOut(1000)
                    .execute(new StringCallback() {

                        @Override
                        public void onBefore(Request request, int id) {
                            super.onBefore(request, id);
                            loadBuilder = new LoadingDialog
                                    .Builder(LoginActivity.this)
                                    .setCancelable(false)
                                    .setCancelOutside(false)
                                    .setMessage("登录中");
                            dialog = loadBuilder.create();
                            dialog.show();
                        }

                        @Override
                        public void onAfter(int id) {
                            super.onAfter(id);
                            dialog.dismiss();

                        }

                        @Override
                        public void onError(Call call, Exception e, int id) {
                            builder.setTitle("登录失败");
                            builder.setMessage("网络不佳");

                            builder.setPositiveButton("OK", null);
                            builder.show();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            int result = 0;
                            String result_msg = "";
                            try {
                                JSONObject object = new JSONObject(response);
                                result = object.getInt("result");
                                result_msg = object.getString("msg");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (result == 0) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setTitle("登录失败");
                                builder.setMessage(result_msg);
                                builder.setPositiveButton("OK", null);
                                builder.show();
                            } else {
                                SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                if (checkBox.isChecked()) {
                                    editor.putString("username", username);
                                    editor.putString("passwd", passwd);
                                    editor.putBoolean("isChecked", true);
                                } else {
                                    editor.putString("username", "");
                                    editor.putString("passwd", "");
                                    editor.putBoolean("isChecked", false);
                                }
                                editor.putBoolean("isLogin", true);
                                editor.commit();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }
                        }
                    });
        }
    }
}
