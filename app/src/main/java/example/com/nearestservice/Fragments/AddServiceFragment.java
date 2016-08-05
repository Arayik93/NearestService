package example.com.nearestservice.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import example.com.nearestservice.Activities.MainActivity;
import example.com.nearestservice.R;
import example.com.nearestservice.Service;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddServiceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddServiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddServiceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    Service s;
    private OnFragmentInteractionListener mOnFragmentInteractionListener;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AddServiceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddServiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddServiceFragment newInstance(String param1, String param2) {
        AddServiceFragment fragment = new AddServiceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        try {
            mOnFragmentInteractionListener = (OnFragmentInteractionListener) getActivity();
        } catch (ClassCastException e) {
            throw new IllegalStateException("activiti ... ");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText serviceName_edt = (EditText) view.findViewById(R.id.AddFragmentServiceName);
        final Spinner serviceCategory_edt = (Spinner) view.findViewById(R.id.AddFragmentServiceSpinner);
        final EditText serviceDescription_edt = (EditText) view.findViewById(R.id.AddFragmentServiceDescriptione);
        final EditText serviceAddress_edt = (EditText) view.findViewById(R.id.AddFragmentServiceAddres);


        view.findViewById(R.id.button_cancel_activityAddService).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnFragmentInteractionListener.cancelButonOnAddFragmentPressed();

            }
        });

        view.findViewById(R.id.button_save_activityAddService).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Realm realm = MainActivity.realm;
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();

                RealmResults rel = realm.where(Service.class).findAll();

                int id = 1;

                if (rel.size() != 0) {
                    Service s = (Service) rel.last();
                    id = s.getId() + 1;
                }


                s = new Service();
                s.setId(id);

                s.setRating(0);
                s.setName(serviceName_edt.getText().toString());
                s.setAddress(serviceAddress_edt.getText().toString());
                s.setCategory(serviceCategory_edt.getSelectedItem().toString());
                s.setDescription(serviceDescription_edt.getText().toString());


                realm.copyToRealm(s);

                //realm.copyToRealmOrUpdate(u);//esi karanq nuyn id-n tanq update anenq

                realm.commitTransaction();
                mOnFragmentInteractionListener.addButonOnAddFragmentPressed();

            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        //void onFragmentInteraction(Uri uri);
        void cancelButonOnAddFragmentPressed();

        void addButonOnAddFragmentPressed();
    }
}
