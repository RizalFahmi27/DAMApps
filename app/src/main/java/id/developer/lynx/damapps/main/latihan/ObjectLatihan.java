package id.developer.lynx.damapps.main.latihan;

/**
 * @author bend
 * @version 1.0
 * @date 11/6/2016
 * Object untuk list data latihan
 */

public class ObjectLatihan {

    private String text, gambar;

    public ObjectLatihan(String text, String gambar){
        this.text = text;
        this.gambar = gambar;
    }

    public String getGambar() {
        return gambar;
    }

    public String getText() {
        return text;
    }
}
