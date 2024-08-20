package com.example.movie_ticket_booking.Controllers;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movie_ticket_booking.Models.Address;
import com.example.movie_ticket_booking.Models.Cinema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;


public class CinemaController extends GenericController<Cinema> {
    private static CinemaController _instance = null;

    private CinemaController() {
        super("cinemas", Cinema.class);
    }

    public static synchronized CinemaController getInstance() {
        if (_instance == null)
            _instance = new CinemaController();
        return _instance;
    }
    public Map<String, List<Cinema>> reorderedMap(List<Cinema> cinemas){
        AuthUserController controller = AuthUserController.getInstance();
        Map<String, List<Cinema>> map = new HashMap<>();


        map.put("closest", new ArrayList<>());

        cinemas.forEach(x -> {
            Log.d("cinemas", String.format("%s: %f",x.toString(), distance(x.getAddress(),controller.getLatitudeLocation().getValue(), controller.getLongtitudeLocation().getValue())));
                if(distance(x.getAddress(),controller.getLatitudeLocation().getValue(), controller.getLongtitudeLocation().getValue()) <= 20.0)
                    map.get("closest").add(x);
                if(map.get(x.getAddress().getProvince()) == null)
                    map.put(x.getAddress().getProvince(), new ArrayList<>());
                map.get(x.getAddress().getProvince()).add(x);
        });

        return map;
    }


//    d = 2R × sin⁻¹(√[sin²((θ₂ - θ₁)/2) + cosθ₁ × cosθ₂ × sin²((φ₂ - φ₁)/2)])
    private double distance(Address C, double latitude, double longitude){
        double R = 6371;
        double dLat = deg2rad(latitude - C.getLatitude());
        double dLon = deg2rad(longitude - C.getLongitude());
        double a = Math.pow(Math.sin(dLat/2), 2) +Math.cos(deg2rad(C.getLatitude())) * Math.cos(deg2rad(latitude))
                *Math.pow(Math.sin(dLon/2),2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return  R * c;
    }

    private  double deg2rad(double deg) {return deg * (Math.PI/180);}
    public void initial() {
        Cinema c1 = new Cinema("TP Cine Quang Trung", new Address("645 Quang Trung, Phường 11", "Gò Vấp", "tp HCM", 10.835345,106.660409),new ArrayList<>(), new ArrayList<>());
        Cinema c2 = new Cinema("TP Cine Trần Quang Khải", new Address("62 Quang Trung, Phường 11", "Tân Định", "tp HCM", 10.792770,106.693936),new ArrayList<>(), new ArrayList<>());
        Cinema c3 = new Cinema("TP Cine Vincom Rạch Giá", new Address("62 Cô Bắc, Phường Vĩnh Bảo", "Rạch Giá", "Kiên Giang", 10.003273,105.081162),new ArrayList<>(), new ArrayList<>());
        Cinema c4 = new Cinema("TP Cine Hoàng Hoa Thám", new Address("10 Hoàng Hoa Thám", "Nha Trang", "Khánh Hòa", 12.248916,109.193900),new ArrayList<>(), new ArrayList<>());
        Cinema c5 = new Cinema("TP Cine Vincom Trần Phú", new Address("78 Trần Phú, Phường Lộc Thọ", "Nha Trang", "Khánh Hòa", 12.233234,109.197069),new ArrayList<>(), new ArrayList<>());
        Cinema c6 = new Cinema("TP Cine Hai Bà Trưng", new Address("135 Hai Bà Trưng, Phường Bến Nghé", "Quận 1", "tp HCM", 10.782650,106.698159),new ArrayList<>(), new ArrayList<>());
        _instance.add(c1);
        _instance.add(c2);
        _instance.add(c3);
        _instance.add(c4);
        _instance.add(c5);
        _instance.add(c6);
    }
    public LiveData<List<Cinema>> getCinemaPresenting(String MovieId){
        MutableLiveData<List<Cinema>> liveData = new MutableLiveData<>();

        this.db.collection(this.collectionPath)
                .whereArrayContains("nowPresenting", MovieId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Cinema> l = new ArrayList<>();
                    queryDocumentSnapshots.forEach(doc -> {
                        Cinema m = doc.toObject(Cinema.class);
                        m.setId(doc.getId());
                        l.add(m);
                    });
                    liveData.setValue(l);
                })
                .addOnFailureListener(e -> {
                    Log.e(collectionPath, "Error fetching document", e);
                    liveData.setValue(Collections.emptyList());
                });

        return liveData;
    }
}
