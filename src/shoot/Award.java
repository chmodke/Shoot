package shoot;
/**
 * 奖励
 * @author KeHao
 *
 */
public interface Award {
	public final static int FTRE=0;
	public final static int LIFE=1;
	/**
	 * 获取奖励类型
	 * @return
	 */
	int getType();
}
