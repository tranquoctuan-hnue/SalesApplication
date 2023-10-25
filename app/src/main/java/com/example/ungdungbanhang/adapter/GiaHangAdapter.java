package com.example.ungdungbanhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ungdungbanhang.R;
import com.example.ungdungbanhang.model.GioHang;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GiaHangAdapter extends BaseAdapter
{
    Context context;
    ArrayList<GioHang> ArrayCart;

    public GiaHangAdapter(Context context, ArrayList<GioHang> arrayCart) {
        this.context = context;
        ArrayCart = arrayCart;
    }

    @Override
    public int getCount() {
        return ArrayCart.size();
    }

    @Override
    public Object getItem(int position) {
        return ArrayCart.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class ViewHolder{
        public TextView txtNameCart, txtPriceCart;
        public Button btnMinus,btnValues, btnPlus;
        public ImageView imgCard;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GioHang gioHang = (GioHang) getItem(position);
        ViewHolder viewHolder = null;
        if(viewHolder == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dong_giohang, null);
            viewHolder.txtNameCart = convertView.findViewById(R.id.txtNameCart);
            viewHolder.txtPriceCart = convertView.findViewById(R.id.txtPriceCart);
            viewHolder.btnMinus = convertView.findViewById(R.id.btnMinus);
            viewHolder.btnValues = convertView.findViewById(R.id.btnValues);
            viewHolder.btnPlus = convertView.findViewById(R.id.btnPlus);
            viewHolder.imgCard = convertView.findViewById(R.id.imgCard);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtNameCart.setText(gioHang.getTenSP());
        viewHolder.txtPriceCart.setText(gioHang.getGiaSP() + " VNĐ");
        Picasso.get().load(gioHang.getHinhSP())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(viewHolder.imgCard);
        viewHolder.btnValues.setText(gioHang.getSoluongSP() +"");
        return convertView;
    }
}
