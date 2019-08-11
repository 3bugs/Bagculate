package th.ac.dusit.dbizcom.bagculate.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import th.ac.dusit.dbizcom.bagculate.R;
import th.ac.dusit.dbizcom.bagculate.etc.Utils;
import th.ac.dusit.dbizcom.bagculate.model.Object;
import th.ac.dusit.dbizcom.bagculate.model.ObjectType;
import th.ac.dusit.dbizcom.bagculate.net.ApiClient;
import th.ac.dusit.dbizcom.bagculate.net.GetObjectResponse;
import th.ac.dusit.dbizcom.bagculate.net.MyRetrofitCallback;
import th.ac.dusit.dbizcom.bagculate.net.WebServices;

public class ObjectFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = ObjectFragment.class.getName();

    private ObjectFragmentListener mListener;

    private Map<String, ObjectType> mObjectTypeMap = null;

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

        if (mObjectTypeMap == null) {
            doGetObject();
        } else {
            //
        }
    }

    private void doGetObject() {
        Retrofit retrofit = ApiClient.getClient();
        WebServices services = retrofit.create(WebServices.class);

        Call<GetObjectResponse> call = services.getObject();
        call.enqueue(new MyRetrofitCallback<>(
                getActivity(),
                null,
                null,
                new MyRetrofitCallback.MyRetrofitCallbackListener<GetObjectResponse>() {
                    @Override
                    public void onSuccess(GetObjectResponse responseBody) {
                        if (getContext() == null) {
                            return;
                        }

                        List<Object> objectList = responseBody.objectList;
                        populateObjectTypeList(objectList);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        if (getActivity() != null) {
                            Utils.showOkDialog(getActivity(), "ผิดพลาด", errorMessage);
                        }
                    }
                }
        ));
    }

    private void populateObjectTypeList(List<Object> objectList) {
        mObjectTypeMap = new HashMap<>();

        String[] objectType = {
                ObjectType.TYPE_SHIRT,
                ObjectType.TYPE_PANTS,
                ObjectType.TYPE_SHOES,
                ObjectType.TYPE_THING,
                ObjectType.TYPE_ETC,
        };
        for (String type : objectType) {
            mObjectTypeMap.put(type, new ObjectType(type));
        }

        try {
            for (Object object : objectList) {
                mObjectTypeMap.get(object.type).objectList.add(object);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            if (getActivity() != null) {
                Utils.showOkDialog(
                        getActivity(),
                        "ผิดพลาด",
                        "เกิดข้อผิดพลาดในการสร้างตัวแปรข้อมูล"
                );
            }
            return;
        }

        Iterator iterator = mObjectTypeMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Log.i(TAG, "##### ประเภทสิ่งของ: " + entry.getKey());
            List<Object> list = ((ObjectType) entry.getValue()).objectList;
            for (Object object : list) {
                Log.i(TAG, "- " + object.name);
            }
        }
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
            mListener.setTitle("เลือกประเภทสิ่งของ");
            //mListener.updateNavView(1);
        }
    }

    @Override
    public void onClick(View view) {
        String type = null;
        switch (view.getId()) {
            case R.id.shirt_image_view:
                type = ObjectType.TYPE_SHIRT;
                break;
            case R.id.pants_image_view:
                type = ObjectType.TYPE_PANTS;
                break;
            case R.id.shoes_image_view:
                type = ObjectType.TYPE_SHOES;
                break;
            case R.id.thing_image_view:
                type = ObjectType.TYPE_THING;
                break;
            case R.id.etc_image_view:
                type = ObjectType.TYPE_ETC;
                break;
        }
        if (mListener != null) {
            mListener.onClickObjectTypeImage(mObjectTypeMap.get(type));
        }
    }

    public interface ObjectFragmentListener {
        void setTitle(String title);

        void updateNavView(int which);

        void onClickObjectTypeImage(ObjectType objectType);
    }
}
