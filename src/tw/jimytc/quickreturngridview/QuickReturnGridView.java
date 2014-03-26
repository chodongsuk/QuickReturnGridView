package tw.jimytc.quickreturngridview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class QuickReturnGridView extends GridView {

    private int mItemCount;
    private int mItemOffsetY[];
    private boolean scrollIsComputed = false;
    private int mHeight;

    public QuickReturnGridView(Context context) {
        super(context);
    }

    public QuickReturnGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QuickReturnGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public int getListHeight() {
        return mHeight;
    }

    public void computeScrollY() {
        mHeight = 0;
        mItemCount = getAdapter().getCount();
        int numColumn = getNumColumns();
        int halfNumColumn = (numColumn + 1) / 2;
        int rowNumber = (mItemCount + halfNumColumn) / numColumn;
        if (mItemOffsetY == null) {
            mItemOffsetY = new int[rowNumber];
        }
        for (int i = 0; i < mItemCount; i += numColumn) {
            View view = getAdapter().getView(i, null, this);
            int realIndex = i / numColumn;
            mItemOffsetY[realIndex] = mHeight;
            view.measure(
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            mHeight += view.getMeasuredHeight();
        }
        scrollIsComputed = true;
    }

    public boolean scrollYIsComputed() {
        return scrollIsComputed;
    }

    public int getComputedScrollY() {
        int pos, nScrollY, nItemY;
        View view = null;
        pos = getFirstVisiblePosition();
        int numColumn = getNumColumns();
        int rowPos = pos / numColumn;
        view = getChildAt(0);
        nItemY = view.getTop();
        nScrollY = mItemOffsetY[rowPos] - nItemY;
        return nScrollY;
    }
}
