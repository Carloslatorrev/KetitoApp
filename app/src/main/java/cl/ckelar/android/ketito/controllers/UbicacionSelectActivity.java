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
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import cl.ckelar.android.ketito.MainActivity;
import cl.ckelar.android.ketito.R;
import cl.ckelar.android.ketito.api.impl.UbicacionApi;
import cl.ckelar.android.ketito.dto.Ubicacion;
import cl.ckelar.android.ketito.dto.UserSesion;
import cl.ckelar.android.ketito.dto.api.UbicacionData;
import cl.ckelar.android.ketito.helpers.db.KetitoDatabase;
import cl.ckelar.android.ketito.helpers.tags.TagApi;


public class UbicacionSelectActivity extends AppCompatActivity{

    private static final String TAG = "UbicacionSelectActivity";

    private ListView lvUbicacionSelect;
    private KetitoDatabase ketitoDatabase;
    private UserSesion userSesion;
    private Ubicacion ubicacion;

    private UbicacionApi ubicacionApi;
    private List<UbicacionData> ubicacionDataList;
    private List<String> listNomUbicacion = new ArrayList<>();
    private ArrayList<String> listUbicacion = new ArrayList<>();
    private UbicacionSelectActivity.Adapter ubiAdapter;
    private String nomUbicacion = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ubicacion_select);

        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setIcon(R.drawable.ketito);

        ketitoDatabase = ketitoDatabase.getInstance(UbicacionSelectActivity.this);
        userSesion = ketitoDatabase.userSesionDao().getUserSesionFirst();

        if(userSesion == null){
            Intent iAccount = new Intent(UbicacionSelectActivity.this, LoginActivity.class);
            startActivity(iAccount);
            finish();
        }
        else {
            new UbicacionSelectActivity.AsyncGetUbicacion().execute();
        }

        lvUbicacionSelect = findViewById(R.id.lvUbicacionSelect);

        lvUbicacionSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                lvUbicacionSelect.setSelection(position);
                Log.d("UbicacionSelectActivity", "Ubicacion Selected: " + lvUbicacionSelect.getItemAtPosition(position).toString());

                Ubicacion mUbicacion = ketitoDatabase.ubicacionDao().getUbicacionByNombre(lvUbicacionSelect.getItemAtPosition(position).toString());
                nomUbicacion = mUbicacion.getUbicacion();

                List<Ubicacion> getAll = ketitoDatabase.ubicacionDao().getAll();
                if (getAll != null && getAll.size() > 0) {
                    for(Ubicacion ubi : getAll) {
                        ubi.setSelected(false);
                        ketitoDatabase.ubicacionDao().updateUbicacion(ubi);
                    }
                }

                mUbicacion.setSelected(true);
                ketitoDatabase.ubicacionDao().updateUbicacion(mUbicacion);

                if (ketitoDatabase.ubicacionDao().getUbicacionBySelected(true) != null) {
                    Intent intent = new Intent(UbicacionSelectActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }

    /**
     * Obtiene el listado de Tokens asociados al usuario
     * **/

    private class AsyncGetUbicacion extends AsyncTask<String, String, String>{


        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;

        @Override
        protected void onPreExecute() {
            dialogBuilder = new AlertDialog.Builder(UbicacionSelectActivity.this);
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

            ubicacionDataList = new ArrayList<>();
            ubicacionApi = new UbicacionApi();

            ubicacionDataList = ubicacionApi.getUbicaciones(TagApi.API_HEADER_CONTENT_TYPE_VALUE, userSesion.getToken_type()+" "+userSesion.getAccess_token());
            if(ubicacionDataList != null && ubicacionDataList.size()>0){

                for (UbicacionData ubi: ubicacionDataList) {
                    ubicacion = new Ubicacion();
                    ubicacion.setIdUbicacion(ubi.getIdUbicacion());
                    ubicacion.setDescripcion(ubi.getDescripcion());
                    ubicacion.setIdLocalidad(ubi.getIdLocalidad());
                    ubicacion.setLocalidad(ubi.getLocalidad());
                    ubicacion.setUbicacion(ubi.getUbicacion());
                    ubicacion.setSelected(false);

                    Ubicacion ubicacionAux = new Ubicacion();
                    ubicacionAux = ketitoDatabase.ubicacionDao().getUbicacionById(ubi.getIdUbicacion());

                    if(ubicacionAux == null){
                        ketitoDatabase.ubicacionDao().insertUbicacion(ubicacion);
                    }

                    listNomUbicacion.add(ubicacion.getUbicacion());
                    listUbicacion.add(ubicacion.getUbicacion());


                }

                ubiAdapter = new UbicacionSelectActivity.Adapter(UbicacionSelectActivity.this, listUbicacion);
                ubiAdapter.notifyDataSetChanged();
                //lvTokenSelect.setAdapter(tokenAdapter);
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if(listNomUbicacion != null && listNomUbicacion.size() >0){
                lvUbicacionSelect.setAdapter(ubiAdapter);
                ubiAdapter.notifyDataSetChanged();
                Log.d(TAG, "onPostExecute: "+lvUbicacionSelect.getAdapter().getItem(0));
                Log.d(TAG, "onPostExecute: "+lvUbicacionSelect.getItemAtPosition(0));
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
            UbicacionSelectActivity.Adapter.ViewHolder holder = null;


            if(convertView == null) {
                //LayoutInflater inflater = _context.getLayoutInflater();
                //convertView = inflater.inflate(R.layout.list_business_wi,parent,false);

                convertView = LayoutInflater.from(_context).inflate(
                        R.layout.list_ubicacion_wi, null);

                holder = new UbicacionSelectActivity.Adapter.ViewHolder();
                holder.textView = (TextView)convertView.findViewById(R.id.item_textoUbicacion);
                //holder.imageView = (ImageView)convertView.findViewById(R.id.item_image);

                convertView.setTag(holder);
            } else {
                holder = (UbicacionSelectActivity.Adapter.ViewHolder) convertView.getTag();
            }

            holder.textView.setText(elements.get(position));
            //String url = elements.get(position).getRutaImagen().toString();

            //* Carga url de imagen en ImageView
            //Picasso.get().load(url).into(holder.imageView);

            return convertView;
        }

    }


}
