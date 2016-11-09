package id.developer.lynx.damapps.main.tantangan;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Bend on 11/9/2016.
 */

public class ObjectSoal implements Parcelable {

    public Integer id;
    public String soal, jawaban_a, jawaban_b, jawaban_c, jawaban_d, jawaban_benar, type, jawaban_user;

    public ObjectSoal(){}

    public ObjectSoal(Integer id, String soal, String jawaban_a, String jawaban_b, String jawaban_c, String jawaban_d, String jawaban_benar, String type) {
        this.id = id;
        this.soal = soal;
        this.jawaban_a = jawaban_a;
        this.jawaban_b = jawaban_b;
        this.jawaban_c = jawaban_c;
        this.jawaban_d = jawaban_d;
        this.jawaban_benar = jawaban_benar;
        this.type = type;
    }

    public ObjectSoal(Parcel in) {
        id = in.readInt();
        soal = in.readString();
        jawaban_a = in.readString();
        jawaban_b = in.readString();
        jawaban_c = in.readString();
        jawaban_d = in.readString();
        jawaban_benar = in.readString();
        type = in.readString();
        jawaban_user = in.readString();
    }

    public void setJawaban_user(String jawaban_user) {
        this.jawaban_user = jawaban_user;
    }

    public static final Creator<ObjectSoal> CREATOR = new Creator<ObjectSoal>() {
        @Override
        public ObjectSoal createFromParcel(Parcel in) {
            return new ObjectSoal(in);
        }

        @Override
        public ObjectSoal[] newArray(int size) {
            return new ObjectSoal[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(soal);
        dest.writeString(jawaban_a);
        dest.writeString(jawaban_b);
        dest.writeString(jawaban_c);
        dest.writeString(jawaban_d);
        dest.writeString(jawaban_benar);
        dest.writeString(type);
        dest.writeString(jawaban_user);
    }
}
