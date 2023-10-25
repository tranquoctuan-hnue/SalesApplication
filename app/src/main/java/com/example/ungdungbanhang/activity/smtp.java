package com.example.ungdungbanhang.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ungdungbanhang.R;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class smtp extends AppCompatActivity {
    Button btnSend;
    EditText editEmail,edtName,edtSDT, edtAdress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.to_send_infor);
        btnSend = findViewById(R.id.btnSend);
        editEmail = findViewById(R.id.edtEmail);
        edtName = findViewById(R.id.editNameClient);
        edtSDT = findViewById(R.id.edtNumberPhone);
        edtAdress = findViewById(R.id.edtAdress);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String fromEmail = "quoctuan19032k@gmail.com";
                    String emailPassword = "nfuapawxqznvevev";
                    String toemail = editEmail.getText().toString();
                    String fullname = edtName.getText().toString();
                    String SDT = edtSDT.getText().toString();
                    String DiaChi = edtAdress.getText().toString();
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
                    mimeMessage.setText(fullname + SDT + DiaChi);
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
                    editEmail.setText("");
                    edtName.setText("");
                    edtSDT.setText("");
                    edtAdress.setText("");
                    Toast.makeText(smtp.this, "Đặt hàng thành công! " + toemail,
                            Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(smtp.this, "có sự cố xảy ra", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
