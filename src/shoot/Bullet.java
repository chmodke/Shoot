package shoot;


/**
 * 子弹
 * @author KeHao
 *
 */
public class Bullet extends FlyingObject{
	private int speed=3;
	public Bullet(int x,int y) {
		image=ShootGame.bullet;
		width=image.getWidth();
		height=image.getHeight();
		this.x=x;
		this.y=y;
	}
	@Override
	public void step() {
		y-=speed;
	}
	@Override
	public boolean outOfBounds() {
		if(this.y+this.height<0){
			return true;
		}
		return false;
	}
}
