import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  email: string = '';
  password: string = '';

  constructor(private router: Router, private http: HttpClient) {}

  login() {
    const bodyData = {
      email: this.email,
      password: this.password,
    };

    this.http.post<any>('http://localhost:8080/tsp/login', bodyData).subscribe(
      (resultData: any) => {
        if (resultData.message === 'Email not exists') {
          alert('Email not exists');
        } else if (resultData.message === 'Login Success') {
          this.router.navigateByUrl('/dashboard');
        } else {
          alert('Incorrect Email and Password do not match');
        }
      },
      (error) => {
        console.error('Error occurred during login:', error);
        alert('An error occurred during login. Please try again later.');
      }
    );
  }
}
