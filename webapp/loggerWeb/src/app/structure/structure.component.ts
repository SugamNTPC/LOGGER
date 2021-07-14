import { Component, OnInit, Input } from '@angular/core';
import { StructureService } from '../structure.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-structure',
  templateUrl: './structure.component.html',
  styleUrls: ['./structure.component.css']
})

export class StructureComponent implements OnInit {

  @Input() parent_id : string = 'null';

  public title = "Structure Admin";
  public panelOpenState = false;
  public structures = [];
  public childs = [];
  public editEnabled = false;
  public dtypes = ["number", "text", "checkbox", "dropdown", "long-text"];
  public bool = true;

  public errorMsg;
  public successMsg;

  constructor(private _structureService : StructureService, private _snackBar : MatSnackBar) { }

  ngOnInit(): void {
    // this.getStructures();
    console.log("Parent_id : " + this.parent_id);
    this.getChilds();
  }

  destroyExpansion(){
    // this.bool = false;
    console.log("Destroying");
  }

  editEnable(child){
    // this.editEnabled = true;
    child.edit_enabled = true;
  }

  update(child){
    this._structureService.updateStructure(child._id, JSON.stringify(child))
      .subscribe(data => { this.openSnackBar("Updating ", data); child.edit_enabled=false; },
                error => this.errorMsg = error);
  }

  add(child){
    let passed_child = Object.assign({}, child);
    // let passed_child = child;
    passed_child.name="New Entry";
    if(passed_child.level_leaf=='M'){
      passed_child.level_leaf="M";
    }
    else if(passed_child.level_leaf=='L2'){
      passed_child.level_leaf="L1";
    }
    else if(passed_child.level_leaf=='L1'){
      passed_child.level_leaf="L0";
    }
    
    passed_child.parent=passed_child._id;
    passed_child.id = "none";
    delete passed_child._id;
    this._structureService.addStructure(JSON.stringify(passed_child))
      .subscribe(data => { this.openSnackBar("Adding ", data); child.edit_enabled=false; },
                error => this.errorMsg = error);
  }

  delete(child){
    this._structureService.deleteStructure(child._id)
      .subscribe(data => { this.openSnackBar("Deleting ", data); child.is_hidden=true; },
                error => this.errorMsg = error);
  }

  openPanel(id){
    console.log(id  + " opened");
  }
  
  getStructures(){
    console.log("Structures api callled");
    this._structureService.getStructure()
      .subscribe(data => this.structures = data,
                error => this.errorMsg = error);
  }

  getChilds(){
    console.log("Childs api callled");
    this._structureService.getChilds(this.parent_id)
      .subscribe(data =>{this.childs = data; this.fillAllowedLevels(this.childs);} ,
                error => this.errorMsg = error);
  }

  fillAllowedLevels(childs){
    for(const child of childs){
      if(child.level_leaf=='M'){
        child.levels_allowed = ["M","L2"];
      }
      else if(child.level_leaf=='L2'){
        child.levels_allowed = ["M","L2"];
      }
      else if(child.level_leaf=='L1' || child.level_leaf=='L0'){
        child.levels_allowed = ["L1","L0"]
      }
    }
  }

  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action, {
      duration: 10000,
    });
  }




}
