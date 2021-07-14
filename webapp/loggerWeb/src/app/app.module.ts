import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { StructureComponent } from './structure/structure.component';
import { StructureService } from './structure.service';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatButtonModule, MatButton } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule, MatSelect } from '@angular/material/select';
import { FormsModule } from '@angular/forms';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { UserComponent } from './user/user.component';
import { MatListModule } from '@angular/material/list';
import { MatCardModule, MatCardMdImage } from '@angular/material/card';
import { LoginComponent } from './login/login.component';
import { MatIconModule } from '@angular/material/icon';

 
@NgModule({
  declarations: [
    AppComponent,
    StructureComponent,
    UserComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatExpansionModule,
    MatButtonModule, 
    MatInputModule,
    MatSelectModule,
    FormsModule, 
    MatSnackBarModule,
    MatListModule,
    MatCardModule,
    MatIconModule,
  
  ],
  providers: [StructureService],
  bootstrap: [AppComponent]
})
export class AppModule { }
