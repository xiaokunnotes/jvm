package com.jinkun.jvm.notes;

/**
 * ClassName: BitStoreUtils
 * Function:  TODO
 * Date:      2021/1/6 15:44
 * author     wangxiaofei9
 * version    V1.0
 */
public class BitStoreUtils {

  /**
   * 最多位数
   */
  final static Integer MAX_LEN = 65536;


  /**
   * 创位图数组
   * @param len 位图长度
   * @return
   * @throws Exception
   */
  public static byte[] createBytes(int len) throws Exception{
    if (len <= 0 || len > MAX_LEN){
      throw new Exception("超出范围");
    }

    int pos = len%8==0?len/8:len/8+1;
    byte[] bm = new byte[pos];
    return bm;
  }

  /**
   * 取得指定位的值
   * @param bm  位图结构数据
   * @param position  须>0
   * @return  true/false
   */
  public static boolean get(byte[] bm, int position){
    int index = getIndex(position);
    int pos=getBitPos(position);

    byte b = bm[index];
    b = (byte)((b >> (7-pos)) & 0x1);
    return b==1?true:false;
  }

  /**
   * 设置某位为true
   * @param bm  位图结构数据
   * @param position  位置  >0
   * @param value 设置值 true/false
   */
  public static void set(byte[] bm,int position,boolean value){
    int index = getIndex(position);
    int pos=getBitPos(position);

    //转换为8个二进制表示字符串
    String bin = byte2Bin(bm[index]);

    //用valve替换原来的值
    StringBuilder sb = new StringBuilder(bin);
    sb.replace(pos,pos+1,value==true?"1":"0");

    //二进制转换为byte
    byte nb = bin2Byte(sb.toString());
    //更新
    bm[index]=nb;
  }
  /**
   * 统计全部true的个数
   * @param bm 位图结构数据
   * @return
   */
  public static int countTrue(byte[] bm){
    return countTrue(bm,1,bm.length*8);
  }

  /**
   * 统计一定范围内true的个数
   * @param bm  位图结构数据
   * @param from  开始位置  >0
   * @param to  结束位置  >0 && <=总位数
   * @return
   */
  public static int countTrue(byte[] bm,int from,int to){
    int count = 0;

    //处理第一个byte
    int i1 = getIndex(from);
    int p1 = getBitPos(from);
    count += countBytes(bm[i1],p1,7);
    //处理中间的n个byte
    int i2 = getIndex(to);
    int p2=getBitPos(to);
    for(int i=i1+1;i<i2;i++){
      count += countBytes(bm[i],0,7);
    }
    //处理最后一个byte
    count += countBytes(bm[i2],0,p2);
    return count;
  }

  /**
   * 统计全部false的个数
   * @param bm 位图结构数据
   * @param from  开始位置  >0
   * @param to  结束位置  >0 && <=总位数
   * @return
   */
  public static int countFalse(byte[] bm,int from,int to){
    return to - countTrue(bm,from,to);
  }

  /**
   * 首个为true 位置
   * @param bm
   * @return
   */
  public static int firstTrue(byte[] bm){
    int position = 0;
    boolean found = false;
    for(int i=0;i<bm.length;i++){
      byte b=bm[i];
      byte bits[] = new byte[8];
      for (int j = 7; j >= 0; j--) {
        bits[j] = (byte)(b & 1);
        b = (byte) (b >> 1);
      }
      for (int k = 0; k <= 7; k++) {
        if (bits[k] == 1){
          found = true;
          break;
        }else{
          position++;
        }

      }
      if (found){
        break;
      }
    }
    return found?position+1:0;
  }
  /**
   * 计算每一个byte中1的个数
   * @param b
   * @param fromIndex
   * @param toIndex
   * @return
   */
  private static int countBytes(byte b,int fromIndex, int toIndex) {
    int count = 0;
    for (int i = 7; i >= 0; i--) {
      //当前位等于1且在开始和结束 的范围内，则计数
      if (i >= fromIndex && i <= toIndex && (byte)(b & 1) == 1){
        count++;
      }
      b = (byte) (b >> 1);
    }
    return count;
  }

  /**
   * 取得字符 的8位二进制字符串
   * 如2，返回 00000010
   * @param b
   * @return
   */
  private static String byte2Bin(byte b) {
    String result = ""
      + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
      + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
      + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
      + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
    return result;
  }
  /**
   * 二进制字符转byte
   * @param bin
   * @return
   */
  private static byte bin2Byte(String bin) {
    int result, len;
    if (null == bin) {
      return 0;
    }
    len = bin.length();
    if (len == 8){
      //第一位是0表示正数否则负数
      result = Integer.parseInt(bin, 2);
      result = bin.charAt(0) == '0'?result:result - 256;
    }else if (len == 4){
      result = Integer.parseInt(bin, 2);
    }else {
      result = 0;
    }
    return (byte) result;
  }


  /**
   * 根据位置取得第几byte (数组下标由0开始)
   * 如22则为1，24则为2
   * @param position
   * @return
   */
  private static int getIndex(Integer position){
    return position%8==0?position/8-1:position/8;
  }
  /**
   * 根据位置取得byte中第几位 (数组下标由0开始)
   * @param position
   * @return
   */
  private static Integer getBitPos(Integer position){
    //没有余数，则位于前一个字符的第7位
    return position%8==0?7:position%8-1;
  }

  public static void main(String[] args) {

    byte[] b;
    try {
      b = BitStoreUtils.createBytes(365);
      System.out.println(String.format("第  %s 日签到前=%s",5,BitStoreUtils.get(b,5)));
      System.out.println(String.format("第  %s 日签到前=%s",17,BitStoreUtils.get(b,17)));
      BitStoreUtils.set(b,5,true);
      BitStoreUtils.set(b,17,true);
      System.out.println(String.format("第  %s 日签到后=%s",5,BitStoreUtils.get(b,5)));
      System.out.println(String.format("第  %s 日签到后=%s",17,BitStoreUtils.get(b,17)));
      System.out.println(String.format("签到总次数=%s",BitStoreUtils.countTrue(b)));
      System.out.println(String.format("前 %s 日签到次数=%s",10,BitStoreUtils.countTrue(b,1,10)));
      System.out.println(String.format("前 %s 日签到次数=%s",20,BitStoreUtils.countTrue(b,1,20)));
      System.out.println(String.format("前 %s 日未签到次数=%s",10,BitStoreUtils.countFalse(b,1,10)));
      System.out.println(String.format("前 %s 日未签到次数=%s",20,BitStoreUtils.countFalse(b,1,20)));
      System.out.println(String.format("首次签到是第 %s 日",BitStoreUtils.firstTrue(b)));
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
