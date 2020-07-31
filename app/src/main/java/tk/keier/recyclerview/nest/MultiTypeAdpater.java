package tk.keier.recyclerview.nest;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiTypeAdpater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<ItemModel> mList = new ArrayList<>();
    private Map<Integer, Delegate> delegates = new HashMap<>();

    public static MultiTypeAdpater create(){
        return new MultiTypeAdpater();
    }

    public void addDelegate(int viewType, Delegate<? extends ItemModel> delegate){
        delegates.put(viewType, delegate);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return delegates.get(viewType).onCreateViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        delegates.get(viewType).onBindViewHolder(holder, mList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).viewType();
    }

    public void submit(List<ItemModel> list){
        mList.clear();
        if(list != null){
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int viewType = holder.getItemViewType();
        delegates.get(viewType).onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        int viewType = holder.getItemViewType();
        delegates.get(viewType).onViewDetachedFromWindow(holder);
    }

    public interface Delegate<T extends ItemModel> {

        RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent);

        void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, T itemData, int position);

        void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder);

        void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder);
    }

}
