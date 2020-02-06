package com.example.punchja.gallerygpss.Adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.punchja.gallerygpss.Model.GalleryItem;
import com.example.punchja.gallerygpss.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class GalleryListAdapter extends ArrayAdapter<GalleryItem> {

    private Context mContext;
    private int mResource;
    private List<GalleryItem> mGalleryItemList;


    public GalleryListAdapter(@NonNull Context context,
                            int resource,
                            @NonNull List<GalleryItem> galleryItemList) {
        super(context, resource, galleryItemList);
        this.mContext = context;
        this.mResource = resource;
        this.mGalleryItemList = galleryItemList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(mResource, parent, false);

        TextView titleTextView = view.findViewById(R.id.title_text_view_show);
        TextView locationTextView = view.findViewById(R.id.location_text_view_show);
        ImageView imageView = view.findViewById(R.id.image_view_show);

        GalleryItem galleryItem = mGalleryItemList.get(position);
        String title = galleryItem.title;
        String location = galleryItem.location;
        String filename = galleryItem.image;

        titleTextView.setText(title);
        locationTextView.setText(location);

        AssetManager am = mContext.getAssets();
        try {
            InputStream is = am.open(filename);
            Drawable drawable = Drawable.createFromStream(is, "");
            imageView.setImageDrawable(drawable);
        } catch (IOException e) {
            File privateDir = mContext.getFilesDir();
            File logoFile = new File(privateDir, filename);

            Bitmap bitmap = BitmapFactory.decodeFile(logoFile.getAbsolutePath(), null);
            imageView.setImageBitmap(bitmap);

            e.printStackTrace();
        }

        return view;
    }
}
