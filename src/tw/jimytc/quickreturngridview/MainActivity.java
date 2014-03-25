package tw.jimytc.quickreturngridview;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private GridView mGridView;
        private View mSticky;
        PlaceholderAdapter mAdapter;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container,
                    false);
            mGridView = (GridView) rootView.findViewById(R.id.gridview);
            int numColumn = getResources().getInteger(R.integer.gridview_column);
            int placeholderHeight = getResources().getDimensionPixelSize(R.dimen.sticky_header_height);
            String[] data = getResources().getStringArray(R.array.gridview_items);
            mAdapter = new PlaceholderAdapter(getActivity(), R.layout.grid_item,
                    data, numColumn, placeholderHeight);
            mGridView.setAdapter(mAdapter);
            mSticky = rootView.findViewById(R.id.sticky);
            return rootView;
        }
    }

    public static class PlaceholderAdapter extends ArrayAdapter<String> {

        private int mGridViewColumnNum;
        private int mPlaceholderHeight;
        AbsListView.LayoutParams mPlaceholderParams;

        public PlaceholderAdapter(Context context, int resource, String[] data, int numColumn, int placeholderHeight) {
            super(context, resource, data);
            mGridViewColumnNum = numColumn;
            mPlaceholderHeight = placeholderHeight;
            mPlaceholderParams = new AbsListView.LayoutParams(1, mPlaceholderHeight);
        }

        @Override
        public int getCount() {
            int count = super.getCount();
            return count + mGridViewColumnNum;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (position < mGridViewColumnNum) {
                if (convertView == null || (convertView instanceof TextView)) {
                    convertView = new View(getContext());
                }
                convertView.setLayoutParams(mPlaceholderParams);
                view = convertView;
            } else {
                if (convertView != null && !(convertView instanceof TextView)) {
                    convertView = null;
                }
                int realPosition = position - mGridViewColumnNum;
                view = super.getView(realPosition, convertView, parent);
            }
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
            view.setBackgroundColor(color);
            return view;
        }
    }

}
