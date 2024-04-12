package com.example.baitap1;

import java.io.Serializable;

public class Thisinh  implements Serializable {
    private String sbd;

    public Thisinh(String sbd, String name, double toan, double ly, double hoa) {
        this.sbd = sbd;
        this.name = name;
        Toan = toan;
        Ly = ly;
        Hoa = hoa;
    }

    private String name;
    private double Toan,Ly,Hoa;

    public String getName() {
        return name;
    }

    public double getToan() {
        return Toan;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setToan(double toan) {
        Toan = toan;
    }

    public void setLy(double ly) {
        Ly = ly;
    }

    public void setHoa(double hoa) {
        Hoa = hoa;
    }

    public double getLy() {
        return Ly;
    }

    public double getHoa() {
        return Hoa;
    }


    public String getSbd() {
        return sbd;
    }

    public void setSbd(String sbd) {
        this.sbd = sbd;
    }
}
