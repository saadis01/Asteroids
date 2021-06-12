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

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SpaceShip {
    private final Bitmap image;
    private final Bitmap laserBullet;
    int screenWidth;
    int screenHeight;
    private final Context context;
    private final Resources resources;
    private float top;
    private float left;
    private Timer fireTimer = new Timer();
    private float bulletTop;
    private float bulletLeft;
    private boolean bulletCreated;
    private RectF rectF;
    private RectF rectBulletF;
    private boolean asteroidHit = false;
    private MediaPlayer fireSound;
    private MediaPlayer shipExplodedSound;
    private boolean displayShip = false;
    List<Bitmap> explosionBitmaps = new ArrayList<>();
    private boolean renderExplosion = false;
    private Bitmap explosionBitmap;
    private int explosionRenderedIndex = 0;

    public SpaceShip(@NonNull Bitmap image,
                     @Nullable Float top, @Nullable Float left,
                     Context context, Resources resources){

        this.image = image;
        this.screenWidth = resources.getDisplayMetrics().widthPixels;
        this.screenHeight = resources.getDisplayMetrics().heightPixels;
        this.context = context;
        this.resources = resources;
        if (top != null) {
            this.top = top;
        }
        else {
            this.top = screenHeight/2 - Constants.SPACE_SHIP_Y_OFFSET;
        }
        if (left != null){
            this.left = left;
        }
        else {
            this.left = screenWidth/2 - Constants.SPACE_SHIP_X_OFFSET;
        }

        laserBullet = BitmapFactory.decodeResource(resources, R.drawable.laserbullet);

        rectF = new RectF(this.left, this.top,
                this.left + this.image.getWidth(),
                this.top + this.image.getHeight());

        rectBulletF = new RectF(this.bulletLeft, this.bulletTop - Constants.SPACE_SHIP_BULLET_RECT_Y_OFFSET,
                this.bulletLeft + this.laserBullet.getWidth(),
                this.bulletTop + this.laserBullet.getHeight());

        fireSound = MediaPlayer.create(context, R.raw.fire);
        shipExplodedSound = MediaPlayer.create(context, R.raw.shipexploded);

        createExplosionBitmaps();

    }

    private void createExplosionBitmaps() {
        explosionBitmaps.add(BitmapFactory.decodeResource(resources, R.drawable.explosion1));
        explosionBitmaps.add(BitmapFactory.decodeResource(resources, R.drawable.explosion2));
        explosionBitmaps.add(BitmapFactory.decodeResource(resources, R.drawable.explosion3));
        explosionBitmaps.add(BitmapFactory.decodeResource(resources, R.drawable.explosion4));
        explosionBitmaps.add(BitmapFactory.decodeResource(resources, R.drawable.explosion5));
        explosionBitmaps.add(BitmapFactory.decodeResource(resources, R.drawable.explosion6));
        explosionBitmaps.add(BitmapFactory.decodeResource(resources, R.drawable.explosion7));
        explosionBitmaps.add(BitmapFactory.decodeResource(resources, R.drawable.explosion8));
        explosionBitmap = explosionBitmaps.get(0);
    }

    public Bitmap getImage() {
        return image;
    }

    public float getLeft() {
        return left;
    }

    public float getTop() {
        return top;
    }

    public void Move(float top, float left){
        this.top = top - Constants.SPACE_SHIP_MOVE_Y_OFFSET;
        this.left = left  - Constants.SPACE_SHIP_MOVE_X_OFFSET;
    }

    public void fireLaserBullet(){
        // if asteroid was hit, remove the bullet from the screen
        if (asteroidHit){
            bulletTop = Constants.SPACE_SHIP_BULLET_OFF_SCREEN_Y_OFFSET;
            bulletCreated = false;
            asteroidHit = false;
            return;
        }
        if (bulletCreated) {
            // move bullet up until it's out of the screen
            bulletTop -= Constants.SPACE_SHIP_BULLET_MOVE_UP_SPEED;
            if (bulletTop < 0){
                bulletCreated = false;
            }
        }
        else {
            // create bullet
            if (this.displayShip) fireSound.start();
            bulletLeft = left+Constants.SPACE_SHIP_BULLET_CREATE_X_OFFSET;
            bulletTop = top-Constants.SPACE_SHIP_BULLET_CREATE_Y_OFFSET;
            bulletCreated = true;
        }

        rectBulletF.top = bulletTop - Constants.SPACE_SHIP_BULLET_RECT_Y_OFFSET;
        rectBulletF.left = bulletLeft;
        rectBulletF.right = this.bulletLeft + this.laserBullet.getWidth();
        rectBulletF.bottom = this.bulletTop - Constants.SPACE_SHIP_BULLET_RECT_Y_OFFSET + this.laserBullet.getHeight();
    }

    public Bitmap getLaserBullet() {
        return laserBullet;
    }

    public float getBulletLeft() {
        return bulletLeft;
    }

    public float getBulletTop() {
        return bulletTop;
    }

    public RectF getRectF() {
        rectF.left = this.left + Constants.SPACE_SHIP_RECT_X_OFFSET;
        rectF.top = this.top + Constants.SPACE_SHIP_RECT_Y_OFFSET;
        rectF.right = this.left + this.image.getWidth() - Constants.SPACE_SHIP_RECT_RIGHT_OFFSET;
        rectF.bottom = this.top + this.image.getHeight() - Constants.SPACE_SHIP_RECT_BOTTOM_OFFSET;
        return rectF;
    }

    public RectF getRectBulletF() {
        return rectBulletF;
    }

    public void asteroidHit() {
        this.asteroidHit = true;
        // if asteroid was hit, remove the bullet from the screen
        if (asteroidHit){
            bulletTop = Constants.SPACE_SHIP_BULLET_OFF_SCREEN_Y_OFFSET;
            bulletCreated = false;
            asteroidHit = false;
        }
    }

    public void shipHit(){
        shipExplodedSound.start();
        startExplosionAnimation();
        renderExplosion = true;
        displayShip = false;
    }

    private void startExplosionAnimation() {
        Timer explosionAnimationTimer = new Timer();
        explosionAnimationTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (explosionRenderedIndex >= explosionBitmaps.size()) {
                    renderExplosion = false;
                    explosionRenderedIndex = 0;
                    explosionAnimationTimer.cancel();
                }
                else {
                    explosionBitmap = explosionBitmaps.get(explosionRenderedIndex);
                }
                explosionRenderedIndex++;

            }
        }, Constants.SPACE_SHIP_EXPLOSION_ANIMATION_TIMER_DELAY, Constants.SPACE_SHIP_EXPLOSION_ANIMATION_TIMER_PERIOD);

    }

    public boolean displayShip() {
        return displayShip;
    }

    public void setDisplayShip(boolean displayShip) {
        this.displayShip = displayShip;
    }

    public boolean renderExplosion() {
        return renderExplosion;
    }

    public Bitmap getExplosionBitmap() {
        return explosionBitmap;
    }
}
