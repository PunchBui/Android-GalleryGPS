package com.example.punchja.gallerygpss.Model;

import java.util.Locale;

public class GalleryItem {

    public final long _id;
    public final String title;
    public final String location;
    public final String image;

    public GalleryItem(long id, String title, String location, String image) {
        _id = id;
        this.title = title;
        this.location = location;
        this.image = image;
    }


    @Override
    public String toString() {
        String msg = String.format(
                Locale.getDefault(),
                "%s (%s)",
                this.title,
                this.location
        );
        return msg;
    }
}
