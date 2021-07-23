package cl.ckelar.android.ketito.controllers;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import cl.ckelar.android.ketito.R;
import cl.ckelar.android.ketito.api.impl.EmpresaApi;
import cl.ckelar.android.ketito.dto.Empresa;
import cl.ckelar.android.ketito.dto.UserSesion;
import cl.ckelar.android.ketito.dto.api.EmpresaData;
import cl.ckelar.android.ketito.helpers.Adapter.AdapterImageList;
import cl.ckelar.android.ketito.helpers.db.KetitoDatabase;
import cl.ckelar.android.ketito.helpers.tags.TagApi;

public class BusinessSelectActivity extends AppCompatActivity {

    private ListView lvBusinessSelect;

    private KetitoDatabase ketitoDatabase;
    private UserSesion userSesion;
    private Empresa empresa;
    private ImageView imgLogo;

    private EmpresaApi empresaApi;
    private List<EmpresaData> listEmpresas;
    private List<String> empresas = new ArrayList<>();
    private ArrayAdapter<String> empresasAdapter;
    private String idEmpresa = "";
    private String idAuxEmpresa = "";
    private ArrayList<Empresa> empresaArrayList = new ArrayList<>();
    private AdapterImageList listEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_business_select);

        ketitoDatabase = ketitoDatabase.getInstance(BusinessSelectActivity.this);
        userSesion = ketitoDatabase.userSesionDao().getUserSesionFirst();



        if (userSesion == null) {
            Intent iAccount = new Intent(BusinessSelectActivity.this, LoginActivity.class);
            startActivity(iAccount);
            finish();
        }
        else {
            new AsyncGetEmpresas().execute();
        }
        lvBusinessSelect = findViewById(R.id.lvBusinessSelect);


        //listener de la lista de encuesta
        lvBusinessSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                lvBusinessSelect.setSelection(position);
                Log.d("BusinessSelectActivity", "Business Selected: " + lvBusinessSelect.getItemAtPosition(position).toString());

                Empresa auxEmpAdapter = listEmpresa.getItem(position);
                Empresa mEmpresa = ketitoDatabase.empresaDao().getEmpresaByRazon(auxEmpAdapter.getRazon());
                idEmpresa = mEmpresa.getIdEmpresa();

                //Nos aseguramos de dejar todos los atributos "Selected" de las encuestas en false antes de actualizar el listado de la tabla Encuestas.
                List<Empresa> getAll = ketitoDatabase.empresaDao().getAll();
                if (getAll != null && getAll.size() > 0) {
                    for(Empresa em : getAll) {
                        em.setSelected(false);
                        ketitoDatabase.empresaDao().updateEmpresa(em);
                    }
                }
                //le asignamos true al selected de la empresa seleccionada
                mEmpresa.setSelected(true);
                ketitoDatabase.empresaDao().updateEmpresa(mEmpresa);
                //llamamos al método para indicarle a la API la empresa que se usará en la aplicación.
                new AsyncUseEmpresa().execute();
            }
        });

    }




    /**
     * Obtiene el listado de empresas asociadas al usuario
     * **/
    private class AsyncGetEmpresas extends AsyncTask<String, String, String> {

        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        ProgressBar progressBar;

        @Override
        protected void onPreExecute() {
            dialogBuilder = new AlertDialog.Builder(BusinessSelectActivity.this);
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
        protected String doInBackground(String... params) {
            //Obtenemos un listado de las empresas con un call a la API
            listEmpresas = new ArrayList<>();
            empresaApi = new EmpresaApi();
            listEmpresas = empresaApi.getEmpresas(TagApi.API_HEADER_CONTENT_TYPE_VALUE, userSesion.getToken_type() + " " + userSesion.getAccess_token());

            if (listEmpresas != null && listEmpresas.size() > 0) {
                //realizamos un recorrido y almacenamos la variable en el objeto empresa para posteriormente incluírla en la base de datos.
                // Solo se incluyen los datos de la Empresa, no las preguntas.
                for(EmpresaData ed : listEmpresas) {
                    empresa = new Empresa();
                    empresa.setIdEmpresa(ed.getIdEmpresa());
                    empresa.setRazon(ed.getRazon());
                    empresa.setRut(ed.getRut());
                    empresa.setTelefono(ed.getTelefono());
                    empresa.setWeb(ed.getWeb());
                    empresa.setCorreo(ed.getCorreo());
                    empresa.setContactoEmpresa(ed.getContactoEmpresa());
                    empresa.setDataLetters(ed.getDataLetters());
                    empresa.setRutaImagen(ed.getRutaImagen());
                    //verificamos que la tabla de Empresa de la base de datos local este vacía
                    Empresa empresaAux = new Empresa();
                    empresaAux = ketitoDatabase.empresaDao().getEmpresaById(ed.getIdEmpresa());
                    if (empresaAux == null) {
                        ketitoDatabase.empresaDao().insertEmpresa(empresa);
                        idAuxEmpresa = empresa.getIdEmpresa();
                    }
                    //agregamos la empresa a un array
                    //agregamos el nombre de la empresa a un List
                    empresaArrayList.add(empresa);
                    empresas.add(empresa.getRazon());
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Enviamos el listado de empresa al adaptador para la interfaz.
                        listEmpresa = new AdapterImageList(BusinessSelectActivity.this, empresaArrayList);
                        listEmpresa.getCount();
                        lvBusinessSelect.setAdapter(listEmpresa);
                    }
                });
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            if(empresaArrayList != null && empresaArrayList.size() > 0){
                lvBusinessSelect.setAdapter(listEmpresa);
            }
            alertDialog.cancel();
        }

    }

    /**
     * Define que empresa utilizar
     * **/
    private class AsyncUseEmpresa extends AsyncTask<String, String, String> {

        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        ProgressBar progressBar;
        boolean isUsed = false;

        @Override
        protected void onPreExecute() {
            //AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder = new AlertDialog.Builder(BusinessSelectActivity.this);
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
        protected String doInBackground(String... params) {
            //Se realiza un call con los datos de la empresa seleccionada y solicitamos su uso en la API.
            empresaApi = new EmpresaApi();
            isUsed = empresaApi.useEmpresa(TagApi.API_HEADER_CONTENT_TYPE_VALUE, userSesion.getToken_type() + " " + userSesion.getAccess_token(), idEmpresa);
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            if (isUsed) {
                Intent intent = new Intent(BusinessSelectActivity.this, TokenSelectActivity.class);
                startActivity(intent);
                finish();
            }
            alertDialog.cancel();
        }

    }


}
