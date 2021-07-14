import { Component, OnInit, Input } from '@angular/core';
import { UserService } from '../user.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  public title = "User Admin";
  public users = [];
  public errorMsg;
  public options_webapp = ["Y","N"];
  public options_android = ["Y","N"];
  public webapp_access = "N";
  public logged_user;

  constructor(
    private _userService : UserService, 
    private _snackBar : MatSnackBar, 
    private _router : Router,
    private _route : ActivatedRoute
    ) {}

  ngOnInit(): void {
    this.get_users();
    this.webapp_access =  this._route.snapshot.paramMap.get('webapp_access');
    this.logged_user =  this._route.snapshot.paramMap.get('name');
    if(this.webapp_access=='X'){
      this.options_webapp.push('X');
    }
  }

  editEnable(user){
    // this.editEnabled = true;
    user.edit_enabled = true;
  }

  get_users(){
    console.log("Fetching users");
    this._userService.getUsers()
      .subscribe(data =>{
        data.sort(function(a, b){
            var nameA=a.name.toLowerCase(), nameB=b.name.toLowerCase()
            if (nameA < nameB) //sort string ascending
                return -1 
            if (nameA > nameB)
                return 1
            return 0 //default return value (no sorting)
        })
        this.users = data;
       } ,
        error => this.errorMsg = error);
  }

  add(){
    // let passed_user = Object.assign({}, user);
    let passed_user = Object.assign({}, this.users[0]);
    passed_user.name="AAAAAAAA";
    passed_user.id="none";
    passed_user.phone="9876543210";
    passed_user.webapp_access="N";
    passed_user.app_access="N";
    passed_user.root_node="null";
    passed_user.root_node_name="VSTPS Stage 1";
    delete passed_user._id;
    this._userService.addUser(JSON.stringify(passed_user))
      .subscribe(data => { 
        this.openSnackBar("Adding ", data);
        this.users = [];
        this.get_users();  
      },
        error => this.errorMsg = error);
  }

  update(user){
    this._userService.updateUser(user._id, JSON.stringify(user))
      .subscribe(data => { this.openSnackBar("Updating ", data); user.edit_enabled=false; },
                error => this.errorMsg = error);
  }

  delete(user){
    this._userService.deleteUser(user._id)
      .subscribe(data => { this.openSnackBar("Deleting ", data); user.is_hidden=true; },
                error => this.errorMsg = error);
  }

  structure_admin(){
    this._router.navigate(['../','structure'], { skipLocationChange: true });
  }

  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action, {
      duration: 10000,
    });
  }

}
