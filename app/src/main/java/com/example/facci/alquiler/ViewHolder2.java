package com.example.facci.alquiler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ViewHolder2 extends RecyclerView.ViewHolder {

    View mVIew;

    public ViewHolder2(@NonNull View itemView) {
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
        TextView mTitleView = mVIew.findViewById(R.id.jTitleTv);
        TextView mDetailTv = mVIew.findViewById(R.id.jDescriptionTv);
        ImageView mImageTv = mVIew.findViewById(R.id.jImageView);
        TextView mUsuarioTv = mVIew.findViewById(R.id.jUserTV);
        TextView mEstadoTv = mVIew.findViewById(R.id.jEstado);

        mTitleView.setText(title);
        mDetailTv.setText(description);
        mUsuarioTv.setText(usuario);
        mEstadoTv.setText(estado);
        Picasso.get().load(image).into(mImageTv);
    }
    private ViewHolder2.ClickListener mClickListener;
    public  interface  ClickListener{
        void onItemClick(View view, int position);
        void onItemlongClick(View view, int position);
    }
    public void setOnclickListener(ViewHolder2.ClickListener clickListener){
        mClickListener = clickListener;
    }
}