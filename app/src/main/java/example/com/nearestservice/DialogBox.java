package example.com.nearestservice;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


public class DialogBox extends Dialog{

    private String name, address, description;
    private int imageResource;
    private float rating;
    public Activity mActivity;

    public DialogBox(String name, String address, String description, int imageResource, float rating, Activity activity) {
        super(activity);
        this.name = name;
        this.address = address;
        this.description = description;
        this.imageResource = imageResource;
        this.rating = rating;
        this.mActivity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialo_box_l);


        RatingBar mRatingBar = (RatingBar) findViewById(R.id.DialogBoxRatingBar);
        TextView nameTxt = (TextView) findViewById(R.id.DialogBoxName);
        TextView addressTxt = (TextView) findViewById(R.id.DialogBoxAddress);
        TextView descriptionTxt = (TextView) findViewById(R.id.DialogBoxDescription);
        ImageView mImage = (ImageView) findViewById(R.id.DialogBoxImage);


        mRatingBar.setRating(rating);
        mImage.setImageResource(imageResource);
        nameTxt.setText(name);
        addressTxt.setText(address);
        nameTxt.setText(name);
        descriptionTxt.setText(description);

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating = v;
                Toast.makeText(mActivity, ""+rating, Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

    }


}
