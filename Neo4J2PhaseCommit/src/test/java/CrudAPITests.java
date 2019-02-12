import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insta2phase.Application;
import com.insta2phase.ReplicaApplication;
import com.insta2phase.crudQueries.CreateAccountQuery;
import com.insta2phase.crudQueries.UpdateAccountQuery;
import com.insta2phase.dal.AccountDao;
import com.insta2phase.entities.Account;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.codec.LoggingCodecSupport;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;


import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, ReplicaApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CrudAPITests {

    @LocalServerPort
    private String port;

    private String url;
    private WebClient webClient;

    private AccountDao accountDao;

    @Autowired
    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @PostConstruct
    public void init(){
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.withDefaults();
        exchangeStrategies
                .messageWriters().stream()
                .filter(LoggingCodecSupport.class::isInstance)
                .forEach(writer -> ((LoggingCodecSupport)writer).setEnableLoggingRequestDetails(true));

        url = "http://localhost:" + port +"/primary/accounts";
        System.err.println(url);
        webClient = WebClient.builder()
                .exchangeStrategies(exchangeStrategies)
                .baseUrl(url)
                .build();
    }



    @Before
    public void beforeEachTest(){

    }

    @After
    public void afterEachTest(){
        accountDao.deleteAll();
    }

    @Test
    public void testCreateAccount() {
        CreateAccountQuery createAccountQuery = new CreateAccountQuery("linor@gmail.com", "Linor Dolev", "linordolev","123456");
        Optional<Account> response = webClient.post()
                            .body(Mono.just(createAccountQuery), CreateAccountQuery.class)
                            .retrieve()
                            .bodyToMono(Account.class)
                            .blockOptional();


        try {
            System.err.println(new ObjectMapper().writeValueAsString(response));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assertThat(response).isPresent()
                .get()
                .isEqualToIgnoringGivenFields(createAccountQuery,"passwordHash", "following");
    }


    @Test
    public void testReadAccount() {
        CreateAccountQuery createAccountQuery = new CreateAccountQuery("linor@gmail.com", "Linor Dolev", "linordolev","123456");

        Optional<Account> createdAccountOptional = webClient.post()
                .body(Mono.just(createAccountQuery), CreateAccountQuery.class)
                .retrieve()
                .bodyToMono(Account.class)
                .blockOptional();

        Account response = webClient.get()
                .uri("/" +createAccountQuery.getEmail())
                .retrieve()
                .bodyToMono(Account.class)
                .block();

        try {
            System.err.println(new ObjectMapper().writeValueAsString(response));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assertThat(response)
                .isEqualToComparingFieldByField(createdAccountOptional.get());
    }

    @Test
    public void testUpdateAccount() {
        CreateAccountQuery createAccountQuery = new CreateAccountQuery("linor@gmail.com", "Linor Dolev", "linordolev","123456");

        Account originalAccount = accountDao.save(new Account(createAccountQuery));
        originalAccount.setFullName("Roie Danino");

        UpdateAccountQuery updateAccountQuery = new UpdateAccountQuery(originalAccount);
        webClient.put()
                .body(Mono.just(updateAccountQuery), UpdateAccountQuery.class)
                .exchange()
                .block();

        Optional<Account> optionalAccount = accountDao.findById(createAccountQuery.getEmail());

        assertThat(optionalAccount)
                .isPresent()
                .get()
                .isEqualToComparingFieldByField(originalAccount);
    }

    @Test
    public void testDeleteAccount() {
        String email = "linor@gmail.com";
        CreateAccountQuery createAccountQuery = new CreateAccountQuery(email, "Linor Dolev", "linordolev","123456");

        Account originalAccount = accountDao.save(new Account(createAccountQuery));


        webClient.delete()
                .uri("/"+email)
                .exchange()
                .block();

        Optional<Account> optionalAccount = accountDao.findById(createAccountQuery.getEmail());

        assertThat(optionalAccount)
                .isNotPresent();
    }
}
