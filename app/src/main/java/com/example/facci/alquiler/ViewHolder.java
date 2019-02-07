package com.example.facci.alquiler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {

    View mVIew;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        mVIew = itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v ,getAdapterPosition());
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemlongClick(v,getAdapterPosition());
                return true;
            }
        });

    }

    public void setDetails(Context ctx, String title, String description, String image,String usuario,String telefono,String estado) {
        TextView mTitleView = mVIew.findViewById(R.id.rTitleTv);
        TextView mDetailTv = mVIew.findViewById(R.id.rDescriptionTv);
        ImageView mImageTv = mVIew.findViewById(R.id.rImageView);
        TextView mUsuarioTv = mVIew.findViewById(R.id.rUserTV);
        TextView mEstado =mVIew.findViewById(R.id.rEstador);

        mTitleView.setText(title);
        mDetailTv.setText(description);
        mUsuarioTv.setText(usuario);
        mEstado.setText(estado);

        Picasso.get().load(image).into(mImageTv);
    }
    private ViewHolder.ClickListener mClickListener;
    public  interface  ClickListener{
        void onItemClick(View view,int position);
        void onItemlongClick(View view, int position);
    }
    public void setOnclickListener(ViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }
}