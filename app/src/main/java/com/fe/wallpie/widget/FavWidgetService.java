package com.fe.wallpie.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Farmaan-PC on 04-02-2017.
 */

public class FavWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FavProvider(getApplicationContext(), intent);
    }
}
