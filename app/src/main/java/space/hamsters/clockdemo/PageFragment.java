package space.hamsters.clockdemo;

import android.support.v4.app.Fragment;

/**
 * Created by hamster on 16/7/12.
 *
 * Base fragment for FragmentViewPager. Provides title of the page.
 */
public abstract class PageFragment extends Fragment {
    abstract public String getTitle();
}
