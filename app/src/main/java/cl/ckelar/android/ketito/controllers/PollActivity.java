package cl.ckelar.android.ketito.controllers;

import android.content.Intent;
import android.os.AsyncTask;

import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cl.ckelar.android.ketito.R;
import cl.ckelar.android.ketito.api.impl.DestinatarioApi;
import cl.ckelar.android.ketito.api.impl.EncuestaApi;
import cl.ckelar.android.ketito.dto.Empresa;
import cl.ckelar.android.ketito.dto.Encuesta;
import cl.ckelar.android.ketito.dto.UserSesion;
import cl.ckelar.android.ketito.dto.api.DestinatarioData;
import cl.ckelar.android.ketito.dto.api.EncuestaData;
import cl.ckelar.android.ketito.dto.api.EncuestaPostResponse;
import cl.ckelar.android.ketito.helpers.db.KetitoDatabase;
import cl.ckelar.android.ketito.helpers.tags.TagApi;

public class PollActivity extends AppCompatActivity {

    private KetitoDatabase ketitoDatabase;
    private UserSesion userSesion;
    private Encuesta encuesta;

    private EditText edtClientPhone;
    private EditText edtClientName;
    private EditText edtClientEmail;
    //private TextView txtPollBusiness;
    private Spinner spnBusinessPolls;
    private Button btnSendPoll;

    private TextView txtUserWelcome;
    private TextView txtBusinessWelcome;
    private TextView txtPlanWelcome;

    EncuestaApi encuestaApi;
    List<EncuestaData> listEncuestas;
    List<String> encuestas = new ArrayList<>();
    ArrayAdapter<String> encuestasAdapter;
    String idEncuesta = "";

    DestinatarioApi destinatarioApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_poll);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setLogo(R.drawable.ketito);
        //getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ketito);

        edtClientPhone = findViewById(R.id.edtPollClientPhone);
        edtClientName = findViewById(R.id.edtPollClientName);
        edtClientEmail = findViewById(R.id.edtPollClientEmail);
        //txtPollBusiness = findViewById(R.id.txtPollBusiness);
        spnBusinessPolls = findViewById(R.id.spnPollBussinessPolls);
        btnSendPoll = findViewById(R.id.btnSendPoll);
        //txtUserWelcome = findViewById(R.id.txtUserWelcome);
        txtBusinessWelcome = findViewById(R.id.txtBusinessWelcome);
        //txtPlanWelcome = findViewById(R.id.txtPlanWelcome);
        //txtPlanWelcome.setVisibility(View.GONE);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ketitoDatabase = ketitoDatabase.getInstance(PollActivity.this);     // Inicia la conexión con la base de datos local
        userSesion = ketitoDatabase.userSesionDao().getUserSesionFirst();   // Obtiene el usuario con el cual se está trabajando

        // Verifica si existe un usuario logueado
        if (userSesion == null) {
            // si no existe un usuario logueado, abre la pantalla de Login
            Intent iAccount = new Intent(PollActivity.this, LoginActivity.class);
            startActivity(iAccount);
            finish();
        }
        else {
            //String usernameText = txtUserWelcome.getText().toString();
            //usernameText = usernameText.replace("@username", userSesion.getUserName());
            //txtUserWelcome.setText(usernameText);

            //String planText = txtPlanWelcome.getText().toString();
            //planText = planText.replace("@plan", "");
            //txtPlanWelcome.setText(planText);

            String businessText = txtBusinessWelcome.getText().toString();

            // Obtiene la empresa seleccionada previamente por el usuario
            Empresa businessSelected = ketitoDatabase.empresaDao().getEmpresaBySelected();

            // Si existe la empresa, obtiene las encuestas
            if (businessSelected != null) {
                businessText = businessText.replace("@business", businessSelected.getRazon());
                new PollActivity.AsyncGetEncuestas(getWindow().getDecorView().findViewById(android.R.id.content)).execute();                      // Obtiene las encuestas de la empresa seleccionada
                //txtPollBusiness.setText(businessSelected.getRazon());   // Muestra en un TextView la empresa seleccionada
            } else {
                businessText = businessText.replace("@business", "No Seleccionada");
                // Si no existe una empresa seleccionada, redirecciona a la pantalla de seleccion de empresa
                Intent iBusiness = new Intent(PollActivity.this, BusinessSelectActivity.class);
                startActivity(iBusiness);
                finish();
            }
            txtBusinessWelcome.setText(businessText);

        }

        // Al escribir los 9 digitos del teléfono, automáticamente busca los datos del destinatario
        TextWatcher findDestinatario = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edtClientPhone.getText().length() == 9) {
                    //editText2.requestFocus();

                    edtClientPhone.clearFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                    new PollActivity.AsyncGetDestinatario().execute();
                }
            }
        };
        edtClientPhone.addTextChangedListener(findDestinatario);

        // Permite seleccionar las encuestas que se enviaran a los clientes
        spnBusinessPolls.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spnBusinessPolls.setSelection(position);
                Log.d("MainAvtivity", "ENCUESTA SELECTED: " + spnBusinessPolls.getItemAtPosition(position).toString());

                Encuesta me = ketitoDatabase.encuestaDao().getEncuestaByTitulo(spnBusinessPolls.getItemAtPosition(position).toString());
                idEncuesta = me.getIdEncuesta();

                Log.i("PollActivity", "ID Encuesta Seleccionada: " + idEncuesta);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Acción para enviar la encuesta al cliente
        btnSendPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!idEncuesta.isEmpty()) {

                    // Comprueba que el teléfono y el nombre del el cliente
                    // se hayan ingresado para poder enviar la encuesta
                    if (!edtClientPhone.getText().toString().isEmpty()
                            && !edtClientName.getText().toString().isEmpty()) {

                        new PollActivity.AsyncSendPoll(view).execute();
                    } else {

                        // Si no existe el teléfono del cliente, arroja un mensaje de error
                        if (edtClientPhone.getText().toString().isEmpty()) {
                            edtClientPhone.setError("El número de teléfono del cliente es obligatorio!");
                        }

                        // Si no existe el nombre del cliente, se arroja un mensaje de error
                        if (edtClientName.getText().toString().isEmpty()) {
                            edtClientName.setError("El nombre del cliente es obligatorio!");
                        }

                    }

                } else {
                    //
                    Snackbar.make(view, "Su empresa no tiene ninguna encuesta disponible!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_login);

        if (userSesion != null) {
            item.setTitle("Cerrar Sesión");
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intentMenu;

        int id = item.getItemId();

        // Abrir Alert Dialog "Acerca De"
        //if (id == R.id.action_about) {
        //    Util.infoAppDialog(this);
        //    return true;
        //}

        // Menú para pantalla de LOGIN
        if (id == R.id.action_login) {
            //return true;
            intentMenu = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intentMenu);
            finish();
        }
        // Menú para seleccionar la empresa con la que se enviará la encuesta
        else if (id == R.id.action_business_select) {
            //return true;
            intentMenu = new Intent(getBaseContext(), BusinessSelectActivity.class);
            startActivity(intentMenu);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Obtiene el listado de encuestas asociadas a la empresa
     * **/
    private class AsyncGetEncuestas extends AsyncTask<String, String, String> {

        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        ProgressBar progressBar;

        private View rootView;

        public AsyncGetEncuestas(View rootView) {
            this.rootView = rootView;
        }

        @Override
        protected void onPreExecute() {
            // Crea un dialogo que indica que se está cargando la información
            dialogBuilder = new AlertDialog.Builder(PollActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.alert_loading_layout, null);
            dialogBuilder.setView(dialogView);
            alertDialog = dialogBuilder.create();
            alertDialog.show();

        }

        @Override
        protected void onProgressUpdate(String... text) {
        }

        @Override
        protected String doInBackground(String... params) {

            //
            listEncuestas = new ArrayList<>();
            encuestaApi = new EncuestaApi();        // Inicia la instancia para conectar con la API

            // Obtiene el listado de las encuestas
            listEncuestas = encuestaApi.getEncuestas(TagApi.API_HEADER_CONTENT_TYPE_VALUE, userSesion.getToken_type() + " " + userSesion.getAccess_token());

            // Comprueba que se hayan encontrado encuestas
            if (listEncuestas != null && !listEncuestas.isEmpty()) {

                // Recorre las encuestas encontradas para mostrarlas en la pantalla
                for(EncuestaData ed : listEncuestas) {

                    encuesta = new Encuesta();
                    encuesta.setIdEncuesta(ed.getIdEncuesta());
                    encuesta.setTitulo(ed.getTitulo());
                    //encuesta.setTipo(ed.getTipo());
                    encuesta.setDescripcion(ed.getDescripcion());

                    Encuesta encuestaAux = new Encuesta();
                    encuestaAux = ketitoDatabase.encuestaDao().getEncuestaById(ed.getIdEncuesta());

                    if (encuestaAux == null) {
                        ketitoDatabase.encuestaDao().insertEncuesta(encuesta);
                    }

                    // Comprueba no exista previamente en la lista, para que no se repitan
                    if (!encuestas.contains(encuesta.getTitulo())) {
                        encuestas.add(encuesta.getTitulo());
                    }

                }

            } else {
                encuestas = new ArrayList<>();
            }

            // Se agrega el listado de encuestas al adapter
            encuestasAdapter = new ArrayAdapter<String>(PollActivity.this,
                    android.R.layout.simple_spinner_item, encuestas);

            // Se asigna la primera encuesta como seleccionada por defecto
            idEncuesta = (listEncuestas != null && !listEncuestas.isEmpty()) ? listEncuestas.get(0).getIdEncuesta() : "";

            return null;
        }


        @Override
        protected void onPostExecute(String result) {

            if (encuestas != null) {
                encuestasAdapter.notifyDataSetChanged();
                spnBusinessPolls.setAdapter(encuestasAdapter);

                Log.i("AsyncGetEncuestas", "Agrega nuevas encuestas");
            }
            else {

                if (encuestasAdapter != null && !encuestasAdapter.isEmpty()) {
                    encuestasAdapter.clear();
                    encuestasAdapter.notifyDataSetChanged();
                    spnBusinessPolls.setAdapter(encuestasAdapter);

                    Log.i("AsyncGetEncuestas", "Elimina las encuestas");
                }
            }

            if (idEncuesta.isEmpty()) {

                rootView.clearFocus();

                edtClientPhone.setEnabled(false);
                edtClientName.setEnabled(false);
                edtClientEmail.setEnabled(false);

                Snackbar.make(rootView, "Su empresa no tiene ninguna encuesta disponible!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

            alertDialog.cancel();
        }

    }

    /**
     * Envía la encuesta al destinatario
     * **/
    private class AsyncSendPoll extends AsyncTask<String, String, String> {

        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        ProgressBar progressBar;

        EncuestaPostResponse postResponse = new EncuestaPostResponse();

        private View rootView;

        public AsyncSendPoll(View rootView) {
            this.rootView = rootView;
        }

        @Override
        protected void onPreExecute() {
            dialogBuilder = new AlertDialog.Builder(PollActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.alert_sending_layout, null);
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

            String phone = edtClientPhone.getText().toString();
            String name = edtClientName.getText().toString();
            String email = edtClientEmail.getText().toString();

            encuestaApi = new EncuestaApi();
            postResponse = encuestaApi.sendEncuesta(TagApi.API_HEADER_CONTENT_TYPE_VALUE, userSesion.getToken_type() + " " + userSesion.getAccess_token(), phone, name, email, idEncuesta);

            return null;
        }


        @Override
        protected void onPostExecute(String result) {

            String msgResponse = "";

            if (postResponse != null) {
                msgResponse = "SMS: " + postResponse.getSMSResult() + "; Email: " + postResponse.getEmailResult();

                edtClientPhone.getText().clear();
                edtClientName.getText().clear();
                edtClientEmail.getText().clear();
            }
            else {
                msgResponse = "No fue posible enviar la encuesta. Por favor intentelo nuevamente.";
            }

            Snackbar.make(rootView, msgResponse, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            alertDialog.cancel();
        }

    }

    /**
     * Obtiene los datos del destinatario
     * **/
    private class AsyncGetDestinatario extends AsyncTask<String, String, String> {

        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        ProgressBar progressBar;

        DestinatarioData destinatarioData = new DestinatarioData();

        @Override
        protected void onPreExecute() {
            // Crea un dialogo que indica que se está cargando la información
            dialogBuilder = new AlertDialog.Builder(PollActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.alert_loading_layout, null);
            dialogBuilder.setView(dialogView);
            alertDialog = dialogBuilder.create();
            alertDialog.show();

        }

        @Override
        protected void onProgressUpdate(String... text) {
        }

        @Override
        protected String doInBackground(String... params) {

            //
            destinatarioApi = new DestinatarioApi();

            String phone = edtClientPhone.getText().toString();

            destinatarioData = destinatarioApi.findDestinatario(TagApi.API_HEADER_CONTENT_TYPE_VALUE, userSesion.getToken_type() + " " + userSesion.getAccess_token(), phone);

            return null;
        }


        @Override
        protected void onPostExecute(String result) {

            if (destinatarioData != null) {
                if (destinatarioData.getNombre() != null && !destinatarioData.getNombre().isEmpty()) {
                    edtClientName.setText(destinatarioData.getNombre());
                } else {
                    edtClientName.setText("");
                }

                if (destinatarioData.getEmail() != null && !destinatarioData.getEmail().isEmpty()) {
                    edtClientEmail.setText(destinatarioData.getEmail());
                } else {
                    edtClientEmail.setText("");
                }
            } else {
                edtClientName.setText("");
                edtClientEmail.setText("");
            }

            alertDialog.cancel();
        }

    }

}
