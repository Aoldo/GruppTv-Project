package com.grupptva.runnergame.game.model.gamecharacter.hook;

import com.grupptva.runnergame.game.model.gamecharacter.Point;

/**
 * Responsibility: Define methods for hooks.
 *
 * Used by:
 * @see AbstractHook
 *
 * Uses:
 * @see Point
 *
 * @Author Karl and Agnes
 */

public interface IHook {

	void initHook(Point hookPosition, float hookLength);

	float getYPosAfterSwing(float characterXPos);

	float getReleaseVelocity(float characterXPos, float characterYPos, float jumpInitialVelocity);

	float getReleaseVelocity(float hookXPos, float hookYPos, float characterXPos, float characterYPos,
	                         float jumpInitialVelocity);

	void moveHook(float pixelsPerFrame);

	boolean canSwing(float characterXPos);

}
