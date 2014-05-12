package tw.jimytc.quickreturngridview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private QuickReturnGridView mGridView;
        private View mQuickReturnView;
        PlaceholderAdapter mAdapter;

        private int mCachedVerticalScrollRange;
        private int mQuickReturnHeight;

        private static final int STATE_ONSCREEN = 0;
        private static final int STATE_OFFSCREEN = 1;
        private static final int STATE_RETURNING = 2;
        private int mState = STATE_ONSCREEN;
        private int mScrollY;
        private int mMinRawY = 0;

        private TranslateAnimation anim;

        public PlaceholderFragment() {
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            String[] data = getResources().getStringArray(R.array.gridview_items);
            mAdapter = new PlaceholderAdapter(getActivity(), R.layout.grid_item, R.id.content_text,
                    data);
            mGridView.setAdapter(mAdapter);

            mGridView.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            mQuickReturnHeight = mQuickReturnView.getHeight();
                            mGridView.computeScrollY();
                            mCachedVerticalScrollRange = mGridView.getListHeight();
                        }
                    });

            mGridView.setOnScrollListener(new OnScrollListener() {
                @SuppressLint("NewApi")
                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                        int visibleItemCount, int totalItemCount) {

                    mScrollY = 0;
                    int translationY = 0;

                    if (mGridView.scrollYIsComputed()) {
                        mScrollY = mGridView.getComputedScrollY();
                    }

                    int rawY = mGridView.getTop()
                            - Math.min(mCachedVerticalScrollRange - mGridView.getHeight(), mScrollY);

                    switch (mState) {
                    case STATE_OFFSCREEN:
                        if (rawY <= mMinRawY) {
                            mMinRawY = rawY;
                        } else {
                            mState = STATE_RETURNING;
                        }
                        translationY = rawY;
                        break;

                    case STATE_ONSCREEN:
                        if (rawY < -mQuickReturnHeight) {
                            mState = STATE_OFFSCREEN;
                            mMinRawY = rawY;
                        }
                        translationY = rawY;
                        break;

                    case STATE_RETURNING:
                        translationY = (rawY - mMinRawY) - mQuickReturnHeight;
                        if (translationY > 0) {
                            translationY = 0;
                            mMinRawY = rawY - mQuickReturnHeight;
                        }

                        if (rawY > 0) {
                            mState = STATE_ONSCREEN;
                            translationY = rawY;
                        }

                        if (translationY < -mQuickReturnHeight) {
                            mState = STATE_OFFSCREEN;
                            mMinRawY = rawY;
                        }
                        break;
                    }

                    /** this can be used if the build is below honeycomb **/
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
                        anim = new TranslateAnimation(0, 0, translationY,
                                translationY);
                        anim.setFillAfter(true);
                        anim.setDuration(0);
                        mQuickReturnView.startAnimation(anim);
                    } else {
                        mQuickReturnView.setTranslationY(translationY);
                    }

                }

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }
            });
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container,
                    false);
            mGridView = (QuickReturnGridView) rootView.findViewById(R.id.gridview);
            mQuickReturnView = rootView.findViewById(R.id.sticky);

            return rootView;
        }
    }

    public static class PlaceholderAdapter extends ArrayAdapter<String> {

        LinearLayout.LayoutParams mPlaceholderParams;

        public PlaceholderAdapter(Context context, int resource, int textViewId, String[] data) {
            super(context, resource, textViewId, data);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = (convertView != null) ? convertView :
                    super.getView(position, convertView, parent);

            int colorSelector = position % 4;
            int color = Color.BLACK;
            switch (colorSelector) {
                case 0:
                    color = Color.CYAN;
                    break;
                case 1:
                    color = Color.MAGENTA;
                    break;
                case 2:
                    color = Color.BLUE;
                    break;
                case 3:
                    color = Color.GREEN;
                    break;
            }

            convertView.setBackgroundColor(color);
            return convertView;
        }
    }

}
