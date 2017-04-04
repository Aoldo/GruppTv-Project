package com.grupptva.runnergame.character;

public class CharacterModel {
	/**
	 * The box representing the character's shape. Contains the character's
	 * position, width and height.
	 */
	public Rectangle box;
	/**
	 * A reference to the character's box's position. Used to make changing the
	 * position easier.
	 */
	private Point position;

	public float xVelocity = 2.2f;
	private float yVelocity = 0;

	private float gravityAcceleration = -0.4f;

	private boolean collidingWithGround = false;

	/**
	 * This is the initival velocity that the character's yVelocity gets sets to
	 * when initiating a jump.
	 */
	private float jumpInitialVelocity = 10f;
	/**
	 * This coordinate represents the y coordinate the character has to be at in
	 * order to collide with the flor with a single pixel. This is used to put
	 * the character on top of the floor and to prevent the stuttering that
	 * happens if the character were to be put just above the floor.
	 */
	private float groundCollisionCoordinate = 0;

	/**
	 * True if the character has started using the hook. While true the
	 * character will do things related to the hook, like check if the hook has
	 * been fully extended, and when it has been, start swinging with it.
	 */
	public boolean attachedWithHook = false;
	/**
	 * True if the character has fully extended the hook. While true the
	 * character will be swinging with the hook.
	 */
	private boolean hookExtended = false;
	/**
	 * The attachment point of the hook.
	 */
	public Point hookPoint;
	/**
	 * The angle that the hook is thrown at.
	 */
	private float hookAngle = 1f;

	/**
	 * Represents the length of the hook when it is fully extended.
	 */
	public float hookRadius = 100;

	public CharacterModel(float x, float y) {
		box = new Rectangle(x, y, 25, 25);
		position = box.position;
	}

	//-----------------Methods used for gameloop---------------------------------
	/**
	 * Updates the characters state, should be called once per iteration of the
	 * game's logic loop.
	 */
	public void update() {
		moveRight();
		fall();

		hook();

		if (position.getX() > 630)
			position.setX(0);
		if (position.getY() < -100)
			position.setY(0);
	}

	/**
	 * Moves the character to the right.
	 */
	private void moveRight() {
		position.moveX(xVelocity);
	}

	/**
	 * Accelerates the character downwards and then updates its y coordinate, if
	 * it is currently falling.
	 */
	private void fall() {
		if (!collidingWithGround) {
			yVelocity += gravityAcceleration;

			position.moveY(yVelocity);
		}
	}

	/**
	 * Initiates a jump if the character is currently on ground.
	 */
	public void jump() {
		if (collidingWithGround) {
			//Force character out of the object it is currently standing on.
			position.moveY(1);

			yVelocity = jumpInitialVelocity;
		}
	}

	/**
	 * Does the logic for the character's hook, if it is currently in use.
	 */
	private void hook() {
		if (attachedWithHook) {
			if (!hookExtended) {
				if (position.distanceTo(hookPoint) > hookRadius) {
					hookExtended = true;
				}
			} else {
				float newY = hookGetNewY();
				if (attachedWithHook) {
					position.setY(newY);
				}
			}
		}
	}

	/**
	 * Detaches the hook by setting attachedWithHook to false. Also causes the
	 * character to "jump" a bit as it releases the hook, velocity depending on
	 * the angle between the hook and character.
	 */
	public void stopHook() {
		attachedWithHook = false;
		float angleToHook = position.angleBetween(hookPoint);
		//Arbitrary value
		yVelocity = (float) Math.sin(angleToHook + Math.PI / 2) * 10;
	}

	/**
	 * Returns the bottom y coordinate in the circle that is built up by
	 * {@See hookPoint} and {@See hookRadius}, depending on the character's
	 * current x coordinate.
	 * 
	 * @return
	 */
	private float hookGetNewY() {
		//Circle equation: r^2 = (x-a)^2+(y-b)^2 = x-2ax+a^2+y-2yb+b^2
		//TODO: Remove and use actual variables instead of new versions
		float r = hookRadius;
		float x = position.getX();
		float a = hookPoint.getX();
		float b = hookPoint.getY();
		float insideSqrt = -(a * a) + 2 * a * x + r * r - (x * x);
		if (insideSqrt < 0) {
			//If this happens the characters x value is outside of the hooks radius.
			stopHook();
			return 0;
		}
		//get the lowest solution to y.
		return (b - Math.sqrt(insideSqrt) < b + Math.sqrt(insideSqrt))
				? b - (float) Math.sqrt(insideSqrt) : b + (float) Math.sqrt(insideSqrt);
	}

	/**
	 * Initiates the characters hook by attaching it to a point to the top right
	 * of the character, and setting {@See attachWithHook} to true. TODO: Make
	 * it attach to world object instead of a point in the sky.
	 */
	public void initHook() {
		hookPoint = new Point(position);
		hookPoint.moveInDirection(hookRadius, hookAngle);
		attachedWithHook = true;
		hookExtended = false;
	}

	//-----------------Get methods-----------------------------------------------
	/**
	 * Returns the x coordinate of the bottom left corner of the character's
	 * box.
	 * 
	 * @return the characters x coordinate.
	 */
	public float getX() {
		return position.getX();
	}

	/**
	 * Returns the x coordinate of the bottom left corner of the character's
	 * box.
	 * 
	 * @return the characters x coordinate.
	 */
	public float getY() {
		return position.getY();
	}

	//-----------------Public methods used for controlling the model-------------

	/**
	 * Sets the characters x velocity.
	 * 
	 * @param velocity
	 *            the velocity that the character should move at.
	 */
	public void setXVelocity(float velocity) {
		xVelocity = velocity;
	}

	/**
	 * Tell model it is colliding with the ground at coordinate {@Code y}, this
	 * coordinate is supposed to be point of collision because the character is
	 * then moved to that spot, in case it clipped deeper into whatever it is
	 * colliding with.
	 * 
	 * @param y
	 *            the y coordinate the character is colliding with.
	 * @See setGroundCollision, setGroundCollisionCoordinate.
	 */
	public void collideWithGround(float y) {
		//Order here important because setGroundCollision updates characters y position to the coordinate!
		setGroundCollisionCoordinate(y);
		setGroundCollision(true);
	}

	/**
	 * Sets whether the model is colliding with the ground currently or not. If
	 * setting to true, consider using {@See collideWithGround} instead since
	 * this method doesn't update the coordinate that the model is colliding at.
	 * 
	 * @param truth
	 *            the value to set collidingWithGround to.
	 */
	public void setGroundCollision(boolean truth) {
		collidingWithGround = truth;
		if (truth) {
			position.setY(groundCollisionCoordinate);
			yVelocity = 0;
		}
	}

	/**
	 * Sets the y coordinate that the character is collding with ground at. The
	 * character will not move below this value due to gravity as long as
	 * {@See collidingWithGround} is true.
	 * 
	 * @param y
	 */
	public void setGroundCollisionCoordinate(float y) {
		groundCollisionCoordinate = y;
	}
}