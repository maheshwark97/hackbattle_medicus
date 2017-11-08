package byteofpi.com.medicus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import static com.google.firebase.auth.GoogleAuthProvider.getCredential;


public class DetailsActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener{
    public Button Butt;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private String userId;
    private TextInputLayout namewrapper;
    private TextInputLayout bloodgroupwrapper;
    private TextInputLayout addresswrapper;
    private TextInputLayout fatherwrapper;
    private TextInputLayout insurancewrapper;
    private TextInputLayout allergieswrapper;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private String TAG="ACTIVITYLOG";
    private FirebaseUser user;
    private SignInButton signInButton;
    private AuthCredential credential;
// ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details2);
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        namewrapper=(TextInputLayout) findViewById(R.id.nameWrapper);
        bloodgroupwrapper=(TextInputLayout) findViewById(R.id.bloodGroupwrapper);
        addresswrapper=(TextInputLayout) findViewById(R.id.addresswrapper);
        fatherwrapper=(TextInputLayout) findViewById(R.id.fatherwrapper);
        insurancewrapper=(TextInputLayout) findViewById(R.id.insurancewrapper);
        allergieswrapper=(TextInputLayout) findViewById(R.id.allergieswrapper);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mAuth = FirebaseAuth.getInstance();

        userId ="The Person";
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);



            }
        });


        Butt=(Button)findViewById(R.id.Continue_button);
        Butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(signInButton.getVisibility()!=View.VISIBLE){
                    writeNewUser(namewrapper.getEditText().getText().toString(),
                            bloodgroupwrapper.getEditText().getText().toString(),
                            addresswrapper.getEditText().getText().toString(),
                            fatherwrapper.getEditText().getText().toString(),
                            insurancewrapper.getEditText().getText().toString(),
                            allergieswrapper.getEditText().getText().toString());
                Intent hola=new Intent(DetailsActivity.this,NotBlindActivity.class);
                startActivity(hola);
                }
                else{
                    Toast.makeText(DetailsActivity.this, "YOU HAVE NOT SIGNED IN USING GOOGLE YET", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                if(account!=null){
                firebaseAuthWithGoogle(account);}
                else {
                    Log.d(TAG,"GOOGLE ACCOUNT IS STILL NULL");
                }
            } else {
                Log.d(TAG,"GOOGLE SIGN IN ACCOUNT NOT RETRIEVED");
            }
        }
    }
    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                    }
                });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        credential = getCredential(acct.getIdToken(),null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            user = mAuth.getCurrentUser();
                            signInButton.setEnabled(false);
                            signInButton.setVisibility(View.GONE);
                            findViewById(R.id.Start_text).setVisibility(View.GONE);

                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(DetailsActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @IgnoreExtraProperties
    public static class User {

        public String name;
        public String bloodgroup;
        public String address;
        public String fathersname;
        public String insuranceno;
        public String allergies;
        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String name,
                    String bloodgroup,
                    String address,
                    String fathersname,
                    String insuranceno,
                    String allergies) {
            this.name = name;
            this.bloodgroup = bloodgroup;
            this.address=address;
            this.fathersname=fathersname;
            this.insuranceno=insuranceno;
            this.allergies=allergies;
        }


    }
    public void writeNewUser(String name,
                             String bloodgroup,
                             String address,
                             String fathersname,
                             String insuranceno,
                             String allergies) {
        User user = new User(name,
                bloodgroup,
                address,
                fathersname,
                insuranceno,
                allergies);

        mDatabase.child(userId).setValue(user);
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
}
