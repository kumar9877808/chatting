package sonu.kumar.chatwithfriends;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    TextView register;
    EditText username, password;
    Button loginButton;
    String user, pass;
    private static final String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView)findViewById(R.id.register);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        loginButton = (Button)findViewById(R.id.loginButton);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, CreateNewAccount.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = username.getText().toString();
                pass = password.getText().toString();

                if(user.equals("")){
                    username.setError("can't be blank");
                }
                else if(pass.equals("")){
                    password.setError("can't be blank");
                }
                else{
                    String url ="https://chatwithfriends-1a3b6.firebaseio.com/users.json";
                    final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                    pd.setMessage("Loading...");
                    pd.show();
                    StringRequest stringRequest =new StringRequest(
                            StringRequest.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d(TAG, "onResponse: "+response);
                                    if (response.equals("null")){
                                        Toast.makeText(LoginActivity.this, "user not found", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        try {
                                            JSONObject jsonObject =new JSONObject(response);
                                            if (!jsonObject.has(user)){
                                                Toast.makeText(LoginActivity.this, "user not found", Toast.LENGTH_SHORT).show();

                                            }
                                            else  if (jsonObject.getJSONObject(user).getString("password").equals(pass)){
                                                pd.dismiss();
                                                UserDetails.username = user;
                                                UserDetails.password = pass;
                                                startActivity(new Intent(LoginActivity.this, UsersActivity.class));
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }




                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    System.out.println("" + error);
                                    pd.dismiss();
                                    
                                }
                            }
                    );
                    RequestQueue rQueue = Volley.newRequestQueue(LoginActivity.this);
                    rQueue.add(stringRequest);


                }
            }
        });
    }
}
