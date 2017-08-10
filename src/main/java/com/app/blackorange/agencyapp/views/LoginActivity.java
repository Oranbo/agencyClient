package com.app.blackorange.agencyapp.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.blackorange.agencyapp.R;
import com.app.blackorange.agencyapp.controllers.UserController;
import com.app.blackorange.agencyapp.models.Usr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>,TextWatcher {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    private static int TAG=0;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText repeatPassword;
    private View mProgressView;
    private View mLoginFormView;
    private TextView regeisterText;
    private TextView backPassword;
    private Button mEmailSignInButton;
    private String regName;
    private String regpassword;
    private String repeatP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        repeatPassword = (EditText) findViewById(R.id.repeat_password);
        repeatPassword.setVisibility(View.GONE);
        mEmailView.addTextChangedListener(this);
        mPasswordView.addTextChangedListener(this);
        repeatPassword.addTextChangedListener(this);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        regeisterText = (TextView) findViewById(R.id.regeister_button);
        backPassword = (TextView) findViewById(R.id.back_password);
        initEvents();
    }

    private void initView() {
        if (TAG==1){
            mEmailSignInButton.setText("登陆");
            mEmailSignInButton.setBackgroundColor(Color.parseColor("#28ff28"));
            repeatPassword.setVisibility(View.GONE);
            regeisterText.setVisibility(View.VISIBLE);
            backPassword.setVisibility(View.VISIBLE);
            mEmailSignInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    attemptLogin();
                }
            });
            TAG=0;
        }
    }

    private void initEvents() {
        regeisterText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmailSignInButton.setText("注册");
                mEmailSignInButton.setBackgroundColor(Color.parseColor("#7d7dff"));
                repeatPassword.setVisibility(View.VISIBLE);
                regeisterText.setVisibility(View.GONE);
                backPassword.setVisibility(View.GONE);
                mEmailSignInButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!regName.isEmpty()&&!regpassword.isEmpty()&&!repeatP.isEmpty()){
                            if (regpassword.equals(repeatP)){
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        int status = -1;
                                        UserController ucon = new UserController();
                                        try {
                                            status = ucon.getStatus("regeister.json",regName,regpassword);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        if (status==1){
                                            Looper.prepare();
                                            Toast toast = Toast.makeText(getApplicationContext(), "用户名已存在！", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                            Looper.loop();
                                        }if (status==0){
                                            Message msg = new Message();
                                            Bundle data = new Bundle();
                                            data.putString("TAG","1");
                                            msg.setData(data);
                                            handler.sendMessage(msg);
                                            Looper.prepare();
                                            Toast toast = Toast.makeText(getApplicationContext(), "恭喜您，注册成功！", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                            Looper.loop();

                                        }if (status==2){
                                            Looper.prepare();
                                            Toast toast = Toast.makeText(getApplicationContext(), "用户名只能英文字母及符号！", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                            Looper.loop();
                                        }
                                    }
                                }).start();
                            }else {
                                repeatPassword.setError("两次输入密码不一致");
                            }
                        }else {
                            if (regName.isEmpty()){
                                mEmailView.setError("请输入用户名");
                            }if (regpassword.isEmpty()){
                                mPasswordView.setError("请输入密码");
                            }if (repeatP.isEmpty()){
                                repeatPassword.setError("请重复输入密码");
                            }
                        }

                    }
                });
            }
        });
        backPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,BackPActivity.class);
                intent.putExtra("name",regName);
                startActivity(intent);
            }
        });
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError("This text is required");
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
           mAuthTask.execute((Void) null);
//            mAuthTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

//    private boolean isEmailValid(String email) {
//        //TODO: Replace this with your own logic
//        return email.contains("@");
//    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        regName = mEmailView.getText().toString();
        regpassword =mPasswordView.getText().toString();
        repeatP = repeatPassword.getText().toString();
    }

    private Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {

            // Activity跳转
            Bundle data = msg.getData();
            TAG=Integer.parseInt(data.getString("TAG"));
            initView();
        }
    };

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final String URl = "login.json";

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            int status=2;
            try {
                // Network access.
                UserController uc = new UserController();
                status = uc.getStatus(URl,mEmail,mPassword);
                if (status == 0)
                    return true;
            }  catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Message msg = new Message();
                Bundle data = new Bundle();
                Usr usr = new Usr();
                usr.setLogin_name(mEmail);
                usr.setPasswd(mPassword);
                ArrayList list = new ArrayList();
                list.add(usr);
                data.putParcelableArrayList("user", list);
                msg.setData(data);
                uiHandler.sendMessage(msg);
                finish();
            } else {

                mEmailView.setError("");
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }
        private Handler uiHandler= new Handler(){
            @Override
            public void handleMessage(Message msg) {

                // Activity跳转
                Bundle data = msg.getData();
                ArrayList list = data.getParcelableArrayList("user");
                Usr usr = (Usr) list.get(0);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("user",usr);
                startActivity(intent);
            }
        };
        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

