package com.nekods.nyaPlus.test;

import com.nekods.nyaPlus.core.TaskDistributer;

import com.nekods.nyaPlus.core.Controller;

import java.util.Scanner;

public class testMain {


    public static void main(String[] args) {


        TaskDistributer td = new TaskDistributer(1,"C:/Users/31910/Desktop/dics/test_dic.nya");
        Controller.setOUTPUTTER(new Outputter());
        Controller.setVMFactory(new VMF());
        td.init();
        Scanner sc = new Scanner(System.in);
        while(true){
            td.execute(sc.nextLine(), 13123123L);
        }

    }



//    public static void main(String[] args) {
//
//        try {
//
//
//            VarManager varManager = new VarManager();
//            TaskDistributer executer = new TaskDistributer(varManager);
//            System.out.println("----------------------------");
//            executer.add("你好");
//            System.out.println("----------------------------");
//
//
//        } catch (NyaPlusException e) {
//            e.show();
//            e.printStackTrace();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//
//    }


}
