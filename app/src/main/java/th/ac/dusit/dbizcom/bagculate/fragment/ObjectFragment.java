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

public class ObjectFragment extends Fragment implements View.OnClickListener {

    private ObjectFragmentListener mListener;

    public ObjectFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_object, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.shirt_image_view).setOnClickListener(this);
        view.findViewById(R.id.pants_image_view).setOnClickListener(this);
        view.findViewById(R.id.shoes_image_view).setOnClickListener(this);
        view.findViewById(R.id.thing_image_view).setOnClickListener(this);
        view.findViewById(R.id.etc_image_view).setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ObjectFragmentListener) {
            mListener = (ObjectFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ObjectFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mListener != null) {
            //mListener.updateNavView(1);
        }
    }

    @Override
    public void onClick(View view) {
        int objectType = 0;
        switch (view.getId()) {
            case R.id.shirt_image_view:
                objectType = 0;
                break;
            case R.id.pants_image_view:
                objectType = 1;
                break;
            case R.id.shoes_image_view:
                objectType = 2;
                break;
            case R.id.thing_image_view:
                objectType = 3;
                break;
            case R.id.etc_image_view:
                objectType = 4;
                break;
        }
        if (mListener != null) {
            mListener.onClickObjectTypeImage(objectType);
        }
    }

    public interface ObjectFragmentListener {
        void updateNavView(int which);

        void onClickObjectTypeImage(int objectType);
    }
}
