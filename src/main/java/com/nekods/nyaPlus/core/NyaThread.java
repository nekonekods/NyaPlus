//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.nekods.nyaPlus.core;

import com.nekods.nyaPlus.exceptions.NyaDicNotFoundException;
import com.nekods.nyaPlus.exceptions.NyaPlusException;
import com.nekods.nyaPlus.smallTools.Task;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class NyaThread extends Thread {
    LineAnalyser lineAnalyser = new LineAnalyser();

    public NyaThread() {
    }

    public void run() {

         /*reminder 从task队列中获取task，检查task中的文件中额能不能成功获取到代码集，
               能的话就获取VM，注入VM，将整个TASK移交给LA
               如果不能，就销毁TASK，线程阻塞
               补：
               task为主要媒介*/

        Queue<Task> taskQueue = ThreadPool.getINSTANCE().getTaskQueue();//不安全，不保证被初始化了


        while(ThreadPool.getINSTANCE().isRunning()) {
            Task task;
            synchronized(taskQueue) {
                while(taskQueue.isEmpty()) {
                    try {
                        // 如果队列为空，线程等待
                        taskQueue.wait();
                    } catch (InterruptedException var8) {
                        Thread.currentThread().interrupt();
                    }
                }

                task = taskQueue.poll();
            }

            String[] sentences;
            if (task != null && (sentences = this.search(task)) != null) {
                try {
                    lineAnalyser.analyze(sentences, task);
                } catch (NyaPlusException e) {
                    e.setHead(task.getCommend()).show();
                    e.printStackTrace();
                }
            }
            //else{}
            //没找到，就等着下一个就好了。


    }

    }

    public String[] search(Task t){
        t.setLineAnalyser(lineAnalyser);
        String head = null;
        BufferedReader fr  = null;
        try {
            fr = new BufferedReader(new FileReader(t.getPath()));
            String line;
            boolean isCollecting = false;
            boolean isFindingHead = true;
            ArrayList<String> sentences = new ArrayList();
            String input = t.getCommend();
            VarManager varManager = null;

            while (true) {
                line = fr.readLine();
                if (line == null) {   //这是文件到头了的分支
                    if (!sentences.isEmpty()) {
                        t.setVarManager(varManager);
                        return sentences.toArray(new String[0]);
                    }
                    break;
                }
                if (isFindingHead) {
                    if (line.isBlank()) {

                    } else if (input.matches(line)) {
                        head = line;
                        isFindingHead = false;
                        isCollecting = true;
                        varManager= Controller.getVMFactory().getVarManager(t);
                        varManager.setVar("括号0", input);
                        varManager.setVar("参数-1", input);
                        Matcher matcher = Pattern.compile(line).matcher(input);

                        if (matcher.find()) {
                            for (int i = 1; i <= matcher.groupCount(); i++) {
                                varManager.setVar("括号" + i, matcher.group(i));
                            }
                        }

                    } else {
                        isFindingHead = false;
                    }
                } else if (isCollecting) {
                    if (line.isBlank()) {
                        t.setVarManager(varManager);
                        return sentences.toArray(new String[0]);
                    } else {
                        sentences.add(line);
                    }
                } else {
                    if (line.isBlank()) {
                        isFindingHead = true;
                    }
                }
            }
        } catch (IOException e) {
            throw new NyaDicNotFoundException(t.getPath()).setHead(head);
        } catch (NyaPlusException e) {
            throw e.setHead(head);
        } catch (Exception e){
        }finally{
            try {
                fr.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

}
