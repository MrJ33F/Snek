package project.android.snek.gameframework.player;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import project.android.snek.gameframework.GameConstants;
import project.android.snek.gameframework.controller.GameViewController;
import project.android.snek.gameframework.controller.Grid;
import project.android.snek.gameframework.controller.interfaces.OnDirectionChangedListener;
import project.android.snek.gameframework.controller.interfaces.ScoreboardChangedListener;
import project.android.snek.gameframework.food.FoodProvider;
import project.android.snek.gameframework.player.bot.BotPlayer;
import project.android.snek.gameframework.player.bot.Brain;
import project.android.snek.gameframework.player.bot.NeuralNet;
import project.android.snek.gameframework.player.bot.Radar;
import project.android.snek.gameframework.player.human.HumanPlayer;

public class PlayerManager implements PlayerProvider {

    private final Runnable onGameOverCallback;
    private final ScoreboardChangedListener scoreboardChangedListener;

    private Player human;
    private List<Player> bots;

    public PlayerManager(Context context, OnDirectionChangedListener listener, Runnable onGameOver, ScoreboardChangedListener scoreboardChangedListener) {
        human = new HumanPlayer(context, listener, Grid.absoluteCenter(), Config.getInstance(context).getName());
        bots = new ArrayList<>(GameConstants.BOT_COUNT);

        onGameOverCallback = onGameOver;
        this.scoreboardChangedListener = scoreboardChangedListener;

        attachPlayer(human);
    }

    public void initBots(Context context, FoodProvider foodProvider) {
        final Brain brain = new NeuralNet(new Radar(foodProvider, this));

        Arrays.stream(GameConstants.BOT_NAMES, 0, GameConstants.BOT_COUNT)
                .map(name -> new BotPlayer(context, name, brain))
                .peek(this::attachPlayer)
                .forEach(bot -> bots.add(bot));

        final Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                List<Player> topPlayers = top5();

                final String score = IntStream.rangeClosed(1, topPlayers.size())
                        .mapToObj(i -> {
                            final Player player = topPlayers.get(i - 1);
                            return i + ". " + player.getName() + " " + (int) player.getRadius() + "\n";
                        }).reduce("", (a, b) -> a + b);
                scoreboardChangedListener.onScoreChanged(score);
            }
        }, 2500, 250);
    }

    public List<Player> update() {
        human.update();
        Grid.getInstance().update(human.getAbsolutePosition(), human.getMagnitude());
        bots.forEach(Player::update);
        checkForIntersections();

        List<Player> toUpdate = new ArrayList<>(bots);
        toUpdate.add(human);

        return toUpdate;
    }

    @Override
    public List<Player> playersWithinRadius(Player bot, double radius) {
        List<Player> within = bots.stream()
                .filter(player -> player.getAbsolutePosition().distance(bot.getAbsolutePosition()) < radius)
                .collect(Collectors.toList());

        if (human.getAbsolutePosition().distance(bot.getAbsolutePosition()) < radius)
            within.add(human);

        within.remove(bot);

        return within;
    }

    private void checkForIntersections() {
        List<Player> toDelete = new LinkedList<>();

        for (int i = 0; i < bots.size() - 1; i++) {
            Player playerA = bots.get(i);
            for (int j = i + 1; j < bots.size(); j++) {
                Player playerB = bots.get(j);

                if (playerA.intersects(playerB) && playerA.getRadius() > playerB.getRadius())
                    toDelete.add(onKill(playerA, playerB));
                else if (playerB.intersects(playerA) && playerB.getRadius() > playerA.getRadius())
                    toDelete.add(onKill(playerB, playerA));
            }
        }

        for (Player bot : bots) {
            if (human.intersects(bot) && human.getRadius() > bot.getRadius())
                toDelete.add(onKill(human, bot));
            else if (bot.intersects(human) && bot.getRadius() > human.getRadius())
                onGameOver();
        }

        bots.removeAll(toDelete);
        GameViewController.getInstance().detachViews(toDelete);
    }

    private Player onKill(Player eater, Player eaten) {
        eater.grow(eaten.getRadius());
        eaten.onDeath();

        return eaten;
    }

    private void attachPlayer(Player player) {
        GameViewController.getInstance().attachView(player);
    }

    private void onGameOver() {
        onGameOverCallback.run();
    }

    private List<Player> top5() {
        List<Player> players = new ArrayList<>(bots);
        players.add(human);

        players.sort((playerA, playerB) -> {
            final double a = playerA.getRadius();
            final double b = playerB.getRadius();

            return - Double.compare(a, b);
        });

        return players.subList(0, Math.min(players.size(), 5));
    }

}
