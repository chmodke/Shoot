package shoot;

import java.awt.image.BufferedImage;
/**
 * 飞行物
 * @author KeHao
 *
 */
public abstract class FlyingObject {
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected BufferedImage image;
	
	public abstract void step();
	
	/**
	 * 敌人被子弹撞
	 * @param b
	 * @return
	 */
	public boolean shootBy(Bullet b){
		if(b.x>this.x&&b.x<this.x+width&&b.y>this.y&&b.y<this.y+height){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 飞行物是否越界
	 * @return
	 */
	public abstract boolean outOfBounds();
}
