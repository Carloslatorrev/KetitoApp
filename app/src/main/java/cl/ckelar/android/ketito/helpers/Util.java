package cl.ckelar.android.ketito.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import cl.ckelar.android.ketito.R;

public class Util {

    /**
     * Obtiene el nombre de la versión actual de la app
     * @see Context
     * @param context
     * @return Retorna el nombre de la versión actual de la app
     * **/
    public static String getVersionName(Context context) {
        String versionName = "";

        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
            //versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(context.getClass().getName(), e.toString());
            versionName = "S/N";
        }

        return versionName;

    }

    /**
     * Obtiene un cuadro dialogo seteado con información del desarrollador
     * @see Context
     * @param context
     * **/
    public static void infoAppDialog(final Context context) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle(context.getResources().getString(R.string.action_about));


        String aboutText = "";

        aboutText += "<p>";
        aboutText += "<b>" + context.getResources().getString(R.string.lbl_about_dev_title) + "</b>";
        aboutText += ": " + context.getResources().getString(R.string.lbl_about_dev_name);
        aboutText += "</p>";

        aboutText += "<p>";
        aboutText += "<b>" + context.getResources().getString(R.string.lbl_about_dev_email_title) + "</b>";
        aboutText += ": <a href='mailto:" + context.getResources().getString(R.string.lbl_about_dev_email) + "'>" + context.getResources().getString(R.string.lbl_about_dev_email) + "</a>";
        aboutText += "</p>";

        aboutText += "<p>";
        aboutText += "<b>" + context.getResources().getString(R.string.lbl_about_dev_website_title) + "</b>";
        aboutText += ": <a href='" + context.getResources().getString(R.string.lbl_about_dev_website_url) + "'>" + context.getResources().getString(R.string.lbl_about_dev_website_url) + "</a>";
        aboutText += "</p>";

        aboutText += "<p><b>";
        aboutText += context.getResources().getString(R.string.lbl_about_dev_copyright);
        aboutText += "</b></p>";

        aboutText += "<p><b>";
        aboutText += context.getResources().getString(R.string.app_version_name_label);
        aboutText += ": " + Util.getVersionName(context);
        aboutText += "</b></p>";

        // set dialog message
        alertDialogBuilder
                .setMessage(Html.fromHtml(aboutText))
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.lbl_about_ok_btn)
                        ,new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        textView.setTextSize(12);
        textView.setTextColor(Color.GRAY);
        textView.setGravity(Gravity.CENTER);

        // Para pantallas LARGE
        if ((context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                (Configuration.SCREENLAYOUT_SIZE_LARGE)) {

            textView.setTextSize(16);

        }

        // Para pantallas XLARGE
        if ((context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                (Configuration.SCREENLAYOUT_SIZE_XLARGE)) {

            textView.setTextSize(16);

        }

        textView.setMovementMethod(LinkMovementMethod.getInstance());

    }

    /**
     * Obtiene un cuadro dialogo seteado con información del desarrollador
     * @see Context
     * @param context
     * **/
    public static void errorAppDialog(final Context context, String message) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Error");

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setIcon(R.drawable.error_icon)
                .setPositiveButton(context.getResources().getString(R.string.lbl_about_ok_btn)
                        ,new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        textView.setTextSize(12);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);

        // Para pantallas LARGE
        if ((context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                (Configuration.SCREENLAYOUT_SIZE_LARGE)) {

            textView.setTextSize(16);

        }

        // Para pantallas XLARGE
        if ((context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                (Configuration.SCREENLAYOUT_SIZE_XLARGE)) {

            textView.setTextSize(16);

        }

        textView.setMovementMethod(LinkMovementMethod.getInstance());

    }

}
