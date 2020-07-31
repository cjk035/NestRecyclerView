package tk.keier.recyclerview.nest.nestchildrecyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;



public class ChildRecyclerView extends RecyclerView {

    public ChildRecyclerView(@NonNull Context context) {
        super(context);
    }

    public ChildRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ChildRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        deliverySelfToParentRecyclerView();
    }

    //将childRecyclerView传递给ParentRecyclerView
    private void deliverySelfToParentRecyclerView(){
        ParentRecyclerView parentRecyclerView = findParentRecyclerView(this);
        parentRecyclerView.setChildRecyclerView(this);
    }

    private ParentRecyclerView findParentRecyclerView(View view){
        ViewGroup parent = (ViewGroup) view.getParent();
        if(parent instanceof ParentRecyclerView){
            return (ParentRecyclerView) parent;
        }
        return findParentRecyclerView(parent);
    }
}
