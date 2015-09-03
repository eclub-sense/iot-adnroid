package com.eclubprague.iot.android.weissmydeweiss.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.eclubprague.iot.android.weissmydeweiss.cloud.NewTokenByRegisterJson;
import com.eclubprague.iot.android.weissmydeweiss.cloud.TokenWrapper;
import com.eclubprague.iot.android.weissmydeweiss.cloud.UserRegistrator;

import org.restlet.resource.ClientResource;

/**
 * Created by Dat on 31.8.2015.
 */
public class UserRegistrationTask extends AsyncTask<String, Void, TokenWrapper> {

    public interface TaskDelegate {
        public void onUserRetrieveTokenTaskCompleted(TokenWrapper token);
    }

    private TaskDelegate delegate;

    public UserRegistrationTask(TaskDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    protected TokenWrapper doInBackground(String ... params) {

        try {

            ClientResource resource = new ClientResource("http://mlha-139.sin.cvut.cz:8080/user_registration");
            //resource.post( (new NewTokenJson(codes[0]))  );
            //return resource.get(TokenWrapper.class);
            UserRegistrator ur = resource.wrap(UserRegistrator.class);

            NewTokenByRegisterJson message = new NewTokenByRegisterJson(params[0], params[1]);

            Log.e("TOKSTRING", (message.toString()));
            return ur.retrieveTokenByRegister(message);
        } catch (Exception e) {
            Log.e("REGUSERTASK", e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(TokenWrapper token) {
        if(token != null) {
            try {
                Log.e("TOKENOK", token.toString());
            } catch (Exception e) {
                Log.e("TOKENOKEX", e.toString());
            }
        }

        delegate.onUserRetrieveTokenTaskCompleted(token);
    }
}

