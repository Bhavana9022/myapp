package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.AndroidFlickrActivity;
import com.example.myapplication.FlickrImage;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

public class FlickrAdapter extends BaseAdapter{

    private Context context;
    public FlickrImage[] FlickrAdapterImage;
    public Gallery photoBar;
    ListView listView;
    ImageView image;
    ViewHolder viewholder;
   /*RecyclerView recyclerView;*/


    public FlickrAdapter(Context c, FlickrImage[] fImage) {
        context = c;
        FlickrAdapterImage = fImage;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return FlickrAdapterImage.length;
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return FlickrAdapterImage[position];
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView (int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        String FlickrPhotoPath =
                "http://farm" + FlickrAdapterImage[position].getFarm() + ".static.flickr.com/"
                        + FlickrAdapterImage[position].getServer() + "/" + FlickrAdapterImage[position].getId() + "_" + FlickrAdapterImage[position].getSecret() + "_m.jpg";
        if (convertView == null) {

            convertView= LayoutInflater.from(context).inflate(R.layout.activity_main,parent,false);
            viewholder = new ViewHolder(convertView);
            convertView.setTag(viewholder);

            image = new ImageView(context);
            image.setLayoutParams(new Gallery.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            image.setPadding(8, 8, 8, 8);
           /* image.setImageResource(FlickrPhotoPath [1]);*/

        }
        else {
            image = (ImageView) convertView;
        }


        Picasso.with(context)
                .load(FlickrPhotoPath)
                .rotate(90f)
                .into(image);
        return image;

        viewholder.image;
    }

    private class ViewHolder {
        public ViewHolder(@NonNull View view) {
            
            photoBar = (Gallery) view.findViewById(R.id.photobar);
            listView=(ListView)view.findViewById(R.id.listview);
            image = new ImageView(context);
        }

}}

