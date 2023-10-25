package com.example.ungdungbanhang.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.ungdungbanhang.R;
import com.example.ungdungbanhang.adapter.DienThoaiAdapter;
import com.example.ungdungbanhang.adapter.SanPhamAdapter;
import com.example.ungdungbanhang.model.GioHang;
import com.example.ungdungbanhang.model.LoaiSP;
import com.example.ungdungbanhang.model.SanPham;
import com.example.ungdungbanhang.ultil.CheckConnection;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewHomePage;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ArrayList<LoaiSP> mangLoaiSP;
    ArrayList<SanPham> mangsanpham;
    public static ArrayList<GioHang> ArrCart;
    SanPhamAdapter sanPhamAdapter;
    private ImageView imgAvatar;
    private TextView txtName,txtEmail;
//    private DrawerLayout drawerLayout;
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_PHONE = 1;
    private static final int FRAGMENT_LOCATION = 2;
    private static final int FRAGMENT_MY_PROFILE = 3;
    private int mCurrentFragment = FRAGMENT_HOME;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolBarHomePage);
        viewFlipper = findViewById(R.id.viewFlipper);
        recyclerViewHomePage = findViewById(R.id.recyclerView);
        drawerLayout = findViewById(R.id.drawerLayout);
        mangLoaiSP = new ArrayList<>();
        mangsanpham = new ArrayList<>();
        if(ArrCart != null){
        }
        else {
            ArrCart = new ArrayList<>();
        }
        sanPhamAdapter = new SanPhamAdapter(getApplicationContext(), mangsanpham, new DienThoaiAdapter.ClickListenerDetail() {
            @Override
            public void onClickDetail(SanPham sanPham) {
                showDetailActivity(sanPham);
            }
        });
        recyclerViewHomePage.setHasFixedSize(true);
        recyclerViewHomePage.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerViewHomePage.setAdapter(sanPhamAdapter);
        if(CheckConnection.haveNetworkConnection(getApplicationContext()))
        {
            initUi();
            ActionBar();
            ActionViewFlipper();
            getDataNew();
            showUserInfor();
        }
        else {
            CheckConnection.showToast(getApplicationContext(),"Bạn hãy kiểm tra lại kết nối");
            finish();
        }
    }

    public void showUserInfor() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null)
        {
            return;
        }
        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();
        if(name == null){
            txtName.setVisibility(View.GONE);
        }
        else {
            txtName.setVisibility(View.VISIBLE);
            txtName.setText(name);
        }
        txtEmail.setText(email);
        Glide.with(this).load(photoUrl).error(R.drawable.avatar).into(imgAvatar);
    }

    private void initUi() {
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
        imgAvatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imgAvatar);
        txtEmail = navigationView.getHeaderView(0).findViewById(R.id.txtEmail);
        txtName = navigationView.getHeaderView(0).findViewById(R.id.txtName);

    }

    private void showDetailActivity(SanPham sanPham) {
        Intent intent = new Intent(getApplicationContext(),DetailPhoneActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("SanPham", sanPham);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    //Lấy dữ liệu để thành sản phẩm mới nhất
    private void getDataNew() {
        FirebaseDatabase database;
        DatabaseReference data;
        database = FirebaseDatabase.getInstance();
        data = database.getReference("DIENTHOAI");
        Query query = data.limitToLast(6);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                SanPham sp = snapshot.getValue(SanPham.class);
                if(sp != null)
                {
                    mangsanpham.add(sp);
                    sanPhamAdapter.notifyDataSetChanged();
                }

            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                SanPham sp = snapshot.getValue(SanPham.class);
                if(sp == null || mangsanpham == null || mangsanpham.isEmpty()){
                    return;
                }
                for(int i = 0 ; i <  mangsanpham.size(); i++)
                {
                    if(sp.getId() == mangsanpham.get(i).getId()){
                        mangsanpham.set(i,sp);
                        break;
                    }
                }
                sanPhamAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                SanPham sp = snapshot.getValue(SanPham.class);
                if(sp == null || mangsanpham == null || mangsanpham.isEmpty()){
                    return;
                }
                for(int i = 0 ; i <  mangsanpham.size(); i++)
                {
                    if(sp.getId() == mangsanpham.get(i).getId()){
                        mangsanpham.remove(mangsanpham.get(i));
                        break;
                    }
                }
                sanPhamAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Hàm chạy quảng cáo
    private void ActionViewFlipper() {
        ArrayList<String> mangQuangCao = new ArrayList<>();
        mangQuangCao.add("https://bloganchoi.com/wp-content/uploads/2020/02/jennie-red4.jpg");
        mangQuangCao.add("https://4.bp.blogspot.com/-vEcWxzApsg4/V4ptylc-Y-I/AAAAAAAAGlw/6oEW1Xfku_QtwqcWQqjB-nL5xt7SGaxSQCLcB/s1600/hongocha-quangcao-dienthoai-oppo-f1-plus.jpg");
        mangQuangCao.add("https://uploads-ssl.webflow.com/6073fad993ae97919f0b0772/609fa687874b84361fc495db_%C4%91t.jpg");
        for(int  i = 0 ; i < mangQuangCao.size(); i ++)
        {
            ImageView imageView = new ImageView(getApplicationContext());
            Picasso.get().load(mangQuangCao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY); // Chỉnh kích thước vừa đủ viewFlipper
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(4000); // Thời gian thay đổi, giống như delay vậy
        viewFlipper.setAutoStart(true);		// Tự động chạy khi mở màn hình
        Animation slidein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation slideout = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(slidein);
        viewFlipper.setOutAnimation(slideout);
    }

    //Hàm menu trong màn hình chính
    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // hiện icon menu trên toolbar
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size); // đặt icon cho cái nút
        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.END));
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_home)
        {
            if(mCurrentFragment != FRAGMENT_HOME){
                Intent intent = new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent);
                mCurrentFragment = FRAGMENT_HOME;
            }

        }
        else if(id == R.id.nav_dienThoai){
                Intent intent = new Intent(MainActivity.this,DienThoaiActivity.class);
                startActivity(intent);
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else if(id == R.id.nav_diaChi){
                Intent intent = new Intent(MainActivity.this,ThongTinActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
        }
        else if(id == R.id.nav_dangXuat){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this,SignlnActivity.class);
            startActivity(intent);
            finish();
        }
        else if(id == R.id.nav_thongTin){
                Intent intent = new Intent(MainActivity.this,LienHeActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}