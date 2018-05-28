package br.edu.ifspsaocarlos.sdm.fototagz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import br.edu.ifspsaocarlos.sdm.fototagz.model.TaggedImage;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class GalleryAdapter extends RealmRecyclerViewAdapter<TaggedImage, GalleryAdapter.MyViewHolder> {

    public GalleryAdapter(@Nullable OrderedRealmCollection<TaggedImage> data) {
        super(data, true);
    }

    @NonNull
    @Override
    public GalleryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryAdapter.MyViewHolder holder, int position) {
        final TaggedImage obj = getItem(position);
        holder.taggedImage = obj;
        //final String imageUri = obj.getImageUri();
        final String imageUri = obj.getImageUri();
        holder.image.setImageURI(Uri.parse(imageUri));
        //Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imageUri), 120, 120);
        //holder.image.setImageBitmap(ThumbImage);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        public TaggedImage taggedImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.iv_rv_item);
        }
    }
}
