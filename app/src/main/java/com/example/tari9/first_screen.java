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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class first_screen extends Activity {

    private static final String TAG = "PhoneAuthActivity";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private EditText fphone, phone, code;
    private TextView resend,tv;
    private Button next, login;
    private String nbrphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        fphone = findViewById(R.id.fphone);
        phone = findViewById(R.id.phone);
        code = findViewById(R.id.code);
        resend = findViewById(R.id.resend);
        next = findViewById(R.id.next);
        login = findViewById(R.id.login);
        tv = findViewById(R.id.tv);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
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
                    if ((fphone.getText().toString().length()<=9)&&
                            (fphone.getText().toString().length()>1)&&
                            (fphone.getText().toString().charAt(0) == '+')){
                        if ((phone.getText().toString().length()==9)&&
                                (Integer.parseInt(phone.getText().toString().substring(0,1))!=0)){
                            nbrphone = fphone.getText().toString()+phone.getText().toString();
                            startPhoneNumberVerification(nbrphone);
                        }
                        else {
                            Toast.makeText(first_screen.this, "Verify your phone number",Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(first_screen.this, "Verify country code",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(first_screen.this,"A field id empty",Toast.LENGTH_LONG).show();
                }
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
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                if (code.getText().toString().length()==6){
                    verifyPhoneNumberWithCode(mVerificationId, code.getText().toString());
                }
                else {
                    Toast.makeText(first_screen.this, "Verify your code",Toast.LENGTH_LONG).show();
                }
            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode(phone.getText().toString(),mResendToken);
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

                tv.setText("Enter Code:");
                fphone.setVisibility(View.GONE);
                phone.setVisibility(View.GONE);
                next.setVisibility(View.GONE);
                code.setVisibility(View.VISIBLE);
                resend.setVisibility(View.VISIBLE);
                login.setVisibility(View.VISIBLE);
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) first_screen.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(phone.getWindowToken(), 0);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
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
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();

                            db.collection("worker").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            token_worker(user.getUid());
                                            Intent in = new Intent(first_screen.this, list_requests_worker.class);
                                            first_screen.this.startActivity(in);
                                            finish();
                                        } else {
                                            db.collection("client").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {
                                                            update_client(user.getUid(), nbrphone);
                                                            Intent in = new Intent(first_screen.this, menu.class);
                                                            first_screen.this.startActivity(in);
                                                            finish();
                                                        } else {
                                                            create_client(user.getUid(), nbrphone);
                                                            Intent in = new Intent(first_screen.this, menu.class);
                                                            first_screen.this.startActivity(in);
                                                            finish();
                                                        }
                                                    } else {
                                                        Log.d(TAG, "Failed to get document.", task.getException());
                                                    }
                                                }
                                            });

                                        }
                                    } else {
                                        Log.d(TAG, "Failed to get document.", task.getException());
                                    }
                                }
                            });
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            }
                        }
                    }
                });
    }

    private void create_client(String id, String phone) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {
                            String token = task.getResult();
                            Log.d(TAG, "Firebase token: " + token);

                            // Store the device token in Firestore
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference userRef = db.collection("client").document(""+id);
                            Map<String, Object> m = new HashMap<>();
                            m.put("id", id);
                            m.put("phone", phone);
                            m.put("token", token);
                            userRef.set(m);
                        } else {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        }
                    }
                });
    }
    private void update_client(String id, String phone) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {
                            String token = task.getResult();
                            Log.d(TAG, "Firebase token: " + token);

                            // Store the device token in Firestore
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference userRef = db.collection("client").document(id);
                            Map<String, Object> m = new HashMap<>();
                            m.put("id", id);
                            m.put("phone", phone);
                            m.put("token", token);
                            userRef.update(m); // create a new user object with the token
                        } else {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        }
                    }
                });
    }
    private void token_worker(String id){
        db.collection("worker").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot dc = task.getResult();
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (task.isSuccessful()) {
                                    String token = task.getResult();
                                    Log.d(TAG, "Firebase token: " + token);

                                    // Store the device token in Firestore
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    DocumentReference userRef = db.collection(dc.get("type").toString()).document(id);
                                    Map<String, Object> m = new HashMap<>();
                                    m.put("token", token);
                                    userRef.update(m); // create a new user object with the token
                                } else {
                                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                }
                            }
                        });
            }
        });

    }
    private void updateUI(FirebaseUser user) {

    }
}