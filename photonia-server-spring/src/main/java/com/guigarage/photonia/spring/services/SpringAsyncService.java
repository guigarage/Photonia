package com.guigarage.photonia.spring.services;

import com.guigarage.photonia.service.AsyncService;
import com.guigarage.photonia.util.ObservableFutureTask;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.function.Consumer;

@Service
public class SpringAsyncService implements AsyncService {

    private ThreadPoolTaskExecutor executor;

    @PostConstruct
    private void init() {
        executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.initialize();
    }

    @Override
    public <V> Future<V> call(Callable<V> callable, Consumer<Future<V>> onDone) {
        ObservableFutureTask<V> task = new ObservableFutureTask<V>(callable);
        task.onDone(onDone);
        return executor.submitListenable(() -> {
            task.run();
            return task.get();
        });
    }

    @Override
    public Future<Void> run(Runnable runnable, Consumer<Future<Void>> onDone) {
        ObservableFutureTask<Void> task = new ObservableFutureTask<Void>(runnable);
        task.onDone(onDone);
        task.run();
        return executor.submitListenable(() -> {
            task.run();
            return task.get();
        });
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
