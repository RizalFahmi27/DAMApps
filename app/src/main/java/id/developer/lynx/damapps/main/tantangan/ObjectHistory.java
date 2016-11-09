package id.developer.lynx.damapps.main.tantangan;

/**
 * Created by Bend on 11/9/2016.
 */

public class ObjectHistory {

    private Integer id, hasil;
    private String tanggal;

    public ObjectHistory(Integer id, String tanggal, Integer hasil){
        this.id = id;
        this.tanggal = tanggal;
        this.hasil = hasil;
    }

    public Integer getHasil() {
        return hasil;
    }

    public Integer getId() {
        return id;
    }

    public String getTanggal() {
        return tanggal;
    }
}
