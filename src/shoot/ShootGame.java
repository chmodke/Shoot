package shoot;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 * 主程序类
 * @author KeHao
 *
 */
public class ShootGame extends JPanel{
	public static final int WIDTH=400;//窗口宽度
	public static final int HEIGHT=654;//窗口高度
	
	public static final int START=0;
	public static final int RUNNING=1;
	public static final int PAUSE=2;
	public static final int OVER=3;
	
	private int state=0;//初始状态
	
	public static BufferedImage background;
	public static BufferedImage start;
	public static BufferedImage pause;
	public static BufferedImage gameover;
	public static BufferedImage airplane;
	public static BufferedImage bee;
	public static BufferedImage bullet;
	public static BufferedImage hero0;
	public static BufferedImage hero1;
	
	private Hero hero=new Hero();//英雄机
	private FlyingObject[]flyings={};//敌人（敌机+小蜜蜂）
	private Bullet[] bullets={};//子弹
	
	private long score;//得分
	
	static{
		try {
			background=ImageIO.read(ShootGame.class.getResource("background.png"));
			start=ImageIO.read(ShootGame.class.getResource("start.png"));
			pause=ImageIO.read(ShootGame.class.getResource("pause.png"));
			gameover=ImageIO.read(ShootGame.class.getResource("gameover.png"));
			airplane=ImageIO.read(ShootGame.class.getResource("airplane.png"));
			bee=ImageIO.read(ShootGame.class.getResource("bee.png"));
			bullet=ImageIO.read(ShootGame.class.getResource("bullet.png"));
			hero0=ImageIO.read(ShootGame.class.getResource("hero0.png"));
			hero1=ImageIO.read(ShootGame.class.getResource("hero1.png"));
		} catch (IOException e) {
			System.out.println("加载图片失败");
			e.printStackTrace();
		}
	}
	/**
	 * 创建敌机+小蜜蜂对对象
	 * @return
	 */
	public static FlyingObject nextOne(){
		Random rand =new Random();
		int type=rand.nextInt(20);
		if(type==0){
			return new Bee();
		}else{
			return new Airplane();
		}
	}
	
	int flyEnteredIndex=0;//敌人入场计数
	/**
	 * 敌人入场
	 */
	public void enterAction(){
		flyEnteredIndex++;
		if(flyEnteredIndex%40==0){
			FlyingObject obj=nextOne();
			flyings=Arrays.copyOf(flyings, flyings.length+1);
			flyings[flyings.length-1]=obj;//将敌人对象装入数组最后
		}
	}
	
	/**
	 * 飞行物走步方法
	 */
	public void stepAction(){
		hero.step();
		for(FlyingObject f:flyings){
			f.step();
		}
		for(Bullet b:bullets){
			b.step();
		}
	}
	int shootIndex=0;
	/**
	 * 英雄机发射子弹
	 */
	public void shootAction(){
		shootIndex++;
		if(shootIndex%30==0){
			Bullet[] bs= hero.shoot();
			bullets=Arrays.copyOf(bullets, bullets.length+bs.length);
			System.arraycopy(bs,0,bullets,bullets.length-bs.length,bs.length);
		}
	}
	
	/**
	 * 检测子弹与所有敌人撞击
	 */
	public void bangAction(){
		for(Bullet b:bullets){
			bang(b);
		}
	}
	/**
	 * 检测一颗子弹与那些敌人撞击
	 * @param b
	 */
	public void bang(Bullet b){
		int index=-1;
		for(int i=0;i<flyings.length;i++){
			FlyingObject f=flyings[i];
			if(f.shootBy(b)){
				index=i;//撞上的敌人的下标
				break;
			}
		}
		if(index!=-1){//有敌人撞上了
			FlyingObject one=flyings[index];
			if(one instanceof Enemy){
				Enemy e=(Enemy)one;
				score+=e.getScore();
//				System.out.println(score);
			}
			if(one instanceof Award){
				Award a=(Award)one;
				switch(a.getType()){
				case Award.FTRE:
					hero.addFire();break;
				case Award.LIFE:
					hero.addLife();break;
				}
			}
			
			FlyingObject t=flyings[index];
			flyings[index]=flyings[flyings.length-1];
			flyings[flyings.length-1]=t;
			flyings=Arrays.copyOf(flyings, flyings.length-1);
		}
	}
	/**
	 * 删除越界飞行物
	 */
	public void outOfBoundsAction(){
		//删敌人------------------------------------------
		int index=0;//不越界数组下标，不越界敌人数量
		FlyingObject[] flyingLives=new FlyingObject[flyings.length];
		for(int i=0;i<flyings.length;i++){
			if(!flyings[i].outOfBounds()){
				flyingLives[index]=flyings[i];
				index++;
			}
		}
		flyings=Arrays.copyOf(flyingLives, index);
		
		//删子弹------------------------------------------
		index=0;
		Bullet[] bulletLives=new Bullet[bullets.length];
		for(int i=0;i<bullets.length;i++){
			if(!bullets[i].outOfBounds()){
				bulletLives[index]=bullets[i];
				index++;
			}
		}
		bullets=Arrays.copyOf(bulletLives, index);
	}
	/**
	 * 检查游戏是否结束
	 */
	public void checkGameOverAction(){
		if(isGameOver()){
			state=OVER;
		}
	}
	/**
	 * 判断游戏结束
	 * @return
	 */
	public boolean isGameOver(){
		int index=0;
		for(int i=0;i<flyings.length;i++){
			if(hero.hit(flyings[i])){
				FlyingObject t=flyings[index];
				flyings[index]=flyings[flyings.length-1];
				flyings[flyings.length-1]=t;
				flyings=Arrays.copyOf(flyings, flyings.length-1);
				
				hero.subtractLife();
				hero.setFire(0);
			}
		}
		if(hero.getLife()<=0){
			return true;
		}
		return false;
	}
	
	private Timer timer;
	private int interval=10;
	/**
	 * 启动程序执行
	 */
	public void action(){
		timer=new Timer();
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				if(state==RUNNING){
					enterAction();//敌人入场
					stepAction();//飞行物走一步
					shootAction();//子弹入场
					bangAction();//检测子弹与敌人撞击
					outOfBoundsAction();//删除越界的飞行物
					checkGameOverAction();//检查游戏是否结束
				}
				repaint();
			}}, interval,interval);
		/**
		 * 鼠标事件处理
		 */
		MouseAdapter l=new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if(state==RUNNING){
					hero.moveTo(e.getX(), e.getY());
				}
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				switch(state){
				case START:
					state=RUNNING;
					break;
				case OVER:
					hero=new Hero();
					flyings=new FlyingObject[0];
					bullets=new Bullet[0];
					state=START;
					score=0;
					break;
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				if(state==PAUSE){
					state=RUNNING;
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if(state==RUNNING){
					state=PAUSE;
				}
			}
		};
		//添加监听器
		this.addMouseListener(l);
		this.addMouseMotionListener(l);
	}
	
	/**
	 * 画图入口
	 */
	public void paint(Graphics g){
		g.drawImage(background,0,0,null);
		paintHero(g);
		paintFlyingObjects(g);
		paintBullets(g);
		paintScore(g);
		paintState(g);
	}
	/**
	 * 画英雄机对象
	 * @param g
	 */
	public void paintHero(Graphics g){
		g.drawImage(hero.image, hero.x, hero.y, null);
	}
	/**
	 * 画敌人对象
	 * @param g
	 */
	public void paintFlyingObjects(Graphics g){
		for(FlyingObject f:flyings){
			g.drawImage(f.image, f.x, f.y, null);
		}
	}
	/**
	 * 画子弹
	 * @param g
	 */
	public void paintBullets(Graphics g){
		for(Bullet b:bullets){
			g.drawImage(b.image, b.x, b.y, null);
		}
	}
	
	public void paintScore(Graphics g){
		g.setColor(new Color(0xff0000));
		g.setFont(new Font("微软雅黑",Font.BOLD,18));
		g.drawString("SCORE:"+score, 5, 20);
		g.drawString("LIFE:"+hero.getLife(), 5, 40);
		g.drawString("FIRE:"+hero.getFire(), 5, 60);
	}
	public void paintState(Graphics g) {
		switch(state){
		case START:
			g.drawImage(ShootGame.start, 0, 0, null);
			break;
		case PAUSE:
			g.drawImage(ShootGame.pause, 0, 0, null);
			break;
		case OVER:
			g.drawImage(ShootGame.gameover, 0, 0, null);
			break;
		}
	}
	public static void main(String[] args) {
		JFrame frame=new JFrame("Fly");
		ShootGame game=new ShootGame();
		frame.add(game);
		frame.setSize(WIDTH, HEIGHT);
		frame.setAlwaysOnTop(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		game.action();//启动执行
	}
}