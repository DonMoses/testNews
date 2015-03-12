package com.example.moses.mosesnews.josnbeams.tuling;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by 丹 on 2015/1/17.
 */
public class TuLingInfo implements Parcelable{
    public static final String INFO_IN = "INFO_IN";
    public static final String INFO_OUT = "INFO_OUT";
    private String infoStr;
    private String isInfoIn;

    public void setIsInfoIn(String isInfoIn) {
        this.isInfoIn = isInfoIn;
    }

    public String getInfoStr() {
        return infoStr;
    }

    public void setInfoStr(String infoStr) {
        this.infoStr = infoStr;
    }

    public String getIsInfoIn() {
        return isInfoIn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(infoStr);
        dest.writeString(isInfoIn);
    }

    public static final Creator<TuLingInfo> CREATOR = new Creator<TuLingInfo>() {
        @Override
        public TuLingInfo createFromParcel(Parcel source) {
            TuLingInfo tuLingInfo = new TuLingInfo();
            tuLingInfo.setInfoStr(source.readString());
            tuLingInfo.setIsInfoIn(source.readString());
            return tuLingInfo;
        }

        @Override
        public TuLingInfo[] newArray(int size) {
            return null;

        }
    };

}
