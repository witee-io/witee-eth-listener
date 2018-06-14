package com.mbr.chain.common.utils;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Ethan
 * @date 上午11:54:18
 * @email windofdusk@gmail.com
 * 类说明
 */

public class TimestampPkGenerator {
	
	private String APP = "10";
	private static final int PK_LENGTH = 18;
	private static final int PK_MAX_LENGTH = 14;
	private static final String LENGTH_LACK_FILL = "0";
	private Lock lock = new ReentrantLock();

	public Long next(Class<?> clazz) {
		return this.next(clazz.getSimpleName());
	}

	public Long next(String clazzName) {
		Long ret = null;
		try{
			lock.lock();
			ret = Long.valueOf(padding(String.valueOf(System.nanoTime()), APP));
		}finally{
			lock.unlock();
		}
		return ret;
	}
	
//	public static void main(String[] args){
//	 	for (int i=0;i<100;i++){
//			System.out.println(new TimestampPkGenerator().next(CoinInfo.class));
//		}
//	}
	
	public static String padding(String str, String app){
		StringBuffer sbf = new StringBuffer(app);
		sbf.append(str);
		int lengthLack = 0;
		if(sbf.length() < PK_LENGTH){
			lengthLack = PK_LENGTH - sbf.length();
		}
		for(int i = 0; i < lengthLack; i++){
			sbf.append(LENGTH_LACK_FILL);
		}
		if(sbf.length() > PK_MAX_LENGTH){
            //截取前几位
           return sbf.substring(sbf.length() - PK_MAX_LENGTH, sbf.length());
        }
		return sbf.toString();
	}


}
