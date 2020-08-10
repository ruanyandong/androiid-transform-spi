package com.wings.comp.module;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.wings.comp.base.module.game.IGameService;
import com.wings.comp.base.module.movie.IMovieService;
import com.wings.spi.repository.ServiceRegistry;

/**
 * @author AI
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView)findViewById(R.id.tv)).setText(ServiceRegistry.get(IGameService.class).getGameName()+" "+ServiceRegistry.get(IMovieService.class).getMovieName());
    }
}