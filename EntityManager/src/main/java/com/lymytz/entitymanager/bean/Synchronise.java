package com.lymytz.entitymanager.bean;

public class Synchronise {
    private Object distant;
    private Server serveur;

    public Synchronise(Object distant, Server serveur) {
        this.distant = distant;
        this.serveur = serveur;
    }

    public Object getDistant() {
        return distant;
    }

    public void setDistant(Object distant) {
        this.distant = distant;
    }

    public Server getServeur() {
        return serveur;
    }

    public void setServeur(Server serveur) {
        this.serveur = serveur;
    }

    public static class Server {
        private Integer id;
        private String adresseWs;
        private String adresseIp;

        public Server(Integer id,  String adresseWs, String adresseIp) {
            this.id = id;
            this.adresseWs = adresseWs;
            this.adresseIp = adresseIp;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getAdresseIp() {
            return adresseIp;
        }

        public void setAdresseIp(String adresseIp) {
            this.adresseIp = adresseIp;
        }

        public String getAdresseWs() {
            return adresseWs;
        }

        public void setAdresseWs(String adresseWs) {
            this.adresseWs = adresseWs;
        }
    }
}
