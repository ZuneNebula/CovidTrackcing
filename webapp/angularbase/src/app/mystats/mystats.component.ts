import { BreakpointObserver } from '@angular/cdk/layout';
import { Component, OnInit } from '@angular/core';
import { Interest } from '../interest';
import { ApiService } from '../services/api.service';

@Component({
  selector: 'app-mystats',
  templateUrl: './mystats.component.html',
  styleUrls: ['./mystats.component.css']
})
export class MystatsComponent implements OnInit {

  constructor(private breakpointObserver: BreakpointObserver, private apiService:ApiService) {}

  stats:any=[];
  preferences!:Interest;
  loading:boolean = false;

  ngOnInit(){
    this.apiService.getMyCountriesData().subscribe(data=>{
      console.log("my countries");
      console.log(data);
      this.stats = data;
      this.loading = true;
    })
    this.apiService.getUserPreferences().subscribe(data=>{
      this.preferences = data;
      console.log(data);
    })
  }

  markAsFavourite(country: String){
    this.apiService.setUserPreferences(country).subscribe(data=>{
      console.log("success");
      window.location.reload();
    })
  }

  isFavourite(country:String):boolean{
    let result = false;
    this.preferences.countries.forEach((element)=>{
      if(element===country){
        result = true;
      }
    })
    return result;
  }

  displayedColumns: string[] = ['country', 'cases', 'todaycases', 'deaths', 'todaydeaths','recovered', 'todayrecovered', 'favourites'];

}
