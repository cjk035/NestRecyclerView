package tk.keier.recyclerview.nest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import tk.keier.recyclerview.nest.nestchildrecyclerview.ChildRecyclerView;
import tk.keier.recyclerview.nest.MultiTypeAdpater.Delegate;


public class MainActivity extends AppCompatActivity {

    private MultiTypeAdpater mainAdapter = MultiTypeAdpater.create();
    private RecyclerView parentRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        parentRecyclerView = findViewById(R.id.parentRecyclerView);

        parentRecyclerView.setLayoutManager(getLayoutManager());
        parentRecyclerView.setAdapter(mainAdapter);
        mainAdapter.addDelegate(0, new TextDelegate());
        mainAdapter.addDelegate(1, new ChildDelegate());
        mainAdapter.submit(createData(10, 50));
    }

    private LinearLayoutManager getLayoutManager(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false) {

        };
        return linearLayoutManager;
    }



    static class TextDelegate implements Delegate<TextItemModel>{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
            View view = View.inflate(parent.getContext(), R.layout.item, null);
            view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, 150));
            return new RecyclerView.ViewHolder(view) {};
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, TextItemModel itemData, int position) {
            TextView itemTv = holder.itemView.findViewById(R.id.itemTv);
            itemTv.setText(itemData.data);
        }

        @Override
        public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {

        }

        @Override
        public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {

        }
    }

    static class ChildDelegate implements Delegate<ChildItemModel>{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
            View view = View.inflate(parent.getContext(), R.layout.item_nest, null);
            return new RecyclerView.ViewHolder(view) {};
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, ChildItemModel itemData, int position) {
            ChildRecyclerView childRecyclerView = holder.itemView.findViewById(R.id.childRecyclerView);
            childRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), RecyclerView.VERTICAL, false));
            childRecyclerView.setAdapter(new ChildAdapter(itemData.data));
            TabLayout tabLayout = holder.itemView.findViewById(R.id.tabLayout);
            initTabLayout(tabLayout);
            CoordinatorLayout coordinatorLayout = holder.itemView.findViewById(R.id.coordinatorLayout);
            ViewCompat.setNestedScrollingEnabled(coordinatorLayout, true);
        }

        @Override
        public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
            RecyclerView parent = (RecyclerView)holder.itemView.getParent();
            holder.itemView.getLayoutParams().width = RecyclerView.LayoutParams.MATCH_PARENT;
            holder.itemView.getLayoutParams().height = parent.getHeight();
        }

        @Override
        public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {

        }


    }

    static class ChildAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private List<TextItemModel> list;

        public ChildAdapter(List<TextItemModel> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(), R.layout.item, null);
            view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, 150));
            return new RecyclerView.ViewHolder(view) {};
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            TextView itemTv = holder.itemView.findViewById(R.id.itemTv);
            itemTv.setText(list.get(position).data);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }


    static class TextItemModel implements ItemModel {

        public TextItemModel(String tag, String data) {
            this.data = tag + " - " + data;
        }

        private String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }


        @Override
        public int viewType() {
            return 0;
        }
    }

    static class ChildItemModel implements ItemModel {

        public ChildItemModel(List<TextItemModel> data) {
            this.data = data;
        }

        private List<TextItemModel> data;

        public List<TextItemModel> getData() {
            return data;
        }

        @Override
        public int viewType() {
            return 1;
        }
    }

    private List<ItemModel> createData(int count, int childCount){
        List<ItemModel> list = new ArrayList<>();
        for(int i=0;i<count;i++){
            list.add(new TextItemModel("Parent", String.valueOf(i)));
        }


        List<TextItemModel> childList = new ArrayList<>();
        for(int i=0;i<childCount;i++){
            childList.add(new TextItemModel("Child", String.valueOf(i)));
        }

        list.add(new ChildItemModel(childList));
        return list;
    }

    private static void initTabLayout(TabLayout tabLayout){
        tabLayout.removeAllTabs();
        for(int i=0;i<10;i++){
            tabLayout.addTab(tabLayout.newTab().setText("Tab_"+i));
        }
    }
}