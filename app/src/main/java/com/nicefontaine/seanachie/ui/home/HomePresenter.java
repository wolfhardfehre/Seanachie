package com.nicefontaine.seanachie.ui.home;


import android.app.Activity;
import android.content.Intent;

import java.io.IOException;

public interface HomePresenter {

    void camera(Activity activity, int width) throws IOException;

    void story(Activity activity);

    void result(int requestCode, int resultCode, Intent intent);

}
