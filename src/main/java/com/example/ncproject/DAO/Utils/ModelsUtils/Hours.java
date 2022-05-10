package com.example.ncproject.DAO.Utils.ModelsUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class Hours {
    private Map<String,Integer> prices;
    private Offer offer;

    public Map<String, Integer> getPrices() {
        return prices;
    }

    public Offer getOffer() {
        return offer;
    }

    public static Hours creat(){
        Map<String,Integer> val = new LinkedHashMap<>();
        int p;
        String name;
        for(int i=0;i<=23;i++){
            p = (int) Math.round(Math.random()*1000);
            name = String.format("%02d",i);
            val.put(name,p);
        }
        for(Map.Entry<String,Integer> entry: val.entrySet()){
            System.out.println(entry.getKey()+" - "+entry.getValue());
        }
        return new Hours(val,Offer.creat());
    }

    public Hours(Map<String, Integer> prices,Offer offer) {
        this.prices = prices;
        this.offer = offer;
    }

}
