import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environement } from '../environnement/env';
  
@Injectable({
  providedIn: 'root'
})
export class ClientService {

 
    base_api=environement.basUrl;
  constructor(private http:HttpClient) { }


  get_clients():Observable<any>
  {
    return this.http.get(this.base_api+"/clients");
  }

  import_clients(data:any):Observable<any>
  {
    return this.http.post(this.base_api+"/import",{data});
  }

  // API url 
     
   
  // Returns an observable 
  upload(file:any):Observable<any> { 
  
      // Create form data 
      const formData = new FormData();  
        
      // Store form name as "file" with file data 
      formData.append("file", file, file.name); 
        
      // Make http post request over api 
      // with formData as req 
      return this.http.post(this.base_api+"/import", formData) 
  } 

 
}
