package com.zubisoft.noterecorder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.zubisoft.noterecorder.R;
import com.zubisoft.noterecorder.data.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryItemViewHolder> {

    private List<Category> categories=new ArrayList<>();
    private CategoryInteractionListener categoryInteractionListener;

    @NonNull
    @Override
    public CategoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_item, parent,false);
        return new CategoryItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryItemViewHolder holder, int position) {
        Category category=categories.get(position);
        if(category!=null){
            holder.txtCatTitle.setText(category.getTitle());
            ((MaterialCardView)holder.itemView).setCardBackgroundColor(category.getColor());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    categoryInteractionListener.onCategoryClicked(category);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public void setCategoryInteractionListener(CategoryInteractionListener categoryInteractionListener) {
        this.categoryInteractionListener = categoryInteractionListener;
    }

    static class CategoryItemViewHolder extends RecyclerView.ViewHolder{

        private final TextView txtCatTitle;

        public CategoryItemViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCatTitle=itemView.findViewById(R.id.txtCatTitle);
        }
    }

    public interface CategoryInteractionListener{
        void onCategoryClicked(Category category);
    }
}
