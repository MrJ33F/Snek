package project.android.snek.gameframework.controller;

import android.app.Activity;
import android.view.ViewGroup;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import project.android.snek.gameframework.controller.interfaces.OnDirectionChangedListener;
import project.android.snek.gameframework.controller.interfaces.ScoreboardChangedListener;
import project.android.snek.gameframework.controller.interfaces.ViewController;
import project.android.snek.gameframework.food.FoodManager;
import project.android.snek.gameframework.perk.PerkManager;
import project.android.snek.gameframework.player.Player;
import project.android.snek.gameframework.player.PlayerManager;
import project.android.snek.utils.Vector;

public class GameEngine {

    private static GameEngine instance;

    public static final int FPS = 60;
    private static final long INTERVAL_MS = 1000 / FPS;
    private static final long DELAY_MS = 500;

    private final ViewController viewController;
    private final Timer timer;


    private FoodManager foodManager;
    private PerkManager perkManager;
    private PlayerManager playerManager;

    private GameEngine(Activity ctx, ViewGroup group, OnDirectionChangedListener listener, ScoreboardChangedListener scoreboardChangedListener) {
        this.timer = new Timer();
        this.viewController = GameViewController.init(ctx, group);

        int screenWidth = ctx.getResources().getDisplayMetrics().widthPixels;
        int screenHeight = ctx.getResources().getDisplayMetrics().heightPixels;
        Vector playerInitPosition = Grid.absoluteCenter();

        Grid.init(screenWidth, screenHeight, playerInitPosition);

        playerManager = new PlayerManager(ctx, listener, this::stop, scoreboardChangedListener);
        foodManager = new FoodManager(ctx);
        perkManager = new PerkManager(ctx);

        playerManager.initBots(ctx, foodManager);

        initView();
    }

    public static GameEngine init(Activity activity, ViewGroup viewGroup, OnDirectionChangedListener listener, ScoreboardChangedListener scoreboardChangedListener) {
        instance = new GameEngine(activity, viewGroup, listener, scoreboardChangedListener);

        return instance;
    }

    private void initView() {
        viewController.attachView(foodManager);
    }

    public void start() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                update();
            }
        }, DELAY_MS, INTERVAL_MS);
    }

    private void update() {
        List<Player> playerList = playerManager.update();

        foodManager.update(playerList);
        perkManager.update(playerList);
    }

    public void stop() {
        timer.cancel();
        viewController.destroy();
        instance = null;
    }

}
