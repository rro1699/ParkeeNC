package com.example.ncproject.DAO.Utils.ModelsUtils;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Setter
@Getter
public class Offer {
    private int startHour;
    private int endHour;
    private int n;
    private int m;
    private List<Addition> after;

    @Override
    public String toString(){
        return getStartHour()+", "+getEndHour()+", "+getN()+", "+getM()+", "+getAfter().toString();
    }

    public static Offer creat(){
        List<Addition> list = new ArrayList<>();
        list.add(new Addition(8,18,100,true));
        list.add(new Addition(18,19,0));
        return new Offer(5,6,3,33, list);
    }
    public Offer(int startHour, int endHour, int n, int m) {
        this.startHour = startHour;
        this.endHour = endHour;
        this.n = n;
        this.m = m;
        after = Collections.EMPTY_LIST;
    }

    public Offer(int startHour, int endHour, int n, int m,List<Addition>after) {
        this.startHour = startHour;
        this.endHour = endHour;
        this.n = n;
        this.m = m;
        this.after = after;
    }
}
