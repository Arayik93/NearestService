package example.com.nearestservice.DialogBoxes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import example.com.nearestservice.R;


public class InternetDialogBox extends Dialog implements View.OnClickListener {

    private final String info = "Turn on Wi-Fi ?";
    private final String subInfo = "miacru  wifi-@ vor hascenern avtomat generacven axper jan";
    private TextView titleTxt;
    private TextView subTitleTxt;
    private Button okButton;
    private Button cancelButton;
    private ImageView wifiImage;


    public InternetDialogBox(Activity activity) {
        super(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_SWIPE_TO_DISMISS);
        setContentView(R.layout.internet_dialog_box);

        initialiseViews();
        setViewsParams();
    }

    private void initialiseViews() {
        okButton = (Button) findViewById(R.id.buttonOKInternetDialogBox);
        cancelButton = (Button) findViewById(R.id.buttonCancelInternetDialogBox);
        titleTxt = (TextView) findViewById(R.id.textViewTitleDialogBox);
        subTitleTxt = (TextView) findViewById(R.id.textViewSubTitleDialogBox);
        wifiImage = (ImageView) findViewById(R.id.imageInternetDialogBox);
    }

    private void setViewsParams() {
        wifiImage.setImageResource(R.drawable.wifiicon);
        titleTxt.setText(info);
        subTitleTxt.setText(subInfo);
        okButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }


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

    private void turnOnOffWiFi(boolean b) {
        WifiManager wifiManager = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(b);
    }
}