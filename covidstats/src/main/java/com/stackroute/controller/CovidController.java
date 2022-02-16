package com.stackroute.controller;

import com.stackroute.cookie.CookieConfig;
import com.stackroute.exception.UserAlreadyExistsException;
import com.stackroute.exception.UserNotFoundException;
import com.stackroute.model.Interest;
import com.stackroute.model.JsonCountry;
import com.stackroute.service.InterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/tracking")
@CrossOrigin
public class CovidController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private InterestService interestService;

    @Autowired
    private CookieConfig cookieConfig;

    private String url= "https://corona.lmao.ninja/v2/countries/";

    private ResponseEntity responseEntity;

    //Rest template for all countrues
    @GetMapping("/all")
    public ResponseEntity<?>  getAllStats(){

        HttpHeaders headers = new HttpHeaders();

        //API URL
        String allStatsUrl = this.url;
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        return this.restTemplate.exchange(url, HttpMethod.GET, entity,Object.class);
    }

    //rest template for a specific country
    @GetMapping("/{country}")
    public ResponseEntity<?> getCountryStats(@PathVariable String country){
        HttpHeaders headers = new HttpHeaders();

        //API URL
        String countryUrl = this.url + country;
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        return this.restTemplate.exchange(countryUrl, HttpMethod.GET, entity,Object.class);
    }

    //FIXME sends data in interleaved, half the data is empty
    @GetMapping("/myCountries")
    public ResponseEntity<?> getMyCountryStats(@CookieValue("JWT-TOKEN") Cookie cookie) throws UserNotFoundException, Exception{
//
        Interest interest = this.interestService.getInterest(this.cookieConfig.getUserIdFromCookie(cookie));
        if(interest==null){
            Interest interest2 = new Interest();
            interest2.setUserId(this.cookieConfig.getUserIdFromCookie(cookie));
            this.interestService.saveInterest(interest2);
            interest = this.interestService.getInterest(this.cookieConfig.getUserIdFromCookie(cookie));
        }

        List<CompletableFuture<Void>> completableFutures = new ArrayList<>(interest.getCountries().size());
        List<Object> responses = new ArrayList<>();
        ExecutorService myExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        for(String country: interest.getCountries()){
            CompletableFuture<Void> requestCompletableFuture = CompletableFuture
                    .supplyAsync(
                            () -> restTemplate.exchange(this.url + country, HttpMethod.GET, entity, Object.class),
                            myExecutor
                    )//Supply the task you wanna run, in your case http request
                    .thenApply((responseEntity) -> {
                        responses.add(responseEntity.getBody());
                        return responseEntity;
                    })//now you can add response body to responses
                    .thenAccept((responseEntity) -> {
                        //responses.add(responseEntity);
                    })//here you can do more stuff with responseEntity (if you need to)
                    .exceptionally(ex -> {
                        System.out.println(ex);
                        return null;
                    });//do something here if an exception occurs in the execution;

            completableFutures.add(requestCompletableFuture);
        }
        try {
            CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[completableFutures.size()])).get(); //Now block till all of them are executed by building another completablefuture with others.
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        this.responseEntity = new ResponseEntity(responses, HttpStatus.OK);
        return this.responseEntity;
    }

    //getting data for user's interests
    @GetMapping("/preferences")
    public ResponseEntity<?> getInterests(@CookieValue("JWT-TOKEN") Cookie cookie) throws UserNotFoundException, UserAlreadyExistsException {
        try{
            Interest interest = interestService.getInterest(this.cookieConfig.getUserIdFromCookie(cookie));
            this.responseEntity = new ResponseEntity(interest, HttpStatus.OK);
        }
        catch (UserNotFoundException e){
            Interest interest = new Interest();
            interest.setUserId(this.cookieConfig.getUserIdFromCookie(cookie));
            this.interestService.saveInterest(interest);
            this.responseEntity = new ResponseEntity(interest, HttpStatus.CREATED);
        }
//        catch (Exception e2){
//            this.responseEntity = new ResponseEntity("Error in getting user's preferences", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
        return this.responseEntity;
    }

    @PostMapping("/preferences")
    public ResponseEntity<?> setPreferences(@CookieValue("JWT-TOKEN") Cookie cookie, @RequestBody JsonCountry data) throws UserNotFoundException, UserAlreadyExistsException {
        String country = data.getCountry();
        try{
            Interest interest = this.interestService.getInterest(this.cookieConfig.getUserIdFromCookie(cookie));
            List<String> countries = interest.getCountries();
            if(countries==null){
                countries = new ArrayList<>();
            }
            if(countries.contains(country)){
                countries.remove(country);
            }
            else{
                countries.add(country);
            }
            interest.setCountries(countries);
            this.interestService.updateInterest(interest);
            this.responseEntity = new ResponseEntity(interest, HttpStatus.ACCEPTED);

        }catch (UserNotFoundException e){
            Interest interest = new Interest();
            interest.setUserId(this.cookieConfig.getUserIdFromCookie(cookie));
            List<String> countries = new ArrayList<String>();
            countries.add(country);
            interest.setCountries(countries);
            this.interestService.saveInterest(interest);
            this.responseEntity = new ResponseEntity(interest, HttpStatus.CREATED);
        }
//        catch (Exception e){
//            this.responseEntity = new ResponseEntity("error in setting preferences", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
        return this.responseEntity;
    }

}
