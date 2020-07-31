package tk.keier.recyclerview.nest.nestchildrecyclerview;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 监听RecyclerView的fling开始和结束
 */
public class FlingState extends RecyclerView.OnScrollListener {
    private RecyclerView recyclerView;
    private OnFlingStateListener listener;
    private FlingHelper flingHelper;
    private int state;
    private int mVelocityY;
    private int mScrollOffset;

    public FlingState(RecyclerView recyclerView, FlingHelper flingHelper, OnFlingStateListener listener) {
        this.recyclerView = recyclerView;
        this.flingHelper = flingHelper;
        this.listener = listener;
        this.recyclerView.addOnScrollListener(this);
    }

    public void fling(int velocityX, int velocityY){
        mVelocityY = velocityY;
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        //滚动停止
        if(newState == RecyclerView.SCROLL_STATE_IDLE){
            if(state == RecyclerView.SCROLL_STATE_SETTLING){
                if(listener != null){
                    listener.onFlingFinished(getVelocityYOnFlingFinished());
                }
            }
            mScrollOffset = 0;
        }

        //fling中
        if(newState == RecyclerView.SCROLL_STATE_SETTLING){
            if(listener != null){
                listener.onFlingStart(mVelocityY);
            }
        }

        //触摸中
        if(newState == RecyclerView.SCROLL_STATE_DRAGGING){

        }
        state = newState;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        if(state == RecyclerView.SCROLL_STATE_SETTLING){
            mScrollOffset += dy;
        }
    }

    //获取fling结束时的速度
    private int getVelocityYOnFlingFinished(){
        double distance = flingHelper.getSplineFlingDistance(mVelocityY);
        double completedDistance = Math.abs(mScrollOffset);
        double residualDistance = distance - completedDistance;
        int symbol = mVelocityY == 0 ? 1 : mVelocityY / Math.abs(mVelocityY);//得到正负的符号
        int velocityY = flingHelper.getVelocityByDistance(residualDistance) * symbol;
        Log.e("parent", "mVelocityY-parent: " +mVelocityY + "; distance: "+distance + "; residualDistance: "+residualDistance + "; completedDistance: "+completedDistance + "; velocityY: "+velocityY );
        return velocityY;
    }

    public interface OnFlingStateListener{
        void onFlingStart(int startVelocityY);
        void onFlingFinished(int finishedVelocityY);
    }
}
