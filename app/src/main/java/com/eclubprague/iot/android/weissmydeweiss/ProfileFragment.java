package com.eclubprague.iot.android.weissmydeweiss;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import fr.tkeunebr.gravatar.Gravatar;

/**
 * Created by Dat on 29.9.2015.
 */
public class ProfileFragment extends Fragment {

    ImageView user_avatar;
    TextView user_email;
    TextView user_first_name;
    TextView user_last_name;
    TextView user_location;
    View rootView;

    private int mAvatarImageViewPixelSize;
    private String email;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        mAvatarImageViewPixelSize = getResources().getDimensionPixelSize(R.dimen.avatar_image_view_size);

        user_avatar = (ImageView) rootView.findViewById(R.id.user_avatar);
        user_email = (TextView) rootView.findViewById(R.id.user_email);
        user_first_name = (TextView) rootView.findViewById(R.id.user_first_name);
        user_last_name = (TextView) rootView.findViewById(R.id.user_last_name);
        user_location = (TextView) rootView.findViewById(R.id.user_location);

        new GetAvatarTask().execute();

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(2);
    }

    public void setEmail(String email) {
        this.email = email;
    }


    private class GetAvatarTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            String gravatarUrl = Gravatar.init().with(email).force404().size(mAvatarImageViewPixelSize).build();

            Log.e("GURL", gravatarUrl);

            Picasso.with(getActivity())
                    .load(gravatarUrl)
                    .placeholder(R.drawable.ic_contact_picture)
                    .error(R.drawable.ic_contact_picture)
                    .into((ImageView) rootView.findViewById(R.id.user_avatar));

            rootView.invalidate();
        }
    }
}
