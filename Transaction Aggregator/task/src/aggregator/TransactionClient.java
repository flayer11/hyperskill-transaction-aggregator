package aggregator;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class TransactionClient
{
    private final AsyncTaskExecutor asyncTaskExecutor;
    private static final ParameterizedTypeReference<List<Account>> ACCOUNT_LIST = new ParameterizedTypeReference<List<Account>>() {};


    public TransactionClient(@Qualifier("applicationTaskExecutor") AsyncTaskExecutor asyncTaskExecutor){
        this.asyncTaskExecutor = asyncTaskExecutor;
    }

    @Cacheable(cacheNames = "urls", key = "#url+#account")
    public CompletableFuture<List<Account>> fetchTransactions(String url, String account){
        RestClient restClient = RestClient.builder()
                .baseUrl(url)
                .build();

        return CompletableFuture.supplyAsync(
                () -> {
                    int i = 0;
                    while(i < 6){
                        try {
                            return restClient
                                    .get()
                                    .uri("transactions?account={account}", account)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .retrieve()
                                    .body(ACCOUNT_LIST);
                        }
                        catch (HttpServerErrorException httpServerErrorException) {
                            i++;
                        }

                    }

                    throw new HttpServerErrorException(HttpStatusCode.valueOf(500));

                }, this.asyncTaskExecutor);
    }
}
