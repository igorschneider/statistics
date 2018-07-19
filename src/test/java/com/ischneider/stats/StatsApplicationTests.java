package com.ischneider.stats;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ischneider.stats.model.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StatsApplicationTests {

    @Autowired
    private MockMvc mvc;

	@Test
	public void contextLoads() {
	}

	@Test
    public void postTransaction() throws Exception {
        Transaction t1 = new Transaction( 14D, new Date( Instant.now().minusSeconds( 58L ).toEpochMilli() ) );

        mvc.perform( post("/transactions")
                .contentType( MediaType.APPLICATION_JSON )
                .content( new ObjectMapper().writeValueAsString( t1 ) ) )
                .andExpect( status().isCreated() );
    }

    @Test
    public void postTransactionOlderThan60Seconds() throws Exception {
        Transaction t1 = new Transaction( 14D, new Date( Instant.now().minusSeconds( 61L ).toEpochMilli() ) );

        mvc.perform( post("/transactions")
                .contentType( MediaType.APPLICATION_JSON )
                .content( new ObjectMapper().writeValueAsString( t1 ) ) )
                .andExpect( status().isNoContent() );
    }

    @Test
    public void getAfter3Insertions() throws Exception {
        Transaction t1 = new Transaction( 14D, new Date( Instant.now().minusSeconds( 58L ).toEpochMilli() ) );
        Transaction t2 = new Transaction( 50D, new Date( Instant.now().minusSeconds( 30L ).toEpochMilli() ) );
        Transaction t3 = new Transaction( 26D, new Date( Instant.now().minusSeconds( 10L ).toEpochMilli() ) );

        mvc.perform( post("/transactions")
                .contentType( MediaType.APPLICATION_JSON )
                .content( new ObjectMapper().writeValueAsString( t1 ) ) );
        mvc.perform( post("/transactions")
                .contentType( MediaType.APPLICATION_JSON )
                .content( new ObjectMapper().writeValueAsString( t2 ) ) );
        mvc.perform( post("/transactions")
                .contentType( MediaType.APPLICATION_JSON )
                .content( new ObjectMapper().writeValueAsString( t3 ) ) );
        mvc.perform( get("/statistics") )
                .andExpect( status().isOk() )
                .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON ) )
                .andExpect( jsonPath("sum", equalTo( 90D ) ) )
                .andExpect( jsonPath("avg", equalTo( 30D ) ) )
                .andExpect( jsonPath("max", equalTo( 50D ) ) )
                .andExpect( jsonPath("min", equalTo( 14D ) ) )
                .andExpect( jsonPath("count", equalTo( 3 ) ) );
    }

    @Test
    public void getAfter3InsertionsAnd1Removal() throws Exception {
        Transaction t1 = new Transaction( 14D, new Date( Instant.now().minusSeconds( 58L ).toEpochMilli() ) );
        Transaction t2 = new Transaction( 50D, new Date( Instant.now().minusSeconds( 30L ).toEpochMilli() ) );
        Transaction t3 = new Transaction( 26D, new Date( Instant.now().minusSeconds( 10L ).toEpochMilli() ) );

        mvc.perform( post("/transactions")
                .contentType( MediaType.APPLICATION_JSON )
                .content( new ObjectMapper().writeValueAsString( t1 ) ) );
        mvc.perform( post("/transactions")
                .contentType( MediaType.APPLICATION_JSON )
                .content( new ObjectMapper().writeValueAsString( t2 ) ) );
        mvc.perform( post("/transactions")
                .contentType( MediaType.APPLICATION_JSON )
                .content( new ObjectMapper().writeValueAsString( t3 ) ) );
        Thread.sleep( 3000L );
        mvc.perform( get("/statistics") )
                .andExpect( status().isOk() )
                .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON ) )
                .andExpect( jsonPath("sum", equalTo( 76D ) ) )
                .andExpect( jsonPath("avg", equalTo( 38D ) ) )
                .andExpect( jsonPath("max", equalTo( 50D ) ) )
                .andExpect( jsonPath("min", equalTo( 26D ) ) )
                .andExpect( jsonPath("count", equalTo( 2 ) ) );
    }

    @Test
    public void getAfter3InsertionsAnd3Removals() throws Exception {
        Transaction t1 = new Transaction( 14D, new Date( Instant.now().minusSeconds( 58L ).toEpochMilli() ) );
        Transaction t2 = new Transaction( 50D, new Date( Instant.now().minusSeconds( 57L ).toEpochMilli() ) );
        Transaction t3 = new Transaction( 26D, new Date( Instant.now().minusSeconds( 56L ).toEpochMilli() ) );

        mvc.perform( post("/transactions")
                .contentType( MediaType.APPLICATION_JSON )
                .content( new ObjectMapper().writeValueAsString( t1 ) ) );
        mvc.perform( post("/transactions")
                .contentType( MediaType.APPLICATION_JSON )
                .content( new ObjectMapper().writeValueAsString( t2 ) ) );
        mvc.perform( post("/transactions")
                .contentType( MediaType.APPLICATION_JSON )
                .content( new ObjectMapper().writeValueAsString( t3 ) ) );
        Thread.sleep( 5000L );
        mvc.perform( get("/statistics") )
                .andExpect( status().isOk() )
                .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON ) )
                .andExpect( jsonPath("sum", equalTo( 0D ) ) )
                .andExpect( jsonPath("avg", equalTo( 0D ) ) )
                .andExpect( jsonPath("max", equalTo( 0D ) ) )
                .andExpect( jsonPath("min", equalTo( 0D ) ) )
                .andExpect( jsonPath("count", equalTo( 0 ) ) );
    }
}
