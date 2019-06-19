package com.dhanvi.fashionfusion1;

import android.content.Intent;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dhanvi.fashionfusion1.database_call.NetworkCall;
import com.dhanvi.fashionfusion1.database_call.jsn;
import com.dhanvi.fashionfusion1.databinding.ActivityRejistrationBinding;

import org.json.JSONObject;

import java.util.HashMap;

public class rejistration extends AppCompatActivity {
Button btn;
    EditText edit, edit1, edit2, edit3, edit4;
    String name, number, email, password, conpass;
    ActivityRejistrationBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_rejistration);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_rejistration);

        binding.btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        name = binding.edit.getText().toString();
                        number = binding.edit1.getText().toString();
                        email = binding.edit2.getText().toString();
                        password = binding.edit3.getText().toString();
                        conpass = binding.edit4.getText().toString();
                        if (name.isEmpty()) {
                            binding.edit.setError("please enter name");
                        } else if (number.isEmpty()) {
                            binding.edit1.setError("please enter number");
                        } else if (email.isEmpty())
                        {
                            binding.edit2.setError("please enter email");
                        } else if (password.isEmpty())
                        {
                            binding.edit3.setError("please enter password");
                        }
                        else if (!conpass.equals(password))
                        {
                            binding.edit4.setError("please confirm pass");
                        }
                        else

                        {
                            signIn();
                        }
                    }
                });
        }

    private void signIn() {

        HashMap<String, String> param=new HashMap<>();
        param.put("type","save");
        param.put("table","user");
        param.put("name",name);
        param.put("email",email);
        param.put("mobile",number);
        param.put("password",password);

        NetworkCall.call(param).setDataResponseListener(new NetworkCall.SetDataResponse() {
            @Override
            public void setResponse(String responseStr) {
                if (jsn.checkResponseStr(responseStr)){
                    JSONObject jsonObjectAt0 = jsn.getJSONObjectAt0(responseStr);
                    MyParam.userId = jsn.getJSONString(jsonObjectAt0,"id");
                    startMainActivity();
                    Toast.makeText(rejistration.this, "Saved ... ", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(rejistration.this, "Not Saved ... ", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(rejistration.this, fashion.class);
        startActivity(intent);
    }
}

