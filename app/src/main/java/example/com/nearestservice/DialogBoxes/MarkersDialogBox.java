package example.com.nearestservice.DialogBoxes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;

import example.com.nearestservice.Activities.MainActivity;
import example.com.nearestservice.R;
import example.com.nearestservice.Services.FavoriteService;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class MarkersDialogBox extends Dialog implements View.OnClickListener{

    private String name, address, description;
    private int imageResource;
    private float rating;
    private LatLng userPosition;
    private LatLng servicePosition;
    private Activity mActivity;
    private double distance;

    public MarkersDialogBox(LatLng servicePosition, LatLng userPosition,String name, String address,
                            String description, int imageResource, float rating, Activity activity,
                            double distance) {
        super(activity);
        this.name = name;
        this.address = address;
        this.description = description;
        this.imageResource = imageResource;
        this.rating = rating;
        this.mActivity = activity;
        this.userPosition = userPosition;
        this.servicePosition = servicePosition;
        this.distance = distance;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            requestWindowFeature(Window.FEATURE_SWIPE_TO_DISMISS);
        }else {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        setContentView(R.layout.markers_dialog_box);

        RatingBar mRatingBar = (RatingBar) findViewById(R.id.DialogBoxRatingBar);
        TextView nameTxt = (TextView) findViewById(R.id.DialogBoxName);
        TextView addressTxt = (TextView) findViewById(R.id.DialogBoxAddress);
        TextView descriptionTxt = (TextView) findViewById(R.id.DialogBoxDescription);
        TextView distanceTxt = (TextView) findViewById(R.id.DialogBoxDistance);
        ImageView mImage = (ImageView) findViewById(R.id.DialogBoxImage);
        Button directionButton = (Button)findViewById(R.id.buttonDirectionMarkersDialogBox);
        final CheckBox favoriteCheckBox = (CheckBox)findViewById(R.id.DialogBoxCheckBox);

        LayerDrawable stars = (LayerDrawable) mRatingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        mRatingBar.setRating(rating);

        mImage.setImageResource(imageResource);
        nameTxt.setText("name: "+name);
        addressTxt.setText("address: "+address);
        descriptionTxt.setText("Description: "+description);

        distance =Double.parseDouble(new DecimalFormat("##.####").format(distance));
        distanceTxt.setText("Distance: "+String.valueOf(distance*100000)+" meters");

        directionButton.setOnClickListener(this);
        favoriteCheckBox.setOnClickListener(this);
        /*favoriteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                RealmResults rel = realm.where(FavoriteService.class).findAll();
                if(b){
                    FavoriteService service = new FavoriteService();
                    service.setName(name);
                    service.setDescription(description);
                    service.setAddress(address);
                    service.setLatitude(servicePosition.latitude);
                    service.setLongitude(servicePosition.longitude);
                    service.setRating(String.valueOf(rating));
                    service.setImageResource(R.drawable.favoriteheartbutton);

                    int id = 1;
                    if (rel.size() != 0) {
                        FavoriteService serviceFromDB = (FavoriteService) rel.last();
                        id = serviceFromDB.getId() + 1;
                    }
                    favoriteId = id-1;
                    service.setId(id);
                    realm.copyToRealm(service);
                    realm.commitTransaction();

                }else{
                    rel.remove(id);
                }
            }
        });*/

        //TODO vercnel rayting@ u tal et servicein
        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating = v;
                Toast.makeText(mActivity, ""+rating, Toast.LENGTH_SHORT).show();
                //dismiss();
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonDirectionMarkersDialogBox:
                final Intent intent = new
                        Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" +
                        "saddr="+ servicePosition.latitude + "," + servicePosition.longitude + "&daddr=" + userPosition.latitude + "," +
                        userPosition.longitude));
                intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
                getContext().startActivity(intent);
                break;
            case R.id.DialogBoxCheckBox:
                CheckBox checkBox = (CheckBox) view;
                if(checkBox.isChecked()){
                    Toast.makeText(getContext(), name + " added to favorite list", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), name + " removed from favorite list", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }


}
