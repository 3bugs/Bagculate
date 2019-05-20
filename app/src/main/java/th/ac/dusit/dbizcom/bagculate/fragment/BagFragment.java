package th.ac.dusit.dbizcom.bagculate.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import th.ac.dusit.dbizcom.bagculate.R;
import th.ac.dusit.dbizcom.bagculate.adapter.BagListAdapter;
import th.ac.dusit.dbizcom.bagculate.etc.Utils;
import th.ac.dusit.dbizcom.bagculate.model.Bag;
import th.ac.dusit.dbizcom.bagculate.net.ApiClient;
import th.ac.dusit.dbizcom.bagculate.net.GetBagResponse;
import th.ac.dusit.dbizcom.bagculate.net.MyRetrofitCallback;
import th.ac.dusit.dbizcom.bagculate.net.WebServices;

public class BagFragment extends Fragment {

    private BagFragmentListener mListener;

    private ListView mBagListView;

    private List<Bag> mBagList = null;
    private BagListAdapter mAdapter;

    public BagFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBagListView = view.findViewById(R.id.bag_list_view);

        RadioGroup bagTypeRadioGroup = view.findViewById(R.id.bag_type_radio_group);
        bagTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if (R.id.bag_type_0_radio_button == id) {
                    mAdapter.setShowBagType(0);
                } else {
                    mAdapter.setShowBagType(1);
                }
            }
        });

        doGetBag();
    }

    private void doGetBag() {
        Retrofit retrofit = ApiClient.getClient();
        WebServices services = retrofit.create(WebServices.class);

        Call<GetBagResponse> call = services.getBag();
        call.enqueue(new MyRetrofitCallback<>(
                getActivity(),
                null,
                null,
                new MyRetrofitCallback.MyRetrofitCallbackListener<GetBagResponse>() {
                    @Override
                    public void onSuccess(GetBagResponse responseBody) { // register สำเร็จ
                        if (getContext() == null) {
                            return;
                        }

                        mBagList = responseBody.bagList;
                        mAdapter = new BagListAdapter(
                                getContext(),
                                R.layout.item_bag,
                                mBagList,
                                0
                        );
                        mBagListView.setAdapter(mAdapter);
                        mBagListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                Bag bag = mAdapter.getBagByPosition(position);
                                if (mListener != null) {
                                    mListener.onSelectBag(bag);
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(String errorMessage) { // register ไม่สำเร็จ หรือเกิดข้อผิดพลาดอื่นๆ (เช่น ไม่มีเน็ต, server ล่ม)
                        if (getActivity() != null) {
                            Utils.showOkDialog(getActivity(), "ผิดพลาด", errorMessage);
                        }
                    }
                }
        ));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BagFragmentListener) {
            mListener = (BagFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement BagFragmentListener");
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
            //mListener.updateNavView(0);
        }
    }

    public interface BagFragmentListener {
        void updateNavView(int which);
        void onSelectBag(Bag bag);
    }
}
