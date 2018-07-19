package com.ischneider.stats.repository;

import com.ischneider.stats.model.Statistics;
import com.ischneider.stats.model.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.Comparator;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

public class TransactionRepositoryTest {

    private TransactionRepository repository;

    @Before
    public void init() {
        repository = new TransactionRepository(
                new PriorityQueue<>( (t1, t2) -> t2.getAmount().compareTo( t1.getAmount() ) ),
                new PriorityQueue<>( Comparator.comparing( Transaction::getAmount ) ),
                0D,
                0D,
                0L,
                Executors.newSingleThreadScheduledExecutor()
        );
    }

    @Test
    public void getAfter3Insertions() {
        Transaction t1 = new Transaction( 14D, new Date( Instant.now().minusSeconds( 58L ).toEpochMilli() ) );
        Transaction t2 = new Transaction( 50D, new Date( Instant.now().minusSeconds( 30L ).toEpochMilli() ) );
        Transaction t3 = new Transaction( 26D, new Date( Instant.now().minusSeconds( 10L ).toEpochMilli() ) );

        repository.add( t1 );
        repository.add( t2 );
        repository.add( t3 );
        Statistics statistics = repository.get();

        assertEquals( 90D, statistics.getSum(), 0.01D );
        assertEquals( 30D, statistics.getAvg(), 0.01D );
        assertEquals( 50D, statistics.getMax(), 0.01D );
        assertEquals( 14D, statistics.getMin(), 0.01D );
        assertEquals(3L, (long) statistics.getCount());
    }

    @Test
    public void getAfter3InsertionsAnd1Removal() throws Exception {
        Transaction t1 = new Transaction( 14D, new Date( Instant.now().minusSeconds( 58L ).toEpochMilli() ) );
        Transaction t2 = new Transaction( 50D, new Date( Instant.now().minusSeconds( 30L ).toEpochMilli() ) );
        Transaction t3 = new Transaction( 26D, new Date( Instant.now().minusSeconds( 10L ).toEpochMilli() ) );

        repository.add( t1 );
        repository.add( t2 );
        repository.add( t3 );
        Thread.sleep( 3000L );
        Statistics statistics = repository.get();

        assertEquals( 76D, statistics.getSum(), 0.01D );
        assertEquals( 38D, statistics.getAvg(), 0.01D );
        assertEquals( 50D, statistics.getMax(), 0.01D );
        assertEquals( 26D, statistics.getMin(), 0.01D );
        assertEquals(2L, (long) statistics.getCount());
    }

    @Test
    public void getAfter3InsertionsAnd3Removals() throws Exception {
        Transaction t1 = new Transaction( 14D, new Date( Instant.now().minusSeconds( 58L ).toEpochMilli() ) );
        Transaction t2 = new Transaction( 50D, new Date( Instant.now().minusSeconds( 57L ).toEpochMilli() ) );
        Transaction t3 = new Transaction( 26D, new Date( Instant.now().minusSeconds( 56L ).toEpochMilli() ) );

        repository.add( t1 );
        repository.add( t2 );
        repository.add( t3 );
        Thread.sleep( 5000L );
        Statistics statistics = repository.get();

        assertEquals( 0D, statistics.getSum(), 0.01D );
        assertEquals( 0D, statistics.getAvg(), 0.01D );
        assertEquals( 0D, statistics.getMax(), 0.01D );
        assertEquals( 0D, statistics.getMin(), 0.01D );
        assertEquals(0L, (long) statistics.getCount());
    }
}