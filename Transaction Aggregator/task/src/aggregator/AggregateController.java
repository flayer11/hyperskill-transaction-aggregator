package aggregator;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import java.util.*;


@RestController
public class AggregateController{

    public final AggregateService aggregateService;

    public  AggregateController(AggregateService aggregateService){
        this.aggregateService = aggregateService;
    }


    @GetMapping("/aggregate")
    @ResponseBody
    public List<Account> handle(@RequestParam("account") String account){
        try{
            return this.aggregateService.aggregate(account);
        } catch (Exception e){
            throw new HttpServerErrorException(HttpStatusCode.valueOf(500));

        }

    }

}