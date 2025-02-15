//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.nekods.nyaPlus.core;

import com.nekods.nyaPlus.smallTools.Task;
import java.util.LinkedList;
import java.util.Queue;

public class ThreadPool {
    private int poolSize;
    private static ThreadPool INSTANCE;
    private boolean isRunning = true;
    private Queue<Task> taskQueue = null;
    private Thread[] threads = null;

    public Queue<Task> getTaskQueue() {
        return this.taskQueue;
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public static ThreadPool getINSTANCE() {
        if (INSTANCE == null) {
            throw new RuntimeException("线程池未初始化");
        } else {
            return INSTANCE;
        }
    }

    public static ThreadPool getINSTANCE(int pool) {
        if (INSTANCE == null) {
            return INSTANCE = new ThreadPool(pool);
        } else {
            throw new RuntimeException("线程池已经初始化");
        }
    }

    private ThreadPool() {
    }

    private ThreadPool(int poolSize) {
        this.poolSize = poolSize;
        this.taskQueue = new LinkedList();
        this.threads = new Thread[poolSize];
        INSTANCE = new ThreadPool();

        // 创建并启动线程池中的线程
        for(int i = 0; i < poolSize; ++i) {
            this.threads[i] = new NyaThread();
            this.threads[i].start();
        }

    }

    public void add(Task task) {
        synchronized(this.taskQueue) {
            // 将任务添加到队列
            this.taskQueue.add(task);
            // 唤醒一个等待的线程（如果有）来处理任务
            this.taskQueue.notify();

        }
    }

    public void shutdown() {
        this.isRunning = false;
        synchronized(this.taskQueue) {
            // 唤醒所有等待的线程，让它们退出循环
            this.taskQueue.notifyAll();
        }
    }
}
