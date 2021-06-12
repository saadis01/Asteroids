/*
 * MIT License
 *
 * Copyright (c) 2021 Saqib Saadi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.saqibsaadi.asteroids;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_POINTER_DOWN;

public class MainActivity extends AppCompatActivity {

    GameView gameView;
//    Paint drawPaint = new Paint();
    Paint rectPaint = new Paint();
    Paint scorePaint = new Paint();
    Paint gameOverPaint = new Paint();
    Paint creditsPaint = new Paint();

    List<Asteroid> asteroidList = new ArrayList<>();
    int screenWidthPixels;
    int screenHeightPixels;
    SpaceShip spaceShip;

    Bitmap laserBitemap;
    Bitmap scaledBackground;
    Bitmap scaledBackground2;
    Bitmap startButton;
    Bitmap spaceShipBitmap;
    Bitmap asteroidExplosionBitmap;

    float startButtonLeft;
    float startButtonTop;
    int startButtonWidth;
    int startButtonHeight;

    boolean moveTheShip = true;
    GameController gameController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        // Initialize game view
        gameView = new GameView(this);
        this.setContentView(gameView);

        // Initialize game controller
        gameController = new GameController(this, getResources());
        // Get scaled background image
        scaledBackground = gameController.getScaledBackground();
        scaledBackground2 = gameController.getScaledBackground();

        // Initialize space ship
        spaceShipBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.spaceship_sm);
        InitializeSpaceShip();

        // Get screen width and height
        screenWidthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
        screenHeightPixels = Resources.getSystem().getDisplayMetrics().heightPixels;

        // Get start button
        startButton = BitmapFactory.decodeResource(getResources(), R.drawable.startbutton);
        startButtonLeft = screenWidthPixels/2 - Constants.START_BUTTON_LEFT_OFFSET;
        startButtonTop = screenHeightPixels/2 + Constants.START_BUTTON_TOP_OFFSET;
        startButtonWidth = startButton.getWidth();
        startButtonHeight = startButton.getHeight();

        // Score paint
        scorePaint.setAlpha(255);
        scorePaint.setColor(Color.GREEN);
        scorePaint.setTextSize(100);
        // Rect around objects
        rectPaint.setAlpha(155);
        rectPaint.setColor(Color.WHITE);
        // Game over paint
        gameOverPaint.setAlpha(255);
        gameOverPaint.setColor(Color.WHITE);
        gameOverPaint.setTextSize(Constants.GAME_OVER_TEXT_SIZE);
        // Credits Paint
        creditsPaint.setAlpha(255);
        creditsPaint.setColor(Color.CYAN);
        creditsPaint.setTextSize(Constants.CREDITS_TEXT_SIZE);

        // Create asteroids
        asteroidList = gameController.createAsteroids();
        asteroidExplosionBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.asteroidexplosion);

    }

    private void InitializeSpaceShip() {

        spaceShip = new SpaceShip(spaceShipBitmap,
                null, null,
                this, getResources());

        laserBitemap = spaceShip.getLaserBullet();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    /*
    * Runnable to run it in a separate thread
    * */
    public class GameView extends SurfaceView implements Runnable {

        Thread viewThread = null;
        SurfaceHolder holder;
        boolean threadOK = true;
        float touchX = -1;
        float touchY = -1;
        float spaceShipCrashLocationX;
        float spaceShipCrashLocationY;
        RectF bulletRect;
        RectF spaceshipRect;
        Paint redPaint = new Paint();
        Paint yellowPaint = new Paint();

        public GameView(Context context) {
            super(context);
            holder = this.getHolder();
            redPaint.setColor(Color.RED);
            yellowPaint.setColor(Color.YELLOW);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
//            Log.d(Constants.DEBUG_TAG,"onTouchEvent fired");
            touchX = event.getX();
            touchY = event.getY();

            int actionMasked = event.getActionMasked();
            int actionIndex = event.getActionIndex();
            int actionPointer = event.getPointerId(actionIndex);

/*            Log.d(Constants.DEBUG_TAG,"onTouchEvent touchX: " + touchX);
            Log.d(Constants.DEBUG_TAG,"onTouchEvent startButtonLeft: " + startButtonLeft);
            Log.d(Constants.DEBUG_TAG,"onTouchEvent touchY: " + touchY);
            Log.d(Constants.,"onTouchEvent startButtonTop: " + startButtonTop);
            Log.d(Constants.DEBUG_TAG,"onTouchEvent actionIndex: " + actionIndex);
            Log.d(Constants.DEBUG_TAG,"onTouchEvent actionPointer: " + actionPointer);*/

            switch (actionMasked){
                case ACTION_DOWN:
                case ACTION_POINTER_DOWN:
                    // Touched the start button
                    if ((touchX >= startButtonLeft && touchX <= startButtonLeft + startButton.getWidth())
                            &&(touchY >= startButtonTop && touchY <= startButtonTop + startButton.getHeight())){
                        //Log.d(Constants.DEBUG_TAG,"onTouchEvent touched the start button");
                        // action_move is getting called after this. Setting moveTheShip to false to skip
                        // moving the ship
                        moveTheShip = false;
                        if (!gameController.isActive()){
                            // Start the game
                            gameController.startGame();
                            InitializeSpaceShip();
                            spaceShip.setDisplayShip(true);
                        }
                    }

                    break;
                case ACTION_MOVE:
                    if (moveTheShip){
                        if (spaceShip != null){
                            spaceShip.Move(touchY, touchX);
                        }
                    }
                    else {
                        moveTheShip = true;
                    }
                    break;
            }


            return true;
        }

        @Override
        public void run() {

            //lock and post so we don't get flickers
            // also called double buffering
            while (threadOK){
                if (!holder.getSurface().isValid()){
                    continue;
                }

                // create a secondary view in the background
                Canvas gameCanvas = holder.lockCanvas();
                drawMe(gameCanvas);
                holder.unlockCanvasAndPost(gameCanvas);
            }

        }

        private void drawMe(Canvas canvas){


            // Draw background
            canvas.drawBitmap(scaledBackground, gameController.getBackgroundLeft(), gameController.getBackgroundTop(), null);
            //canvas.drawRect(gameController.getBackground1RectF(), yellowPaint);
            canvas.drawBitmap(scaledBackground2, gameController.getBackgroundLeft(), gameController.getImage2BackgroundTop(), null);
            //canvas.drawRect(gameController.getBackground2RectF(), redPaint);

            gameController.moveBackground();

            // Draw start button and game over text
            if (!gameController.isActive()) {
                canvas.drawBitmap(startButton, startButtonLeft, startButtonTop, null);
                canvas.drawText(Constants.GAME_OVER_TEXT, screenWidthPixels/2-Constants.GAME_OVER_X_OFFSET, screenHeightPixels/2, gameOverPaint);

                //Credits
                canvas.drawText(Constants.CREDITS_LABEL, screenWidthPixels/2-Constants.GAME_OVER_X_OFFSET+90f,
                        screenHeightPixels/2 + Constants.CREDITS_TOP_OFFSET, creditsPaint);
                canvas.drawText(Constants.CREDITS_TEXT_LINE_1, screenWidthPixels/2-Constants.CREDITS_X_OFFSET,
                        screenHeightPixels/2 + Constants.CREDITS_TOP_OFFSET + 100f, creditsPaint);
                canvas.drawText(Constants.CREDITS_TEXT_LINE_2, screenWidthPixels/2-Constants.CREDITS_X_OFFSET,
                        screenHeightPixels/2 + Constants.CREDITS_TOP_OFFSET + 200f, creditsPaint);
            }

            // Draw score
            canvas.drawText(String.valueOf(gameController.getScore()), screenWidthPixels/2, Constants.SCORE_TOP, scorePaint);

            bulletRect = spaceShip.getRectBulletF();
            //canvas.drawRect(bulletRect, rectPaint);
            spaceshipRect = spaceShip.getRectF();
            //canvas.drawRect(spaceshipRect, rectPaint);

            for (Asteroid asteroid: asteroidList) {
                asteroid.Move();
                RectF astroidRectF = asteroid.getRectF();
                //canvas.drawRect(astroidRectF, rectPaint);
                // Draw Asteroid
                if (asteroid.displayAsteroid()){
                    canvas.drawBitmap(asteroid.getImage(), asteroid.getLeft(), asteroid.getTop(), null);
                }

                // if game is active detect collisions
                if (gameController.isActive()) {
                    if (asteroid.displayAsteroid() && astroidRectF.intersect(bulletRect)) {
                        //Log.d(Constants.DEBUG_TAG,"Asteroid hit!!!");
                        spaceShip.asteroidHit();
                        asteroid.hit();
                        if (asteroid.renderExplosion()) {
                            canvas.drawBitmap(asteroidExplosionBitmap, asteroid.getLeft()-100,
                                    asteroid.getTop(), null);
                        }
                        gameController.setScore(1);
                    }
                    if (asteroid.displayAsteroid() && astroidRectF.intersect(spaceshipRect)){
                        //Log.d(Constants.DEBUG_TAG,"Spaceship hit!!!");
                        spaceShipCrashLocationX = spaceShip.getLeft();
                        spaceShipCrashLocationY = spaceShip.getTop();
                        spaceShip.shipHit();
                        gameController.stopGame();
                    }
                }
            }

            if (spaceShip.displayShip()) {
                // Draw spaceship
                canvas.drawBitmap(spaceShipBitmap, spaceShip.getLeft(), spaceShip.getTop(), null);

                // Draw spaceship laser bullet
                spaceShip.fireLaserBullet();
                canvas.drawBitmap(laserBitemap, spaceShip.getBulletLeft(), spaceShip.getBulletTop(), null);
            }
            else if (spaceShip.renderExplosion()) {
                canvas.drawBitmap(spaceShip.getExplosionBitmap(), spaceShipCrashLocationX, spaceShipCrashLocationY, null);
            }
        }

        // when application is not active
        public void pause() {
            threadOK = false;
            gameController.pauseBackgroundMusic();
            // try to join the thread
            while (true){
                try {
                    viewThread.join();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                break;
            }
            viewThread = null;
        }

        public void resume(){
            threadOK = true;
            viewThread = new Thread(this);
            viewThread.start();
            gameController.resumeBackgroundMusic();
        }
    }

}