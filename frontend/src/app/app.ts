import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { Header } from "./header/header"; // Importa a biblioteca SheetJS para ler arquivos Excel
import { HttpClientModule } from '@angular/common/http';
import { NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { Footer } from "../footer/footer";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CommonModule, Header, HttpClientModule,  NgbPaginationModule, Footer],
  standalone: true, // <-- ESSENCIAL
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected title = 'my-tcc-frontend';


}
