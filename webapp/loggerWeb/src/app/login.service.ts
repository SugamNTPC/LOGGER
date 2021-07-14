import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { GlobalConstants } from './global-constants';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private server_ip = GlobalConstants.ip_server;
  private _url_login : string = "http://" + this.server_ip + "/api/user/check_valid";
  constructor(private http: HttpClient) { }

  attemptLogin(phone, password) : Observable <any>{
    let params = new HttpParams().set("phone", phone).set("password", password);
    return this.http.get<any>(this._url_login, {params : params})
            .pipe(catchError(this.handleError));
  }

  handleError(error : HttpErrorResponse){
    return throwError(error || "Server Error"); 
  }

}
