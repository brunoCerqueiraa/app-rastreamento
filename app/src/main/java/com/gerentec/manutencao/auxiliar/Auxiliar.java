package com.gerentec.manutencao.auxiliar;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.os.BatteryManager;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

public class Auxiliar {

    public static String dataAtualCompleta() {
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        String mes = String.valueOf(cal.get(Calendar.MONTH) + 1);
        String dia = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        String ano = String.valueOf(cal.get(Calendar.YEAR));
        String horas = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
        String minutos = String.valueOf(cal.get(Calendar.MINUTE));
        String segundos = String.valueOf(cal.get(Calendar.SECOND));

        if (dia.length() < 2) { dia = "0" + dia; }
        if (mes.length() < 2) { mes = "0" + mes; }
        if (horas.length() < 2) { horas = "0" + horas; }
        if (minutos.length() < 2) { minutos = "0" + minutos; }
        if (segundos.length() < 2) { segundos = "0" + segundos; }
        return ano + mes + dia + horas + minutos + segundos;
    }

    public static String tempoDaUltimaSincCel() {
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        String mes = String.valueOf(cal.get(Calendar.MONTH) + 1);
        String dia = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        String ano = String.valueOf(cal.get(Calendar.YEAR));
        String horas = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
        String minutos = String.valueOf(cal.get(Calendar.MINUTE));
        String segundos = String.valueOf(cal.get(Calendar.SECOND));

        if (dia.length() < 2) { dia = "0" + dia; }
        if (mes.length() < 2) { mes = "0" + mes; }
        if (horas.length() < 2) { horas = "0" + horas; }
        if (minutos.length() < 2) { minutos = "0" + minutos; }
        if (segundos.length() < 2) { segundos = "0" + segundos; }
        return ano + "-" + mes + "-" + dia + " " + horas + ":" + minutos + ":" + segundos;
    }

    public static String tempoDaUltimaSinc(String data) {
        if (data == null || data.equals("")){
            return "__/__/____ - __:__:__";
        }
        else {
            //YYYYMMDDHHMMSS
            String ano = data.substring(0, 4);
            String mes = data.substring(4, 6);
            String dia = data.substring(6, 8);

            String horas = data.substring(8, 10);
            String minutos = data.substring(10, 12);
            String segundos = data.substring(12, 14);

            return ano + "-" + mes + "-" + dia + " " + horas + ":" + minutos + ":" + segundos;
        }
    }

    public static String formataDataDigitada(String data){
        try {
            //DD/MM/YYYY HH:MM
            if (data == null || data.equals("")) {
                return "";
            } else {
                return data.substring(6, 10) + data.substring(3, 5) + data.substring(0, 2) + data.substring(11, 13) + data.substring(14, 16) + "00";
            }
        }
        catch (Exception e){
            return "";
        }
    }

    public static String formatDataByDataBanco(String data) {
        if (data == null || data.equals("")){
            return "__/__/____ - __:__:__";
        }
        else {
            //YYYYMMDDHHMMSS
            String ano = data.substring(0, 4);
            String mes = data.substring(4, 6);
            String dia = data.substring(6, 8);

            String horas = data.substring(8, 10);
            String minutos = data.substring(10, 12);
            String segundos = data.substring(12, 14);

            return dia + "/" + mes + "/" + ano + " - " + horas + ":" + minutos + ":" + segundos;
        }
    }

    public static String dataDiaria() {
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        String mes = String.valueOf(cal.get(Calendar.MONTH) + 1);
        String dia = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        String ano = String.valueOf(cal.get(Calendar.YEAR));

        if (dia.length() < 2) { dia = "0" + dia; }
        if (mes.length() < 2) { mes = "0" + mes; }
        return ano + mes + dia;
    }

    public static String dataSincronizacao() {
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        String mes = String.valueOf(cal.get(Calendar.MONTH) + 1);
        String dia = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        String ano = String.valueOf(cal.get(Calendar.YEAR));
        String horas = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
        String minutos = String.valueOf(cal.get(Calendar.MINUTE));
        String segundos = String.valueOf(cal.get(Calendar.SECOND));

        if (dia.length() < 2) { dia = "0" + dia; }
        if (mes.length() < 2) { mes = "0" + mes; }
        if (horas.length() < 2) { horas = "0" + horas; }
        if (minutos.length() < 2) { minutos = "0" + minutos; }
        if (segundos.length() < 2) { segundos = "0" + segundos; }
        return dia + "/" + mes + "/" + ano + " - " + horas + ":" + minutos + ":" + segundos;
    }

    public static Long multiplicarPor100(String num){
        Double tmp = Double.parseDouble(num.replaceAll(",",".")) * 100;
        return tmp.longValue();
    }

    public static String formataQtde(Long qtde){
        return String.valueOf(Double.parseDouble(qtde.toString()) / 100).replace(".",",");
    }

    public static void copiarArquivo(String srFile, String dtFile) throws Exception {

        File f1 = new File(srFile);
        File f2 = new File(dtFile);
        InputStream in = new FileInputStream(f1);
        OutputStream out = new FileOutputStream(f2);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public static void ajustarRotacaoImagem(String path) throws Exception {
        try {
            ExifInterface exif = new ExifInterface(path);
            String orientacaoExif = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            int orientacaoAjuste = 0;

            if(!TextUtils.isEmpty(orientacaoExif)) {
                if(Integer.parseInt(orientacaoExif) == ExifInterface.ORIENTATION_NORMAL) {
                    orientacaoAjuste = 0;
                }
                else if(Integer.parseInt(orientacaoExif) == ExifInterface.ORIENTATION_ROTATE_90) {
                    orientacaoAjuste = 90;
                }
                else if(Integer.parseInt(orientacaoExif) == ExifInterface.ORIENTATION_ROTATE_180) {
                    orientacaoAjuste = 180;
                }
                else if(Integer.parseInt(orientacaoExif) == ExifInterface.ORIENTATION_ROTATE_270) {
                    orientacaoAjuste = 270;
                }
            }

            Matrix matrix = new Matrix();
            matrix.postRotate(90);

            if(orientacaoAjuste == 90) {
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(),
                        matrix, true);
            }
        }
        catch (Exception e) {
            e.getStackTrace();
        }
    }

    public static void ajustaFoto(String path) {
        Bitmap bitmap = null;
        int w = 0;
        int h = 0;
        Matrix mtx = new Matrix();
        try {
            bitmap = BitmapFactory.decodeFile(path);
            w = bitmap.getWidth();
            h = bitmap.getHeight();
            mtx = new Matrix();

            ExifInterface exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch(orientation) {
                case 3:
                    mtx.postRotate(180);
                    break;
                case 6:
                    mtx.postRotate(90);
                    break;
                case 8:
                    mtx.postRotate(270);
                    break;
                default:
                    mtx.postRotate(0);
                    break;
            }
        }
        catch(Exception e){
            e.getStackTrace();
        }

        Bitmap rotatedBmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
        BitmapDrawable bmpd = new BitmapDrawable(rotatedBmp);
        Integer lateral = 800;
        try {
            FileOutputStream out = new FileOutputStream(path);
            Bitmap bmp = bmpd.getBitmap();
            Integer idx = 1;

            w = bmp.getWidth();
            h = bmp.getHeight();

            if ( w >= h){
                idx = w / lateral;
            } else {
                idx = h / lateral;
            }
            w = w / idx;
            h = h / idx;
            Bitmap bmpReduzido = Bitmap.createScaledBitmap(bmp, w, h, true);
            bmpReduzido.compress(Bitmap.CompressFormat.JPEG, 90, out);
        }
        catch(Exception e) {
            e.getStackTrace();
        }
    }

    public static String showData() {
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        String mes = String.valueOf(cal.get(Calendar.MONTH) + 1);
        String dia = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        String ano = String.valueOf(cal.get(Calendar.YEAR));
        String horas = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
        String minutos = String.valueOf(cal.get(Calendar.MINUTE));

        if (dia.length() < 2) { dia = "0" + dia; }
        if (mes.length() < 2) { mes = "0" + mes; }
        if (horas.length() < 2) { horas = "0" + horas; }
        if (minutos.length() < 2) { minutos = "0" + minutos; }
        return dia + "/" + mes + "/" + ano + " - " + horas + ":" + minutos;
    }

    public static int nivelBateria(Context context){
        float batteryPct = 0;
        try {
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context.registerReceiver(null, ifilter);

            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            batteryPct = (level / (float) scale) * 100;

        }
        catch (Exception e){
            batteryPct = 0;
        }

        return (int)(batteryPct);
    }

    public static boolean gpsHabilitado(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);

        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return false;
        }
        return true;
    }

    public static String removerCaracteres(String text) {

        if(text != null) {

            return text.replaceAll("[ãâàáä]", "a")
                    .replaceAll("[êèéë]", "e")
                    .replaceAll("[îìíï]", "i")
                    .replaceAll("[õôòóö]", "o")
                    .replaceAll("[ûúùü]", "u")
                    .replaceAll("[ÃÂÀÁÄ]", "A")
                    .replaceAll("[ÊÈÉË]", "E")
                    .replaceAll("[ÎÌÍÏ]", "I")
                    .replaceAll("[ÕÔÒÓÖ]", "O")
                    .replaceAll("[ÛÙÚÜ]", "U")
                    .replace('ç', 'c')
                    .replace('Ç', 'C')
                    .replace('ñ', 'n')
                    .replace('Ñ', 'N')
                    .replaceAll("!", "")
                    .replaceAll("%", "")
                    .replaceAll ("\\[\\´\\`\\?!\\@\\#\\$\\%\\¨\\*"," ")
                    .replaceAll("\\(\\)\\=\\{\\}\\[\\]\\~\\^\\]"," ")
                    .replaceAll("[\\.\\;\\-\\_\\+\\'\\ª\\º\\:\\;\\/]"," ")
                    .replaceAll("  ", " ")
                    .trim();
        }
        else {

            return text;
        }
    }
}
