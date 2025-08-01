package com.ppmdev.agriman.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.ppmdev.agriman.R;

import java.util.concurrent.TimeUnit;

public class NewRegistration extends AppCompatActivity {


    private Button regButton;
    private EditText userName;
    EditText userEmail;
    private EditText userPhone;
    private EditText userPass;
    private EditText userRetypePass;
    private CountryCodePicker countryCodePicker;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_newregistration);

        //initialization
        regButton=findViewById(R.id.btn_register);
        userName=findViewById(R.id.et_REG_name);        //id initialize in xml to design
        userEmail=findViewById(R.id.et_REG_email);
        userPhone=findViewById(R.id.et_REG_phn);
        userPass=findViewById(R.id.et_REG_pass);
        userRetypePass=findViewById(R.id.et_REG_retypepass);
        countryCodePicker=findViewById(R.id.countryCodePicker);

        countryCodePicker.registerCarrierNumberEditText(userPhone);  //countrywise


        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inputedUserName;
                String inputedUserEmail;
                String inputedUserPhn;
                String inputedUserPass;
                String inputedUserRetypePass;

                inputedUserName=userName.getText().toString();
                inputedUserEmail=userEmail.getText().toString();
                //inputedUserPhn=userPhone.getText().toString();
                inputedUserPass=userPass.getText().toString();
                inputedUserRetypePass=userRetypePass.getText().toString();


               if(TextUtils.isEmpty(inputedUserName)){
                   //Toast.makeText(NewRegistration.this, "Enter name", Toast.LENGTH_SHORT).show();
                   userName.setError("Enter Your Name");
                   return;
               }
                if(inputedUserEmail.matches(emailPattern) && TextUtils.isEmpty(inputedUserEmail)){
                    //Toast.makeText(NewRegistration.this, "Enter email", Toast.LENGTH_SHORT).show();
                    userEmail.setError("Enter valid email");
                    return;
                }
                if(TextUtils.isEmpty(inputedUserPass) || inputedUserPass.length()<6){
                    //Toast.makeText(NewRegistration.this, "Enter name", Toast.LENGTH_SHORT).show();
                    userPass.setError("Password at least 6 characters");
                    return;
                }
                if(TextUtils.isEmpty(inputedUserRetypePass) || !inputedUserPass.equals(inputedUserRetypePass)){
                    //Toast.makeText(NewRegistration.this, "Enter retype password", Toast.LENGTH_SHORT).show();
                    userRetypePass.setError("Password doesn't match");
                    return;
                }

                if(!countryCodePicker.isValidFullNumber()){
                    userPhone.setError("Phone number not valid");
                    return;
                } else if (countryCodePicker.isValidFullNumber()) {
                    Intent intent=new Intent(NewRegistration.this, OTPConfirmationWithRegistration.class);
                    intent.putExtra("userName", userName.getText().toString());
                    intent.putExtra("userEmail",userEmail.getText().toString());
                    intent.putExtra("userPhone",countryCodePicker.getFullNumberWithPlus());
                    intent.putExtra("userPass",userPass.getText().toString());
                    startActivity(intent);
                } else{
                    Toast.makeText(NewRegistration.this, "Please enter all filed", Toast.LENGTH_SHORT).show();
                }

            }
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

}