package th.ac.dusit.dbizcom.bagculate.fragment;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;
import java.util.Locale;

import th.ac.dusit.dbizcom.bagculate.R;
import th.ac.dusit.dbizcom.bagculate.model.Object;
import th.ac.dusit.dbizcom.bagculate.model.ObjectType;

public class ObjectListFragment extends Fragment implements View.OnDragListener {

    private static final String TAG = ObjectListFragment.class.getName();
    private static final String ARG_OBJECT_TYPE_JSON = "object_type_json";

    private ObjectType mObjectType;
    private ObjectListAdapter mAdapter;

    private ObjectListFragmentListener mListener;

    private FloatingActionButton mBagFab;

    public ObjectListFragment() {
        // Required empty public constructor
    }

    public static ObjectListFragment newInstance(ObjectType objectType) {
        ObjectListFragment fragment = new ObjectListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_OBJECT_TYPE_JSON, new Gson().toJson(objectType));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String objectTypeJson = getArguments().getString(ARG_OBJECT_TYPE_JSON);
            mObjectType = new Gson().fromJson(objectTypeJson, ObjectType.class);
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

        if (mListener != null) {
            mListener.setTitle(mObjectType.name);
        }

        if (getContext() != null) {
            RecyclerView objectRecyclerView = view.findViewById(R.id.object_recycler_view);
            mAdapter = new ObjectListAdapter(
                    getContext(),
                    mObjectType,
                    mListener.getObjectListInBag(),
                    mListener
            );
            objectRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            objectRecyclerView.addItemDecoration(new SpacingDecoration(getContext()));
            objectRecyclerView.setAdapter(mAdapter);
        }

        mBagFab = view.findViewById(R.id.bag_fab);
        mBagFab.setOnDragListener(this);
        mBagFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClickBagFab();
                }
            }
        });
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

    @Override
    public boolean onDrag(View v, DragEvent event) {
        // Defines a variable to store the action type for the incoming event
        int action = event.getAction();

        // Handles each of the expected events
        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                // Determines if this View can accept the dragged data
                /*if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    // applies a blue color tint to the View to indicate that it can accept the data
                    v.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
                    // Invalidate the view to force a redraw in the new tint
                    v.invalidate();
                    // returns true to indicate that the View can accept the dragged data.
                    return true;
                }*/
                // Returns false. During the current drag and drop operation, this View will
                // not receive events again until ACTION_DRAG_ENDED is sent.
                return true;

            case DragEvent.ACTION_DRAG_ENTERED:
                // Applies a YELLOW or any color tint to the View. Return true; the return value is ignored.
                /*v.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);
                // Invalidate the view to force a redraw in the new tint
                v.invalidate();
                return true;*/
                v.getBackground().setColorFilter(Color.parseColor("#2DCCFF"), PorterDuff.Mode.SRC_IN);
                v.invalidate();
                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                // Ignore the event
                return true;

            case DragEvent.ACTION_DRAG_EXITED:
                /*// Re-sets the color tint to blue, if you had set the BLUE color or any color in ACTION_DRAG_STARTED. Returns true; the return value is ignored.
                v.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
                //If u had not provided any color in ACTION_DRAG_STARTED then clear color filter.
                v.getBackground().clearColorFilter();
                // Invalidate the view to force a redraw in the new tint
                v.invalidate();*/
                v.getBackground().clearColorFilter();
                v.invalidate();
                return true;

            case DragEvent.ACTION_DROP:
                // Gets the item containing the dragged data
                ClipData.Item item = event.getClipData().getItemAt(0);
                // Gets the text data from the item.
                String objectJson = item.getText().toString();

                //Utils.showOkDialog(getActivity(), "test", objectJson);

                if (mListener != null) {
                    Object objectDropped = new Gson().fromJson(objectJson, Object.class);
                    for (Object o : mObjectType.objectList) {
                        if (o.id == objectDropped.id) {
                            o.increaseCount();
                            mListener.addObjectIntoBag(o);
                            mAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }
                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                v.getBackground().clearColorFilter();
                v.invalidate();
                return true;

            // An unknown action type was received.
            default:
                Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                break;
        }
        return false;
    }

    public interface ObjectListFragmentListener {
        void setTitle(String title);

        List<Object> getObjectListInBag();

        void addObjectIntoBag(Object object);

        void onClickBagFab();
    }

    private static class ObjectListAdapter extends RecyclerView.Adapter<ObjectListAdapter.ObjectViewHolder> {

        private final Context mContext;
        private final ObjectType mObjectType;
        private final List<Object> mObjectListInBag;
        private final ObjectListFragmentListener mListener;

        ObjectListAdapter(Context context, ObjectType objectType, List<Object> objectListInBag, ObjectListFragmentListener listener) {
            mContext = context;
            mObjectType = objectType;
            mObjectListInBag = objectListInBag;
            mListener = listener;
        }

        @NonNull
        @Override
        public ObjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_object, parent, false
            );
            return new ObjectViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ObjectViewHolder holder, int position) {
            final Object object = mObjectType.objectList.get(position);

            holder.mObject = object;
            holder.mObjectNameTextView.setText(object.name);
            String weightText = String.format(Locale.getDefault(), "น้ำหนัก %d กรัม", object.weight);
            holder.mObjectWeightTextView.setText(weightText);

            for (Object o : mObjectListInBag) {
                if (o.id == object.id) {
                    object.setCount(o.getCount());
                }
            }

            holder.mBadgeTextView.setText(String.valueOf(object.getCount()));
            if (object.getCount() > 0) {
                holder.mBadgeTextView.setVisibility(View.VISIBLE);
            } else {
                holder.mBadgeTextView.setVisibility(View.GONE);
            }

            int imageResource = -1;
            switch (mObjectType.type) {
                case ObjectType.TYPE_SHIRT:
                    imageResource = R.drawable.ic_type_shirt;
                    break;
                case ObjectType.TYPE_PANTS:
                    imageResource = R.drawable.ic_type_pants;
                    break;
                case ObjectType.TYPE_SHOES:
                    imageResource = R.drawable.ic_type_shoes;
                    break;
                case ObjectType.TYPE_THING:
                    imageResource = R.drawable.ic_type_thing;
                    break;
                case ObjectType.TYPE_ETC:
                    imageResource = R.drawable.ic_type_etc;
                    break;
            }
            if (imageResource != -1) {
                holder.mObjectImageView.setImageResource(imageResource);
            }
        }

        @Override
        public int getItemCount() {
            return mObjectType.objectList.size();
        }

        class ObjectViewHolder extends RecyclerView.ViewHolder {

            private final View mRootView;
            private final TextView mObjectNameTextView;
            private final TextView mObjectWeightTextView;
            private final ImageView mObjectImageView;
            private final TextView mBadgeTextView;

            private Object mObject;

            ObjectViewHolder(View itemView) {
                super(itemView);

                mRootView = itemView;
                mObjectNameTextView = itemView.findViewById(R.id.object_name_text_view);
                mObjectWeightTextView = itemView.findViewById(R.id.object_weight_text_view);
                mObjectImageView = itemView.findViewById(R.id.object_image_view);
                mBadgeTextView = itemView.findViewById(R.id.badge_text_view);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Utils.showOkDialog(mContext, "test", mObject.name);
                    }
                });
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        // Create a new ClipData.Item from the View object's tag
                        ClipData.Item item = new ClipData.Item(new Gson().toJson(mObject));

                        // Create a new ClipData using the tag as a label, the plain text MIME type, and
                        // the already-created item. This will create a new ClipDescription object within the
                        // ClipData, and set its MIME type entry to "text/plain"
                        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                        ClipData data = new ClipData("", mimeTypes, item);

                        // Instantiates the drag shadow builder.
                        View.DragShadowBuilder dragShadow = new View.DragShadowBuilder(v);

                        // Starts the drag
                        v.startDrag(data       // data to be dragged
                                , dragShadow   // drag shadow
                                , v            // local data about the drag and drop operation
                                , 0            // flags set to 0 because not using currently
                        );
                        return true;
                    }
                });
            }
        }
    }

    public class SpacingDecoration extends RecyclerView.ItemDecoration {

        private final static int MARGIN_TOP = 12;
        private final static int MARGIN_BOTTOM = 100;
        private final int mMarginTop, mMarginBottom;

        SpacingDecoration(@NonNull Context context) {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            mMarginTop = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    MARGIN_TOP,
                    metrics
            );
            mMarginBottom = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    MARGIN_BOTTOM,
                    metrics
            );
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                   @NonNull RecyclerView parent,
                                   @NonNull RecyclerView.State state) {
            final int itemPosition = parent.getChildAdapterPosition(view);
            if (itemPosition == RecyclerView.NO_POSITION) {
                return;
            }
            if (itemPosition == 0) {
                outRect.top = mMarginTop;
            }
            final RecyclerView.Adapter adapter = parent.getAdapter();
            if ((adapter != null) && (itemPosition == adapter.getItemCount() - 1)) {
                outRect.bottom = mMarginBottom;
            }
        }
    }
}
