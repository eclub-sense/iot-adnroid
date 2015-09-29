package com.eclubprague.iot.android.weissmydeweiss;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestBuilder;

import org.restlet.resource.ClientResource;
import org.restlet.resource.Get;

import java.util.List;

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
        ((MainActivity) activity).onSectionAttached(3);
    }

    public void setEmail(String email) {
        this.email = email;
    }


    private class GetAvatarTask extends AsyncTask<Void, Void, GravatarUser> {

        RequestBuilder builder;

        @Override
        protected GravatarUser doInBackground(Void... params) {

            try {

                String imageUrl = Gravatar.init().with(email).force404().size(mAvatarImageViewPixelSize).build();

                Log.e("GURL", imageUrl);

                builder = Picasso.with(getActivity()).load(imageUrl);

                String userUrl = "";

                int no_slashes = 0;
                for (int i = 0; i < imageUrl.length(); i++) {
                    if (imageUrl.charAt(i) == '?') break;
                    if (imageUrl.charAt(i) == '/') {
                        no_slashes++;
                    }
                    if (no_slashes <= 3) continue;
                    userUrl += imageUrl.charAt(i);

                }

                Log.e("UURL", userUrl);

                //ClientResource resource = new ClientResource("http://en.gravatar.com/bffa8cdcccb527156e2bd502afdf2d19.json");
                ClientResource resource = new ClientResource("http://en.gravatar.com" + userUrl + ".json");
                GravatarInterface gr = resource.wrap(GravatarInterface.class);
                return gr.getGravatarEntry().getEntry().get(0);
            }catch (Exception e) {
                Log.e("URLFAILED", e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(GravatarUser user) {

            builder.placeholder(R.drawable.ic_contact_picture)
                    .error(R.drawable.ic_contact_picture)
                    .into(user_avatar);

            if(user == null) {
                Toast.makeText(ProfileFragment.this.getActivity(), "Cannot fetch profile", Toast.LENGTH_SHORT).show();
                return;
            }

            user_email.setText("Email: " + email);
            user_first_name.setText("First name: " + user.getName().getGivenName());
            user_last_name.setText("Last name: " + user.getName().getFamilyName());
            user_location.setText("Location: " + user.getCurrentLocation());

            rootView.invalidate();
        }
    }


    private class Name {
        private String givenName;
        private String familyName;

        public Name(String givenName, String familyName) {
            this.givenName = givenName;
            this.familyName = familyName;
        }

        public String getGivenName() {
            return givenName;
        }

        public String getFamilyName() {
            return familyName;
        }
    }

    private class GravatarUser {
        private Name name;
        private String currentLocation;

        public GravatarUser(Name name, String currentLocation) {
            this.name = name;
            this.currentLocation = currentLocation;
        }

        public Name getName() {
            return name;
        }

        public String getCurrentLocation() {
            return currentLocation;
        }
    }

    private class GravatarEntry {
        private List<GravatarUser> entry;

        public GravatarEntry(List<GravatarUser> entry) {
            this.entry = entry;
        }

        public List<GravatarUser> getEntry() {
            return entry;
        }
    }


    private interface GravatarInterface {

        @Get("json")
        public GravatarEntry getGravatarEntry();
    }

}
