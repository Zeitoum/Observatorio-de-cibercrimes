import { Routes } from '@angular/router';
import {Home} from './home/home'
import { ProcessosApi } from './processos-api/processos-api';

export const routes: Routes = [
  {path: '', component: Home},
  {path: 'processosApi', component: ProcessosApi},
];
