package org.succlz123.blockanalyzer;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;

import java.util.HashMap;
import java.util.concurrent.FutureTask;

/**
 * Created by succlz123 on 2016/12/16.
 */

public class HandlerThreads {
    public static final int THREAD_UI = 0;
    public static final int THREAD_REPORT = 1;
    public static final int THREAD_BACKGROUND = 2;
    public static final int THREAD_BACK_IO = 3;

    public static final int THREAD_SIZE = 4;
    /** 线程信息数组 */
    private static final Handler[] INTERNAL_HANDLER_LIST = new Handler[THREAD_SIZE];
    private static final String[] INTERNAL_THREAD_NAME_LIST = {
            "thread_ui",
            "thread_report",
            "thread_background",
            "thread_back_io",
    };
    private static final Object LOCK = new Object();
    private static final HashMap<String, Handler> HANDLER_MAP = new HashMap<>(4); // guardby LOCK

    /**
     * 派发任务
     *
     * @param index 线程索引
     * @param r     Runnable
     */
    public static void post(int index, Runnable r) {
        Handler handler = getHandler(index);
        handler.post(r);
    }

    public static void postDelayed(int index, Runnable r, long delayMillis) {
        Handler handler = getHandler(index);
        handler.postDelayed(r, delayMillis);
    }

    public static void postAtFront(int index, Runnable r) {
        Handler handler = getHandler(index);
        handler.postAtFrontOfQueue(r);
    }

    /**
     * 获取线程Handler
     *
     * @param index 线程索引
     * @return 线程的Handler
     */
    public static Handler getHandler(int index) {
        if (index < 0 || index >= THREAD_SIZE) {
            throw new IndexOutOfBoundsException();
        }

        if (INTERNAL_HANDLER_LIST[index] == null) {
            synchronized (INTERNAL_HANDLER_LIST) {
                Handler handler;
                if (index == THREAD_UI) {
                    handler = new Handler(Looper.getMainLooper());
                } else {
                    HandlerThread thread = new HandlerThread(INTERNAL_THREAD_NAME_LIST[index],
                            Process.THREAD_PRIORITY_DEFAULT + Process.THREAD_PRIORITY_LESS_FAVORABLE);
                    thread.start();
                    handler = new Handler(thread.getLooper());
                }
                INTERNAL_HANDLER_LIST[index] = handler;
            }
        }
        return INTERNAL_HANDLER_LIST[index];
    }

    /**
     * Gets or creates a handler instance with specified name
     *
     * @param tname unique handler thread name, null means to UI handler
     * @return the handler instance
     */
    public static Handler getHandler(String tname) {
        if (tname == null) {
            return getHandler(THREAD_UI);
        }
        tname = tname.intern();
        Handler handler = HANDLER_MAP.get(tname);
        if (handler == null) {
            synchronized (LOCK) {
                handler = HANDLER_MAP.get(tname);
                if (handler != null) {
                    return handler;
                }
                // normal priority
                HandlerThread thread = new HandlerThread(tname);
                thread.start();
                handler = new Handler(thread.getLooper());
                HANDLER_MAP.put(tname, handler);
            }
        }
        return handler;
    }

    public static Looper getLooper(int index) {
        return getHandler(index).getLooper();
    }

    /**
     * Gets or creates a Looper instance belong with specified name Handler thread
     *
     * @param tname unique handler thread name, null means to UI handler
     * @return the Looper instance
     */
    public static Looper getLooper(String tname) {
        return getHandler(tname).getLooper();
    }

    /**
     * @return true if the current thread is the specified handler thread.
     */
    public static boolean runningOn(int index) {
        return getHandler(index).getLooper() == Looper.myLooper();
    }

    /**
     * @return true if the current thread is the specified handler thread.
     */
    public static boolean runningOn(String tname) {
        return getHandler(tname).getLooper() == Looper.myLooper();
    }

    /**
     * Run the supplied Runnable on the thread. The method will block until the Runnable
     * completes.
     *
     * @param r The Runnable to run.
     */
    public static void runOnBlocking(int index, final Runnable r) {
        if (runningOn(index)) {
            r.run();
        } else {
            FutureTask<Void> task = new FutureTask<Void>(r, null);
            post(index, task);
            try {
                task.get();
            } catch (Exception e) {
                throw new RuntimeException("Exception occured while waiting for runnable", e);
            }
        }
    }

    public static void runOn(int index, final Runnable r) {
        if (runningOn(index)) {
            r.run();
        } else {
            post(index, r);
        }
    }

    /**
     * Removes callback in the specified handler thread
     */
    public static void remove(int index, final Runnable r) {
        getHandler(index).removeCallbacks(r);
    }

    /**
     * Removes callback in the specified handler thread
     */
    public static void remove(String tname, final Runnable r) {
        getHandler(tname).removeCallbacks(r);
    }
}
