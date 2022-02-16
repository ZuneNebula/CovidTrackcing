import { Component } from '@angular/core';
import { map } from 'rxjs/operators';
import { Breakpoints, BreakpointObserver } from '@angular/cdk/layout';
import { ApiService } from '../services/api.service';
import { Interest } from '../interest';

@Component({
  selector: 'app-stats',
  templateUrl: './stats.component.html',
  styleUrls: ['./stats.component.css']
})
export class StatsComponent {
  
  constructor(private breakpointObserver: BreakpointObserver, private apiService:ApiService) {}

  stats:any=[];
  preferences!:Interest;
  loading:boolean = false;
  public chartData=[];

  public testData = [
    { Franchise: "Marvel Universe All Films", TotalWorldBoxOfficeRevenue: 22.55, HighestGrossingMovieInSeries: 2.8 },
    { Franchise: "Star Wars", TotalWorldBoxOfficeRevenue: 10.32, HighestGrossingMovieInSeries: 2.07 },
    { Franchise: "Harry Potter", TotalWorldBoxOfficeRevenue: 9.19, HighestGrossingMovieInSeries: 1.34 },
    { Franchise: "Avengers", TotalWorldBoxOfficeRevenue: 7.76, HighestGrossingMovieInSeries: 2.8 },
    { Franchise: "Spider Man", TotalWorldBoxOfficeRevenue: 7.22, HighestGrossingMovieInSeries: 1.28 },
    { Franchise: "James Bond", TotalWorldBoxOfficeRevenue: 7.12, HighestGrossingMovieInSeries: 1.11 }
];

test(){
  console.log(this.testData);
}

  ngOnInit(){
    this.apiService.getAllCovidData().subscribe(data=>{
      this.stats = data;
      this.loading = true;
    })
    this.apiService.getUserPreferences().subscribe(data=>{
      this.preferences = data;
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
