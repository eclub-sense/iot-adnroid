package com.eclubprague.iot.android.weissmydeweiss.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.eclubprague.iot.android.weissmydeweiss.cloud.NewTokenJson;
import com.eclubprague.iot.android.weissmydeweiss.cloud.TokenWrapper;
import com.eclubprague.iot.android.weissmydeweiss.cloud.UserRegistrator;

import org.restlet.resource.ClientResource;

import java.util.ArrayList;

/**
 * Created by Dat on 31.8.2015.
 */
public class RetrieveTokenTask extends AsyncTask<String, Void, TokenWrapper> {

    public interface TaskDelegate {
        public void onRetrieveTokenTaskCompleted(TokenWrapper token);
    }

    private TaskDelegate delegate;

    public RetrieveTokenTask(TaskDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    protected TokenWrapper doInBackground(String ... codes) {

        try {

            ClientResource resource = new ClientResource("http://mlha-139.sin.cvut.cz:8080/new_token");
            //resource.post( (new NewTokenJson(codes[0]))  );
            //return resource.get(TokenWrapper.class);
            UserRegistrator ur = resource.wrap(UserRegistrator.class);

            NewTokenJson token = new NewTokenJson(codes[0]);

            Log.e("TOKSTRING", (token.toString()));
//            Log.e("BYY", ur.retrieveToken_3(token));
//            return null;
            return ur.retrieveToken(token);
            //Log.e("TOKSTRING", (new NewTokenJson(codes[0])).toString());
            //return ur.retrieveToken_2( (new NewTokenJson(codes[0])).toString() );
        } catch (Exception e) {
            Log.e("RETRTOKENTASK", e.toString());
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

        delegate.onRetrieveTokenTaskCompleted(token);
    }
}

