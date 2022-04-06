package com.gerentec.manutencao.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

public class HttpNormalImplementation extends Http {

    public static String request(Map params) throws IOException {
        String response = Http.getInstance(Http.default_).doPost(Http.getUrlServlet() + "/"+Http.getServletRequest(), params);
        return response;
    }

    public String doPost(String url, Map params) throws IOException {
        String queryString = getQueryString(params);
        String text = doPost(url, queryString);
        return text;
    }

    private String doPost(String url, String params) throws IOException {
        String text = "";
        try {
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(4000);
            conn.connect();

            OutputStream out = conn.getOutputStream();
            byte[] bytes = params.getBytes("UTF8");
            out.write(bytes);
            out.flush();
            out.close();

            InputStream in = conn.getInputStream();
            text = readString(in);
            conn.disconnect();
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        return text;
    }

    private String getQueryString(Map params){
        String urlParams = null;
        if (params == null || params.size() == 0) {
            return null;
        }
        else {
            Iterator e = params.keySet().iterator();
            while (e.hasNext()) {
                String key = (String) e.next();
                Object object = params.get(key);
                String value = object.toString();
                urlParams = urlParams == null ? "" : urlParams + "&";
                urlParams += key + "=" + value;
            }
        }
        return urlParams;
    }

    private byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            byte[] bytes = byteArrayOutputStream.toByteArray();
            return bytes;
        }
        finally {
            byteArrayOutputStream.close();
            inputStream.close();
        }
    }

    private String readString(InputStream inputStream) throws IOException {
        byte[] bytes = readBytes(inputStream);
        String text = new String(bytes);
        return text;
    }
}