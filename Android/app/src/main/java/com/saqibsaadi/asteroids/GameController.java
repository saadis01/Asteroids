package com.saqibsaadi.asteroids;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameController {

    private final int screenWidthPixels;
    private final int screenHeightPixels;
    private int score = 0;      // game score
    private boolean isActive = false; // game being played
    private float backgroundTop = 0;
    private float backgroundLeft = 0;
    private int backgoundImageHeight = 0;
    private float image2BackgroundTop = 0;
    private static Random random = new Random();
    private final Context context;
    private MediaPlayer backgroundMusicInactive;
    private MediaPlayer backgroundMusicActive;
    private final Resources resources;
    private float gameSpeed = Constants.GAME_SPEED;
    private Bitmap scaledBackground;
    private RectF background1RectF;
    private RectF background2RectF;

    public GameController(Context context, Resources resources) {
        this.context = context;
        this.resources = resources;

        backgroundMusicActive = MediaPlayer.create(context, R.raw.gameactivemusic);
        backgroundMusicActive.setLooping(true);

        backgroundMusicInactive = MediaPlayer.create(context, R.raw.gameinactivemusic);
        backgroundMusicInactive.setLooping(true);

        screenWidthPixels = resources.getDisplayMetrics().widthPixels;
        screenHeightPixels = resources.getDisplayMetrics().heightPixels;

        createBackgroundImageResources();
        createAsteroids();
    }

    private void createBackgroundImageResources() {
        Bitmap backgroundImage = BitmapFactory.decodeResource(this.resources, R.drawable.galaxy);
        if (backgroundImage != null) {
            float scale = (float)backgroundImage.getHeight()/(float)screenHeightPixels;
            int newWidth = Math.round(backgroundImage.getWidth()/scale);
            int newHeight = Math.round(backgroundImage.getHeight()/scale);
            scaledBackground = Bitmap.createScaledBitmap(backgroundImage, newWidth, newHeight, true);
            this.backgoundImageHeight = scaledBackground.getHeight();
            this.image2BackgroundTop = (this.backgoundImageHeight) * -1;

            background1RectF = new RectF(this.backgroundLeft, this.backgroundTop,
                    this.backgroundLeft + this.scaledBackground.getWidth(),
                    this.backgroundTop + this.scaledBackground.getHeight());

            background2RectF = new RectF(this.backgroundLeft, this.image2BackgroundTop,
                    this.backgroundLeft + this.scaledBackground.getWidth(),
                    this.image2BackgroundTop + this.scaledBackground.getHeight());
        }
    }

    public void startGame(){
        this.isActive = true;
        score = 0;
        if (backgroundMusicInactive.isPlaying()) backgroundMusicInactive.pause();
        while (!backgroundMusicActive.isPlaying()){
            backgroundMusicActive.start();
            break;
        }
    }

    public void stopGame() {
        this.isActive = false;
        if (backgroundMusicActive.isPlaying()) backgroundMusicActive.pause();
        while (!backgroundMusicInactive.isPlaying()){
            backgroundMusicInactive.start();
            break;
        }
    }

    public List<Asteroid> createAsteroids() {

        List<Asteroid> asteroidList = new ArrayList<>();
        asteroidList.add(
                new Asteroid(BitmapFactory.decodeResource(resources, R.drawable.asteroid1),
                        gameSpeed, screenWidthPixels, screenHeightPixels, 1, this.context));
        asteroidList.add(new Asteroid(
                BitmapFactory.decodeResource(resources, R.drawable.asteroid2),
                gameSpeed, screenWidthPixels, screenHeightPixels, 2, this.context));
        asteroidList.add(new Asteroid(
                BitmapFactory.decodeResource(resources, R.drawable.asteroid3),
                gameSpeed, screenWidthPixels, screenHeightPixels, 2, this.context));
        asteroidList.add(new Asteroid(
                BitmapFactory.decodeResource(resources, R.drawable.asteroid4),
                gameSpeed, screenWidthPixels, screenHeightPixels, 3, this.context));
        asteroidList.add(new Asteroid(
                BitmapFactory.decodeResource(resources, R.drawable.asteroid5),
                gameSpeed, screenWidthPixels, screenHeightPixels, 5, this.context));

        return asteroidList;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score += score;
    }

    public float getBackgroundTop() {
        return backgroundTop;
    }

    public void setBackgroundTop(float backgroundTop) {
        this.backgroundTop = backgroundTop;
    }

    public float getBackgroundLeft() {
        return backgroundLeft;
    }

    public void setBackgroundLeft(float backgroundLeft) {
        this.backgroundLeft = backgroundLeft;
    }

    public void moveBackground() {
        //if (!this.isActive) return;
        this.backgroundTop += 20;
        this.image2BackgroundTop += 20;
        if (this.backgroundTop >= screenHeightPixels){
            this.backgroundTop = (this.backgoundImageHeight) * -1;
        }
        if (this.image2BackgroundTop >= screenHeightPixels){
            this.image2BackgroundTop = (this.backgoundImageHeight) * -1;
        }
    }

    public int getBackgoundImageHeight() {
        return backgoundImageHeight;
    }

    public float getImage2BackgroundTop() {
        return image2BackgroundTop;
    }

    public static int getRandomInt(int bound){
        return random.nextInt(bound);
    }

    public void pauseBackgroundMusic(){
        backgroundMusicActive.pause();
        backgroundMusicInactive.pause();
    }
    public void resumeBackgroundMusic(){
        if (this.isActive) {
            if (backgroundMusicInactive.isPlaying()) backgroundMusicInactive.pause();
            backgroundMusicActive.start();
        }
        else {
            if (backgroundMusicActive.isPlaying()) backgroundMusicActive.pause();
            backgroundMusicInactive.start();
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Bitmap getScaledBackground() {
        return scaledBackground;
    }

    public RectF getBackground1RectF() {
        background1RectF.left = this.backgroundLeft;
        background1RectF.top = this.backgroundTop;
        background1RectF.right = this.backgroundLeft + this.scaledBackground.getWidth();
        background1RectF.bottom = this.backgroundTop + this.scaledBackground.getHeight();
        return background1RectF;
    }

    public RectF getBackground2RectF() {
        background2RectF.left = this.backgroundLeft;
        background2RectF.top = this.image2BackgroundTop;
        background2RectF.right = this.backgroundLeft + this.scaledBackground.getWidth();
        background2RectF.bottom = this.image2BackgroundTop + this.scaledBackground.getHeight();
        return background2RectF;
    }
}
