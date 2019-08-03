package th.ac.dusit.dbizcom.bagculate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import retrofit2.Call;
import retrofit2.Retrofit;
import th.ac.dusit.dbizcom.bagculate.db.LocalDb;
import th.ac.dusit.dbizcom.bagculate.etc.Utils;
import th.ac.dusit.dbizcom.bagculate.model.User;
import th.ac.dusit.dbizcom.bagculate.net.ApiClient;
import th.ac.dusit.dbizcom.bagculate.net.LoginResponse;
import th.ac.dusit.dbizcom.bagculate.net.MyRetrofitCallback;
import th.ac.dusit.dbizcom.bagculate.net.WebServices;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsernameEditText, mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsernameEditText = findViewById(R.id.username_edit_text);
        mPasswordEditText = findViewById(R.id.password_edit_text);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        if (username != null) {
            mUsernameEditText.setText(username);
        }

        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFormValid()) {
                    doLogin();
                }
            }
        });
    }

    private boolean isFormValid() {
        boolean valid = true;

        String password = mPasswordEditText.getText().toString().trim();
        if (password.isEmpty()) {
            mPasswordEditText.setText("");
            mPasswordEditText.setError("กรอกรหัสผ่าน");
            valid = false;
        }
        String username = mUsernameEditText.getText().toString().trim();
        if (username.isEmpty()) {
            mUsernameEditText.setText("");
            mUsernameEditText.setError("กรอกชื่อผู้ใช้");
            valid = false;
        }

        return valid;
    }

    private void doLogin() {
        final String username = mUsernameEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString().trim();

        Retrofit retrofit = ApiClient.getClient();
        WebServices services = retrofit.create(WebServices.class);

        Call<LoginResponse> call = services.login(
                username, password
        );
        call.enqueue(new MyRetrofitCallback<>(
                LoginActivity.this,
                null,
                null,
                new MyRetrofitCallback.MyRetrofitCallbackListener<LoginResponse>() {
                    @Override
                    public void onSuccess(LoginResponse responseBody) { // register สำเร็จ
                        User user = responseBody.user;
                        new LocalDb(LoginActivity.this).logUser(user);

                        Utils.showLongToast(LoginActivity.this, "ยินดีต้อนรับ " + user.name);

                        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(String errorMessage) { // register ไม่สำเร็จ หรือเกิดข้อผิดพลาดอื่นๆ (เช่น ไม่มีเน็ต, server ล่ม)
                        Utils.showOkDialog(LoginActivity.this, "ผิดพลาด", errorMessage);
                    }
                }
        ));
    }
}
