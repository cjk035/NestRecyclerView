package tk.keier.recyclerview.nest.nestchildrecyclerview;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.NestedScrollingChild2;
import androidx.core.view.NestedScrollingChild3;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;


import com.google.android.material.appbar.AppBarLayout;

public class NestCoordinatorLayout extends CoordinatorLayout implements NestedScrollingChild2, NestedScrollingChild3 {

    private static final int MAX_VERTICAL_OFFSET = -(int)(Resources.getSystem().getDisplayMetrics().density * 40);
    private NestedScrollingChildHelper nestedScrollingChildHelper;
    private AppBarLayout appBarLayout;
    private FlingHelper flingHelper;
    private ParentRecyclerView parentRecyclerView;
    private int mVerticalOffset = 0;
    private int mVelocityY = 0;

    public NestCoordinatorLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public NestCoordinatorLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NestCoordinatorLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        nestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        flingHelper = new FlingHelper(getContext());
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if(ev.getAction() == MotionEvent.ACTION_DOWN){
//            requestDisallowInterceptTouchEvent(true);
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int count = getChildCount();
        for(int i=0;i<count;i++){
           View child = getChildAt(i);
           if(child instanceof AppBarLayout){
               appBarLayout = (AppBarLayout) child;
               break;
           }
        }

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                mVerticalOffset = verticalOffset;
            }
        });

    }

    //>>> child start>>>
    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        nestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
        super.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return super.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes, int type) {
        return nestedScrollingChildHelper.startNestedScroll(axes, type);
    }

    @Override
    public void stopNestedScroll(int type) {
        nestedScrollingChildHelper.startNestedScroll(type);
    }

    @Override
    public boolean hasNestedScrollingParent(int type) {
        return nestedScrollingChildHelper.hasNestedScrollingParent(type);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type) {
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed,dyUnconsumed, offsetInWindow, type, null);
        return true;
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow, int type) {
        return nestedScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return nestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return nestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public void dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type, @NonNull int[] consumed) {
        nestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type, consumed);
    }
    //<<< child end<<<







    //>>> parent start>>>
    //>>>>>>>>>>>>>>>>>>>>
    //>>>>>>>>>>>>>>>>>>>>
    //>>>>>>>>>>>>>>>>>>>>
    //>>>>>>>>>>>>>>>>>>>>
    @Override
    public boolean onStartNestedScroll(View child, View target, int axes, int type) {
        Log.e("Coordinator", "onStartNestedScroll --> child: "+ child.getClass().getSimpleName()+ "; target: "+ target.getClass().getSimpleName() + "; axes: "+ axes+ "; type: "+type);
       if(type == ViewCompat.TYPE_NON_TOUCH){
           startNestedScroll(axes, type);
       }
        return super.onStartNestedScroll(child, target, axes, type);
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes, int type) {
        Log.e("Coordinator", "onNestedScrollAccepted --> child: "+ child.getClass().getSimpleName() + "; target: "+ target.getClass().getSimpleName() +"; axes: "+ nestedScrollAxes);
        super.onNestedScrollAccepted(child, target, nestedScrollAxes, type);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed, int type) {
        boolean preScroll = dispatchNestedPreScroll(dx, dy, consumed, null, type);
        Log.e("Coordinator", "onNestedPreScroll -->  target: "+ target.getClass().getSimpleName() +"; dx: "+ dx+ "; dy: "+dy+"; consumed: ["+consumed[0] + ","+consumed[1]+"]; type: "+type + "; preScroll: "+preScroll);
        if(type == ViewCompat.TYPE_TOUCH){
            if(dy != consumed[1]){
                super.onNestedPreScroll(target, dx, dy, consumed, type);
            }
        }else if(type == ViewCompat.TYPE_NON_TOUCH){
            if(dy != consumed[1]){
                super.onNestedPreScroll(target, dx, dy, consumed, type);
            }
        }
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        Log.e("Coordinator", "onNestedScroll -->  target: "+ target.getClass().getSimpleName() +"; dxConsumed: "+ dxConsumed+ "; dyConsumed: "+dyConsumed+"; dxUnconsumed: "+ dxUnconsumed+ "; dyUnconsumed: "+dyUnconsumed+"; type: "+type);
        if(type == ViewCompat.TYPE_TOUCH){
            if(mVerticalOffset != 0 || target.canScrollVertically(-1)){
                super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
            }
        }

        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, null, type, consumed);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        Log.e("Coordinator", "onNestedPreFling -->  target: "+ target.getClass().getSimpleName() +"; velocityX: "+ velocityX+ "; velocityY: "+velocityY);
        mVelocityY = (int)velocityY;
        return dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        Log.e("Coordinator", "onNestedFling -->  target: "+ target.getClass().getSimpleName() +"; velocityX: "+ velocityX+ "; velocityY: "+velocityY +"; consumed: "+consumed);
        return dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public void onStopNestedScroll(View target, int type) {
        Log.e("Coordinator", "onNestedFling -->  target: "+ target.getClass().getSimpleName() +"; type: "+type);
        super.onStopNestedScroll(target, type);
        stopNestedScroll(type);
    }
    //<<< parent end<<<
    //<<<<<<<<<<<<<<<<<
    //<<<<<<<<<<<<<<<<<
    //<<<<<<<<<<<<<<<<<
    //<<<<<<<<<<<<<<<<<
















    private ParentRecyclerView findParentRecyclerView(View view){
        ViewGroup parent = (ViewGroup) view.getParent();
        if(parent == null){
            return null;
        }
        if(parent instanceof ParentRecyclerView){
            return (ParentRecyclerView) parent;
        }
        return findParentRecyclerView(parent);
    }
}
