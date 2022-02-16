import { Component } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { FormBuilder, Validators, FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from '../services/api.service';
import { User } from '../user';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  constructor(private breakpointObserver: BreakpointObserver, private fb: FormBuilder, private router:Router, private apiService:ApiService) {}

  //check if password and confirm password match later
  registerForm = this.fb.group({
    username: [null],
    firstName: [null, Validators.required],
    lastName: [null, Validators.required],
    email: [null, Validators.required],
    password: [null, Validators.required],
    confirmPassword: [null, Validators.required]
  });

  loginForm = this.fb.group({
    email: [null, Validators.required],
    password: [null, Validators.required]
  });


  ngOnInit(){
    console.log("form data");
    console.log(this.registerForm.value);
  }

  onRegister(){
    let submittedUser: User ={
      userId: '',
      username: this.registerForm.get('email')?.value,
      firstName: this.registerForm.get('firstName')?.value,
      lastName: this.registerForm.get('lastName')?.value,
      avatarUrl: '',
      email: this.registerForm.get('email')?.value,
      password: this.registerForm.get('password')?.value
    };

    this.apiService.registerUser(submittedUser).subscribe(data=>{
      console.log("User Registered");
    });

  }

  onLogin(){
    this.apiService.loginUser(this.loginForm.get('email')?.value,this.loginForm.get('password')?.value).subscribe(data=>{
      console.log("User logged in");
      this.router.navigateByUrl("users");
    });
  }

}
