package aggregator;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import java.util.*;
import java.util.concurrent.CompletableFuture;


@RestController
@EnableCaching
public class AggregateController{

    public final AggregateService aggregateService;

    public  AggregateController(AggregateService aggregateService){
        this.aggregateService = aggregateService;
    }


    @GetMapping("/aggregate")
    @ResponseBody
    public List<Account> handle(@RequestParam("account") String account){

        try{
        CompletableFuture<List<Account>> futureBody1 = this.aggregateService.getBody("http://localhost:8889/", account);
        CompletableFuture<List<Account>> futureBody2 = this.aggregateService. getBody("http://localhost:8888/", account);


            return this.aggregateService.processBody(futureBody1.get(), futureBody2.get());
        } catch (Exception e){
            throw new HttpServerErrorException(HttpStatusCode.valueOf(500));

        }

    }

}