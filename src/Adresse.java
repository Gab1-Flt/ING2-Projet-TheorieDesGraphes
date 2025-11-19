public class Adresse {
    private String numero;
    private Arc ar;
    public Adresse(String numero, Arc ar) {
        this.numero = numero;
        this.ar = ar;
    }
    public String getNumero() {
        return numero;
    }

    public void setNumero(int numero) {}
    public Arc getAr() {return ar;}
    public void setAr(Arc ar) {}

}
