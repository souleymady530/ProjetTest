import { Component, OnInit, ViewChild } from '@angular/core';
import { ClientService } from 'src/app/services/client.service';
import{Chart,registerables} from'node_modules/chart.js' 
import { FormBuilder, Validators } from '@angular/forms';
import { environement } from 'src/app/environnement/env';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';

Chart.register(...registerables);


@Component({
  selector: 'app-accuei',
  templateUrl: './accuei.component.html',
  styleUrls: ['./accuei.component.scss'],
  
 })
export class AccueiComponent implements OnInit{
  //private selectedFile:File=null;
  shortLink: string = ""; 
    loading: boolean = false; // Flag variable 
     nom_fichier="";
  private srcResult:any;
  private img_name="";
  file!:File;
  all_client!:Array<any>

  form_Group=this.form_builder.group(
    {
      file:[Validators.required],
    });

    displayedColumns: string[] = ['id', 'Nom', 'Prenom', 'age','Professions','Salaire'];
    data_source!: MatTableDataSource< any>;
  
    @ViewChild(MatPaginator) paginator!: MatPaginator;
    @ViewChild(MatSort) sort!: MatSort;

  constructor(private client$:ClientService,private form_builder:FormBuilder){
    
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.data_source.filter = filterValue.trim().toLowerCase();
  }

async valider(){
  const form_data=new FormData();
  const inputNode: any = document.querySelector('#file');
form_data.append("file",inputNode.files[0]);

  const req1=await fetch(environement.basUrl+"/import",{
    method:'post',
    body:form_data
  });

const response=await req1.json();
 
}
    


  ngOnInit(): void {
      this.client$.get_clients().subscribe({
        next:(val)=>{
          console.log(val);
          
            this.save_client_liste(val);
           
            var profession=new Map();

            for(var i=0;i<val.length;i++){
              profession.set(val[i].profession.trim(),0);
            }
            var data_labels=[];
            var data2=[];
            for(var k of profession.keys()){
              data_labels.push(k.trim());
              var somm=0
              var nbre=0
              for(var i=0;i<val.length;i++){
                if(val[i].profession.trim()==k){
                  somm+=Number(val[i].salaire);
                  nbre++;
                }
                
                 
              }
              data2.push(Number(somm/nbre))
            }

            new Chart('pie-chart', {
        
              type: 'pie',
              data: {
                labels: data_labels,
                
                datasets: [{
                  label: '',
                  data: data2,
                  
                  borderWidth: 1,
                  
                  
                }],
                
              },
              
              options: {
                responsive:true,
                scales: {
                  y: {
                    beginAtZero: true
                  }
                }
              },
             
            });
            
        },
        error:()=>console.log("Une erreur est survenue lors de la recuperation de la liste des clients"),
        complete:console.log
      })
  }

  save_client_liste(liste:any){
    this.all_client=liste;
   
    this.data_source=new MatTableDataSource(liste)
    this.data_source.sort=this.sort;
    this.data_source.paginator=this.paginator;
    //
  }

  
}
/**
 * 
 * 





            
 *  data!:any 
  is_light_mode=sessionStorage.getItem("theme_mode")==='light'? true:false
  grape_color=sessionStorage.getItem("theme_mode")==='light'? 'white':'rgb(20,20,50)'
  grape_bar_color=sessionStorage.getItem("theme_mode")==='light'? 'rgb(127, 127, 184)':'white'

  ngOnInit(): void {
    this.RenderChart()
  }

  constructor(private credit$:CreditService){
    this.credit$.get_credits().subscribe({
      next:(val)=>{
        this.data=new Map()
        //initiatilisation
        val.forEach(element => {
          let activity=element.applicant.profession;
          if(element.loan_statut>=5) 
            if(element.applicant.profession!=='null' )     this.data.set(activity,0);
        });

        var prof_data=[]
        var prof=[]
       let i=0;
        for(const [k,v] of this.data){
          let nbre=0 
          let somme=0
          if(k!=null && k+''!=='')
          {
           
          val.forEach(element => {
            if(element.loan_statut>=5)
              if(element.applicant.profession==k)
              {
                somme+=Number(element.loan_amount)
              } });
          }
          
       
       // console.log('-------------------------------')
       // console.log(nbre)
       i++;
      // console.log(somme+'----------')
        this.data.set(k,somme)
        prof_data[i]=somme
        prof[i]=String(k)
        }

        //chart part
        //on va filtre pour enelever les null et
        prof=prof.filter(function (el) {
          return el != null;
        });
        prof_data=prof_data.filter(function (el) {
          return el != null;
        });
 
        new Chart('pie-prof', {
          
          type: 'bar',
          data: {
            labels:  prof,
            datasets: [{
              label: 'Somme Total:',
              data: prof_data,
              borderWidth: 1,
              
              backgroundColor:this.grape_bar_color,
              

            }]
          },
          options: {
            scales: {
              
              
            },
            
          }
        });


      },
      error:console.log
    })
  }




   img_name="Aucune image"
 errors_tab!:any
private selectedFile:File=null;
private srcResult:any;
agent_form=this.form_builder.group(
  {
    username:[null,[Validators.required,Validators.minLength(4),Validators.maxLength(100)]],
    email:[null,[Validators.email]],
    first_name:[null,[Validators.required,Validators.minLength(2),Validators.maxLength(100)]],
    last_name:[null,[Validators.required,Validators.minLength(2),Validators.maxLength(100)]],
    password:[null,[Validators.required,Validators.minLength(8),Validators.maxLength(100)]],
    password2:[null,[Validators.required,Validators.minLength(8),Validators.maxLength(100)]],
    gender:'M',
    d_naissance:Date,
    phone_number:[null,[Validators.required,Validators.minLength(8),Validators.maxLength(20)]],
    is_agent:true,
    is_manager:false,

  
onFileSelected() { 
  const inputNode: any = document.querySelector('#file');

  if (typeof (FileReader) !== 'undefined') {
    const reader = new FileReader();

    reader.onload = (e: any) => {
      this.srcResult = e.target.result;
      this.img_name=inputNode.files[0].name 
    };

    reader.readAsArrayBuffer(inputNode.files[0]);
  }
}





 */
