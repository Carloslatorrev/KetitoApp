package cl.ckelar.android.ketito.controllers;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import cl.ckelar.android.ketito.R;
import cl.ckelar.android.ketito.api.impl.TokenApi;
import cl.ckelar.android.ketito.dto.TokenC;
import cl.ckelar.android.ketito.dto.UserSesion;
import cl.ckelar.android.ketito.dto.api.TokenData;
import cl.ckelar.android.ketito.helpers.db.KetitoDatabase;
import cl.ckelar.android.ketito.helpers.tags.TagApi;


public class TokenSelectActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ListView lvTokenSelect;
    private KetitoDatabase ketitoDatabase;
    private UserSesion userSesion;
    private TokenC tokenC;
    private String userTotem;
    private TokenApi tokenApi;
    private List<TokenData> listTokens;
    private List<String> nomTokens = new ArrayList<>();
    private ArrayList<String> listTokenC = new ArrayList<>();
    private Adapter tokenAdapter;
    private String userToken = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_token_select);

        ketitoDatabase = ketitoDatabase.getInstance(TokenSelectActivity.this);
        userSesion = ketitoDatabase.userSesionDao().getUserSesionFirst();

        if(userSesion == null){
            Intent iAccount = new Intent(TokenSelectActivity.this, LoginActivity.class);
            startActivity(iAccount);
            finish();
        }
        else {
            new AsyncGetToken().execute();
        }

        lvTokenSelect = findViewById(R.id.lvTokenSelect);
        lvTokenSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                lvTokenSelect.setSelection(position);
                Log.d("TokenSelectActivity", "Token Selected: " + lvTokenSelect.getItemAtPosition(position).toString());

                TokenC mTokenC = ketitoDatabase.tokenDao().getTokenByNombre(lvTokenSelect.getItemAtPosition(position).toString());
                userToken = mTokenC.getToken();

                List<TokenC> getAll = ketitoDatabase.tokenDao().getAll();
                if (getAll != null && getAll.size() > 0) {
                    for(TokenC tk : getAll) {
                        tk.setSelected(false);
                        ketitoDatabase.tokenDao().updateTokenC(tk);
                    }
                }

                mTokenC.setSelected(true);
                ketitoDatabase.tokenDao().updateTokenC(mTokenC);
                new AsyncUseToken().execute();
            }
        });
    }

    /**
     * Obtiene el listado de Tokens asociados al usuario
     * **/

    private class AsyncGetToken extends AsyncTask<String, String, String>{

        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        ProgressBar progressBar;

        @Override
        protected void onPreExecute() {
            dialogBuilder = new AlertDialog.Builder(TokenSelectActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.alert_loading_layout, null);
            dialogBuilder.setView(dialogView);
            alertDialog = dialogBuilder.create();
            alertDialog.show();
        }

        @Override
        protected void onProgressUpdate(String... text) {
            //finalResult.setText(text[0]);
        }

        @Override
        protected String doInBackground(String... strings) {

            listTokens = new ArrayList<>();
            tokenApi = new TokenApi();

            listTokens = tokenApi.GetUsersToken(TagApi.API_HEADER_CONTENT_TYPE_VALUE, userSesion.getToken_type()+" "+userSesion.getAccess_token());
            if(listTokens != null && listTokens.size()>0){

                for (TokenData tk: listTokens) {
                    tokenC = new TokenC();
                    tokenC.setToken(tk.getToken());
                    tokenC.setTokenName(tk.getTokenName());
                    tokenC.setFH_Token(tk.getFh_token());
                    tokenC.setSelected(false);
                    TokenC tokenAux = new TokenC();
                    tokenAux = ketitoDatabase.tokenDao().getTokenByToken(tk.getToken());
                    if(tokenAux == null){
                        ketitoDatabase.tokenDao().InsertToken(tokenC);
                    }
                    nomTokens.add(tokenC.getTokenName());
                    listTokenC.add(tokenC.getTokenName());
                }
                tokenAdapter = new Adapter(TokenSelectActivity.this, listTokenC);
                tokenAdapter.notifyDataSetChanged();
                //lvTokenSelect.setAdapter(tokenAdapter);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if(nomTokens != null && nomTokens.size()>0){
                lvTokenSelect.setAdapter(tokenAdapter);
                tokenAdapter.notifyDataSetChanged();
                Log.d(TAG, "onPostExecute: "+lvTokenSelect.getAdapter().getItem(0));
                Log.d(TAG, "onPostExecute: "+lvTokenSelect.getItemAtPosition(0));
            }
            alertDialog.cancel();
        }
    }

    /**
     * Se selecciona que token se va a utilizar
     * **/
    private class AsyncUseToken extends AsyncTask<String, String, String>{

        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        ProgressBar progressBar;

        @Override
        protected void onPreExecute() {
            //AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder = new AlertDialog.Builder(TokenSelectActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.alert_loading_layout, null);
            dialogBuilder.setView(dialogView);
            alertDialog = dialogBuilder.create();
            alertDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            TokenC auxTokenC = ketitoDatabase.tokenDao().getTokenBySelected();
            UserSesion userAux = new UserSesion();
            if(auxTokenC != null){
                userAux.setIssued(userSesion.getIssued());
                userAux.setAccess_token(auxTokenC.getToken());
                userAux.setUserName(userSesion.getUserName());
                userAux.setToken_type(userSesion.getToken_type());
                userAux.setExpires_in(userSesion.getExpires_in());
                userAux.setExpires(userSesion.getExpires());

                    UserSesion userAux2 = ketitoDatabase.userSesionDao().getUserSesion(userAux.getAccess_token());
                    if(userAux2 == null){
                        userSesion= ketitoDatabase.userSesionDao().getUserSesionFirst();
                        ketitoDatabase.userSesionDao().deleteUserSesion(userSesion);
                        UserSesion aux = ketitoDatabase.userSesionDao().getUserSesion(userSesion.getAccess_token());
                        if(aux == null){
                            ketitoDatabase.userSesionDao().insertUserSesion(userAux);
                        }
                    }else{
                        userSesion = ketitoDatabase.userSesionDao().getUserSesionFirst();
                    }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if (userSesion.getAccess_token() != null) {
                Intent intent = new Intent(TokenSelectActivity.this, EncuestaSelectActivity.class);
                startActivity(intent);
                finish();
            }
            alertDialog.cancel();
        }
    }

    public class Adapter extends BaseAdapter {

        private final Activity _context;
        private final ArrayList<String> elements;

        public Adapter(Activity context, ArrayList<String> elements) {
            _context = context;
            this.elements = elements;
        }

        public class ViewHolder {
            TextView textView;
        }

        @Override
        public int getCount() {
            return elements.size();
        }

        @Override
        public String getItem(int position) {
            return elements.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            Adapter.ViewHolder holder = null;

            if(convertView == null) {
                convertView = LayoutInflater.from(_context).inflate(
                        R.layout.list_token_wi, null);
                holder = new Adapter.ViewHolder();
                holder.textView = (TextView)convertView.findViewById(R.id.item_texto);

                convertView.setTag(holder);
            } else {
                holder = (Adapter.ViewHolder) convertView.getTag();
            }
            holder.textView.setText(elements.get(position));

            return convertView;
        }
    }

}
