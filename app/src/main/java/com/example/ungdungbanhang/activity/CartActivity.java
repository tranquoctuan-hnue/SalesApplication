package com.example.ungdungbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ungdungbanhang.R;
import com.example.ungdungbanhang.activity.MainActivity;
import com.example.ungdungbanhang.adapter.GiaHangAdapter;
import com.example.ungdungbanhang.model.GioHang;
import com.example.ungdungbanhang.model.SanPham;
import com.squareup.picasso.Picasso;

import java.net.Inet4Address;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class CartActivity extends AppCompatActivity {
    androidx.appcompat.widget.Toolbar toolbarCart;
    ListView lvCart;
    TextView txtNotify,txtTongTien;
    Button btnThanhToan,btnThoat;
    GiaHangAdapter giaHangAdapter;
    Dialog dialogInforClient;
    int tongTien = 0 ;
    Button Send;
    EditText NameClient,Email,NumberPhone,Adress;
    String id, tenSp ,giaSP,hinhAnhSP;
//    public static ArrayList<GioHang> ArrCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        lvCart = findViewById(R.id.lvCart);
        giaHangAdapter = new GiaHangAdapter(getApplicationContext(),MainActivity.ArrCart);
//        lvCart.setAdapter(giaHangAdapter);
        giaHangAdapter.notifyDataSetChanged();

        initUi();
        ActionToolBar();
        EvenUltit();
        CheckData();
        initListener();
        onClickInforClient();
    }

    private void initListener() {
        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tongTien == 0)
                {
                    Toast.makeText(getApplicationContext(), "Bạn chưa có sản phẩm nào. Vui lòng chọn mua sản phẩm !!!", Toast.LENGTH_LONG).show();
                }
                else{
                    dialogInforClient.show();
                }

            }
        });
    }

    private void onClickInforClient() {
        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String fromEmail = "quoctuan19032k@gmail.com";
                    String emailPassword = "nfuapawxqznvevev";
                    String toemail = Email.getText().toString();
                    String fullname = NameClient.getText().toString();
                    String SDT = NumberPhone.getText().toString();
                    String DiaChi = Adress.getText().toString();
                    String host = "smtp.gmail.com";
                    Properties properties = System.getProperties();
                    properties.put("mail.smtp.host", host);
                    properties.put("mail.smtp.port", "465");
                    properties.put("mail.smtp.ssl.enable", "true");
                    properties.put("mail.smtp.auth", "true");
                    Session session = Session.getInstance(properties, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(fromEmail,emailPassword);
                        }
                    });

                    MimeMessage mimeMessage = new MimeMessage(session);
//                    mimeMessage.addRecipient(Message.Rec);
                    mimeMessage.addRecipient(Message.RecipientType.TO,
                            new InternetAddress(toemail));
                    mimeMessage.setSubject(fullname);
                    mimeMessage.setText("Tên khách hàng: " + fullname + "\n" + "Số điện thoại: " +SDT + "\n"+"Địa chỉ: "  + DiaChi);
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Transport.send(mimeMessage);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                    Email.setText("");
                    NameClient.setText("");
                    NumberPhone.setText("");
                    Adress.setText("");
                    Toast.makeText(CartActivity.this, "Đặt hàng thành công! " + toemail,
                            Toast.LENGTH_SHORT).show();
                    dialogInforClient.dismiss();
                    txtTongTien.setText(Integer.toString(0) + " VNĐ");
                    txtNotify.setVisibility(View.VISIBLE);
                    lvCart.setVisibility(View.INVISIBLE);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(CartActivity.this, "có sự cố xảy ra", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void EvenUltit() {

        for(int i = 0 ; i < MainActivity.ArrCart.size(); i ++)
        {
            tongTien += Integer.parseInt(MainActivity.ArrCart.get(i).getGiaSP());
            System.out.print("Ten sp: " + MainActivity.ArrCart.get(i).getTenSP());
        }
        txtTongTien.setText(Integer.toString(tongTien) + " VNĐ");
    }
    private void CheckData() {
        if(MainActivity.ArrCart.size() <= 0){

            txtNotify.setVisibility(View.VISIBLE);
            lvCart.setVisibility(View.INVISIBLE);
        }
        else {
//            giaHangAdapter.notifyDataSetChanged();
            txtNotify.setVisibility(View.INVISIBLE);
            lvCart.setVisibility(View.VISIBLE);
        }
    }

    private void initUi() {
        toolbarCart = findViewById(R.id.toolbarCart);
        txtNotify = findViewById(R.id.txtNotify);
        txtTongTien = findViewById(R.id.txtTongTien);
        btnThanhToan = findViewById(R.id.btnThanhToan);
        btnThoat = findViewById(R.id.btnThoat);
        dialogInforClient = new Dialog(this);
        dialogInforClient.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogInforClient.setCanceledOnTouchOutside(false);
        dialogInforClient.setContentView(R.layout.to_send_infor);
        NameClient = dialogInforClient.findViewById(R.id.editNameClient);
        Email = dialogInforClient.findViewById(R.id.edtEmail);
        NumberPhone = dialogInforClient.findViewById(R.id.edtNumberPhone);
        Adress = dialogInforClient.findViewById(R.id.edtAdress);
        Send = dialogInforClient.findViewById(R.id.btnSend);
    }
    private void ActionToolBar() {
        setSupportActionBar(toolbarCart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarCart.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}