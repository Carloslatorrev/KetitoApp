package cl.ckelar.android.ketito.helpers.ChartGenerator;


import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;

import java.util.ArrayList;
import java.util.List;

import cl.ckelar.android.ketito.dto.Preguntas;
import cl.ckelar.android.ketito.dto.PreguntasAlternativas;
import cl.ckelar.android.ketito.dto.Respuesta;

public class PieChartGenerator {

    /**
     * Genera el Chart/Gráfica
     * @param respuestasList Corresponde al Listado de Respuestas
     * @param preguntasAlternativasList Corresponde al Listado de Alternativas asociado a la pregunta
     * @param preguntas Corresponde al objeto de la Clase Pregunta en relación a la selección del usuario
     * @return Retorna el objeto pie de la clase AnyChart.Pie()
     */
    public Pie GeneratorPie(List<Respuesta> respuestasList, List<PreguntasAlternativas> preguntasAlternativasList, Preguntas preguntas){
        Pie pie = AnyChart.pie();

        List<DataEntry> data = new ArrayList<>();
        if(preguntas.getIdTipo() == 1 || preguntas.getIdTipo() == 2){

            int count = 0;
            for (PreguntasAlternativas pregAlt: preguntasAlternativasList) {
                for (Respuesta respuesta: respuestasList) {
                    if(respuesta.getIdAlternativa().equals(((Integer)pregAlt.getIdAlternativa()).toString())){
                        count++;
                    }
                }
                data.add(new ValueDataEntry(pregAlt.getAlternativa(),count));
                count = 0;
            }

            pie.data(data);

        }else{
            ArrayList<String> niveles = new ArrayList<>();
            niveles.add("1");
            niveles.add("2");
            niveles.add("3");
            niveles.add("4");
            niveles.add("5");

            int count = 0;
            for (String nivel: niveles) {
                for (Respuesta respuesta: respuestasList) {
                    if(respuesta.getNivel().equals(nivel)){
                        count++;
                    }
                }
                if(preguntas.getIdSimbolo() == 2){
                    Integer nivelNum = Integer.parseInt(nivel);
                    switch (nivelNum){
                        case 1:
                            data.add(new ValueDataEntry("1 estrella",count));
                            break;
                        case 2:
                            data.add(new ValueDataEntry("2 estrella",count));
                            break;
                        case 3:
                            data.add(new ValueDataEntry("3 estrella",count));
                            break;
                        case 4:
                            data.add(new ValueDataEntry("4 estrella",count));
                            break;
                        case 5:
                            data.add(new ValueDataEntry("5 estrella",count));
                            break;
                    }
                }else if(preguntas.getIdSimbolo() == 3){
                    Integer nivelNum = Integer.parseInt(nivel);
                    switch (nivelNum){
                        case 1:
                            data.add(new ValueDataEntry("Molesto/Muy Mal",count));
                            break;
                        case 2:
                            data.add(new ValueDataEntry("Triste/Mal",count));
                            break;
                        case 3:
                            data.add(new ValueDataEntry("Serio/Regular",count));
                            break;
                        case 4:
                            data.add(new ValueDataEntry("Sonriente/Bien",count));
                            break;
                        case 5:
                            data.add(new ValueDataEntry("Feliz/Muy Bien",count));
                            break;
                    }
                }else{
                    data.add(new ValueDataEntry(nivel,count));
                }
                count = 0;
            }
            pie.data(data);
        }

        pie.title(preguntas.getPregunta());

        pie.labels().position("outside");

        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("Alternativas")
                .padding(0d, 0d, 10d, 0d);

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);

        return  pie;


    }

}
