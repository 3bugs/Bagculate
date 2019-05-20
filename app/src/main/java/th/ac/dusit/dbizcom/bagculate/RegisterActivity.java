package th.ac.dusit.dbizcom.bagculate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import retrofit2.Call;
import retrofit2.Retrofit;
import th.ac.dusit.dbizcom.bagculate.etc.Utils;
import th.ac.dusit.dbizcom.bagculate.model.User;
import th.ac.dusit.dbizcom.bagculate.net.ApiClient;
import th.ac.dusit.dbizcom.bagculate.net.MyRetrofitCallback;
import th.ac.dusit.dbizcom.bagculate.net.RegisterResponse;
import th.ac.dusit.dbizcom.bagculate.net.WebServices;

public class RegisterActivity extends AppCompatActivity {

    private EditText mUsernameEditText, mPasswordEditText, mNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUsernameEditText = findViewById(R.id.username_edit_text);
        mPasswordEditText = findViewById(R.id.password_edit_text);
        mNameEditText = findViewById(R.id.name_edit_text);

        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFormValid()) {
                    doRegister();
                }
            }
        });
    }

    private boolean isFormValid() {
        boolean valid = true;

        String name = mNameEditText.getText().toString().trim();
        if (name.isEmpty()) {
            mNameEditText.setText("");
            mNameEditText.setError("กรอกชื่อ");
            valid = false;
        }
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

    private void doRegister() {
        final String username = mUsernameEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString().trim();
        String name = mNameEditText.getText().toString().trim();

        Retrofit retrofit = ApiClient.getClient();
        WebServices services = retrofit.create(WebServices.class);

        Call<RegisterResponse> call = services.register(
                username, password, name
        );
        call.enqueue(new MyRetrofitCallback<>(
                RegisterActivity.this,
                null,
                null,
                new MyRetrofitCallback.MyRetrofitCallbackListener<RegisterResponse>() {
                    @Override
                    public void onSuccess(RegisterResponse responseBody) { // register สำเร็จ
                        User user = responseBody.user;
                        Utils.showLongToast(RegisterActivity.this, responseBody.errorMessage);

                        // ส่ง username ที่ register สำเร็จ กลับไปแสดงในหน้า login
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(String errorMessage) { // register ไม่สำเร็จ หรือเกิดข้อผิดพลาดอื่นๆ (เช่น ไม่มีเน็ต, server ล่ม)
                        Utils.showOkDialog(RegisterActivity.this, "ผิดพลาด", errorMessage);
                    }
                }
        ));
    }
}
