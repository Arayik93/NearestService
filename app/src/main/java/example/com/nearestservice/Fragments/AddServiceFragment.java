package example.com.nearestservice.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import example.com.nearestservice.R;

public class AddServiceFragment extends Fragment {

    private OnFragmentInteractionListener mOnFragmentInteractionListener;
    private static View mView;
    private Spinner mSpinner;

    public AddServiceFragment() {
    }

    public static AddServiceFragment newInstance(String param1, String param2) {
        AddServiceFragment fragment = new AddServiceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mOnFragmentInteractionListener = (OnFragmentInteractionListener) getActivity();
        } catch (ClassCastException e) {
            throw new IllegalStateException("activiti ... ");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mView = view;
        mSpinner = (Spinner) view.findViewById(R.id.AddFragmentServiceSpinner);
        final TextView name_edt = ((EditText) view.findViewById(R.id.AddFragmentServiceName));
        final TextView address_edt = ((EditText) view.findViewById(R.id.AddFragmentServiceAddress));
        final TextView description_edt = ((EditText) view.findViewById(R.id.AddFragmentServiceDescription));

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                mSpinner.performClick();
            }
        }, 1);

        view.findViewById(R.id.button_save_activityAddService).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnFragmentInteractionListener.addButtonOnAddFragmentPressed(
                        mSpinner.getSelectedItemPosition(), new String[]{
                                name_edt.getText().toString(),
                                address_edt.getText().toString(),
                                description_edt.getText().toString()});
            }
        });

        view.findViewById(R.id.button_cancel_activityAddService).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mOnFragmentInteractionListener.cancelButtonOnAddFragmentPressed();
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mOnFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnFragmentInteractionListener = null;
    }

    public static void addressDetected(String[] address) {

        EditText address_edt = (EditText) mView.findViewById(R.id.AddFragmentServiceAddress);
        address_edt.setText(address[3] + " " + address[2] + "\n" + address[1] + " " + address[0]);
    }


    public interface OnFragmentInteractionListener {

        void cancelButtonOnAddFragmentPressed();

        void addButtonOnAddFragmentPressed(int serviceIndex, String[] params);
    }
}