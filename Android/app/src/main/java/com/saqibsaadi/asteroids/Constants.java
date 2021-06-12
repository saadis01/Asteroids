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

public interface Constants {

    public static final String DEBUG_TAG = "DEBUG";

    public static final String GAME_OVER_TEXT = "GAME OVER";
    public static final float GAME_OVER_X_OFFSET = 150f;
    public static final float GAME_OVER_TEXT_SIZE = 60f;

    public static final String CREDITS_LABEL = "Credits:";
    public static final String CREDITS_TEXT_LINE_1 = "Music by Eric Matyas";
    public static final String CREDITS_TEXT_LINE_2 = "www.soundimage.org";
    public static final float CREDITS_X_OFFSET = 210f;
    public static final float CREDITS_TEXT_SIZE = 50f;
    public static final float CREDITS_TOP_OFFSET = 600f;

    public static final float GAME_SPEED = 10f;

    public static final float SCORE_TOP = 100f;

    public static final float START_BUTTON_LEFT_OFFSET = 150f;
    public static final float START_BUTTON_TOP_OFFSET = 200f;

    public static final float SPACE_SHIP_Y_OFFSET = 400f;
    public static final float SPACE_SHIP_X_OFFSET = 120f;
    public static final float SPACE_SHIP_RECT_Y_OFFSET = 25f;
    public static final float SPACE_SHIP_RECT_X_OFFSET = 25f;
    public static final float SPACE_SHIP_RECT_RIGHT_OFFSET = 25f;
    public static final float SPACE_SHIP_RECT_BOTTOM_OFFSET = 50f;
    public static final float SPACE_SHIP_MOVE_Y_OFFSET = 120f;
    public static final float SPACE_SHIP_MOVE_X_OFFSET = 100f;

    public static final float SPACE_SHIP_BULLET_OFF_SCREEN_Y_OFFSET = -200f;
    public static final float SPACE_SHIP_BULLET_MOVE_UP_SPEED = 40f;
    public static final float SPACE_SHIP_BULLET_CREATE_X_OFFSET = 70f;
    public static final float SPACE_SHIP_BULLET_CREATE_Y_OFFSET = 45f;
    public static final float SPACE_SHIP_BULLET_RECT_Y_OFFSET = 50f;

    public static final int SPACE_SHIP_EXPLOSION_ANIMATION_TIMER_DELAY = 300;
    public static final int SPACE_SHIP_EXPLOSION_ANIMATION_TIMER_PERIOD = 100;

    public static final float ASTEROID_OFF_SCREEN_TOP_OFFSET = -600f;
    public static final float ASTEROID_OFF_SCREEN_BOTTOM_OFFSET = 400f;
    public static final float ASTEROID_OFF_SCREEN_LEFT_OFFSET = -600f;
    public static final float ASTEROID_OFF_SCREEN_RIGHT_OFFSET = 400f;
    public static final float ASTEROID_RECT_X_OFFSET = 25f;
    public static final float ASTEROID_RECT_Y_OFFSET = 25f;
    public static final float ASTEROID_RECT_RIGHT_OFFSET = 25f;
    public static final float ASTEROID_RECT_BOTTOM_OFFSET = 50f;
    public static final int ASTEROID_REENTER_TIMER_DELAY = 5000;
    public static final int ASTEROID_EXPLOSION_HIDE_TIMER_DELAY = 5000;
    public static final float ASTEROID_REENTERY_LEFT = -200f;
    public static final float ASTEROID_REENTERY_TOP = -200f;

}
