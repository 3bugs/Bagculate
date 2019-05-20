package th.ac.dusit.dbizcom.bagculate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import th.ac.dusit.dbizcom.bagculate.etc.Utils;
import th.ac.dusit.dbizcom.bagculate.fragment.BagFragment;
import th.ac.dusit.dbizcom.bagculate.fragment.ObjectFragment;
import th.ac.dusit.dbizcom.bagculate.fragment.ObjectListFragment;
import th.ac.dusit.dbizcom.bagculate.model.Bag;

public class MenuActivity extends AppCompatActivity implements
        BagFragment.BagFragmentListener,
        ObjectFragment.ObjectFragmentListener,
        ObjectListFragment.ObjectListFragmentListener {

    public static final String TAG_FRAGMENT_BAG = "bag_fragment";
    public static final String TAG_FRAGMENT_OBJECT = "object_fragment";
    public static final String TAG_FRAGMENT_OBJECT_LIST = "object_fragment_list";

    BottomNavigationView mNavView;

    private Bag mSelectedBag = null;

    protected enum FragmentTransitionType {
        NONE,
        SLIDE;
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_bag:
                    loadFragment(
                            new BagFragment(),
                            TAG_FRAGMENT_BAG,
                            false,
                            FragmentTransitionType.NONE
                    );
                    return true;
                case R.id.nav_object:
                    loadFragment(
                            new ObjectFragment(),
                            TAG_FRAGMENT_OBJECT,
                            true,
                            FragmentTransitionType.SLIDE
                    );
                    return true;
                case R.id.nav_list:
                    //todo:
                    return true;
                case R.id.nav_history:
                    //todo:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mNavView = findViewById(R.id.nav_view);
        mNavView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new BagFragment(), TAG_FRAGMENT_BAG, false, FragmentTransitionType.NONE);
    }

    protected void loadFragment(Fragment fragment, String tag, boolean addToBackStack,
                                FragmentTransitionType transitionType) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (transitionType == FragmentTransitionType.SLIDE) {
            transaction.setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
            );
        }
        transaction.replace(
                R.id.fragment_container,
                fragment,
                tag
        );
        if (addToBackStack) {
            transaction.addToBackStack(null).commit();
        } else {
            transaction.commit();
        }
    }

    protected void popAllBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    protected void popBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
    }

    @Override
    public void updateNavView(int which) {
        switch (which) {
            case 0:
                //mNavView.setSelectedItemId(R.id.nav_bag);
                break;
            case 1:
                //mNavView.setSelectedItemId(R.id.nav_object);
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }

    @Override
    public void onClickObjectTypeImage(int objectType) {
        loadFragment(
                ObjectListFragment.newInstance(objectType),
                TAG_FRAGMENT_OBJECT_LIST,
                true,
                FragmentTransitionType.SLIDE
        );
    }

    @Override
    public void onSelectBag(Bag bag) {
        Utils.showShortToast(this, "เลือก " + bag.name);
        mSelectedBag = bag;
        /*loadFragment(
                new ObjectFragment(),
                TAG_FRAGMENT_OBJECT,
                true,
                FragmentTransitionType.SLIDE
        );*/
        mNavView.setSelectedItemId(R.id.nav_object);
    }
}
