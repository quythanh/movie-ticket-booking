package com.example.movie_ticket_booking.Controllers;

import com.example.movie_ticket_booking.Models.Movie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;

@Getter
public class MovieController {
    private static List<Movie> movies;

    static {
        initial();
    }

    private MovieController(){};

    private static void initial(){
        movies = new ArrayList<>();
        Movie m1 = new Movie("P0001", "Kẻ cắp mặt trăng 4", "https://image.tienphong.vn/350x210/Uploaded/2024/uug-onattvnat/2024_01_29/ava-7-4009.jpg", "https://iguov8nhvyobj.vcdn.cloud/media/catalog/product/cache/1/image/c5f0a1eff4c394a251036189ccddaacd/d/m/dm4_posterminion_700x1000.jpg");
        Movie m2 = new Movie("P0002", "Những mảnh ghép cảm xúc 2", "https://iguov8nhvyobj.vcdn.cloud/media/catalog/product/cache/1/image/1800x/71252117777b696995f01934522c402d/p/o/poster_harbor_instagram_teaser_poster_vietnam.jpeg", "https://m.media-amazon.com/images/M/MV5BYTc1MDQ3NjAtOWEzMi00YzE1LWI2OWUtNjQ0OWJkMzI3MDhmXkEyXkFqcGdeQXVyMDM2NDM2MQ@@._V1_.jpg");
        Movie m3 = new Movie("P0003", "Deadpool 3: Deadpool và Wolverine", "https://iguov8nhvyobj.vcdn.cloud/media/catalog/product/cache/1/image/1800x/71252117777b696995f01934522c402d/1/2/1280x720.jpg", "https://iguov8nhvyobj.vcdn.cloud/media/catalog/product/cache/1/image/1800x/71252117777b696995f01934522c402d/4/7/470x700-deadpool.jpg");
        Movie m4 = new Movie("P0004", "Dr. Strange: Đa vũ trụ hỗn loạn", "https://media1.houstonpress.com/hou/imager/u/slideshow/13329443/hou_art_20220506_dsitmom_header.jpg", "https://upload.wikimedia.org/wikipedia/vi/thumb/3/3c/%C3%81p_ph%C3%ADch_phim_Ph%C3%B9_th%E1%BB%A7y_t%E1%BB%91i_th%C6%B0%E1%BB%A3ng_trong_%C4%90a_V%C5%A9_tr%E1%BB%A5_h%E1%BB%97n_lo%E1%BA%A1n.jpg/220px-%C3%81p_ph%C3%ADch_phim_Ph%C3%B9_th%E1%BB%A7y_t%E1%BB%91i_th%C6%B0%E1%BB%A3ng_trong_%C4%90a_V%C5%A9_tr%E1%BB%A5_h%E1%BB%97n_lo%E1%BA%A1n.jpg");
        movies.add(m1);
        movies.add(m2);
        movies.add(m3);
        movies.add(m4);
    }
    public static List<Movie> getCurrentShowingFilm(){
        if(movies==null || movies.isEmpty())
            initial();
        return MovieController.movies;
    }
}
