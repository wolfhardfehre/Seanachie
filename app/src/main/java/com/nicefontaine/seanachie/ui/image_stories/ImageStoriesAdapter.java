package com.nicefontaine.seanachie.ui.image_stories;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nicefontaine.seanachie.R;
import com.nicefontaine.seanachie.data.models.ImageStory;
import com.nicefontaine.seanachie.ui.ItemTouchCallback;
import com.nicefontaine.seanachie.utils.ImageUtils;

import java.io.IOException;
import java.util.List;

import static com.nicefontaine.seanachie.utils.Utils.isNull;
import static com.nicefontaine.seanachie.utils.Utils.leftShift;
import static com.nicefontaine.seanachie.utils.Utils.rightShift;


class ImageStoriesAdapter extends RecyclerView.Adapter<ImageStoriesAdapter.PetHolder> implements
        ItemTouchCallback.OnItemTouchListener {

    private final Context context;
    private LayoutInflater inflater;
    private List<ImageStory> imageStories;

    ImageStoriesAdapter(Context context, List<ImageStory> imageStories) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        setImageStories(imageStories);
    }

    void setImageStories(List<ImageStory> imageStories) {
        this.imageStories = imageStories;
    }

    List<ImageStory> getImageStories() {
        return imageStories;
    }

    @Override
    public ImageStoriesAdapter.PetHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_item_image_story, parent, false);
        return new ImageStoriesAdapter.PetHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageStoriesAdapter.PetHolder holder, int position) {
        final ImageStory imageStory = imageStories.get(position);
        String path = imageStory.getImagePath();
        if (!isNull(path)) {
            Bitmap bitmap = ImageUtils.loadImage(path, 100);
            try {
                bitmap = ImageUtils.rotateImage(bitmap, path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            holder.image.setImageBitmap(bitmap);
        }
        holder.name.setText(imageStory.getForm().getCategories().get(0).getValue());
        String category = String.format("%s %s",
                imageStory.count(), context.getString(R.string.recycler_story_filled_categories));
        holder.category.setText(category);
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
            image = (ImageView) itemView.findViewById(R.id.recycler_item_image_story_image);
            name = (TextView) itemView.findViewById(R.id.recycler_item_image_story_name);
            category = (TextView) itemView.findViewById(R.id.recycler_item_image_story_first_category);
        }
    }
}