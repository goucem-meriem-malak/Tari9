package com.example.tari9;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class insign extends AppCompatActivity {
    private static final String TAG = "PhoneAuthActivity";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private EditText fphone, phone, code;
    private TextView resend, codetv,signin;
    private Button signup;
    private LinearLayout userdata, codedata;
    private ProgressBar progressBar;
    private String nbrphone;
    private AtomicBoolean isClicked;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insign);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        fphone = findViewById(R.id.fphone);
        phone = findViewById(R.id.phone);
        code = findViewById(R.id.code);

        signup = findViewById(R.id.signup);

        progressBar = findViewById(R.id.progressBar);

        resend = findViewById(R.id.resend);
        signin = findViewById(R.id.signin);
        codetv = findViewById(R.id.codetv);

        userdata = findViewById(R.id.userdata);
        codedata = findViewById(R.id.codedata);

        isClicked = new AtomicBoolean(false);

        fphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean hasText = s.toString().trim().length() > 0;

                int transparency = hasText ? 128 : 0;

                int color = Color.argb(transparency, 14, 96, 70);

                Drawable buttonBackground = signup.getBackground().mutate();
                GradientDrawable drawable = (GradientDrawable) buttonBackground;
                drawable.setColor(color);

                signup.setBackground(buttonBackground);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean hasText = s.toString().trim().length() > 0;

                int transparency = hasText ? 128 : 0;

                int color = Color.argb(transparency, 14, 96, 70);

                Drawable buttonBackground = signup.getBackground().mutate();
                GradientDrawable drawable = (GradientDrawable) buttonBackground;
                drawable.setColor(color);

                signup.setBackground(buttonBackground);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean hasText = s.toString().trim().length() > 0;

                int transparency = hasText ? 128 : 0;

                int color = Color.argb(transparency, 14, 96, 70);

                Drawable buttonBackground = signup.getBackground().mutate();
                GradientDrawable drawable = (GradientDrawable) buttonBackground;
                drawable.setColor(color);

                signup.setBackground(buttonBackground);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        fphone.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    hideKeyboard(fphone);
                }
            }
        });
        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    hideKeyboard(phone);
                    if (phone.getText().toString().isEmpty()){
                        phone.setHint("000 000 000");
                    }
                }
                else {
                    phone.setHint("");
                }
            }
        });
        code.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    hideKeyboard(code);
                    if (code.getText().toString().isEmpty()){
                        code.setHint("00 00 00");
                    }
                }
                else {
                    code.setHint("");
                }
            }
        });

        fphone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
                    phone.requestFocus();
                    return true;
                }
                return false;
            }
        });
        phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    hideKeyboard(phone);
                    return true;
                }
                return false;
            }
        });
        code.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    hideKeyboard(code);
                    return true;
                }
                return false;
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isClicked.set(true);

                if (signup.getText().equals("Next")){
                    //simulateBackgroundTask();
                    if (fphone.getText()!=null
                            && phone.getText()!=null){
                        if ((fphone.getText().toString().length()<=9)&&
                                (fphone.getText().toString().length()>1)&&
                                (fphone.getText().toString().charAt(0) == '+')){
                            if ((phone.getText().toString().length()==9)&&
                                    (Integer.parseInt(phone.getText().toString().substring(0,1))!=0)){
                                int color = Color.parseColor("#0E6046");
                                Drawable buttonBackground = signup.getBackground().mutate();
                                GradientDrawable drawable = (GradientDrawable) buttonBackground;
                                drawable.setColor(color);
                                signup.setBackground(buttonBackground);

                                nbrphone = fphone.getText().toString()+phone.getText().toString();
                                simulateBackgroundTask();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Verify your phone number",Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Verify country code",Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    hideKeyboard(code);
                    if (code.getText().toString().length()==6){
                        verifyPhoneNumberWithCode(mVerificationId, code.getText().toString());
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Verify your code",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode(nbrphone,mResendToken);
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

                signup.setText(R.string.sign_in);
                codedata.setVisibility(View.VISIBLE);
                userdata.setVisibility(View.GONE);
                resend.setVisibility(View.VISIBLE);
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
    }

    private void simulateBackgroundTask() {
        progressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final int targetProgress = 90;

                for (int progress = 0; progress <= 100; progress += 10) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    final int currentProgress = progress;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(currentProgress);
                            progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#0E6046")));
                        }
                    });
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(targetProgress);
                        progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#0E6046")));
                        userExist(nbrphone);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }).start();
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
                            Intent in = new Intent(getApplicationContext(), menu.class);
                            startActivity(in);
                            finish();
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            }
                        }
                    }
                });
    }

    private void userExist(String phone){
        db.collection("client").whereEqualTo("phone", phone).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot snapshot = task.getResult();
                    if (snapshot != null && !snapshot.isEmpty()) {
                        startPhoneNumberVerification(nbrphone);
                        signup.setText(R.string.sign_in);
                        codedata.setVisibility(View.VISIBLE);
                        userdata.setVisibility(View.GONE);
                        resend.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getApplicationContext(), "You are new please sign up.", Toast.LENGTH_SHORT).show();
                    }
                }
                progressBar.setProgress(100);
            }
        });
    }

    private void updateUI(FirebaseUser user) {

    }
}

