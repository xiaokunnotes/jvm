package com.jinkun.jvm.notes;

/**
 * ClassName: VmStackDome
 * Function:  TODO
 * Date:      2020/12/10 17:29
 * author     xiaokun
 * version    V1.0
 */
public class VmStackDome {

    private static Object obj = new Object();

    public void methodOne(int i){

      int j = 1;
      int sum = i+j;
      Object object = obj;
      long start = System.currentTimeMillis();
      methodTwo();

    }

    public void methodTwo(){
      int k = 10;
      System.out.printf("k="+k);
    }

}
