package cl.ckelar.android.ketito.controllers;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChartView;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.charts.TagCloud;
import com.anychart.core.Chart;
import com.squareup.picasso.Picasso;

import java.util.List;

import cl.ckelar.android.ketito.R;
import cl.ckelar.android.ketito.dto.Empresa;
import cl.ckelar.android.ketito.dto.Encuesta;
import cl.ckelar.android.ketito.dto.Preguntas;
import cl.ckelar.android.ketito.dto.PreguntasAlternativas;
import cl.ckelar.android.ketito.dto.Respuesta;
import cl.ckelar.android.ketito.dto.Toma;
import cl.ckelar.android.ketito.dto.Ubicacion;
import cl.ckelar.android.ketito.dto.UserSesion;
import cl.ckelar.android.ketito.helpers.ChartGenerator.ColumnChartGenerator;
import cl.ckelar.android.ketito.helpers.ChartGenerator.PieChartGenerator;
import cl.ckelar.android.ketito.helpers.ChartGenerator.TagCloudGenerator;
import cl.ckelar.android.ketito.helpers.db.KetitoDatabase;


public class ChartviewActivity extends AppCompatActivity {

    private ImageButton imgBtnLogo;
    private ImageButton btnBack;
    private TextView txtPregunta;
    private TextView totalRespuestas;
    private TextView txtEncuesta;
    private TextView totalTomas;

    private TextView txtUbiWelcome;
    private TextView txtBusinessWelcome;

    private List<Respuesta> respuestaList;
    private List<Toma> tomaList;
    private List<Preguntas> preguntasList;
    private List<Chart> listChart;
    private KetitoDatabase ketitoDatabase;
    private UserSesion userSesion;
    private Respuesta respuesta;
    private Encuesta encuesta;
    private AnyChartView anyChartView;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_chartview);

        int idPregunta = getIntent().getExtras().getInt("IdPregunta");

        ketitoDatabase = ketitoDatabase.getInstance(ChartviewActivity.this);
        userSesion = ketitoDatabase.userSesionDao().getUserSesionFirst();
        btnBack = findViewById(R.id.imgButtonBackChart);
        txtEncuesta = findViewById(R.id.txtNomEncuesta);
        txtPregunta = findViewById(R.id.txtNomPregunta);
        totalRespuestas = findViewById(R.id.txtTotalRespuestas);
        totalTomas = findViewById(R.id.txtTotalTomas);
        imgBtnLogo= findViewById(R.id.imgBtnLogoSetting);
        txtBusinessWelcome = findViewById(R.id.txtBusinessWelcome);
        txtUbiWelcome = findViewById(R.id.txtTokenWelcome);


        Preguntas preguntas = ketitoDatabase.preguntasDao().getPreguntasById(idPregunta);
        List<Respuesta> listResp = ketitoDatabase.respuestasDao().getRespuestaByIdPregunta(preguntas.getIdPregunta());
        respuestaList = listResp;
        Encuesta encuesta = ketitoDatabase.encuestaDao().getEncuestaBySelected(true);
        tomaList = ketitoDatabase.tomaDao().getTomaListByEncuesta(encuesta.getIdEncuesta());
        Integer IdTipo = preguntas.getIdTipo();



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

        //Datos tabla:
        String encuestaText = txtEncuesta.getText().toString();
        encuestaText = encuestaText.replace("%nomEncuesta", encuesta.getTitulo());
        txtEncuesta.setText(encuestaText);

        String preguntaText = txtPregunta.getText().toString();
        preguntaText = preguntaText.replace("%nomPregunta", preguntas.getPregunta());
        txtPregunta.setText(preguntaText);

        String totalRespuestasText = totalRespuestas.getText().toString();
        totalRespuestasText= totalRespuestasText.replace("%totalRespuestas", ((Integer)listResp.size()).toString());
        totalRespuestas.setText(totalRespuestasText);

        String totalTomasText = totalTomas.getText().toString();
        totalTomasText = totalTomasText.replace("%totalTomas", ((Integer)tomaList.size()).toString());
        totalTomas.setText(totalTomasText);


        anyChartView = findViewById(R.id.any_chart_view1);
        PieChartGenerator pieChartGenerator = new PieChartGenerator();
        ColumnChartGenerator columnChartGenerator = new ColumnChartGenerator();
        TagCloudGenerator tagCloudGenerator = new TagCloudGenerator();
        Pie pie;
        Cartesian cartesian;
        TagCloud tagCloud;
        List<PreguntasAlternativas> preguntasAlternativasList;


        switch (IdTipo) {
            case 1://multiple
            case 2://unica
                preguntasAlternativasList = ketitoDatabase.preguntasAlternativasDao().getListPreguntasAlternativasByIdPregunta(preguntas.getIdPregunta());
                pie = pieChartGenerator.GeneratorPie(listResp, preguntasAlternativasList,preguntas);
                anyChartView.setChart(pie);
                break;
            case 3://
                pie = pieChartGenerator.GeneratorPie(listResp, null,preguntas);
                anyChartView.setChart(pie);
                break;
            case 4:
                tagCloud = tagCloudGenerator.GeneratorCloud(listResp,preguntas);
                anyChartView.setChart(tagCloud);
                break;
            case 5://preguntalibre
            case 6://preguntamultiple
                cartesian = columnChartGenerator.GeneratorColumn(listResp,preguntas);
                anyChartView.setChart(cartesian);
                break;
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent().setClass(ChartviewActivity.this, DashboardActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });


    }





}
