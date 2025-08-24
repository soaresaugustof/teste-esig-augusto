package teste.augusto.utils;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class AppExecutors {

    private ExecutorService executorService;

    public void onStartup(@Observes @Initialized(ApplicationScoped.class) Object event) {
        System.out.println("==> Iniciando ExecutorService para tarefas assíncronas...");
        this.executorService = Executors.newFixedThreadPool(4);
    }

    public void onShutdown(@Observes @Destroyed(ApplicationScoped.class) Object event) {
        System.out.println("==> Desligando ExecutorService...");
        if (this.executorService != null) {
            try {
                this.executorService.shutdown();
                this.executorService.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                System.err.println("Erro ao aguardar o término do ExecutorService.");
            } finally {
                if (!this.executorService.isTerminated()) {
                    this.executorService.shutdownNow();
                }
                System.out.println("==> ExecutorService desligado.");
            }
        }
    }

    @Produces
    @ApplicationScoped
    public ExecutorService getExecutorService() {
        return this.executorService;
    }
}