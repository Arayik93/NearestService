package example.com.nearestservice.dialog_boxes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import example.com.nearestservice.R;

public class GPS_And_WiFi_Dialog_Box extends Dialog implements View.OnClickListener {

    private TextView titleTxt;
    private TextView subTitleTxt;
    private Button okButton;
    private Button cancelButton;
    private ImageView dialogImage;

    private String info;

    /**
     *
     * @param activity add your activity
     * @param info Add String "GPS" as a second param in order to see GPS dialog box,
     * or add String "WIFI" in order to see WIFI dialog box
     *
     * */
    public GPS_And_WiFi_Dialog_Box(Activity activity, String info) {
        super(activity);
        this.info = info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            requestWindowFeature(Window.FEATURE_SWIPE_TO_DISMISS);
        }else{
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        setContentView(R.layout.gps_and_wifi_dialog_box);

        initialiseViews();
        setViewsParams();
    }

    private void initialiseViews() {
        okButton = (Button) findViewById(R.id.buttonOKInternetDialogBox);
        cancelButton = (Button) findViewById(R.id.buttonCancelInternetDialogBox);
        dialogImage = (ImageView) findViewById(R.id.imageDialogBox);
        titleTxt = (TextView) findViewById(R.id.textViewTitleDialogBox);
        subTitleTxt = (TextView) findViewById(R.id.textViewSubTitleDialogBox);
    }

    private void setViewsParams() {

        final int imageResourceGPS = R.drawable.gpsbig;
        final int imageResourceWifi = R.drawable.wifiicon;
        final String titleWifi = "Turn on Wi-Fi ?";
        final String titleGPS = "Do you want open GPS setting?";
        final String subTitleGPS = "miacru  GPS-@ vor koordinatnerd imanam";
        final String subTitleWiFi = "miacru  wifi-@ vor hascenern avtomat generacven axper jan";

        cancelButton.setOnClickListener(this);
        if(info.equals("GPS")){
            dialogImage.setImageResource(imageResourceGPS);
            titleTxt.setText(titleGPS);
            subTitleTxt.setText(subTitleGPS);
            okButton.setOnClickListener(this);
        }else{
            dialogImage.setImageResource(imageResourceWifi);
            titleTxt.setText(titleWifi);
            subTitleTxt.setText(subTitleWiFi);
            okButton.setOnClickListener(wifiListener);
        }
    }

    View.OnClickListener wifiListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int viewId = view.getId();
            switch (viewId) {
                case R.id.buttonOKInternetDialogBox:
                    turnOnOffWiFi(true);
                    dismiss();
                    break;
                case R.id.buttonCancelInternetDialogBox:
                    dismiss();
                default:
                    break;
            }
        }
    };


    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.buttonOKInternetDialogBox:
                turnOnGPS();
                dismiss();
                break;
            case R.id.buttonCancelInternetDialogBox:
                dismiss();
            default:
                break;
        }
    }

    private void turnOnOffWiFi(boolean b) {
        WifiManager wifiManager = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(b);
    }

    //TODO miacneluc heto pti chshti ete miacac a noric kanchi  mtik servicnern pntrox funkcian,,, ogtvel LocationManager-ic
    private void turnOnGPS() {
        getContext().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        dismiss();
    }



     /*final LocationManager manager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                Toast.makeText(getContext(), "GPS is disabled!", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getContext(), "GPS is enabled!", Toast.LENGTH_LONG).show();*/

}
