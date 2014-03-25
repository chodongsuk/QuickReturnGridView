package tw.jimytc.quickreturngridview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
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
            System.out.println("Init ItemOffsetY array");
            mItemOffsetY = new int[rowNumber];
        } else {
            System.out.println("No Init for ItemOffsetY array");
        }
        System.out.println("Length of offset array: " + mItemOffsetY.length);
        for (int i = 0; i < mItemCount; i += numColumn) {
            View view = getAdapter().getView(i, null, this);
            view.measure(
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int realIndex = i / numColumn;
            mHeight += view.getMeasuredHeight();
            mItemOffsetY[realIndex] = mHeight;
            System.out.println(String.format("index: %d, mHeight: %d" ,realIndex, mHeight));
            System.out.println("mItemOffsetY[" + realIndex + "]: " + mItemOffsetY[realIndex]);
        }
        for (int i = 0; i < mItemOffsetY.length; i++) {
            System.out.println(String.format("OffsetY @%d: %d", i, mItemOffsetY[i]));
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
        System.out
                .println(String
                        .format("First visible item: %d, nItemY: %d, mItemOffsetY[%d]: %d, nScrollY: %d",
                                pos, nItemY, rowPos, mItemOffsetY[rowPos], nScrollY));
        return nScrollY;
    }
}
