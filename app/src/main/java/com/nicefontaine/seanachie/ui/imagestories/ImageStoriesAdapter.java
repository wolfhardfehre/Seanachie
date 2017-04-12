package com.nicefontaine.seanachie.ui.imagestories;


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


class ImageStoriesAdapter extends RecyclerView.Adapter<ImageStoriesAdapter.ImageStoryHolder> implements
        ItemTouchCallback.OnItemTouchListener {

    private final Context context;
    private final ImageStoriesFragment fragment;
    private LayoutInflater inflater;
    private List<ImageStory> imageStories;

    ImageStoriesAdapter(ImageStoriesFragment fragment, Context context, List<ImageStory> imageStories) {
        this.inflater = LayoutInflater.from(context);
        this.fragment = fragment;
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
    public ImageStoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_item_imagestories, parent, false);
        return new ImageStoryHolder(view, fragment);
    }

    @Override
    public void onBindViewHolder(ImageStoryHolder holder, int position) {
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
        holder.name.setText(imageStory.getName());
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

    static class ImageStoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView image;
        private TextView name;
        private TextView category;
        private OnClickCallback listener;

        interface OnClickCallback {
            void onClick(int position);
        }

        ImageStoryHolder(View itemView, ImageStoriesFragment fragment) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.recycler_item_image_story_image);
            name = (TextView) itemView.findViewById(R.id.recycler_item_image_story_name);
            category = (TextView) itemView.findViewById(R.id.recycler_item_image_story_first_category);
            listener = fragment;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(getAdapterPosition());
        }
    }
}