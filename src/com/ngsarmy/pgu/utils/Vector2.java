package com.ngsarmy.pgu.utils;

// Vector2 class:
// this is a class which represents
// a 2d mathematical vector. It has
// built in methods for basic utilities
// as well as a few advanced methods
// for convenience
public class Vector2 
{
	public final static Vector2 X = new Vector2(1, 0);
	public final static Vector2 Y = new Vector2(0, 1);
	public final static Vector2 Zero = new Vector2(0, 0);
	
	/** the x-component of this vector **/
	public float x;
	/** the y-component of this vector **/
	public float y;
	
	/** Constructs a new vector at (0,0) */
	public Vector2 () {
	}
	
	/** Constructs a vector with the given components
	 * @param x The x-component
	 * @param y The y-component */
	public Vector2 (float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/** Constructs a vector from the given vector
	 * @param v The vector */
	public Vector2 (Vector2 v) {
		set(v);
	}
	
	public Vector2 cpy () {
		return new Vector2(this);
	}
	
	public static float len (float x, float y) {
		return (float)Math.sqrt(x * x + y * y);
	}
	
	public float len () {
		return (float)Math.sqrt(x * x + y * y);
	}
	
	public static float len2 (float x, float y) {
		return x * x + y * y;
	}
	
	public float len2 () {
		return x * x + y * y;
	}
	
	public Vector2 set (Vector2 v) {
		x = v.x;
		y = v.y;
		return this;
	}
	
	/** Sets the components of this vector
	 * @param x The x-component
	 * @param y The y-component
	 * @return This vector for chaining */
	public Vector2 set (float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Vector2 sub (Vector2 v) {
		x -= v.x;
		y -= v.y;
		return this;
	}
	
	/** Substracts the other vector from this vector.
	 * @param x The x-component of the other vector
	 * @param y The y-component of the other vector
	 * @return This vector for chaining */
	public Vector2 sub (float x, float y) {
		this.x -= x;
		this.y -= y;
		return this;
	}
	
	public Vector2 nor () {
		float len = len();
		if (len != 0) {
			x /= len;
			y /= len;
		}
		return this;
	}
	
	public Vector2 add (Vector2 v) {
		x += v.x;
		y += v.y;
		return this;
	}
	
	/** Adds the given components to this vector
	 * @param x The x-component
	 * @param y The y-component
	 * @return This vector for chaining */
	public Vector2 add (float x, float y) {
		this.x += x;
		this.y += y;
		return this;
	}
	
	public static float dot (float x1, float y1, float x2, float y2) {
		return x1 * x2 + y1 * y2;
	}
	
	public float dot (Vector2 v) {
		return x * v.x + y * v.y;
	}
	
	public float dot (float ox, float oy) {
		return x * ox + y * oy;
	}
	
	public Vector2 scl (float scalar) {
		x *= scalar;
		y *= scalar;
		return this;
	}
	
	/** Multiplies this vector by a scalar
	 * @return This vector for chaining */
	public Vector2 scl (float x, float y) {
		this.x *= x;
		this.y *= y;
		return this;
	}
	
	public Vector2 scl (Vector2 v) {
		this.x *= v.x;
		this.y *= v.y;
		return this;
	}
	
	public Vector2 mulAdd (Vector2 vec, float scalar) {
		this.x += vec.x * scalar;
		this.y += vec.y * scalar;
		return this;
	}
	
	public Vector2 mulAdd (Vector2 vec, Vector2 mulVec) {
		this.x += vec.x * mulVec.x;
		this.y += vec.y * mulVec.y;
		return this;
	}
	
	public static float dst (float x1, float y1, float x2, float y2) {
		final float x_d = x2 - x1;
		final float y_d = y2 - y1;
		return (float)Math.sqrt(x_d * x_d + y_d * y_d);
	}
	
	public float dst (Vector2 v) {
		final float x_d = v.x - x;
		final float y_d = v.y - y;
		return (float)Math.sqrt(x_d * x_d + y_d * y_d);
	}
	
	/** @param x The x-component of the other vector
	 * @param y The y-component of the other vector
	 * @return the distance between this and the other vector */
	public float dst (float x, float y) {
		final float x_d = x - this.x;
		final float y_d = y - this.y;
		return (float)Math.sqrt(x_d * x_d + y_d * y_d);
	}
	
	public static float dst2 (float x1, float y1, float x2, float y2) {
		final float x_d = x2 - x1;
		final float y_d = y2 - y1;
		return x_d * x_d + y_d * y_d;
	}
	
	public float dst2 (Vector2 v) {
		final float x_d = v.x - x;
		final float y_d = v.y - y;
		return x_d * x_d + y_d * y_d;
	}
	
	/** @param x The x-component of the other vector
	 * @param y The y-component of the other vector
	 * @return the squared distance between this and the other vector */
	public float dst2 (float x, float y) {
		final float x_d = x - this.x;
		final float y_d = y - this.y;
		return x_d * x_d + y_d * y_d;
	}
	
	public Vector2 limit (float limit) {
		if (len2() > limit * limit) {
			nor();
			scl(limit);
		}
		return this;
	}
	
	public Vector2 clamp (float min, float max) {
		final float l2 = len2();
		if (l2 == 0f) return this;
		if (l2 > max * max) return nor().scl(max);
		if (l2 < min * min) return nor().scl(min);
		return this;
	}
	
	@Override
	public String toString () {
		return "[" + x + ":" + y + "]";
	}
	
	/** Calculates the 2D cross product between this and the given vector.
	 * @param v the other vector
	 * @return the cross product */
	public float crs (Vector2 v) {
		return this.x * v.y - this.y * v.x;
	}
	
	/** Calculates the 2D cross product between this and the given vector.
	 * @param x the x-coordinate of the other vector
	 * @param y the y-coordinate of the other vector
	 * @return the cross product */
	public float crs (float x, float y) {
		return this.x * y - this.y * x;
	}
	
	/** @return the angle in degrees of this vector (point) relative to the x-axis. Angles are towards the positive y-axis (typically
	 *         counter-clockwise) and between 0 and 360. */
	public float angle () {
		float angle = (float)Math.toDegrees((float)Math.atan2(y, x));
		if (angle < 0) angle += 360;
		return angle;
	}
	
	/** @return the angle in radians of this vector (point) relative to the x-axis. Angles are towards the positive y-axis.
	 *         (typically counter-clockwise) */
	public float getAngleRad () 
	{
		return (float)Math.atan2(y, x);
	}
	
	/** Sets the angle of the vector in degrees relative to the x-axis, towards the positive y-axis (typically counter-clockwise).
	 * @param degrees The angle in degrees to set. */
	public Vector2 setAngle (float degrees) 
	{
		return setAngleRad((float)Math.toRadians(degrees));
	}
	
	/** Sets the angle of the vector in radians relative to the x-axis, towards the positive y-axis (typically counter-clockwise).
	 * @param radians The angle in radians to set. */
	public Vector2 setAngleRad (float radians) {
		this.set(len(), 0f);
		this.rotateRad(radians);
	
		return this;
	}
	
	/** Rotates the Vector2 by the given angle, counter-clockwise assuming the y-axis points up.
	 * @param degrees the angle in degrees */
	public Vector2 rotate (float degrees) {
		return rotateRad((float)Math.toRadians(degrees));
	}
	
	/** Rotates the Vector2 by the given angle, counter-clockwise assuming the y-axis points up.
	 * @param radians the angle in radians */
	public Vector2 rotateRad (float radians) {
		float cos = (float)Math.cos(radians);
		float sin = (float)Math.sin(radians);
	
		float newX = this.x * cos - this.y * sin;
		float newY = this.x * sin + this.y * cos;
	
		this.x = newX;
		this.y = newY;
	
		return this;
	}
	
	/** Rotates the Vector2 by 90 degrees in the specified direction, where >= 0 is counter-clockwise and < 0 is clockwise. */
	public Vector2 rotate90 (int dir) {
		float x = this.x;
		if (dir >= 0) {
			this.x = -y;
			y = x;
		} else {
			this.x = y;
			y = -x;
		}
		return this;
	}
}
