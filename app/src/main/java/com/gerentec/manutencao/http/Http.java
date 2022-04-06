package com.gerentec.manutencao.http;

public abstract class Http {

    public static final int default_ = 1;
    public static boolean serverDown = false;

    public static HttpNormalImplementation getInstance(int type){
        switch (type){
            case default_:
                return new HttpNormalImplementation();
            default:
                return new HttpNormalImplementation();
        }
    }

    public static void setServidorDown(boolean valor){
        serverDown = valor;
    }
    public static String getUrlServlet() {
        //Deixo oculto a URL da Servlet por questões de segurança de dados.
        return "";
    }
    public static String getServletRequest() {
        //Deixo oculto a URL da Servlet por questões de segurança de dados.
        return "";
    }

}
