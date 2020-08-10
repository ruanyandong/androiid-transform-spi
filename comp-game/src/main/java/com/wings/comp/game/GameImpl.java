package com.wings.comp.game;

import com.wings.comp.base.module.game.IGameService;
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
@ServiceProvider(service = IGameService.class)
public class GameImpl implements IGameService {
    @Override
    public String getGameName() {
        return "DOTA2";
    }
}
