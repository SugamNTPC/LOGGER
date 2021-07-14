import { Injectable } from '@angular/core';
import { HttpClientModule, HttpErrorResponse, HttpParams, HttpClient } from '@angular/common/http'
import {Observable, throwError} from 'rxjs';
import { catchError } from 'rxjs/operators';
import { IStructure } from './structure';
import { GlobalConstants } from './global-constants';

@Injectable({
  providedIn: 'root'
})
export class StructureService {
  private server_ip = GlobalConstants.ip_server;
  private _url_structure : string = "http://" + this.server_ip + "/api/structure";
  private _url_structure_childs : string = "http://" + this.server_ip + "/api/structure/getChilds";
  private _url_structure_update : string = "http://" + this.server_ip + "/api/structure/update";
  private _url_structure_add : string = "http://" + this.server_ip + "/api/structure/add";
  private _url_structure_delete : string = "http://" + this.server_ip + "/api/structure/delete";

  constructor(private http : HttpClient) { }

  getStructure() : Observable <IStructure[]>{
    return this.http.get<IStructure[]>(this._url_structure)
            .pipe(catchError(this.handleError));
  }

  getChilds(parent_id) : Observable <IStructure[]>{
    let params = new HttpParams().set("parent",parent_id);
    return this.http.get<any>(this._url_structure_childs, {params : params})
            .pipe(catchError(this.handleError));
  }

  updateStructure(id, structure) : Observable <any>{
    let params = new HttpParams().set("id", id).set("structure", structure);
    console.log(structure);
    return this.http.get<any>(this._url_structure_update, {params : params})
            .pipe(catchError(this.handleError));
  }

  addStructure(structure) : Observable <any>{
    let params = new HttpParams().set("structure", structure);
    console.log(structure);
    return this.http.get<any>(this._url_structure_add, {params : params})
            .pipe(catchError(this.handleError));
  }

  deleteStructure(id) : Observable <any>{
    console.log("delete called");
    let params = new HttpParams().set("id", id);
    return this.http.get<any>(this._url_structure_delete, {params : params})
            .pipe(catchError(this.handleError));
  }

  handleError(error : HttpErrorResponse){
    return throwError(error || "Server Error"); 
  }
  
}
