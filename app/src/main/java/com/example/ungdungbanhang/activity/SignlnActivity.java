package com.example.ungdungbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ungdungbanhang.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.util.Arrays;

public class SignlnActivity extends AppCompatActivity {
    private LinearLayout layoutSignUp;
    private EditText edtEmail,edtPassword;
    private Button btnSignIn;
    private ProgressDialog progressDialog;
    private ImageView imgFacebook,imgGoogle;
    private AccessToken accessToken;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signln);
        initUi();
        callApi();
        initListener();

    }
    private void callApi() {
        accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link");
        request.setParameters(parameters);
        request.executeAsync();
    }


    private void initListener() {
        layoutSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignlnActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSignIn();
            }
        });
        imgFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onClickFacebook();
                LoginManager.getInstance().logInWithReadPermissions(SignlnActivity.this, Arrays.asList("public_profile"));
            }
        });
    }

        private void onClickFacebook() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Intent intent = new Intent(SignlnActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                });

    }

    private void onClickSignIn() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String strEmail = edtEmail.getText().toString().trim();
        String strPassword = edtPassword.getText().toString().trim();
        progressDialog.show();
        auth.signInWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                           Intent intent = new Intent(SignlnActivity.this, MainActivity.class);
                           startActivity(intent);
                           finishAffinity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignlnActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void initUi() {
        progressDialog = new ProgressDialog(this);
        layoutSignUp = findViewById(R.id.layout_sign_up);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        imgFacebook = findViewById(R.id.imgFacebook);
        imgGoogle = findViewById(R.id.imgGoogle);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}