import { Injectable } from '@angular/core';
import { HttpClientModule, HttpErrorResponse, HttpParams, HttpClient } from '@angular/common/http'
import {Observable, throwError} from 'rxjs';
import { catchError } from 'rxjs/operators';
import { IStructure } from './structure';
import { GlobalConstants } from './global-constants';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private server_ip = GlobalConstants.ip_server;
  private _url_user_get : string = "http://" + this.server_ip + "/api/user";
  private _url_user_update : string = "http://" + this.server_ip + "/api/user/update";
  private _url_user_add : string = "http://" + this.server_ip + "/api/user/add";
  private _url_user_delete : string = "http://" + this.server_ip + "/api/user/delete";

  constructor(private http : HttpClient) { }

  getUsers() : Observable <IStructure[]>{
    return this.http.get<any>(this._url_user_get)
            .pipe(catchError(this.handleError));
  }

  updateUser(id, user) : Observable <any>{
    let params = new HttpParams().set("id", id).set("user", user);
    console.log(user);
    return this.http.get<any>(this._url_user_update, {params : params})
            .pipe(catchError(this.handleError));
  }

  addUser(user) : Observable <any>{
    let params = new HttpParams().set("user", user);
    console.log(user);
    return this.http.get<any>(this._url_user_add, {params : params})
            .pipe(catchError(this.handleError));
  }

  deleteUser(id) : Observable <any>{
    console.log("delete called");
    let params = new HttpParams().set("id", id);
    return this.http.get<any>(this._url_user_delete, {params : params})
            .pipe(catchError(this.handleError));
  }

  handleError(error : HttpErrorResponse){
    return throwError(error || "Server Error"); 
  }

}
