package com.ischneider.stats.repository;

import com.ischneider.stats.model.Statistics;
import com.ischneider.stats.model.Transaction;
import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class TransactionRepository {

    private PriorityQueue<Transaction> max;
    private PriorityQueue<Transaction> min;
    private Double sum;
    private Double avg;
    private Long count;
    private ScheduledExecutorService executor;

    public synchronized void add(Transaction transaction) {
        max.add( transaction );
        min.add( transaction );
        sum += transaction.getAmount();
        count++;
        avg = count.equals( 0L ) ? 0D : sum / count.doubleValue();

        Long delay  = transaction.getTimestamp().getTime() + 60000L - new Date().getTime();
        executor.schedule( () -> remove( transaction ), delay, TimeUnit.MILLISECONDS );
    }

    private synchronized void remove(Transaction transaction) {
        max.remove( transaction );
        min.remove( transaction );
        sum -= transaction.getAmount();
        count--;
        avg = count.equals( 0L ) ? 0D : sum / count.doubleValue();
    }

    public synchronized Statistics get() {
        return new Statistics( sum, avg, getMax(), getMin(), count );
    }

    private Double getMax() {
        return Optional.ofNullable( max.peek() )
                .map( Transaction::getAmount )
                .orElse( 0D );
    }

    private Double getMin() {
        return Optional.ofNullable( min.peek() )
                .map( Transaction::getAmount )
                .orElse( 0D );
    }

}
