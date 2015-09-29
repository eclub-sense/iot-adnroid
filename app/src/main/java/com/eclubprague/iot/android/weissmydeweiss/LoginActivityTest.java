package com.eclubprague.iot.android.weissmydeweiss;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.eclubprague.iot.android.weissmydeweiss.cloud.TokenWrapper;
import com.eclubprague.iot.android.weissmydeweiss.tasks.RetrieveTokenTask;
import com.eclubprague.iot.android.weissmydeweiss.tasks.UserRegistrationTask;
import com.eclubprague.iot.android.weissmydeweiss.ui.login.UserRegistrationDialog;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;

import java.io.IOException;

/**
 * Created by Dat on 25.8.2015.
 */
public class LoginActivityTest extends Activity implements RetrieveTokenTask.TaskDelegate,
UserRegistrationDialog.TaskDelegate, UserRegistrationTask.TaskDelegate {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void signIn(View view) {
        if (view.getId() == R.id.sign_in) {
            register = false;
            pickUserAccount();
//            startApplication(TokenWrapper.getTokenWrapperInstance(
//                    "{\"access_token\":\"fe4b40b4442000725c5229be829d5c30d2a22fa5f3f32dbabdb3079d372da07d4d5b7634d7732ed8\",\"refresh_token\":\"b33cfc5955908773485f5f87163e0712d7aa3e3c40acd6f485a12dd1a477020a290b4f5064497998\",\"token_type\":\"bearer\",\"expires_in\":3600}"
//            ));
        }
    }

    public void register(View view) {
        if (view.getId() == R.id.register) {
            register = true;
            pickUserAccount();
            //Log.e("Register", "boo");
        }
    }

    static final int REQUEST_CODE_PICK_ACCOUNT = 1000;

    private void pickUserAccount() {
        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }

    private String email;

    //private static final int REQ_SIGN_IN_REQUIRED = 55664;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_ACCOUNT && resultCode == RESULT_OK) {
            // We had to sign in - now we can finish off the token request.
            email = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            Log.e("EMAIL", email);
            if(!register) {
                new RetrieveCodeTask().execute(email);
            } else {
                new UserRegistrationDialog(this, email);
            }
        }
    }


    //private String code = null;

    private class RetrieveCodeTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String accountName = params[0];
            //String scopes = "oauth2:profile email";
            String scopes = "oauth2:server:client_id:443649814858-lvc1abj9ccnudm6l199f23ddqapgo1u3.apps.googleusercontent.com:api_scope:email";

            try {
                return GoogleAuthUtil.getToken(getApplicationContext(), accountName, scopes);
                //GoogleAuthUtil.clearToken(getApplicationContext(), code);
            } catch (IOException e) {
                Log.e("TAGI", e.getMessage());
            } catch (UserRecoverableAuthException e) {
                startActivityForResult(e.getIntent(), REQUEST_CODE_PICK_ACCOUNT);
            } catch (GoogleAuthException e) {
                Log.e("TAGIK", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String code) {
            if(code != null) {
                Log.e("CODE", code);
                onCodeRetrieved(code);
            }

        }
    }

    private class ClearTokenTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... params) {
            try {
                GoogleAuthUtil.clearToken(getApplicationContext(), params[0]);
                return 0;
            } catch (Exception e) {
                Log.e("CLEARTOKEN", e.toString());
            }
            return -1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            //continueAfterClearingToken(tokenWrapper);
        }
    }

    public void onCodeRetrieved(String code) {
        Log.e("CODERETR", code);
        new ClearTokenTask().execute(code);

        if(!register) {
            new RetrieveTokenTask(this).execute(code);
        } else {
            new UserRegistrationTask(this).execute(code, password);
        }
    }

    @Override
    public void onUserRetrieveTokenTaskCompleted(TokenWrapper tokenWrapper) {

        if(tokenWrapper == null /*||  token.getError().equals("unregistered_user")*/) {
            Toast.makeText(this, "User already registered", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Log.e("TTOOKKEENN", tokenWrapper.toString());
            startApplication(tokenWrapper);
        } catch (Exception e) {
            Log.e("NULLTOKEN", e.toString());
        }
    }

    @Override
    public void onRetrieveTokenTaskCompleted(TokenWrapper tokenWrapper) {

        if(tokenWrapper == null /*||  token.getError().equals("unregistered_user")*/) {
            Toast.makeText(this, "User not yet registered", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.e("TOKENNN", tokenWrapper.toString());

        startApplication(tokenWrapper);

        //new ClearTokenTask(token).execute(code);
    }

//    private void continueAfterClearingToken(TokenWrapper tokenWrapper) {
//        if(tokenWrapper == null /*||  token.getError().equals("unregistered_user")*/) {
//
//            //TODO register pop-up
//            new UserRegistrationDialog(this, email);
//            return;
//        }
//
//        Log.e("TOKENNN", tokenWrapper.toString());
//
//        startApplication(tokenWrapper);
//    }

    private boolean register = false;
    private String password;

    @Override
    public void onUserRegistrationDialogSubmitted(String password) {
        //TODO register user
        //register = true;
        this.password = password;
        new RetrieveCodeTask().execute(email);
    }


    public void startApplication(TokenWrapper token) {
        Intent intent = new Intent(LoginActivityTest.this, MainActivity.class);
        intent.putExtra("token", token.toString());
        intent.putExtra("email", email);
        startActivity(intent);
        this.finish();
    }
}