package com.example.tari9;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class sign_up extends Activity {

    private static final String TAG = "PhoneAuthActivity";
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private EditText firstname, lastname, fphone, phone, code;
    private TextView resend, sign_in;
    private LinearLayout fullname, phone_nbr, codee, signin;
    private Button next, sign_up;
    private String nbr_phone, first_name, last_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        fullname = findViewById(R.id.fullname);
        fphone = findViewById(R.id.fphone);
        phone = findViewById(R.id.phone);
        phone_nbr = findViewById(R.id.phone_nbr);
        next = findViewById(R.id.next);
        signin = findViewById(R.id.signin);
        sign_in = findViewById(R.id.sign_in);

        codee = findViewById(R.id.codee);
        code = findViewById(R.id.code);
        sign_up = findViewById(R.id.sign_up);
        resend = findViewById(R.id.resend);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        firstname.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    hideKeyboard();
                    if (firstname.getText().toString().isEmpty()){
                        firstname.setHint("First Name");
                    }
                }
                else {
                    firstname.setHint("");
                }
            }
        });
        lastname.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    hideKeyboard();
                    if (lastname.getText().toString().isEmpty()){
                        lastname.setHint("Last Name");
                    }
                }
                else {
                    lastname.setHint("");
                }
            }
        });
        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    hideKeyboard();
                    if (phone.getText().toString().isEmpty()){
                        phone.setHint("000 000 000");
                    }
                }
                else {
                    phone.setHint("");
                }
            }
        });
        fphone.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    hideKeyboard();
                    if (fphone.getText().toString().isEmpty()){
                        fphone.setHint("+213");
                    }
                }
                else {
                    fphone.setHint("");
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                if ((phone.getText()!=null)&&(fphone.getText()!=null)){
                    if ((fphone.getText().toString().trim().length()<=9)&&
                            (fphone.getText().toString().trim().length()>1)&&
                            (fphone.getText().toString().charAt(0) == '+')){
                        if ((phone.getText().toString().trim().length()==9)&&
                                (Integer.parseInt(phone.getText().toString().trim().substring(0,1))!=0)){
                            nbr_phone = fphone.getText().toString().trim()+phone.getText().toString().trim();
                            first_name = firstname.getText().toString();
                            last_name = lastname.getText().toString();

                            startPhoneNumberVerification(nbr_phone);
                        }
                        else {
                            Toast.makeText(sign_up.this, "Verify your phone number",Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(sign_up.this, "Verify country code",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(sign_up.this,"A field id empty",Toast.LENGTH_LONG).show();
                }
            }
        });
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(sign_up.this, sign_in.class);
                sign_up.this.startActivity(intent);
                finish();
            }
        });
        code.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    hideKeyboard();
                    if (code.getText().toString().isEmpty()){
                        code.setHint("00 00 00");
                    }
                }
                else {
                    code.setHint("");
                }
            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                if (code.getText().toString().trim().length()==6){
                    verifyPhoneNumberWithCode(mVerificationId, code.getText().toString());
                }
                else {
                    Toast.makeText(sign_up.this, "Verify your code",Toast.LENGTH_LONG).show();
                }
            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode(nbr_phone,mResendToken);
            }
        });
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                fullname.setVisibility(View.GONE);
                phone_nbr.setVisibility(View.GONE);
                signin.setVisibility(View.GONE);
                next.setVisibility(View.GONE);
                codee.setVisibility(View.VISIBLE);
                sign_up.setVisibility(View.VISIBLE);
                resend.setVisibility(View.VISIBLE);
                sign_in.setVisibility(View.VISIBLE);
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) sign_up.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(phone.getWindowToken(), 0);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        updateUI(currentUser);
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            create_client(auth.getUid(), first_name, last_name, nbr_phone);
                            Intent in = new Intent(sign_up.this, menu.class);
                            sign_up.this.startActivity(in);
                            finish();
                        }
                    }
                });
            }

    private void create_client(String id, String first_name, String last_name, String phone) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {
                            String token = task.getResult();

                            DocumentReference userRef = db.collection("client").document(""+id);
                            Map<String, Object> m = new HashMap<>();
                            m.put("id", id);
                            m.put("phone", phone);
                            m.put("firstname", first_name);
                            m.put("lastname", last_name);
                            m.put("token", token);
                            userRef.set(m);
                        } else {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        }
                    }
                });
    }
    private void updateUI(FirebaseUser user) {

    }
}