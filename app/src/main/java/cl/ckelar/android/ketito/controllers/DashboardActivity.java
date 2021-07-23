package cl.ckelar.android.ketito.controllers;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cl.ckelar.android.ketito.MainActivity;
import cl.ckelar.android.ketito.R;
import cl.ckelar.android.ketito.dto.Empresa;
import cl.ckelar.android.ketito.dto.Encuesta;
import cl.ckelar.android.ketito.dto.Preguntas;
import cl.ckelar.android.ketito.dto.Respuesta;
import cl.ckelar.android.ketito.dto.Toma;
import cl.ckelar.android.ketito.dto.Ubicacion;
import cl.ckelar.android.ketito.dto.UserSesion;
import cl.ckelar.android.ketito.helpers.db.KetitoDatabase;

public class DashboardActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private ImageButton imgBtnLogo;
    private TextView txtUbiWelcome;
    private TextView txtBusinessWelcome;

    private ListView lvChartView;
    private ListView lvPreguntasView;
    private ConstraintLayout headerLayout;
    private KetitoDatabase ketitoDatabase;
    private UserSesion userSesion;
    private Respuesta respuesta;
    private Encuesta encuesta;
    private List<Toma> tomaList;
    private List<Preguntas> preguntasList;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);

        ketitoDatabase = ketitoDatabase.getInstance(DashboardActivity.this);
        userSesion = ketitoDatabase.userSesionDao().getUserSesionFirst();

        imgBtnLogo= findViewById(R.id.imgBtnLogoSetting);
        txtBusinessWelcome = findViewById(R.id.txtBusinessWelcome);
        txtUbiWelcome = findViewById(R.id.txtTokenWelcome);

        //Banner
        Ubicacion ubi = ketitoDatabase.ubicacionDao().getUbicacionBySelected(true);
        String ubiText = txtUbiWelcome.getText().toString();
        ubiText = ubiText.replace("%ubi", ubi.getUbicacion());
        txtUbiWelcome.setText(ubiText);

        String businessText = txtBusinessWelcome.getText().toString();
        Empresa empresa = ketitoDatabase.empresaDao().getEmpresaBySelected();

        if (empresa != null) {
            businessText = businessText.replace("%business", empresa.getRazon());
        } else {
            businessText = businessText.replace("%business", "No Seleccionada");
        }

        txtBusinessWelcome.setText(businessText);

        if(empresa.getRutaImagen() != null && empresa.getRutaImagen().length() > 0){
            String url = empresa.getRutaImagen();
            Picasso.get().load(url).into(imgBtnLogo);
        }



        btnBack = findViewById(R.id.imgButtonBackDash);

        lvPreguntasView = findViewById(R.id.lvPreguntasView);
        //new DashboardActivity.AsyncGetPreguntas().execute();
        encuesta = ketitoDatabase.encuestaDao().getEncuestaBySelected(true);
        preguntasList = ketitoDatabase.preguntasDao().getPreguntasAllByEncuesta(encuesta.getIdEncuesta());
        ArrayList<Preguntas> preguntasAuxList = new ArrayList<>();
        for (Preguntas preg: preguntasList) {
            Preguntas pregAux = new Preguntas();
            pregAux.setIdPregunta(preg.getIdPregunta());
            pregAux.setPregunta(preg.getPregunta());
            preguntasAuxList.add(pregAux);
        }

        ArrayAdapter<Preguntas> preguntasArrayAdapter = new ArrayAdapter<Preguntas>(DashboardActivity.this, android.R.layout.simple_list_item_1, preguntasList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLUE);
                return view;
            }
        };
        lvPreguntasView.setAdapter(preguntasArrayAdapter);


        lvPreguntasView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lvPreguntasView.setSelection(position);
                Log.d("BusinessSelectActivity", "Business Selected: " + lvPreguntasView.getItemAtPosition(position).toString());

                Preguntas mPreguntas = (Preguntas) lvPreguntasView.getItemAtPosition(position);
                Intent intent = new Intent(DashboardActivity.this, ChartviewActivity.class);
                intent.putExtra("IdPregunta", mPreguntas.getIdPregunta());
                startActivity(intent);
                finish();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent().setClass(DashboardActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });

    }


}
