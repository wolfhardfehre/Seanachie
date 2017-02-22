package com.nicefontaine.seanachie.ui.image_stories;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nicefontaine.seanachie.R;
import com.nicefontaine.seanachie.data.models.ImageStory;
import com.nicefontaine.seanachie.ui.ItemTouchCallback;

import java.util.List;

import static com.nicefontaine.seanachie.utils.Utils.leftShift;
import static com.nicefontaine.seanachie.utils.Utils.rightShift;


class ImageStoriesAdapter extends RecyclerView.Adapter<ImageStoriesAdapter.PetHolder> implements
        ItemTouchCallback.OnItemTouchListener {

    private LayoutInflater inflater;
    private List<ImageStory> imageStories;

    ImageStoriesAdapter(Context context, List<ImageStory> imageStories) {
        this.inflater = LayoutInflater.from(context);
        setImageStories(imageStories);
    }

    public void setImageStories(List<ImageStory> imageStories) {
        this.imageStories = imageStories;
    }

    public List<ImageStory> getImageStories() {
        return imageStories;
    }

    @Override
    public ImageStoriesAdapter.PetHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_item_pet, parent, false);
        return new ImageStoriesAdapter.PetHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageStoriesAdapter.PetHolder holder, int position) {
        final ImageStory imageStory = imageStories.get(position);
        // TODO fetch image to holder.image
        holder.name.setText(imageStory.getName());
        holder.category.setText(imageStory.getFirst());
    }

    @Override
    public void onItemDismiss(int position) {
        imageStories.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int from, int to) {
        imageStories = (from < to) ? rightShift(imageStories, from, to) : leftShift(imageStories, from, to);
        notifyItemMoved(from, to);
    }

    @Override
    public int getItemCount() {
        return imageStories.size();
    }

    class PetHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView name;
        private TextView category;

        PetHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.recycler_pet_item_image);
            name = (TextView) itemView.findViewById(R.id.recycler_pet_item_name);
            category = (TextView) itemView.findViewById(R.id.recycler_pet_item_first_category);
        }
    }
}