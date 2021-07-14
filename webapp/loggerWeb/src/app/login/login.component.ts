import { Component, OnInit } from '@angular/core';
import { LoginService } from '../login.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router, ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private loginService : LoginService, private _snackBar : MatSnackBar, private router : Router) { }
  hide = true;

  ngOnInit(): void {
  }

  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action, {
      duration: 2000,
    });
  }

  tryLogging(phone, password){
    //console.log("Login button pressed");
    // console.log("Login attempted with phone:" + phone + " password : " + password);
    this.loginService.attemptLogin(phone, password)
      .subscribe(data =>{
        // console.log(data);
        try{
          var webapp_access = data["webapp_access"];
          var name = data["name"];
          // console.log(webapp_access);
          if(webapp_access=='Y' || webapp_access=='X'){
            this.router.navigate(['/','user', {webapp_access : webapp_access, name : name}], { skipLocationChange: true });
            // { skipLocationChange: true }
          }
          else{
            this.openSnackBar("Webapp access not available to user!!","");
          }
        }
        catch{
          this.openSnackBar("Userid/password mismatch!!","");
        }
        // this.router.navigate(['/','user'],{});
      },
      (error)=>{
        this.openSnackBar(error.error.msg, "X");     
        console.log(error.error);
      })
  }

}
