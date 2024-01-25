import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule, HttpClientXsrfModule} from "@angular/common/http";
 import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AccueiComponent } from './components/accuei/accuei.component';
import { ChartModule } from 'angular-highcharts';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatTableModule} from '@angular/material/table'; 
import {MatPaginatorModule} from '@angular/material/paginator'; 
import {MatFormFieldModule} from '@angular/material/form-field'; 
import { MatInputModule } from '@angular/material/input';
 @NgModule({
  declarations: [
    AppComponent,
    AccueiComponent 
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    ChartModule,
    MatFormFieldModule,
    MatTableModule,
     AppRoutingModule,
     MatPaginatorModule,
     MatInputModule,
         BrowserAnimationsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
