package com.guigarage.photonia.services;

import com.guigarage.photonia.util.ObservableFutureTask;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.function.Consumer;

@Service
public class SpringAsyncService implements AsyncService {

    @Async
    @Override
    public <V> Future<V> call(Callable<V> callable, Consumer<Future<V>> onDone) {
        ObservableFutureTask<V> task = new ObservableFutureTask<V>(callable);
        task.onDone(onDone);
        task.run();
        try {
            return new AsyncResult<>(task.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    @Override
    public Future<Void> run(Runnable runnable, Consumer<Future<Void>> onDone) {
        return call(() -> {
            runnable.run();
            return null;
        }, onDone);
    }

    @Async
    @Override
    public <V> Future<V> call(Callable<V> callable) {
        return call(callable, null);
    }

    @Async
    @Override
    public Future<Void> run(Runnable runnable) {
        return run(runnable, null);
    }
}
