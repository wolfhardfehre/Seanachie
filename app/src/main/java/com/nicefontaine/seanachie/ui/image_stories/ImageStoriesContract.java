package com.nicefontaine.seanachie.ui.image_stories;


import com.nicefontaine.seanachie.data.models.ImageStory;
import com.nicefontaine.seanachie.ui.BasePresenter;
import com.nicefontaine.seanachie.ui.BaseView;

import java.util.List;


public interface ImageStoriesContract {

    interface View extends BaseView<Presenter> {

        void loadImageStories(List<ImageStory> imageStories);

        void noData();

        void initRecycler();

        void updateRecycler();
    }

    interface Presenter extends BasePresenter {

        void itemMoved(List<ImageStory> imageStories);

        void itemRemoved(Integer petId);
    }
}
