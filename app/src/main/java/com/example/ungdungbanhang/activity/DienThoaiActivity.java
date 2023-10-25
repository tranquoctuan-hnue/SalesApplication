package com.example.ungdungbanhang.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ungdungbanhang.R;
import com.example.ungdungbanhang.adapter.DienThoaiAdapter;
import com.example.ungdungbanhang.model.SanPham;
import com.example.ungdungbanhang.ultil.CheckConnection;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.io.IOException;

import java.util.ArrayList;

import java.util.HashMap;

import java.util.List;
import java.util.Map;


enum Img_AccType {
    UPLOAD_IMG,
    EDIT_IMG,
};
enum Data_AccType {
    UPLOAD_DATA,
    EDIT_DATA,
    DEL_DATA
};

public class DienThoaiActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 22;
    private static final int PICK_IMAGE_EDIT = 23;
    androidx.appcompat.widget.Toolbar toolbardt;
    DienThoaiAdapter phoneAdapter;
    ArrayList<SanPham> arrayPhone;
    Dialog dialog;
    Dialog suaSPDialog;
    ListView lvPhone;
    DatabaseReference data;
    Button themSP, thoatThemSP;
    ImageView imgChonAnh;
    ImageView imgAnhSua;
    StorageReference storageReference;
    FirebaseStorage storage;
    FirebaseDatabase database;
    private Uri filePath;
    String id;
    String ten,moTa,gia;
    String tenSua,giaSua,moTaSua;
    String linkImage = "";
    View footerView;
    boolean isLoading = false;
    boolean limit = false;
    androidx.appcompat.widget.SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dien_thoai);
        toolbardt = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolBarDienThoai);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        lvPhone = findViewById(R.id.lvDienThoai);
        arrayPhone = new ArrayList<>();
        searchView =    findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;

            }
        });
        phoneAdapter = new DienThoaiAdapter(getApplicationContext(), arrayPhone, new DienThoaiAdapter.ClickListenerUpdate() {
            @Override
            public void onClickUpdate(SanPham sanPham) {
                showSuaSPDialog(sanPham);
            }
        }, new DienThoaiAdapter.ClickListenerDelete() {
            @Override
            public void onClickDelete(SanPham sanPham) {
                showDeletePhoneDialog(sanPham);
            }
        }, new DienThoaiAdapter.ClickListenerDetail() {
            @Override
            public void onClickDetail(SanPham sanPham) {
                showDetailActivity(sanPham);
            }
        });

        if(CheckConnection.haveNetworkConnection(getApplicationContext())) {
            lvPhone.setAdapter(phoneAdapter);
            ActionToolBar();
            getData();
            addDataToFirebase();
//            loadMoreData();
        }
        else {
            CheckConnection.showToast(getApplicationContext(),"Bạn hãy kiểm tra lại kết nối");
            finish();
        }
    }

    private void showDetailActivity(SanPham sanPham) {
        Intent intent = new Intent(getApplicationContext(),DetailPhoneActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("SanPham", sanPham);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void addDataToFirebase() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_them_san_pham);
        EditText tenSP = dialog.findViewById(R.id.edtTenSP);
        EditText giaSP = dialog.findViewById(R.id.edtGiaSP);
        EditText moTaSP = dialog.findViewById(R.id.edtMoTa);
        imgChonAnh = dialog.findViewById(R.id.imgAnhSanPham);
        Button chonAnh = dialog.findViewById(R.id.btnChonA);
        thoatThemSP = dialog.findViewById(R.id.btnHuyThemSP);
        themSP = dialog.findViewById(R.id.btnThemSP);
        chonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage(Img_AccType.UPLOAD_IMG);
            }
        });
        thoatThemSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        themSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idPhoneNew = data.push().getKey();
                id = idPhoneNew;
                ten = tenSP.getText().toString();
                gia = giaSP.getText().toString();
                moTa = moTaSP.getText().toString();
                if(ten.isEmpty() || moTa.isEmpty() || giaSP.getText().toString().isEmpty()){
                    Toast.makeText(DienThoaiActivity.this,"Xin hãy điền đủ thông tin", Toast.LENGTH_LONG).show();
                }
                uploadImage(Data_AccType.UPLOAD_DATA);
            }
        });
    }

    private void uploadImage(Data_AccType accType) {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        if(accType == Data_AccType.EDIT_DATA)
        {
            StorageReference desertRef  = storageReference.child(id+"jpg");
            desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        }
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        StorageReference ref = storageReference.child(id+".jpg");
        ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                progressDialog.dismiss();Toast.makeText(DienThoaiActivity.this, "Thêm sản phầm thành công!!", Toast.LENGTH_SHORT).show();
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri Downloaduri) {
                        linkImage = Downloaduri.toString();
                        if(accType == Data_AccType.UPLOAD_DATA){
                            database = FirebaseDatabase.getInstance();
                            DatabaseReference data = database.getReference("DIENTHOAI");
                            data.child(id).setValue(new SanPham(id, ten, gia,moTa,linkImage));
                            filePath = null;
                            dialog.dismiss();
                        }
                        else if(accType == Data_AccType.EDIT_DATA){
                            Map<String,Object> map = new HashMap<>();
                            map.put("tensanpham",tenSua);
                            map.put("giasanpham",giaSua);
                            map.put("motasanpham",moTaSua);
                            map.put("linkImage",linkImage);
                            FirebaseDatabase.getInstance().getReference().child("DIENTHOAI")
                                    .child(id).updateChildren(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            suaSPDialog.dismiss();
                                            Toast.makeText(DienThoaiActivity.this, "Sửa sản phẩm thành công !!", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(DienThoaiActivity.this, "Lỗi !! kiểm tra lại !!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                progressDialog.dismiss();Toast.makeText(DienThoaiActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
            {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());progressDialog.setMessage("Uploaded " + (int)progress + "%");
            }
        });
    }

    private void getData() {
        database = FirebaseDatabase.getInstance();
        data = database.getReference("DIENTHOAI");
        data.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                SanPham sp = snapshot.getValue(SanPham.class);
                if(sp != null)
                {
                    arrayPhone.add(sp);
                    phoneAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                SanPham sp = snapshot.getValue(SanPham.class);
                if(sp == null || arrayPhone == null || arrayPhone.isEmpty()){
                    return;
                }
                for(int i = 0 ; i <  arrayPhone.size(); i++)
                {

                    if(sp.getId() == arrayPhone.get(i).getId()){
                        arrayPhone.set(i,sp);
                        break;
                    }

                }
                phoneAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                SanPham sp = snapshot.getValue(SanPham.class);
                if(sp == null || arrayPhone == null || arrayPhone.isEmpty()){
                    return;
                }
                for(int i = 0 ; i <  arrayPhone.size(); i++)
                {
                    if(sp.getId() == arrayPhone.get(i).getId()){
                        arrayPhone.remove(arrayPhone.get(i));
                        break;
                    }
                }
                phoneAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showSuaSPDialog(@NonNull SanPham sp){
        suaSPDialog = new Dialog(this);
        suaSPDialog.setContentView(R.layout.dialog_sua_san_pham);
        suaSPDialog.setCanceledOnTouchOutside(false);
        TextView idSP = suaSPDialog.findViewById(R.id.txtIdSP);
        TextView tenSP = suaSPDialog.findViewById(R.id.edtTenSP);
        TextView giaSp = suaSPDialog.findViewById(R.id.edtGiaSP);
        TextView moTaSP = suaSPDialog.findViewById(R.id.edtMoTa);
        imgAnhSua = suaSPDialog.findViewById(R.id.imgChonAnh);
        Button chonAnhSua = suaSPDialog.findViewById(R.id.btnChonAnhSua);
        Button btnSuaSP = suaSPDialog.findViewById(R.id.btnSuaSP);
        Button btnThoat = suaSPDialog.findViewById(R.id.btnHuySuaSP);
        suaSPDialog.show();
        tenSP.setText(sp.getTensanpham());
        giaSp.setText(sp.getGiasanpham());
        moTaSP.setText(sp.getMotasanpham());
        idSP.setText(sp.getId());
        id = idSP.getText().toString().trim();
        Picasso.get().load(sp.getLinkImage())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(imgAnhSua);
        chonAnhSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage(Img_AccType.EDIT_IMG);
            }
        });
        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suaSPDialog.dismiss();
            }
        });
        btnSuaSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tenSua = tenSP.getText().toString();
                giaSua = giaSp.getText().toString();
                moTaSua = moTaSP.getText().toString();
                if(filePath == null){
                    linkImage = sp.getLinkImage();
                    Map<String,Object> map = new HashMap<>();
                    map.put("tensanpham",tenSua);
                    map.put("giasanpham",giaSua);
                    map.put("motasanpham",moTaSua);
                    map.put("linkImage",linkImage);
                    FirebaseDatabase.getInstance().getReference().child("DIENTHOAI")
                            .child(id).updateChildren(map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    suaSPDialog.dismiss();
                                    Toast.makeText(DienThoaiActivity.this, "Sửa sản phẩm thành công !!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(DienThoaiActivity.this, "Lỗi !! kiểm tra lại !!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else if(filePath != null){
                    uploadImage(Data_AccType.EDIT_DATA);
                }
            }
        });
    }

    private void showDeletePhoneDialog(SanPham sanPham) {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(this);
        dialogDelete.setMessage("Bạn có thực sự muốn xóa sản phẩm ");
        dialogDelete.setCancelable(false);
        dialogDelete.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database = FirebaseDatabase.getInstance();
                id = sanPham.getId();
                storage = FirebaseStorage.getInstance();
                storageReference = storage.getReference();
                StorageReference desertRef  = storageReference.child(id+".jpg");
                desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DatabaseReference myRef = database.getReference("DIENTHOAI");
                        myRef.child(id).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(DienThoaiActivity.this,"Xóa sản phẩm thành công !!!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(DienThoaiActivity.this,"Không thế xóa sản phẩm !!!", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
        dialogDelete.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialogDelete.show();
    }

    private void SelectImage(Img_AccType accType) {
        if(accType == Img_AccType.UPLOAD_IMG)
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
        }
        else if (accType == Img_AccType.EDIT_IMG){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_EDIT);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgChonAnh.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == PICK_IMAGE_EDIT && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgAnhSua.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //menu add item
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_them_san_pham, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuAddSP:
                dialog.show();
                break;
            case R.id.menuCart:
                Intent intent = new Intent(getApplicationContext(),CartActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void filterList(String Text) {
        ArrayList<SanPham> filterList = new ArrayList<>();
        for (SanPham sanPham : arrayPhone){
            if(sanPham.getTensanpham().toLowerCase().contains(Text.toLowerCase())){
                filterList.add(sanPham);
            }
            if(filterList == null){
                Toast.makeText(this, "Không tìm kiếm thấy sản phẩm nào!!", Toast.LENGTH_LONG).show();
            }else {
               phoneAdapter.setFilteredList(filterList);
            }
        }
    }

    //ToolBar comeback the main
    private void ActionToolBar() {
        setSupportActionBar(toolbardt);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbardt.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}