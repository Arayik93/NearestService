package example.com.nearestservice.DialogBoxes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import example.com.nearestservice.R;


public class MarkersDialogBox extends Dialog {

    private String name, address, description;
    private int imageResource;
    private float rating;
    private LatLng userPosition;
    private LatLng servicePosition;
    private Activity mActivity;

    public MarkersDialogBox(LatLng servicePosition, LatLng userPosition,String name, String address, String description, int imageResource, float rating, Activity activity) {
        super(activity);
        this.name = name;
        this.address = address;
        this.description = description;
        this.imageResource = imageResource;
        this.rating = rating;
        this.mActivity = activity;
        this.userPosition = userPosition;
        this.servicePosition = servicePosition;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.markers_dialog_box);


        RatingBar mRatingBar = (RatingBar) findViewById(R.id.DialogBoxRatingBar);
        TextView nameTxt = (TextView) findViewById(R.id.DialogBoxName);
        TextView addressTxt = (TextView) findViewById(R.id.DialogBoxAddress);
        TextView descriptionTxt = (TextView) findViewById(R.id.DialogBoxDescription);
        ImageView mImage = (ImageView) findViewById(R.id.DialogBoxImage);
        Button directionButton = (Button)findViewById(R.id.buttonDirectionMarkersDialogBox);


        mRatingBar.setRating(rating);
        mImage.setImageResource(imageResource);
        nameTxt.setText(name);
        addressTxt.setText(address);
        nameTxt.setText(name);
        descriptionTxt.setText(description);

        //TODO vercnel rayting@ u tal et servicein
        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating = v;
                Toast.makeText(mActivity, ""+rating, Toast.LENGTH_SHORT).show();
                //dismiss();
            }
        });

        directionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new
                        Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" +
                        "saddr="+ servicePosition.latitude + "," + servicePosition.longitude + "&daddr=" + userPosition.latitude + "," +
                        userPosition.longitude));
                intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
                getContext().startActivity(intent);
            }
        });

    }


}
