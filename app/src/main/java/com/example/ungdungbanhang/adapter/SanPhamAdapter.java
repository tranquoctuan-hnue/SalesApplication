package com.example.ungdungbanhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ungdungbanhang.R;
import com.example.ungdungbanhang.model.SanPham;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.installations.internal.FidListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.ItemHolder> {
    Context context;
    ArrayList<SanPham> arraysanpham;
    DienThoaiAdapter.ClickListenerDetail clickListenerDetail;



    public interface ClickListenerDetail{
        void onClickDetail(SanPham sanPham);
    }

    public SanPhamAdapter(Context context, ArrayList<SanPham> arraysanpham, DienThoaiAdapter.ClickListenerDetail clickListenerDetail) {
        this.context = context;
        this.arraysanpham = arraysanpham;
        this.clickListenerDetail = clickListenerDetail;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dong_sanphammoinhat, null);
        ItemHolder itemHolder = new ItemHolder(v);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        SanPham sanPham = arraysanpham.get(position);
        holder.txttensanpham.setText(sanPham.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtgiasanpham.setText("Giá: " + sanPham.getGiasanpham() + " VĐN");
        Picasso.get().load(sanPham.getLinkImage())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(holder.imghinhanhsanpham);
        holder.groupInforPhoneNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListenerDetail.onClickDetail(sanPham);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arraysanpham.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        public ImageView imghinhanhsanpham;
        public TextView txttensanpham,txtgiasanpham;
        public LinearLayout groupInforPhoneNew;
        public ItemHolder(View itemView) {
            super(itemView);
            imghinhanhsanpham = itemView.findViewById(R.id.imgSanPham);
            txttensanpham = itemView.findViewById(R.id.txtTenSanPham);
            txtgiasanpham = itemView.findViewById(R.id.txtGiaSanPham);
            groupInforPhoneNew = itemView.findViewById(R.id.groupInforPhoneNew);
        }
    }
}
