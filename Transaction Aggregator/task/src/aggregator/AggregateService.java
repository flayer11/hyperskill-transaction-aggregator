package aggregator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class AggregateService {

    @Value("${external-api}")
    private List<String> externalApis;
    private final TransactionClient transactionClient;


    public  AggregateService(TransactionClient transactionClient){
        this.transactionClient = transactionClient;
    }

    public List<Account> aggregate(String account) {
        List<CompletableFuture<List<Account>>> listCompletableFutures =  externalApis.stream()
                .map(
                        (externalApi) -> this.transactionClient.fetchTransactions(externalApi, account)
                )
                .toList();

        return listCompletableFutures
                .stream()
                .map((CompletableFuture::join))
                .flatMap(Collection::stream)
                .sorted(
                     Comparator.comparing(Account::timestamp).reversed()
                )
                .toList();

    }


}
