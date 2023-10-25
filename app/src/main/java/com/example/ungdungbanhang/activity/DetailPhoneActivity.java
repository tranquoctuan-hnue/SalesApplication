package com.example.ungdungbanhang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ungdungbanhang.R;
import com.example.ungdungbanhang.model.GioHang;
import com.example.ungdungbanhang.model.SanPham;
import com.squareup.picasso.Picasso;

public class DetailPhoneActivity extends AppCompatActivity {
    androidx.appcompat.widget.Toolbar toolbarDetail;
    ImageView imgDetail;
    TextView txtDetailName,txtDetailPrice, txtDetail;
    Spinner spinner;
    Button btnAddToCart;
    String tenSp = "",moTaSP = "",hinhAnhSP= "",id= "";
    String giaSP="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_phone);
        initUi();
        ActionToobar();
        GetData();
        CatchEventSpinner();
        EventButtonCart();

    }

    private void EventButtonCart() {
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.ArrCart.size() > 0){
                    int sl =  Integer.parseInt(spinner.getSelectedItem().toString());
                    boolean exists = false;
                    for(int i = 0; i < MainActivity.ArrCart.size();i ++){
                        if(MainActivity.ArrCart.get(i).getId() == id){
                            MainActivity.ArrCart.get(i).setSoluongSP(MainActivity.ArrCart.get(i).getSoluongSP() + sl);
                            if(MainActivity.ArrCart.get(i).getSoluongSP() >= 10){
                                MainActivity.ArrCart.get(i).setSoluongSP(10);
                            }
                            MainActivity.ArrCart.get(i).setGiaSP(Integer.toString(Integer.parseInt(giaSP) * MainActivity.ArrCart.get(i).getSoluongSP()));
                            exists = true;
                        }
                    }
                    if(exists == false){
                        int soLuong =Integer.parseInt(spinner.getSelectedItem().toString());
                        int giaMoi = soLuong * Integer.parseInt(giaSP);
                        MainActivity.ArrCart.add(new GioHang(id,tenSp,Integer.toString(giaMoi),hinhAnhSP,soLuong));
                    }
                }
                else {
                    int soLuong =Integer.parseInt(spinner.getSelectedItem().toString());
                    int giaMoi = soLuong * Integer.parseInt(giaSP);
                    MainActivity.ArrCart.add(new GioHang(id,tenSp,Integer.toString(giaMoi),hinhAnhSP,soLuong));
                }

                System.out.println("Các phần tử có trong arrListDouble là: " + MainActivity.ArrCart);
                Intent intent = new Intent(getApplicationContext(),CartActivity.class);
                startActivity(intent);
            }
        });
    }

    private void CatchEventSpinner() {
        Integer[] Quantity = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, Quantity);
        spinner.setAdapter(adapter);
    }

    private void GetData() {
        if(getIntent().getExtras() != null ){
            SanPham sanPham = (SanPham) getIntent().getExtras().get("SanPham");
            id = sanPham.getId();
            tenSp = sanPham.getTensanpham();
            giaSP = sanPham.getGiasanpham();
            moTaSP = sanPham.getMotasanpham();
            hinhAnhSP = sanPham.getLinkImage();
            txtDetailName.setText(tenSp);
            txtDetailPrice.setText(giaSP + " VNĐ");
            txtDetail.setText(moTaSP);
            Picasso.get().load(hinhAnhSP)
                    .placeholder(R.drawable.noimage)
                    .error(R.drawable.error)
                    .into(imgDetail);

        }

    }

    private void ActionToobar() {
        setSupportActionBar(toolbarDetail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initUi() {
        toolbarDetail = findViewById(R.id.toobarDetail);
        imgDetail = findViewById(R.id.imgDetail);
        txtDetailName = findViewById(R.id.txtDetailName);
        txtDetailPrice = findViewById(R.id.txtDetailPrice);
        spinner = findViewById(R.id.spinner);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        txtDetail = findViewById(R.id.txtDetail);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuCart:
                Intent intent = new Intent(getApplicationContext(),CartActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}