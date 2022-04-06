package com.gerentec.manutencao.http;

import com.gerentec.manutencao.auxiliar.Auxiliar;
import com.gerentec.manutencao.modelo.FtSelfie;
import com.gerentec.manutencao.modelo.TbEquipe;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpFileUploader {
    HttpURLConnection connection = null;
    DataOutputStream outputStream = null;
    public static Map params;

    String resposta;
    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary =  "*****";
    int bytesRead, bytesAvailable, bufferSize;
    byte[] buffer;
    int maxBufferSize = 1 * 1024 * 1024;

    public String enviarBD(String pathToOurFile, TbEquipe tbEquipe) throws Exception {
        String urlServer =	Http.getUrlServlet() + "/" + Http.getServletRequest() +
                "?r=envioBD" +
                "&arquivo=" + tbEquipe.getIdEquipe()+"_"+Auxiliar.dataAtualCompleta();

        try {

            FileInputStream fileInputStream = new FileInputStream( new File(pathToOurFile) );
            //FileInputStream fileInputStream = new FileInputStream( new File(Environment.getExternalStorageDirectory(), "TB_TAREFA.txt") );
            URL url = new URL(urlServer);
            connection = (HttpURLConnection) url.openConnection();

            // Allow Inputs & Outputs
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setConnectTimeout(5000);

            // Enable POST method
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

            outputStream = new DataOutputStream( connection.getOutputStream() );
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + pathToOurFile +"\"" + lineEnd);
            outputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // Read file
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {

                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            int serverResponseCode = connection.getResponseCode();
            String serverResponseMessage = connection.getResponseMessage();

            InputStream inputStream = connection.getInputStream();
            resposta = readString(inputStream);

            fileInputStream.close();
            outputStream.flush();
            outputStream.close();
        }
        catch (Exception ex) {

            //Exception handling
            //resposta = "Erro ao enviar arquivo!";
            throw ex;
        }

        return resposta;
    }

    public String enviarFt(String pathToOurFile, FtSelfie ftSelfie) {
        String urlServer =	Http.getUrlServlet() + "/" + Http.getServletRequest() +
                "?r=envioSelfieFoto" +
                "&ftIdSelfie=" + ftSelfie.getIdFtSelfie() +
                "&idSelfie=" + ftSelfie.getIdSelfie() +
                "&arquivo=" + ftSelfie.getArquivo() +
                "&coordX=" + ftSelfie.getCoordX() +
                "&coordY=" + ftSelfie.getCoordY() +
                "&dtCel=" + ftSelfie.getDtInsert();
        try {
            FileInputStream fileInputStream = new FileInputStream( new File(pathToOurFile) );
            URL url = new URL(urlServer);
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setConnectTimeout(5000);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

            outputStream = new DataOutputStream( connection.getOutputStream() );
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + pathToOurFile +"\"" + lineEnd);
            outputStream.writeBytes(lineEnd);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {

                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            int serverResponseCode = connection.getResponseCode();
            String serverResponseMessage = connection.getResponseMessage();
            InputStream inputStream = connection.getInputStream();
            resposta = readString(inputStream);

            fileInputStream.close();
            outputStream.flush();
            outputStream.close();
        }
        catch (Exception e) {
            System.out.println("[HttpFileUploader] Error in HttpFileUploader(Envio Foto Medição Pressão): " + e.toString());
        }
        return resposta;
    }

    private String readString(InputStream in) throws IOException {
        byte[] bytes = readBytes(in);
        String texto = new String(bytes);
        return texto;
    }
    private byte[] readBytes(InputStream in) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) > 0) {
                bos.write(buffer, 0, len);
            }
            byte[] bytes = bos.toByteArray();
            return bytes;
        }
        finally {
            bos.close();
            in.close();
        }
    }
}
