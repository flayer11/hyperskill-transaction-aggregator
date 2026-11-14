package aggregator;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class AggregateService {
    private static final ParameterizedTypeReference<List<Account>> ACCOUNT_LIST = new ParameterizedTypeReference<List<Account>>() {};


    public List<Account> processBody(List<Account>... accountLists) {
        return Arrays.stream(accountLists)
                .flatMap(Collection::stream)
                .sorted(
                        Comparator.comparing(Account::timestamp).reversed())
                .toList();

    }

    @Cacheable(cacheNames = "urls", key = "#url+#account")
    public CompletableFuture<List<Account>> getBody(String url, String account){
        return CompletableFuture.supplyAsync(
                () -> {
                    int i = 0;
                    while(i < 6){
                        try {
                            return RestClient.builder()
                                    .baseUrl(url)
                                    .build()
                                    .get()
                                    .uri("transactions?account={account}", account)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .retrieve()
                                    .body(ACCOUNT_LIST);
                        } catch (RestClientException restClientException) {
                        i++;
                        }

                    }

                    throw new HttpServerErrorException(HttpStatusCode.valueOf(500));

                });
    }
}
