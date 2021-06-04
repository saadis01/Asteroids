package com.saqibsaadi.asteroids;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

enum MoveDirection{
    VerticalDown,
    HorizontalDown,
    Diagnal;

    private static Random random = new Random();

    public static MoveDirection getRandomDirection(){
        return values()[random.nextInt(values().length)];
    }
}

public class Asteroid {
    private final Bitmap image;
    private final Context context;
    private float top = 0;      // Y
    private float left = 0;     // X
    private float selfSpeed;
    private MoveDirection moveDirection;
    private float gameSpeed;
    private int screenWidth, screenHeight;
    private Random random = new Random();
    private float totalSpeedX;
    private float totalSpeedY;
    private RectF rectF;
    private boolean displayAsteroid = false;
    private Timer enableDisplayTimer = new Timer();
    private MediaPlayer bangLarge; //bangSmall, bangMedium, bangLarge;
    private final int numHitsBeforeExploding;
    private int hitCount = 0;
    private boolean renderExplosion = false;

    public Asteroid(@NonNull Bitmap image, @NonNull float gameSpeed,
                    @NonNull int screenWidth, @NonNull int screenHeight,
                    @NonNull int numHitsBeforeExploding,
                    @NonNull Context context){
        this.numHitsBeforeExploding = numHitsBeforeExploding;
        this.context = context;

//        bangSmall = MediaPlayer.create(context,R.raw.bangsmall);
//        bangMedium = MediaPlayer.create(context,R.raw.bangmedium);
        bangLarge = MediaPlayer.create(context,R.raw.banglarge);

        this.image = image;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.gameSpeed = gameSpeed;

        // Randomly set asteroid's self speed that is used to calculate the total speed
        // this makes each asteroid move a different speeds
        // (unless the random number ends up being the same)
        selfSpeed = GameController.getRandomInt(10);
        // Randomly determine how the asteroid should move
        moveDirection = MoveDirection.getRandomDirection();
        // Set asteroid's move speed
        setMoveSpeed();

        rectF = new RectF(this.left, this.top,
                this.left + this.image.getWidth(),
                this.top + this.image.getHeight());

        // Randomly set asteroid's entry location
        this.setRandomEntryLocation();

        displayAsteroid = true;

    }

    private void setMoveSpeed() {
        this.totalSpeedX = this.selfSpeed + this.gameSpeed;
        this.totalSpeedY = this.selfSpeed + this.gameSpeed;
        if (moveDirection == MoveDirection.HorizontalDown){
            totalSpeedY = 300 + this.selfSpeed + this.gameSpeed;
        }
        if (moveDirection == MoveDirection.VerticalDown){
            totalSpeedX = 300 + this.selfSpeed + this.gameSpeed;
        }
    }

    public Bitmap getImage() {
        return image;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public void Move() {
        if (this.displayAsteroid) {
            switch (moveDirection){
                case VerticalDown:
                    moveVerticalDown();
                    break;
                case HorizontalDown:
                    moveHorizontalDown();
                    break;
                case Diagnal:
                    moveDiagonal();
                    break;
            }
        }

    }

    private void moveVerticalDown() {
        // move up/down depending on the value of totalSpeedY
        this.top += totalSpeedY;
        // when out of the screen either on the top or the bottom
        if (this.top < Constants.ASTEROID_OFF_SCREEN_TOP_OFFSET || this.top > screenHeight+Constants.ASTEROID_OFF_SCREEN_BOTTOM_OFFSET){
            // change direction
            totalSpeedY = totalSpeedY * -1;
            // move left/right depending on the value of totalSpeedX
            this.left += totalSpeedX;
        }
        // Out of the screen when moving right
        if (this.left > screenWidth) {
            // make the value negative, so it starts moving left
            totalSpeedX = Math.abs(totalSpeedX) * -1;
        }
        // Out of the screen when moving left
        if (this.left < -100){
            // make the value positive, so it start moving right
            totalSpeedX = Math.abs(totalSpeedX);
        }
    }

    private void moveHorizontalDown() {
        // move left/right depending on the value of totalSpeedX
        this.left += totalSpeedX;
        // when out of the screen either on the left or the right
        if (this.left < Constants.ASTEROID_OFF_SCREEN_LEFT_OFFSET || this.left > screenWidth+Constants.ASTEROID_OFF_SCREEN_RIGHT_OFFSET){
            // change direction
            totalSpeedX = totalSpeedX * -1;
            // move up/down depending on the value of totalSpeedY
            this.top += totalSpeedY;
        }
        // Out of the screen when moving down
        if (this.top > screenHeight) {
            // make the value negative, so it starts moving up
            totalSpeedY = Math.abs(totalSpeedY) * -1;
        }
        // Out of the screen when moving up
        if (this.top < -100){
            // make the value positive, so it start moving down
            totalSpeedY = Math.abs(totalSpeedY);
        }
    }

    private void moveDiagonal() {
        // move left/right
        this.left += totalSpeedX;
        // move up/down
        this.top += totalSpeedY;

        // when out of the screen either on the left or the right
        if (this.left < Constants.ASTEROID_OFF_SCREEN_LEFT_OFFSET  || this.left > screenWidth+Constants.ASTEROID_OFF_SCREEN_RIGHT_OFFSET){
            totalSpeedX = totalSpeedX * -1;
        }
        // when out of the screen either on the top or the bottom
        if (this.top < Constants.ASTEROID_OFF_SCREEN_LEFT_OFFSET  || this.top > screenHeight+Constants.ASTEROID_OFF_SCREEN_RIGHT_OFFSET){
            totalSpeedY = totalSpeedY * -1;
        }
    }

    public RectF getRectF() {
        this.setRectF();
        return rectF;
    }

    private void setRectF() {
        rectF.left = this.left+Constants.ASTEROID_RECT_X_OFFSET;
        rectF.top = this.top+Constants.ASTEROID_RECT_Y_OFFSET;
        rectF.right = this.left + this.image.getWidth()-Constants.ASTEROID_RECT_RIGHT_OFFSET;
        rectF.bottom = this.top + this.image.getHeight()-Constants.ASTEROID_RECT_BOTTOM_OFFSET;
    }

    public void hit(){
        if (hitCount >= numHitsBeforeExploding) {
            renderExplosion = true;
            bangLarge.start();
            startExplosionAnimationTimer();

            // hide asteroid
            this.displayAsteroid = false;

            // Re-enter asteroid in 5 seconds
            enableDisplayTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    hitCount = 0;
                    setRandomEntryLocation();
                    setRectF();
                    moveDirection = MoveDirection.getRandomDirection();
                    setMoveSpeed();
                    displayAsteroid = true;

                }
            }, Constants.ASTEROID_REENTER_TIMER_DELAY);
        }
        else  {
            hitCount++;
        }
    }

    private void startExplosionAnimationTimer() {
        Timer explosionAnimationTimer = new Timer();
        explosionAnimationTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                renderExplosion = false;
            }
        }, Constants.ASTEROID_EXPLOSION_HIDE_TIMER_DELAY);

    }

    private void setRandomEntryLocation(){
        String[] entryLocationsArray = new String[]{"TopLeft", "TopRight", "BottomLeft", "BottomRight"};
        int indx = GameController.getRandomInt(4);
        switch (entryLocationsArray[indx]){
            case "TopLeft":
                this.left = Constants.ASTEROID_REENTERY_LEFT;
                this.top = Constants.ASTEROID_REENTERY_TOP;
                this.setRectF();
            case "TopRight":
                this.left = this.screenWidth;
                this.top = Constants.ASTEROID_REENTERY_TOP;
                this.setRectF();
                break;
            case "BottomLeft":
                this.left = Constants.ASTEROID_REENTERY_LEFT;
                this.top = this.screenHeight;
                this.setRectF();
                break;
            case "BottomRight":
                this.left = this.screenWidth;
                this.top = this.screenHeight;
                this.setRectF();
                break;
            default:
                this.left = Constants.ASTEROID_REENTERY_LEFT;
                this.top = Constants.ASTEROID_REENTERY_TOP;
                this.setRectF();
        }
    }

    public boolean displayAsteroid(){
        return this.displayAsteroid;
    }

    public boolean renderExplosion() {
        return renderExplosion;
    }

}
