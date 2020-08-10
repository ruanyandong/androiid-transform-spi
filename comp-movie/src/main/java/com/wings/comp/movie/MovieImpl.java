package com.wings.comp.movie;

import com.wings.comp.base.module.movie.IMovieService;
import com.wings.spi.annotation.ServiceProvider;

/**
 * @author -> Wings
 * @date -> 2020/8/10
 * @email -> ruanyandongai@gmail.com
 * 729368173@qq.com
 * @phone -> 18983790146
 * @blog -> https://ruanyandong.github.io
 * -> https://blog.csdn.net/qq_34681580
 */
@ServiceProvider(service = IMovieService.class)
public class MovieImpl implements IMovieService {
    @Override
    public String getMovieName() {
        return "海王";
    }
}
