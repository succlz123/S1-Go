package org.succlz123.blockanalyzer;

import android.app.Application;
import android.os.Looper;
import android.util.Log;

/**
 * Created by succlz123 on 2016/12/16.
 */

public class BlockAnalyzer implements BlockPrinter.BlockListener {
    private static final int LOG_CHUNK_SIZE = 4000;
    static final String TAG = "BlockAnalyzer";
    static final boolean DEBUG = false;
    private static BlockAnalyzer sInstance;
    final Application application;
    private final BlockNotifier mBlockNotifier;
    private final BlockWriter mBlockWriter;

    private BlockAnalyzer(Application application) {
        this.application = application;
        this.mBlockNotifier = new BlockNotifier();
        this.mBlockWriter = new BlockWriter();
        Looper.getMainLooper().setMessageLogging(new BlockPrinter(this));
    }

    public static void install(Application application) {
        sInstance = new BlockAnalyzer(application);
    }

    static BlockAnalyzer get() {
        if(sInstance == null) {
            throw new IllegalArgumentException("BlockAnalyzer is null");
        } else {
            return sInstance;
        }
    }

    private static void print(String log) {
        if(log.length() <= 4000) {
            Log.w("BlockAnalyzer", log);
        } else {
            byte[] bytes = log.getBytes();

            for(int i = 0; i < bytes.length; i += 4000) {
                int count = Math.min(bytes.length - i, 4000);
                Log.w("BlockAnalyzer", new String(bytes, i, count));
            }

        }
    }

    public void onBlock(final Block block) {
        HandlerThreads.getHandler(3).post(new Runnable() {
            public void run() {
                String log = block.toString();
                final String path = BlockAnalyzer.this.mBlockWriter.write(block.getBlockTime(), log);
                HandlerThreads.getHandler(0).post(new Runnable() {
                    public void run() {
                        BlockAnalyzer.this.mBlockNotifier.notify(BlockAnalyzer.this.application, block, path);
                    }
                });
            }
        });
    }
}
