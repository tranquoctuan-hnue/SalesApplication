package com.example.ungdungbanhang.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.ungdungbanhang.R;
import com.example.ungdungbanhang.activity.DienThoaiActivity;
import com.example.ungdungbanhang.model.SanPham;
import com.google.firebase.installations.internal.FidListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DienThoaiAdapter extends BaseAdapter {
        Context context;
        ArrayList<SanPham> arrayPhone;
        ClickListenerUpdate clickListenerUpdate;
        ClickListenerDelete clickListenerDelete;
        ClickListenerDetail clickListenerDetail;
        public interface ClickListenerUpdate{
            void onClickUpdate(SanPham sanPham);
        }

        public interface ClickListenerDelete{
            void onClickDelete(SanPham sanPham);
        }
        public interface ClickListenerDetail{
          void onClickDetail(SanPham sanPham);
        }

        public DienThoaiAdapter(Context context, ArrayList<SanPham> arrayPhone,  ClickListenerUpdate clickListenerUpdate, ClickListenerDelete clickListenerDelete , ClickListenerDetail clickListenerDetail) {
            this.context = context;
            this.arrayPhone = arrayPhone;
            this.clickListenerUpdate = clickListenerUpdate;
            this.clickListenerDelete = clickListenerDelete;
            this.clickListenerDetail = clickListenerDetail;
        }
        public void setFilteredList(ArrayList<SanPham> filteredList){
            this.arrayPhone = filteredList;
            notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            return arrayPhone.size();
        }

        @Override
        public SanPham getItem(int position) {
            return arrayPhone.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        public class ViewHolder{
        public TextView txtTenDienThoai, txtGiaDienThoai, txtMoTaDienThoai;
        public ImageView imgAnhDienThoai;
        public ImageView imgDelete,imgEdit;
        public LinearLayout groupInforPhone;
    }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SanPham sanPham = getItem(position);
            ViewHolder viewHolder = null;
        if(viewHolder == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dong_dienthoai, null);
            viewHolder.txtTenDienThoai = convertView.findViewById(R.id.txtTenSanPham);
            viewHolder.txtGiaDienThoai = convertView.findViewById(R.id.txtGiaDienThoai);
            viewHolder.txtMoTaDienThoai = convertView.findViewById(R.id.txtMoTaDienThoai);
            viewHolder.imgAnhDienThoai = convertView.findViewById(R.id.imgAnhDienThoai);
            viewHolder.imgDelete = convertView.findViewById(R.id.imgDelete);
            viewHolder.imgEdit = convertView.findViewById(R.id.imgEdit);
            viewHolder.groupInforPhone = convertView.findViewById(R.id.groupInforPhone);

            convertView.setTag(viewHolder);

            viewHolder.imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListenerUpdate.onClickUpdate(sanPham);
                }
            });
            viewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListenerDelete.onClickDelete(sanPham);
                }
            });
            viewHolder.groupInforPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListenerDetail.onClickDetail(sanPham);
                }
            });
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtTenDienThoai.setText(sanPham.getTensanpham());
//        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.txtGiaDienThoai.setText("Giá: " + sanPham.getGiasanpham() + " VNĐ");
        viewHolder.txtMoTaDienThoai.setMaxLines(2);
        viewHolder.txtMoTaDienThoai.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txtMoTaDienThoai.setText(sanPham.getMotasanpham());
        Picasso.get().load(sanPham.getLinkImage())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(viewHolder.imgAnhDienThoai);
        return convertView;

        }
    }

