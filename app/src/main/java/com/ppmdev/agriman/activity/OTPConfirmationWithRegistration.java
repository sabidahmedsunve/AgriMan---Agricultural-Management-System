package com.ppmdev.agriman.activity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.ppmdev.agriman.R;
import com.ppmdev.agriman.model.UserModel;
import com.ppmdev.agriman.utils.AndroidUtil;
import com.ppmdev.agriman.utils.FirebaseUtil;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class OTPConfirmationWithRegistration extends AppCompatActivity {
    Long timeOutSeconds = 60L;
    String verificationCode;
    PhoneAuthProvider.ForceResendingToken resendingToken;

    private String userName;
    private String userEmail;
    private String userPhone;
    private String userPass;
    private TextView mResendOtp;
    private TextView mOtpTimeCountdown;
    private Button btnVerifyOtp;
    private PinView otpPinView;

    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    private UserModel userModel;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otpconfirmation_with_registration);

        init(); // initialization

        sendOTP(userPhone, false);

        AndroidUtil.showToast(getApplicationContext(), userPhone);

        btnVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setInProgress(true);
                String userEnteredOTP = otpPinView.getText().toString();
                if (userEnteredOTP != null) {
                    if (!userEnteredOTP.isEmpty()) {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, userEnteredOTP);
                        signIn(credential);
                    } else {
                        AndroidUtil.showToast(getApplicationContext(), "Wrong OTP");
                        otpPinView.clearComposingText();
                        setInProgress(false);
                    }
                } else {
                    AndroidUtil.showToast(getApplicationContext(), "Wrong OTP");
                    otpPinView.clearComposingText();
                    setInProgress(false);
                }
            }
        });

        mResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOTP(userPhone, true);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            //reload();
        }
    }

    void init() {
        mResendOtp = findViewById(R.id.tv_btnResend_otp);
        mOtpTimeCountdown = findViewById(R.id.aotpv_tv_otp_countdown);
        btnVerifyOtp = findViewById(R.id.btn_OTPVerify);
        otpPinView = findViewById(R.id.pinViewOTP);
        progressBar = findViewById(R.id.pb_progressBar);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        userName = getIntent().getStringExtra("userName").toString();
        userEmail = getIntent().getStringExtra("userEmail").toString();
        userPhone = getIntent().getStringExtra("userPhone").toString();
        userPass = getIntent().getStringExtra("userPass").toString();
    }

    void sendOTP(String userPhoneNumber, boolean isResend) {    // sending otp
        otpTimer();
        PhoneAuthOptions.Builder builder =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(userPhoneNumber)
                        .setTimeout(timeOutSeconds, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                Log.d(TAG, "onVerificationCompleted: Verification completed");
                                signIn(phoneAuthCredential);
                                setInProgress(false);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Log.w(TAG, "onVerificationFailed: Verification failed", e);
                                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                    AndroidUtil.showToast(getApplicationContext(), "Invalid phone number.");
                                } else if (e instanceof FirebaseTooManyRequestsException) {
                                    AndroidUtil.showToast(getApplicationContext(), "Quota exceeded. Try again later.");
                                } else {
                                    AndroidUtil.showToast(getApplicationContext(), "OTP verification failed");
                                }
                                setInProgress(false);
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationCode = s;
                                resendingToken = forceResendingToken;
                                Log.d(TAG, "onCodeSent: OTP sent successfully to " + userPhoneNumber);
                                AndroidUtil.showToast(getApplicationContext(), "OTP sent successfully");
                                setInProgress(false);
                            }
                        });

        if (isResend) {
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        } else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }


    private void signIn(PhoneAuthCredential phoneAuthCredential) {
        setInProgress(true);
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    setInProgress(false);
                    setUserPhone();
                    linkEmailAuthentication();
                } else {
                    setInProgress(false);
                    AndroidUtil.showToast(getApplicationContext(), "OTP verification failed");
                }
            }
        });
    }

    private void setInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            btnVerifyOtp.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            btnVerifyOtp.setVisibility(View.VISIBLE);
        }
    }

    void otpTimer() {
        long timeDuration = TimeUnit.MINUTES.toMillis(1);

        new CountDownTimer(timeDuration, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                mResendOtp.setVisibility(View.GONE);
                mOtpTimeCountdown.setVisibility(View.VISIBLE);

                String sDuration = String.format(Locale.ENGLISH, "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

                mOtpTimeCountdown.setText(sDuration);
            }

            @Override
            public void onFinish() {
                mOtpTimeCountdown.setVisibility(View.GONE);
                mResendOtp.setVisibility(View.VISIBLE);
            }

        }.start();
    }

    void setUserPhone() {
        if (userModel != null) {
            userModel.setUserPhone(userPhone);
        } else {
            userModel = new UserModel(userName, userEmail, userPass, userPhone);
        }

        FirebaseUtil.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //AndroidUtil.showToast(getApplicationContext(), "OK");
                    Intent intent = new Intent(OTPConfirmationWithRegistration.this, HomePage.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    //AndroidUtil.showToast(getApplicationContext(), "NOT OK");
                }
            }
        });
    }

    void linkEmailAuthentication() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(userEmail, userPass);

            user.linkWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d("Link", "linkWithCredential:success");
                        FirebaseUser linkedUser = task.getResult().getUser();
                        //AndroidUtil.showToast(getApplicationContext(), "Email and password linked successfully");
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Log.w("Link", "linkWithCredential:failure - email already in use", task.getException());
                            AndroidUtil.showToast(getApplicationContext(), "Email already in use");
                        } else {
                            Log.w("Link", "linkWithCredential:failure", task.getException());
                            AndroidUtil.showToast(getApplicationContext(), "Failed to link email and password");
                        }
                    }
                }
            });
        }
    }
}
