package cl.ckelar.android.ketito.helpers.ChartGenerator;

import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.CategoryValueDataEntry;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.TagCloud;
import com.anychart.scales.OrdinalColor;

import java.util.ArrayList;
import java.util.List;

import cl.ckelar.android.ketito.dto.Preguntas;
import cl.ckelar.android.ketito.dto.Respuesta;

public class TagCloudGenerator {


    /**
     * Genera el Chart/Gráfica
     * @param respuestasList Corresponde al Listado de Respuestas
     * @param preguntas Corresponde al objeto de la Clase Pregunta en relación a la selección del usuario
     * @return Retorna el objeto tagCloud de la clase AnyChart.tagCloud()
     */
    public TagCloud GeneratorCloud(List<Respuesta> respuestasList, Preguntas preguntas){

        TagCloud tagCloud = AnyChart.tagCloud();

        tagCloud.title("Palabras con mayor frecuencia");

        OrdinalColor ordinalColor = OrdinalColor.instantiate();
        ordinalColor.colors(new String[] {
                "#26959f", "#f18126", "#3b8ad8", "#60727b", "#e24b26"
        });
        tagCloud.colorScale(ordinalColor);
        tagCloud.angles(new Double[] {-90d, 0d, 90d});

        tagCloud.colorRange().enabled(true);
        tagCloud.colorRange().colorLineSize(15d);


        List<DataEntry> data = new ArrayList<>();
        ArrayList<String> palabras = new ArrayList<>();

        for (Respuesta respuesta: respuestasList) {
            String[] palabra = respuesta.getRespuesta().split(" ");
            for (int i = 0; i < palabra.length; i++) {
                palabras.add(palabra[i]);
            }
        }



        int count = 0;
        Integer categoriaAux = 0;
        for (String strings: palabras) {
            categoriaAux++;
            for (String stringsAux: palabras) {
                if(stringsAux.equalsIgnoreCase(strings)){
                    count++;
                }
            }
            String cat = categoriaAux.toString();

            data.add(new CategoryValueDataEntry(strings, cat, count));
            count = 0;
        }

        tagCloud.data(data);
        return  tagCloud;
    }


}
