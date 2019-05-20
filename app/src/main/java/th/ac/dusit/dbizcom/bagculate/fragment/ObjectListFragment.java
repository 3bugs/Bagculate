package th.ac.dusit.dbizcom.bagculate.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import th.ac.dusit.dbizcom.bagculate.R;

public class ObjectListFragment extends Fragment {

    private static final String ARG_OBJECT_TYPE = "object_type";

    private int mObjectType;

    private ObjectListFragmentListener mListener;

    public ObjectListFragment() {
        // Required empty public constructor
    }

    public static ObjectListFragment newInstance(int objectType) {
        ObjectListFragment fragment = new ObjectListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_OBJECT_TYPE, objectType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mObjectType = getArguments().getInt(ARG_OBJECT_TYPE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_object_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ObjectListFragmentListener) {
            mListener = (ObjectListFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ObjectListFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface ObjectListFragmentListener {
    }
}
