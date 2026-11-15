package aggregator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
            return this.aggregateService.aggregate(account);
    }

}