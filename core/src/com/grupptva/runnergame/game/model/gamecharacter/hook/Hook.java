package com.grupptva.runnergame.game.model.gamecharacter.hook;

import com.grupptva.runnergame.game.model.gamecharacter.Point;

/**
 * Responsibility: Represents the movement of a GameCharacter when it's using a Hook.
 *
 * Used by:
 * @see com.grupptva.runnergame.game.model.gamecharacter.GameCharacter
 *
 * Uses:
 * @see Point
 *
 * @Author Karl and Agnes
 */

public class Hook extends AbstractHook {

	@Override
	public void initHook(Point hookPosition, float hookMaxLength) {
		setPosition(hookPosition);
		setMaxLength(hookMaxLength);
		setLength(getMaxLength());
	}

	@Override
	public float getYPosAfterSwing(float characterXPos) {
		return getPosition().getY() - (float) Math.sqrt(calculateSumForCircleRoot(characterXPos));
	}

	public float calculateSumForCircleRoot(float characterXPos) {
		return getLength() * getLength() - ((characterXPos + 1 - getPosition().getX()) * (characterXPos + 1 - getPosition().getX()));
	}

	@Override
	public boolean canSwing(float characterXPos) {
		return calculateSumForCircleRoot(characterXPos) >= 0;
	}

	@Override
	public float getReleaseVelocity(float characterXPos, float characterYPos, float jumpInitialVelocity) {
		return getReleaseVelocity(getPosition().getX(), getPosition().getY(), characterXPos, characterYPos,
				jumpInitialVelocity);
	}

	@Override
	public float getReleaseVelocity(float hookXPos, float hookYPos, float characterXPos, float characterYPos,
	                                float jumpInitialVelocity) {
		double angle = Math.atan((characterYPos - hookYPos) / (characterXPos - hookXPos)) + 3.1415f / 2;
		return (float) Math.sin(angle) * jumpInitialVelocity;
	}

	@Override
	public void moveHook(float pixelsPerFrame) {
		getPosition().setX(getPosition().getX() - pixelsPerFrame);
	}
}
