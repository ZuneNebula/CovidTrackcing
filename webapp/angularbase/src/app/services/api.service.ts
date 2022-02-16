import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Interest } from '../interest';
import { User } from '../user';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(private httpClient: HttpClient) { }

  //USERSERVICE
  registerUser(user:User){
    return this.httpClient.post<User>("userservice/api/v1/authorize/register",JSON.stringify(user), {'headers':{'Content-Type':'application/json'}});
  }

  loginUser(username:string, password:string){
    let sendObj = {
      username: username,
      password: password
    }
    return this.httpClient.post<User>("userservice/api/v1/authorize/login",JSON.stringify(sendObj), {'headers':{'Content-Type':'application/json'}});
  }

  getUser(user:User){
    return this.httpClient.get<User>("userservice/api/v1/users/");
  }

  updateUser(user:User){
    return this.httpClient.put("userservice/api/v1/users/",user);
  }

  //COVIDSERVICE
  getAllCovidData(){
    return this.httpClient.get("cvdstats/api/v1/tracking/all");
  }

  getCountryCovidData(country:String){
    return this.httpClient.get("cvd/stats/api/v1/tracking/"+ country);
  }

  getMyCountriesData(){
    return this.httpClient.get("cvdstats/api/v1/tracking/myCountries");
  }

  getUserPreferences(){
    return this.httpClient.get<Interest>("cvdstats/api/v1/tracking/preferences");
  }

  setUserPreferences(country:String){
    let jsonData={
      country:country
    }
    console.log(jsonData);
    return this.httpClient.post<Interest>("cvdstats/api/v1/tracking/preferences",JSON.stringify(jsonData), {'headers':{'Content-Type':'application/json'}});
  }

}
