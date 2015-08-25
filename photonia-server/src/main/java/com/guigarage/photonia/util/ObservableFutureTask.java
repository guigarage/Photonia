package com.guigarage.photonia.util;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.function.Consumer;

public class ObservableFutureTask<V> extends FutureTask<V> implements ObservableRunnableFuture<V> {

    private Consumer<Future<V>> futureConsumer;

    private Locker locker;

    public ObservableFutureTask(Callable<V> callable) {
        super(callable);
        locker = new Locker();
    }

    public ObservableFutureTask(Runnable runnable, V result) {
        super(runnable, result);
        locker = new Locker();
    }


    @Override
    protected void done() {
        locker.callLocked(() -> {
            if (futureConsumer != null) {
                futureConsumer.accept(this);
            }
            return null;
        });

    }

    @Override
    public void onDone(Consumer<Future<V>> futureConsumer) {
        locker.runLocked(() -> this.futureConsumer = futureConsumer);
    }
}
