package com.guigarage.photonia.spring.services;

import com.guigarage.photonia.service.AsyncService;
import com.guigarage.photonia.util.ObservableFutureTask;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.function.Consumer;

@Service
public class SpringAsyncService implements AsyncService {

    @Override
    public <V> Future<V> call(Callable<V> callable, Consumer<Future<V>> onDone) {
        ObservableFutureTask<V> task = new ObservableFutureTask<V>(callable);
        task.onDone(onDone);
        task.run();
        return task;
    }

    @Override
    public Future<Void> run(Runnable runnable, Consumer<Future<Void>> onDone) {
        ObservableFutureTask<Void> task = new ObservableFutureTask<Void>(runnable);
        task.onDone(onDone);
        task.run();
        return task;
    }

    @Override
    public <V> Future<V> call(Callable<V> callable) {
        return call(callable, null);
    }

    @Override
    public Future<Void> run(Runnable runnable) {
        return run(runnable, null);
    }
}
