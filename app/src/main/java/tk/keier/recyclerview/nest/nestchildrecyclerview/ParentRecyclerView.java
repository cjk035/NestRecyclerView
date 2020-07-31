package tk.keier.recyclerview.nest.nestchildrecyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;

public class ParentRecyclerView extends RecyclerView implements NestedScrollingParent2, NestedScrollingParent3, FlingState.OnFlingStateListener {

    private FlingHelper flingHelper;
    private FlingState flingState;
    private NestedScrollingParentHelper nestedScrollingParentHelper;
    private WeakReference<ChildRecyclerView> childRecyclerViewWeakReference;
    private int mVelocityY;
    private int mDyConsumed;


    public ParentRecyclerView(@NonNull Context context) {
        super(context);
        init();
    }

    public ParentRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ParentRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        flingHelper = new FlingHelper(getContext());
        flingState = new FlingState(this, flingHelper, this);
        nestedScrollingParentHelper = new NestedScrollingParentHelper(this);
    }


    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        Log.e("parent", "onStartNestedScroll2 --> child: "+child.getClass().getSimpleName() + "; target: "+target.getClass().getSimpleName()+"; axes: "+axes+"; type: "+type);
        if(type == ViewCompat.TYPE_TOUCH){//触摸滚动
            return true;
        }else if(type == ViewCompat.TYPE_NON_TOUCH){//fling
            return true;
        }
        return false;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        Log.e("parent", "onNestedScrollAccepted2 --> child: "+child.getClass().getSimpleName() + "; target: "+target.getClass().getSimpleName()+"; axes: "+axes+"; type: "+type);
        mDyConsumed = 0;
        nestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes, type);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        Log.e("parent", "onStopNestedScroll2 --> target: "+ target.getClass().getSimpleName()+"; type: "+type);
        Log.e("parent", "fling 1--> mDyConsumed: "+mDyConsumed);
        nestedScrollingParentHelper.onStopNestedScroll(target, type);
    }


    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        Log.e("parent", "onNestedScroll2 --> target: "+ target.getClass().getSimpleName() + "; dxConsumed: "+dxConsumed+"; dyConsumed: "+dyConsumed + "; dxUnconsumed: "+dxUnconsumed+"; dyUnconsumed: "+dyUnconsumed);
        //这个方法不会被调用
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        Log.e("parent", "onNestedPreScroll2 --> target: "+ target.getClass().getSimpleName() + "; dx: "+dx+"; dy: "+dy+"; consumed: ["+ consumed[0] + ", "+consumed[1]+"]" + "; type: "+type);
        dispatchNestedPreScroll(dx, dy, consumed, null);
        if(type == ViewCompat.TYPE_TOUCH){
            if(canScrollVertically(1)){
                //parent可以向上滚动时
                scrollBy(0, dy - consumed[1]);
                consumed[1] = dy;//防止子view滑动，全部消费掉
            }
        }else if(type == ViewCompat.TYPE_NON_TOUCH){
            if(canScrollVertically(1)){
                //parent可以向上滚动时
                scrollBy(0, dy - consumed[1]);
                consumed[1] = dy;//防止子view滑动，全部消费掉
            }
        }
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        Log.e("parent", "onNestedScroll3 --> target: "+ target.getClass().getSimpleName() + "; dxConsumed: "+dxConsumed+"; dyConsumed: "+dyConsumed + "; dxUnconsumed: "+dxUnconsumed+"; dyUnconsumed: "+dyUnconsumed +"； type: "+type+"; consumed: ["+consumed[0]+", "+consumed[1]+"]");
        mDyConsumed += dyConsumed;
        if(type == ViewCompat.TYPE_TOUCH){
            //触摸滑动
            if(dyUnconsumed < 0){
                if(!target.canScrollVertically(-1)){
                    //向下滑动时，target不能向下滚动
                    int scrollOffset = computeVerticalScrollOffset();
                    scrollBy(0, dyUnconsumed);
                    int consumedOffset = computeVerticalScrollOffset() - scrollOffset;
                    consumed[1] += consumedOffset;//消费了多少告知父view
                }
            }
        }else if(type == ViewCompat.TYPE_NON_TOUCH){
            //fling滑动
            if(dyUnconsumed < 0){
                Log.e("parent", "parent-->target.canScrollVertically(-1): "+target.canScrollVertically(-1));
                if(!target.canScrollVertically(-1)){//不能向下滚动了
                    //速度mVelocityY可以fling的总距离
                    double distance = flingHelper.getSplineFlingDistance(mVelocityY);
                    //剩余的距离
                    double residualDistance = distance - Math.abs(mDyConsumed);
                    //得到正负的符号
                    int symbol = mVelocityY == 0 ? 1 : mVelocityY / Math.abs(mVelocityY);
                    //剩余距离的初始速度
                    int velocityY = flingHelper.getVelocityByDistance(residualDistance) * symbol;
                    Log.e("parent", "mVelocityY-child: "+mVelocityY + "; distance: "+distance + "; residualDistance: "+residualDistance + "; mDyConsumed: "+mDyConsumed + "; velocityY: "+velocityY);
                    //剩余的距离由parent来fling
                    fling(0, velocityY);
                }
            }
        }
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, null, type, consumed);
    }

    @Override
    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
        mVelocityY = (int)velocityY;
        return dispatchNestedFling(velocityY, velocityY, consumed);
    }

    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        return dispatchNestedPreFling(velocityY, velocityY);
    }

    @Override
    public int getNestedScrollAxes() {
        return nestedScrollingParentHelper.getNestedScrollAxes();
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        Log.e("parent", "fling --> velocityY: "+velocityY);
        flingState.fling(velocityX, velocityY);
        return super.fling(velocityX, velocityY);
    }

    @Override
    public void onFlingStart(int startVelocityY) {
        Log.e("parent", "onFlingStart --> ");
    }

    @Override
    public void onFlingFinished(int finishedVelocityY) {
        Log.e("parent", "onFlingFinished --> finishedVelocityY："+finishedVelocityY);
        if(finishedVelocityY > 0){//在向上滚动
            if(!canScrollVertically(1)){//parent不能向上滚动了
                //将剩余的fling交给ChildRecyclerView来完成
                ChildRecyclerView childRecyclerView = getChildRecyclerView();
                if(childRecyclerView != null){
                    childRecyclerView.fling(0, finishedVelocityY);
                }
            }
        }
    }

    private ChildRecyclerView getChildRecyclerView(){
        if(childRecyclerViewWeakReference != null){
            return childRecyclerViewWeakReference.get();
        }
        return null;
    }

    public void setChildRecyclerView(ChildRecyclerView childRecyclerView){
        childRecyclerViewWeakReference = new WeakReference<>(childRecyclerView);
    }


    private CoordinatorLayout findCoordinatorLayout(View view){
        ViewGroup parent = (ViewGroup) view.getParent();
        if(parent instanceof ParentRecyclerView){
            return null;
        }
        if(parent instanceof CoordinatorLayout){
            return (CoordinatorLayout) parent;
        }
        return findCoordinatorLayout(parent);
    }
}
